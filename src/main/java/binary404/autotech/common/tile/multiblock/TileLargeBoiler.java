package binary404.autotech.common.tile.multiblock;

import binary404.autotech.client.renders.core.ICubeRenderer;
import binary404.autotech.client.renders.core.SimpleCubeRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.block.misc.BlockBoilerCasing;
import binary404.autotech.common.core.lib.multiblock.*;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.fluid.FluidTankList;
import binary404.autotech.common.core.logistics.item.ItemHandlerList;
import binary404.autotech.common.core.recipe.RecipeMaps;
import binary404.autotech.common.core.recipe.core.*;
import binary404.autotech.common.core.recipe.machine.FuelRecipeLogic;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileCore;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.*;

public class TileLargeBoiler extends MultiblockControllerDisplay implements IFuelable {

    private static final int CONSUMPTION_MULTIPLIER = 100;
    private static final int BOILING_TEMPERATURE = 100;

    public final BoilerType boilerType;

    private int currentTemperature;
    private int fuelBurnTicksLeft;
    private int throttlePercentage = 100;
    private boolean isActive;
    private boolean wasActiveAndNeedsUpdate;
    private boolean hasNoWater;
    private int lastTickSteamOutput;

    private FluidTankList fluidImportInventory;
    private ItemHandlerList itemImportInventory;
    private FluidTankList steamOutputTank;

    public TileLargeBoiler() {
        super(ModTiles.large_boiler);
        this.boilerType = BoilerType.BRONZE;
        reinitializeStructurePattern();
    }

    public TileLargeBoiler(BoilerType boilerType) {
        super(ModTiles.large_boiler);
        this.boilerType = boilerType;
        reinitializeStructurePattern();
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.fluidImportInventory = new FluidTankList(true, getAbilities(MultiblockAbility.IMPORT_FLUIDS));
        this.itemImportInventory = new ItemHandlerList(getAbilities(MultiblockAbility.IMPORT_ITEMS));
        this.steamOutputTank = new FluidTankList(true, getAbilities(MultiblockAbility.EXPORT_FLUIDS));
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.fluidImportInventory = new FluidTankList(true);
        this.itemImportInventory = new ItemHandlerList(Collections.emptyList());
        this.steamOutputTank = new FluidTankList(true);
        this.currentTemperature = 0; //reset temperature
        this.fuelBurnTicksLeft = 0;
        this.hasNoWater = false;
        this.isActive = false;
        this.throttlePercentage = 100;
        replaceFireboxAsActive(false);
    }

    @Override
    public void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
        super.onRemoved(world, state, newState, isMoving);
        if (!getWorld().isRemote && isStructureFormed()) {
            replaceFireboxAsActive(false);
        }
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if(isStructureFormed()) {
            textList.add(new TranslationTextComponent("autotech.multiblock.large_boiler.temperature", currentTemperature, boilerType.maxTemperature));
            textList.add(new TranslationTextComponent("autotech.multiblock.large_boiler.steam_output", lastTickSteamOutput, boilerType.baseSteamOutput));

            ITextComponent heatEffText = new TranslationTextComponent("autotech.multiblock.large_boiler.heat_efficiency", (int) (getHeatEfficiencyMultiplier() * 100));
            textList.add(heatEffText);
        }
    }

    private double getHeatEfficiencyMultiplier() {
        double temperature = currentTemperature / (boilerType.maxTemperature * 1.0);
        return 1.0 + Math.round(boilerType.temperatureEffBuff * temperature) / 100.0;
    }

    @Override
    protected void updateFormedValid() {
        if (fuelBurnTicksLeft > 0 && currentTemperature < boilerType.maxTemperature) {
            --this.fuelBurnTicksLeft;
            if (getOffsetTimer() % 20 == 0) {
                this.currentTemperature++;
            }
            if (fuelBurnTicksLeft == 0) {
                this.wasActiveAndNeedsUpdate = true;
            }
        } else if (currentTemperature > 0 && getOffsetTimer() % 20 == 0) {
            --this.currentTemperature;
        }

        this.lastTickSteamOutput = 0;
        if (currentTemperature >= BOILING_TEMPERATURE) {
            boolean doWaterDrain = getOffsetTimer() % 20 == 0;
            FluidStack drainedWater = fluidImportInventory.drain(new FluidStack(Fluids.WATER, 1), doWaterDrain ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE);
            if (drainedWater == null || drainedWater.getAmount() == 0) {
                drainedWater = fluidImportInventory.drain(new FluidStack(Fluids.WATER, 1), doWaterDrain ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE);
            }
            if (drainedWater != null && drainedWater.getAmount() > 0) {
                if (currentTemperature > BOILING_TEMPERATURE && hasNoWater) {
                    float explosionPower = currentTemperature / (float) BOILING_TEMPERATURE * 2.0f;
                    getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
                    getWorld().createExplosion(null, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5,
                            explosionPower, true, Explosion.Mode.DESTROY);
                }
                this.hasNoWater = false;
                if (currentTemperature >= BOILING_TEMPERATURE) {
                    double outputMultiplier = currentTemperature / (boilerType.maxTemperature * 1.0) * getThrottleMultiplier() * getThrottleEfficiency();
                    int steamOutput = (int) (boilerType.baseSteamOutput * outputMultiplier);
                    FluidStack steamStack = new FluidStack(ModFluids.steam, steamOutput);
                    steamOutputTank.fill(steamStack, IFluidHandler.FluidAction.EXECUTE);
                    this.lastTickSteamOutput = steamOutput;
                }
            } else {
                this.hasNoWater = true;
            }
        } else {
            this.hasNoWater = false;
        }

        if (fuelBurnTicksLeft == 0) {
            double heatEfficiency = getHeatEfficiencyMultiplier();
            int fuelMaxBurnTime = (int) Math.round(setupRecipeAndConsumeInputs() * heatEfficiency);
            if (fuelMaxBurnTime > 0) {
                this.fuelBurnTicksLeft = fuelMaxBurnTime;
                if (wasActiveAndNeedsUpdate) {
                    this.wasActiveAndNeedsUpdate = false;
                } else setActive(true);
                markDirty();
            }
        }

        if (wasActiveAndNeedsUpdate) {
            this.wasActiveAndNeedsUpdate = false;
            setActive(false);
        }
    }


    private int setupRecipeAndConsumeInputs() {
        for (IFluidTank fluidTank : fluidImportInventory.getFluidTanks()) {
            FluidStack fuelStack = fluidTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if (fuelStack == null || fuelStack.getFluid() == Fluids.WATER)
                continue; //ignore empty tanks and water
            FuelRecipe dieselRecipe = RecipeMaps.DIESEL_GENERATOR_FUELS.findRecipe(Tier.MaxV.use, fuelStack);
            if (dieselRecipe != null) {
                int fuelAmountToConsume = (int) Math.ceil(dieselRecipe.getRecipeFluid().getAmount() * CONSUMPTION_MULTIPLIER * boilerType.fuelConsumptionMultiplier * getThrottleMultiplier());
                if (fuelStack.getAmount() >= fuelAmountToConsume) {
                    fluidTank.drain(fuelAmountToConsume, IFluidHandler.FluidAction.EXECUTE);
                    long recipeVoltage = FuelRecipeLogic.getTieredVoltage(dieselRecipe.getMinVoltage());
                    int voltageMultiplier = (int) Math.max(1L, recipeVoltage / Tier.LV.use);
                    return (int) Math.ceil(dieselRecipe.getDuration() * CONSUMPTION_MULTIPLIER / 2.0 * voltageMultiplier * getThrottleMultiplier());
                } else continue;
            }
            FuelRecipe denseFuelRecipe = RecipeMaps.SEMI_FLUID_GENERATOR_FUELS.findRecipe(Tier.MaxV.use, fuelStack);
            if (denseFuelRecipe != null) {
                int fuelAmountToConsume = (int) Math.ceil(denseFuelRecipe.getRecipeFluid().getAmount() * CONSUMPTION_MULTIPLIER * boilerType.fuelConsumptionMultiplier * getThrottleMultiplier());
                if (fuelStack.getAmount() >= fuelAmountToConsume) {
                    fluidTank.drain(fuelAmountToConsume, IFluidHandler.FluidAction.EXECUTE);
                    long recipeVoltage = FuelRecipeLogic.getTieredVoltage(denseFuelRecipe.getMinVoltage());
                    int voltageMultiplier = (int) Math.max(1L, recipeVoltage / Tier.LV.use);
                    return (int) Math.ceil(denseFuelRecipe.getDuration() * CONSUMPTION_MULTIPLIER * 2 * voltageMultiplier * getThrottleMultiplier());
                }
            }
        }
        for (int slotIndex = 0; slotIndex < itemImportInventory.getSlots(); slotIndex++) {
            ItemStack itemStack = itemImportInventory.getStackInSlot(slotIndex);
            int fuelBurnValue = (int) Math.ceil(ForgeHooks.getBurnTime(itemStack) / (50.0 * boilerType.fuelConsumptionMultiplier * getThrottleMultiplier()));
            if (fuelBurnValue > 0) {
                if (itemStack.getCount() == 1) {
                    ItemStack containerItem = itemStack.getItem().getContainerItem(itemStack);
                    itemImportInventory.setStackInSlot(slotIndex, containerItem);
                } else {
                    itemStack.shrink(1);
                    itemImportInventory.setStackInSlot(slotIndex, itemStack);
                }
                return fuelBurnValue;
            }
        }
        return 0;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("CurrentTemperature", currentTemperature);
        compound.putInt("FuelBurnTicksLeft", fuelBurnTicksLeft);
        compound.putBoolean("HasNoWater", hasNoWater);
        compound.putInt("ThrottlePercentage", throttlePercentage);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.currentTemperature = nbt.getInt("CurrentTemperature");
        this.fuelBurnTicksLeft = nbt.getInt("FuelBurnTicksLeft");
        this.hasNoWater = nbt.getBoolean("HasNoWater");
        if (nbt.contains("ThrottlePercentage")) {
            this.throttlePercentage = nbt.getInt("ThrottlePercentage");
        }
        this.isActive = fuelBurnTicksLeft > 0;
    }

    private void setActive(boolean active) {
        this.isActive = active;
        if (!getWorld().isRemote) {
            if (isStructureFormed()) {
                replaceFireboxAsActive(active);
            }
            writeCustomData(100, buf -> buf.writeBoolean(isActive));
            markDirty();
        }
    }

    private double getThrottleMultiplier() {
        return throttlePercentage / 100.0;
    }

    private double getThrottleEfficiency() {
        return MathHelper.clamp(1.0 + 0.3 * Math.log(getThrottleMultiplier()), 0.4, 1.0);
    }

    private void replaceFireboxAsActive(boolean isActive) {
        BlockPos centerPos = getPos().offset(facing.getOpposite()).down();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos blockPos = centerPos.add(x, 0, z);
                BlockState state = getWorld().getBlockState(blockPos);
                if (state.getBlock() instanceof BlockBoilerCasing) {
                    state = state.with(BlockBoilerCasing.ACTIVE, isActive);
                    getWorld().setBlockState(blockPos, state);
                }
            }
        }
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buffer) {
        super.writeInitialSyncData(buffer);
        buffer.writeBoolean(isActive);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buffer) {
        super.receiveInitialSyncData(buffer);
        this.isActive = buffer.readBoolean();
    }

    @Override
    public void receiveCustomData(int discriminator, PacketBuffer buffer) {
        super.receiveCustomData(discriminator, buffer);
        if (discriminator == 100) {
            this.isActive = buffer.readBoolean();
        }
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return boilerType == null ? null : FactoryBlockPattern.start()
                .aisle("XXX", "CCC", "CCC", "CCC")
                .aisle("XXX", "C#C", "C#C", "CCC")
                .aisle("XXX", "CSC", "CCC", "CCC")
                .setAmountAtLeast('X', 4)
                .setAmountAtLeast('C', 20)
                .where('S', selfPredicate())
                .where('#', isAirPredicate())
                .where('X', statePredicate(boilerType.casingState)
                        .or(statePredicate(boilerType.casingState.with(BlockBoilerCasing.ACTIVE, true)))
                        .or(abilityPartPredicate(MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.IMPORT_ITEMS)))
                .where('C', statePredicate(boilerType.baseState).or(abilityPartPredicate(MultiblockAbility.EXPORT_FLUIDS)))
                .build();
    }

    @Override
    public void renderTileEntity(CCRenderState renderState, IVertexOperation... pipeline) {
        super.renderTileEntity(renderState, pipeline);
        Textures.MULTIBLOCK_WORKABLE_OVERLAY.render(renderState, facing, isActive, pipeline);
    }

    @Override
    protected boolean checkStructureComponents(List<IMultiblockPart> parts, Map<MultiblockAbility<Object>, List<Object>> abilities) {
        int importFluidsSize = abilities.getOrDefault(MultiblockAbility.IMPORT_FLUIDS, Collections.emptyList()).size();

        return importFluidsSize >= 1 && (importFluidsSize >= 2 || abilities.containsKey(MultiblockAbility.IMPORT_ITEMS)) && abilities.containsKey(MultiblockAbility.EXPORT_FLUIDS);
    }

    private boolean isFireboxPart(IMultiblockPart sourcePart) {
        return isStructureFormed() && ((TileCore) sourcePart).getPos().getY() < getPos().getY();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        if (sourcePart != null && isFireboxPart(sourcePart)) {
            return isActive ? boilerType.casingActiveRenderer : boilerType.casingIdleRenderer;
        }
        return boilerType.baseRenderer;
    }

    @Override
    public Collection<IFuelInfo> getFuels() {
        if (!isStructureFormed())
            return Collections.emptySet();
        final LinkedHashMap<Object, IFuelInfo> fuels = new LinkedHashMap<Object, IFuelInfo>();
        int fluidCapacity = 0; // fluid capacity is all non water tanks
        for (IFluidTank fluidTank : fluidImportInventory.getFluidTanks()) {
            FluidStack fuelStack = fluidTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if (fuelStack.getFluid() != Fluids.WATER)
                fluidCapacity += fluidTank.getCapacity();
        }
        for (IFluidTank fluidTank : fluidImportInventory.getFluidTanks()) {
            FluidStack fuelStack = fluidTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if (fuelStack == null || fuelStack.getFluid() == Fluids.WATER)
                continue;
            FuelRecipe dieselRecipe = RecipeMaps.DIESEL_GENERATOR_FUELS.findRecipe(Tier.MaxV.use, fuelStack);
            if (dieselRecipe != null) {
                long recipeVoltage = FuelRecipeLogic.getTieredVoltage(dieselRecipe.getMinVoltage());
                int voltageMultiplier = (int) Math.max(1L, recipeVoltage / Tier.LV.use);
                int burnTime = (int) Math.ceil(dieselRecipe.getDuration() * CONSUMPTION_MULTIPLIER / 2.0 * voltageMultiplier * getThrottleMultiplier());
                int fuelAmountToConsume = (int) Math.ceil(dieselRecipe.getRecipeFluid().getAmount() * CONSUMPTION_MULTIPLIER * boilerType.fuelConsumptionMultiplier * getThrottleMultiplier());
                int fuelBurnTime = fuelStack.getAmount() * burnTime / fuelAmountToConsume;
                FluidFuelInfo fluidFuelInfo = (FluidFuelInfo) fuels.get(fuelStack.getTranslationKey());
                if (fluidFuelInfo == null) {
                    fluidFuelInfo = new FluidFuelInfo(fuelStack, fuelStack.getAmount(), fluidCapacity, fuelAmountToConsume, fuelBurnTime);
                    fuels.put(fuelStack.getTranslationKey(), fluidFuelInfo);
                } else {
                    fluidFuelInfo.addFuelRemaining(fuelStack.getAmount());
                    fluidFuelInfo.addFuelBurnTime(fuelBurnTime);
                }
            }
            FuelRecipe denseFuelRecipe = RecipeMaps.SEMI_FLUID_GENERATOR_FUELS.findRecipe(Tier.MaxV.use, fuelStack);
            if (denseFuelRecipe != null) {
                long recipeVoltage = FuelRecipeLogic.getTieredVoltage(denseFuelRecipe.getMinVoltage());
                int voltageMultiplier = (int) Math.max(1L, recipeVoltage / Tier.LV.use);
                int burnTime = (int) Math.ceil(denseFuelRecipe.getDuration() * CONSUMPTION_MULTIPLIER * 2 * voltageMultiplier * getThrottleMultiplier());
                int fuelAmountToConsume = (int) Math.ceil(denseFuelRecipe.getRecipeFluid().getAmount() * CONSUMPTION_MULTIPLIER * boilerType.fuelConsumptionMultiplier * getThrottleMultiplier());
                int fuelBurnTime = fuelStack.getAmount() * burnTime / fuelAmountToConsume;
                FluidFuelInfo fluidFuelInfo = (FluidFuelInfo) fuels.get(fuelStack.getTranslationKey());
                if (fluidFuelInfo == null) {
                    fluidFuelInfo = new FluidFuelInfo(fuelStack, fuelStack.getAmount(), fluidCapacity, fuelAmountToConsume, fuelBurnTime);
                    fuels.put(fuelStack.getTranslationKey(), fluidFuelInfo);
                } else {
                    fluidFuelInfo.addFuelRemaining(fuelStack.getAmount());
                    fluidFuelInfo.addFuelBurnTime(fuelBurnTime);
                }
            }
        }
        int itemCapacity = 0; // item capacity is all slots
        for (int slotIndex = 0; slotIndex < itemImportInventory.getSlots(); slotIndex++) {
            itemCapacity += itemImportInventory.getSlotLimit(slotIndex);
        }
        for (int slotIndex = 0; slotIndex < itemImportInventory.getSlots(); slotIndex++) {
            ItemStack itemStack = itemImportInventory.getStackInSlot(slotIndex);
            int burnTime = (int) Math.ceil(ForgeHooks.getBurnTime(itemStack) / (50.0 * boilerType.fuelConsumptionMultiplier * getThrottleMultiplier()));
            if (burnTime > 0) {
                ItemFuelInfo itemFuelInfo = (ItemFuelInfo) fuels.get(itemStack.getTranslationKey());
                if (itemFuelInfo == null) {
                    itemFuelInfo = new ItemFuelInfo(itemStack, itemStack.getCount(), itemCapacity, 1, itemStack.getCount() * burnTime);
                    fuels.put(itemStack.getTranslationKey(), itemFuelInfo);
                } else {
                    itemFuelInfo.addFuelRemaining(itemStack.getCount());
                    itemFuelInfo.addFuelBurnTime(itemStack.getCount() * burnTime);
                }
            }
        }
        return fuels.values();
    }

    public enum BoilerType {
        BRONZE(900, 1.0f, 28, 500,
                ModBlocks.bronze_bricks.getDefaultState(),
                ModBlocks.bronze_boiler_casing.getDefaultState(),
                Textures.BRONZE_BRICKS,
                Textures.BRONZE_BOILER_CASING, Textures.BRONZE_BOILER_CASING_ACTIVE);

        public final int baseSteamOutput;
        public final float fuelConsumptionMultiplier;
        public final int temperatureEffBuff;
        public final int maxTemperature;
        public final BlockState baseState;
        public final BlockState casingState;
        public final ICubeRenderer baseRenderer;
        public final SimpleCubeRenderer casingIdleRenderer;
        public final SimpleCubeRenderer casingActiveRenderer;

        BoilerType(int baseSteamOutput, float fuelConsumptionMultiplier, int temperatureEffBuff, int maxTemperature, BlockState baseState, BlockState casingState, ICubeRenderer baseRenderer, SimpleCubeRenderer casingIdleRenderer, SimpleCubeRenderer casingActiveRenderer) {
            this.baseSteamOutput = baseSteamOutput;
            this.fuelConsumptionMultiplier = fuelConsumptionMultiplier;
            this.temperatureEffBuff = temperatureEffBuff;
            this.maxTemperature = maxTemperature;
            this.baseState = baseState;
            this.casingState = casingState;
            this.baseRenderer = baseRenderer;
            this.casingIdleRenderer = casingIdleRenderer;
            this.casingActiveRenderer = casingActiveRenderer;
        }
    }

}
