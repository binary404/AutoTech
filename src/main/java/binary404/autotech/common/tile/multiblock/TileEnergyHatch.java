package binary404.autotech.common.tile.multiblock;

import binary404.autotech.client.gui.core.ModularUserInterface;
import binary404.autotech.common.core.lib.multiblock.IMultiblockAbilityPart;
import binary404.autotech.common.core.lib.multiblock.IMultiblockPart;
import binary404.autotech.common.core.lib.multiblock.MultiblockAbility;
import binary404.autotech.common.core.lib.multiblock.MultiblockPart;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class TileEnergyHatch extends MultiblockPart implements IMultiblockAbilityPart<IEnergyStorage> {

    private boolean isExportHatch;
    private Energy energyStorage;

    public TileEnergyHatch() {
        super(ModTiles.energy_hatch, Tier.LV);
    }

    public TileEnergyHatch(boolean isExportHatch, Tier tier) {
        super(ModTiles.energy_hatch, tier);
        this.isExportHatch = isExportHatch;
        initializeInventory();
    }

    @Override
    protected void initializeInventory() {
        super.initializeInventory();
        if (newInventory) {
            this.energyStorage = new Energy(tier.maxPower, tier.use, tier.use);
        }
    }

    @Override
    public MultiblockAbility<IEnergyStorage> getAbility() {
        return isExportHatch ? MultiblockAbility.OUTPUT_ENERGY : MultiblockAbility.INPUT_ENERGY;
    }

    @Override
    public void registerAbilities(List<IEnergyStorage> abilityList) {
        abilityList.add(energyStorage);
    }

    @Override
    public ModularUserInterface createUI(PlayerEntity playerEntity) {
        return null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> this.energyStorage).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        this.energyStorage.write(nbt, "EnergyStorage", true, true);
        nbt.putBoolean("IsExportHatch", this.isExportHatch);
        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.energyStorage.read(nbt, "EnergyStorage", true, true);
        this.isExportHatch = nbt.getBoolean("IsExportHatch");
        super.read(state, nbt);
    }
}
