package binary404.autotech.common.tile.multiblock;

import binary404.autotech.client.gui.GuiTextures;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.SlotWidget;
import binary404.autotech.client.renders.core.SimpleOverlayRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.core.lib.multiblock.IMultiblockAbilityPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockAbility;
import binary404.autotech.common.core.logistics.*;
import binary404.autotech.common.tile.ModTiles;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class TileItemHatch extends MultiblockPart implements IMultiblockAbilityPart<IItemHandlerModifiable> {

    private boolean isExportHatch;

    public TileItemHatch(boolean isExportHatch) {
        super(ModTiles.item_hatch, Tier.LV);
        this.isExportHatch = isExportHatch;
        initializeInventory();
    }

    public TileItemHatch() {
        super(ModTiles.item_hatch, Tier.LV);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("IsExportHatch", this.isExportHatch);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.isExportHatch = nbt.getBoolean("IsExportHatch");
        super.read(state, nbt);
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isRemote && getOffsetTimer() % 5 == 0) {
            if (isExportHatch) {
                pushItemsIntoNearbyHandlers(facing);
            } else {
                pullItemsFromNearbyHandlers(facing);
            }
        }
    }

    @Override
    public void renderTileEntity(CCRenderState renderState, IVertexOperation... pipeline) {
        super.renderTileEntity(renderState, pipeline);
        SimpleOverlayRenderer renderer = isExportHatch ? Textures.PIPE_OUT_OVERLAY : Textures.PIPE_IN_OVERLAY;
        renderer.renderSided(this.facing, renderState, pipeline);
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return isExportHatch ? new ItemStackHandler(4) : new ItemStackHandler(0);
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        return isExportHatch ? new ItemStackHandler(0) : new ItemStackHandler(4);
    }

    @Override
    public MultiblockAbility<IItemHandlerModifiable> getAbility() {
        return isExportHatch ? MultiblockAbility.EXPORT_ITEMS : MultiblockAbility.IMPORT_ITEMS;
    }

    @Override
    public void registerAbilities(List<IItemHandlerModifiable> abilityList) {
        abilityList.add(isExportHatch ? this.exportItems : this.importItems);
    }

    @Override
    public ModularUserInterface createUI(PlayerEntity playerEntity) {
        int rowSize = (int) Math.sqrt(4);
        ModularUserInterface.Builder builder = ModularUserInterface.builder(GuiTextures.BACKGROUND, 176, 18 + 18 * rowSize + 94)
                .label(10, 5, getFullName());

        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                int index = y * rowSize + x;
                builder.widget(new SlotWidget(isExportHatch ? exportItems : importItems, index, 89 - rowSize * 9 + x * 18, 18 + y * 18, true, !isExportHatch)
                        .setBackgroundTexture(GuiTextures.SLOT));
            }
        }
        builder.bindPlayerInventory(playerEntity.inventory, GuiTextures.SLOT, 8, 18 + 18 * rowSize + 12);
        return builder.build(this, playerEntity);
    }
}
