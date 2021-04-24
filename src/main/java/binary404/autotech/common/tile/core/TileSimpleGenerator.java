package binary404.autotech.common.tile.core;

import binary404.autotech.client.gui.GuiTextures;
import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.client.gui.core.widget.FluidContainerSlotWidget;
import binary404.autotech.client.gui.core.widget.ImageWidget;
import binary404.autotech.client.gui.core.widget.SlotWidget;
import binary404.autotech.client.gui.core.widget.TankWidget;
import binary404.autotech.client.renders.core.OrientedOverlayRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.core.recipe.machine.FuelRecipeLogic;
import binary404.autotech.common.core.recipe.map.FuelRecipeMap;
import binary404.autotech.common.tile.ModTiles;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class TileSimpleGenerator extends TileTiered {

    private FuelRecipeLogic workableHandler;
    private ItemStackHandler containerInventory;
    private FuelRecipeMap recipeMap;
    protected OrientedOverlayRenderer renderer;

    public TileSimpleGenerator() {
        this(RecipeMaps.EMPTY_FUELS, Textures.GRINDER, Tier.LV);
    }

    public TileSimpleGenerator(FuelRecipeMap recipeMap, OrientedOverlayRenderer renderer, Tier tier) {
        super(ModTiles.simple_generator, tier);
        this.renderer = renderer;
        this.recipeMap = recipeMap;
        this.workableHandler = createWorkableHandler();
        initializeInventory();
        reinitializeEnergyContainer();
    }

    @Override
    public void renderTileEntity(CCRenderState state, IVertexOperation... pipeLine) {
        super.renderTileEntity(state, pipeLine);
        this.renderer.render(state, this.facing, workableHandler != null && workableHandler.isActive(), pipeLine);
    }

    @Override
    protected void initializeInventory() {
        this.containerInventory = new ItemStackHandler(2);
        super.initializeInventory();
    }

    protected FuelRecipeLogic createWorkableHandler() {
        return new FuelRecipeLogic(this, recipeMap, () -> energyStorage, () -> importFluids, this.getTier().use);
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        return new FluidTankList(false, new FluidTank(16000));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("ContainerInventory", containerInventory.serializeNBT());
        compound.putString("RecipeMap", this.recipeMap.unlocalizedName);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.containerInventory.deserializeNBT(nbt.getCompound("ContainerInventory"));
        this.recipeMap = FuelRecipeMap.getByName(nbt.getString("RecipeMap"));
        this.workableHandler = createWorkableHandler();
        super.read(state, nbt);
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isRemote) {
            if (getOffsetTimer() % 5 == 0)
                fillInternalTankFromFluidContainer(containerInventory, containerInventory, 0, 1);

            if (this.energyStorage.getEnergyStored() >= getTier().use) {
                long output = getTier().use;
                for (Direction direction : Direction.values()) {
                    TileEntity tile = getWorld().getTileEntity(this.getPos().offset(direction));
                    Direction opposite = direction.getOpposite();
                    if (tile != null && tile.getCapability(CapabilityEnergy.ENERGY, opposite).isPresent()) {
                        tile.getCapability(CapabilityEnergy.ENERGY, opposite).ifPresent((handler) -> {
                            this.energyStorage.extractEnergy(handler.receiveEnergy((int) output, false), false);
                        });
                    }
                }
            }
        }
    }

    protected ModularUserInterface.Builder createGuiTemplate(PlayerEntity player) {
        ModularUserInterface.Builder builder = ModularUserInterface.defaultBuilder();
        builder.image(7, 16, 81, 55, GuiTextures.DISPLAY);
        TankWidget tankWidget = new TankWidget(importFluids.getTankAt(0), 69, 52, 18, 18)
                .setHideTooltip(true).setAlwaysShowFull(true);
        builder.widget(tankWidget);
        builder.label(11, 20, "autotech.gui.fluid_amount", 0xFFFFFF);
        builder.dynamicLabel(11, 30, tankWidget::getFormattedFluidAmount, 0xFFFFFF);
        builder.dynamicLabel(11, 40, tankWidget::getFluidLocalizedName, 0xFFFFFF);
        return builder.label(6, 6, getFullName())
                .widget(new FluidContainerSlotWidget(containerInventory, 0, 90, 17, true)
                        .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.IN_SLOT_OVERLAY))
                .widget(new ImageWidget(91, 36, 14, 15, GuiTextures.TANK_ICON))
                .widget(new SlotWidget(containerInventory, 1, 90, 54, true, false)
                        .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.OUT_SLOT_OVERLAY))
                .bindPlayerInventory(player.inventory);
    }

    @Override
    public ModularUserInterface createUI(PlayerEntity entityPlayer) {
        return createGuiTemplate(entityPlayer).build(this, entityPlayer);
    }
}
