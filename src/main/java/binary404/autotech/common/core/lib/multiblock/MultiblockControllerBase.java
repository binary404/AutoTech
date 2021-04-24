package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.client.renders.core.ICubeRenderer;
import binary404.autotech.common.tile.core.TileCore;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public abstract class MultiblockControllerBase extends TileCore {

    protected BlockPattern structurePattern;

    private final Map<MultiblockAbility<Object>, List<Object>> multiblockAbilities = new HashMap<>();
    private final List<IMultiblockPart> multiblockParts = new ArrayList<>();

    private boolean structureFormed;

    public MultiblockControllerBase(TileEntityType<?> type) {
        super(type);
        reinitializeStructurePattern();
        initializeInventory();
    }

    @Override
    public void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
        super.onRemoved(world, state, newState, isMoving);
        for (IMultiblockPart part : multiblockParts) {
            part.removeFromMultiBlock(this);
        }
    }

    protected void reinitializeStructurePattern() {
        this.structurePattern = createStructurePattern();
    }

    @Override
    public void tick() {
        if (!getWorld().isRemote) {
            if (getOffsetTimer() % 20 == 0 || getTimer() == 0) {
                checkStructurePattern();
            }
            if (isStructureFormed()) {
                updateFormedValid();
            }
        }
    }

    protected abstract void updateFormedValid();

    protected abstract BlockPattern createStructurePattern();

    protected boolean checkStructureComponents(List<IMultiblockPart> parts, Map<MultiblockAbility<Object>, List<Object>> abilities) {
        return true;
    }

    public static Predicate<BlockWorldState> tilePredicate(BiFunction<BlockWorldState, TileEntity, Boolean> predicate) {
        return blockWorldState -> {
            TileEntity tileEntity = blockWorldState.getTileEntity();
            if (predicate.apply(blockWorldState, tileEntity)) {
                if (tileEntity instanceof IMultiblockPart) {
                    Set<IMultiblockPart> partsFound = blockWorldState.getMatchContext().getOrCreate("MultiblockParts", HashSet::new);
                    partsFound.add((IMultiblockPart) tileEntity);
                }
                return true;
            }
            return false;
        };
    }

    public static Predicate<BlockWorldState> abilityPartPredicate(MultiblockAbility<?>... allowedAbilities) {
        return tilePredicate((state, tile) -> tile instanceof IMultiblockAbilityPart<?> &&
                ArrayUtils.contains(allowedAbilities, ((IMultiblockAbilityPart<?>) tile).getAbility()));
    }

    public static Predicate<BlockWorldState> partPredicate(Class<? extends IMultiblockPart> baseClass) {
        return tilePredicate((state, tile) -> tile instanceof IMultiblockPart && baseClass.isAssignableFrom(tile.getClass()));
    }

    public static Predicate<BlockWorldState> statePredicate(BlockState... allowedStates) {
        return blockWorldState -> ArrayUtils.contains(allowedStates, blockWorldState.getBlockState());
    }

    public static Predicate<BlockWorldState> blockPredicate(Block... block) {
        return blockWorldState -> ArrayUtils.contains(block, blockWorldState.getBlockState().getBlock());
    }

    public static Predicate<BlockWorldState> isAirPredicate() {
        return blockWorldState -> blockWorldState.getBlockState().getBlock().isAir(blockWorldState.getBlockState(), blockWorldState.getWorld(), blockWorldState.getPos());
    }

    public IPatternCenterPredicate selfPredicate() {
        return BlockWorldState.wrap(tilePredicate((state, tile) -> tile.getType().equals(this.getType())));
    }

    public Predicate<BlockWorldState> countMatch(String key, Predicate<BlockWorldState> original) {
        return blockWorldState -> {
            if (original.test(blockWorldState)) {
                blockWorldState.getLayerContext().increment(key, 1);
                return true;
            }
            return false;
        };
    }

    protected void checkStructurePattern() {
        Direction facing = this.facing.getOpposite();
        PatternMatchContext context = structurePattern.checkPatternAt(getWorld(), getPos(), facing);
        if (context != null && !structureFormed) {
            Set<IMultiblockPart> rawPartsSet = context.getOrCreate("MultiblockParts", HashSet::new);
            ArrayList<IMultiblockPart> parts = new ArrayList<>(rawPartsSet);
            parts.sort(Comparator.comparing(it -> ((TileEntity) it).getPos().hashCode()));
            for (IMultiblockPart part : parts) {
                if (part.isAttachedToMultiBlock()) {
                    //disallow sharing of multiblock parts
                    //if part is already attached to another multiblock,
                    //stop here without attempting to register abilities
                    return;
                }
            }
            Map<MultiblockAbility<Object>, List<Object>> abilities = new HashMap<>();
            for (IMultiblockPart multiblockPart : parts) {
                if (multiblockPart instanceof IMultiblockAbilityPart) {
                    IMultiblockAbilityPart<Object> abilityPart = (IMultiblockAbilityPart<Object>) multiblockPart;
                    List<Object> abilityInstancesList = abilities.computeIfAbsent(abilityPart.getAbility(), k -> new ArrayList<>());
                    abilityPart.registerAbilities(abilityInstancesList);
                }
            }
            if (checkStructureComponents(parts, abilities)) {
                parts.forEach(part -> part.addToMultiBlock(this));
                this.multiblockParts.addAll(parts);
                this.multiblockAbilities.putAll(abilities);
                this.structureFormed = true;
                writeCustomData(400, buffer -> buffer.writeBoolean(true));
                formStructure(context);
            }
        } else if (context == null && structureFormed) {
            invalidateStructure();
        }
    }

    protected void formStructure(PatternMatchContext context) {

    }

    public void invalidateStructure() {
        this.multiblockParts.forEach(part -> part.removeFromMultiBlock(this));
        this.multiblockAbilities.clear();
        this.multiblockParts.clear();
        this.structureFormed = false;
        writeCustomData(400, buffer -> buffer.writeBoolean(false));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAbilities(MultiblockAbility<T> ability) {
        @SuppressWarnings("SuspiciousMethodCalls")
        List<T> rawList = (List<T>) multiblockAbilities.getOrDefault(ability, Collections.emptyList());
        return Collections.unmodifiableList(rawList);
    }

    public List<IMultiblockPart> getMultiblockParts() {
        return Collections.unmodifiableList(multiblockParts);
    }

    public boolean isStructureFormed() {
        return structureFormed;
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buffer) {
        super.writeInitialSyncData(buffer);
        buffer.writeBoolean(structureFormed);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buffer) {
        super.receiveInitialSyncData(buffer);
        this.structureFormed = buffer.readBoolean();
    }

    @Override
    public void receiveCustomData(int discriminator, PacketBuffer buffer) {
        super.receiveCustomData(discriminator, buffer);
        if (discriminator == 400) {
            this.structureFormed = buffer.readBoolean();
        }
    }

    public abstract ICubeRenderer getBaseTexture(IMultiblockPart sourcePart);

    @Override
    public void renderTileEntity(CCRenderState renderState, IVertexOperation... pipeline) {
        getBaseTexture(null).render(renderState, Cuboid6.full, pipeline);
    }
}
