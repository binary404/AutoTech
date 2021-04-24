package binary404.autotech.common.tile.multiblock;

import binary404.autotech.client.gui.GuiTextures;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.FluidContainerSlotWidget;
import binary404.autotech.client.gui.core.widget.ImageWidget;
import binary404.autotech.client.gui.core.widget.SlotWidget;
import binary404.autotech.client.gui.core.widget.TankWidget;
import binary404.autotech.common.core.lib.multiblock.IMultiblockAbilityPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockAbility;
import binary404.autotech.common.core.logistics.*;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class TileFluidHatch extends MultiblockPart implements IMultiblockAbilityPart<IFluidTank> {

    private static int[] tankSize = new int[]{8000, 16000, 32000, 64000, 256000, 1024000, 4096000};

    private FluidTankList fluidTankHandler;
    private ItemStackHandler containerInventory;
    private boolean isExportHatch;

    public TileFluidHatch() {
        super(ModTiles.fluid_hatch, Tier.LV);
    }

    public TileFluidHatch(boolean isExportHatch, Tier tier) {
        super(ModTiles.fluid_hatch, tier);
        this.isExportHatch = isExportHatch;
        initializeInventory();
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.put("ContainerInventory", containerInventory.serializeNBT());
        nbt.putBoolean("IsExportHatch", this.isExportHatch);
        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.containerInventory.deserializeNBT(nbt.getCompound("ContainerInventory"));
        this.isExportHatch = nbt.getBoolean("IsExportHatch");
        super.read(state, nbt);
    }

    @Override
    public void initializeInventory() {
        this.containerInventory = new ItemStackHandler(2);
        FluidTank fluidTank = new FluidTank(tankSize[tier.ordinal()]);
        this.fluidTankHandler = new FluidTankList(false, fluidTank);
        super.initializeInventory();
        this.fluidInventory = fluidTankHandler;
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        return isExportHatch ? new FluidTankList(false) : this.fluidTankHandler;
    }

    @Override
    protected FluidTankList createExportFluidHandler() {
        return fluidTankHandler;
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isRemote) {
            fillContainerFromInternalTank(containerInventory, containerInventory, 0, 1);
            if (isExportHatch) {
                pushFluidsIntoNearbyHandlers(facing);
            } else {
                fillInternalTankFromFluidContainer(containerInventory, containerInventory, 0, 1);
                pullFluidsFromNearbyHandlers(facing);
            }
        }
    }

    @Override
    public MultiblockAbility getAbility() {
        return isExportHatch ? MultiblockAbility.EXPORT_FLUIDS : MultiblockAbility.IMPORT_FLUIDS;
    }

    @Override
    public void registerAbilities(List<IFluidTank> abilityList) {
        abilityList.addAll(isExportHatch ? this.exportFluids.getFluidTanks() : this.importFluids.getFluidTanks());
    }

    @Override
    public ModularUserInterface createUI(PlayerEntity playerEntity) {
        return createTankUI((isExportHatch ? exportFluids : importFluids).getTankAt(0), containerInventory, "tank", playerEntity).build(this, playerEntity);
    }

    public ModularUserInterface.Builder createTankUI(IFluidTank fluidTank, IItemHandlerModifiable containerInventory, String title, PlayerEntity player) {
        ModularUserInterface.Builder builder = ModularUserInterface.defaultBuilder();
        builder.image(7, 16, 81, 55, GuiTextures.DISPLAY);
        TankWidget tankWidget = new TankWidget(fluidTank, 69, 52, 18, 18).setHideTooltip(true).setAlwaysShowFull(true);
        builder.widget(tankWidget);
        builder.label(11, 20, "autotech.gui.fluid_amount", 0xFFFFFF);
        builder.dynamicLabel(11, 30, tankWidget::getFormattedFluidAmount, 0xFFFFFF);
        builder.dynamicLabel(11, 40, tankWidget::getFluidLocalizedName, 0xFFFFFF);
        return builder.label(6, 6, title)
                .widget(new FluidContainerSlotWidget(containerInventory, 0, 90, 17, false)
                        .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.IN_SLOT_OVERLAY))
                .widget(new ImageWidget(91, 36, 14, 15, GuiTextures.TANK_ICON))
                .widget(new SlotWidget(containerInventory, 1, 90, 54, true, false)
                        .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.OUT_SLOT_OVERLAY))
                .bindPlayerInventory(player.inventory);
    }
}
