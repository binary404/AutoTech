package binary404.autotech.common.core.lib.multiblock;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.core.TileMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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

public abstract class MultiblockControllerBase extends TileMachine {

    protected BlockPattern structurePattern;

    private final Map<MultiblockAbility<Object>, List<Object>> multiblockAbilities = new HashMap<>();
    private final List<IMultiblockPart> multiblockParts = new ArrayList<>();

    private boolean structureFormed;

    public MultiblockControllerBase(TileEntityType<?> type, Tier tier) {
        super(type, tier);
        reinitializeStructurePattern();
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
    protected int postTick(World world) {
        if (!getWorld().isRemote) {
            if (ticks % 20 == 0) {
                checkStructurePattern();
            }
            if (isStructureFormed()) {
                updateFormedValid();
                return super.postTick(world);
            }
        }

        return -1;
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
                formStructure(context);
            }
        } else if (context == null && structureFormed) {
            System.out.println("Invalidating structure");
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

}
