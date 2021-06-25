package binary404.autotech.common.tile.core;

import binary404.autotech.client.renders.core.SimpleSidedCubeRenderer;
import binary404.autotech.client.renders.core.Textures;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.energy.Energy;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public abstract class TileTiered extends TileCore {

    protected Tier tier;
    protected Energy energyStorage;

    public TileTiered(TileEntityType<?> type) {
        this(type, Tier.LV);
    }

    public TileTiered(TileEntityType<?> type, Tier tier) {
        super(type);
        this.tier = tier;
    }

    protected void reinitializeEnergyContainer() {
        this.energyStorage = new Energy(tier.maxPower, tier.use, tier.use);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> this.energyStorage).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.energyStorage.write(compound, "EnergyStorage", true, true);
        compound.putInt("Tier", this.tier.ordinal());
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.energyStorage = Energy.deserialize(nbt, "EnergyStorage", true, true);
        this.tier = Tier.values()[nbt.getInt("Tier")];
        super.read(state, nbt);
    }

    public Tier getTier() {
        return this.tier;
    }

    protected SimpleSidedCubeRenderer getBaseRenderer() {
        return Textures.CASINGS[tier.ordinal()];
    }

    public void renderTileEntity(CCRenderState state, IVertexOperation... pipeLine) {
        getBaseRenderer().render(state, Cuboid6.full, pipeLine);
    }
}
