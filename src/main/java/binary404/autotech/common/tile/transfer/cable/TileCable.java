package binary404.autotech.common.tile.transfer.cable;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.util.Util;
import binary404.autotech.common.tile.ModTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class TileCable extends TileEntity {

    protected Energy energy = Energy.create(0);

    public final Map<Direction, EnergyProxy> proxyMap = new HashMap<>();
    public final Set<Direction> energySides = new HashSet<>();

    Tier tier;

    public TileCable(Tier tier) {
        super(ModTiles.cable);
        this.tier = tier;
        for (Direction side : Direction.values()) {
            this.proxyMap.put(side, new EnergyProxy());
        }
        this.energy.setCapacity(tier.maxPower);
        this.energy.setTransfer(tier.use);
    }

    public TileCable() {
        this(Tier.LV);
    }

    public Tier getTier() {
        return tier;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        ListNBT list = compound.getList("linked_cables", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, list.size()).mapToObj(list::getCompound).forEach(nbt -> {
            Direction direction = Direction.values()[nbt.getInt("direction")];
            this.proxyMap.put(direction, new EnergyProxy().read(nbt));
        });
        ListNBT list1 = compound.getList("energy_directions", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, list1.size()).mapToObj(list1::getCompound)
                .map(nbt -> Direction.values()[nbt.getInt("energy_direction")])
                .forEach(this.energySides::add);
        this.energy.read(compound, true, false);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        this.proxyMap.forEach((direction, linkedCables) -> {
            CompoundNBT nbt = new CompoundNBT();
            linkedCables.write(nbt);
            nbt.putInt("direction", direction.ordinal());
            list.add(nbt);
        });
        compound.put("linked_cables", list);
        ListNBT list1 = new ListNBT();
        this.energySides.forEach((direction) -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("energy_direction", direction.ordinal());
            list1.add(nbt);
        });
        compound.put("energy_directions", list1);
        this.energy.write(compound, true, false);
        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> new Energy(this.energy) {
                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return Util.safeInt(TileCable.this.extractEnergy(maxExtract, simulate, side));
                }

                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return Util.safeInt(TileCable.this.receiveEnergy(maxReceive, simulate, side));
                }
            }).cast();
        }
        return super.getCapability(cap, side);
    }

    protected long extractEnergy(int maxExtract, boolean simulate, @Nullable Direction side) {
        final Energy energy = this.energy;
        long extracted = Math.min(energy.getStored(), Math.min(energy.getMaxExtract(), maxExtract));
        if (!simulate && extracted > 0) {
            energy.consume(extracted);
        }
        return extracted;
    }

    protected long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction direction) {
        if (this.world == null || direction == null)
            return 0;
        long received = 0;
        received += pushEnergy(this.world, maxReceive, simulate, direction, this);
        for (BlockPos cablePos : this.proxyMap.get(direction).cables()) {
            TileEntity cableTile = this.world.getTileEntity(cablePos);
            if (cableTile instanceof TileCable) {
                TileCable cable = (TileCable) cableTile;
                received += cable.pushEnergy(this.world, maxReceive, simulate, direction, this);
            }
        }
        return received;
    }

    private long pushEnergy(World world, int maxReceive, boolean simulate, @Nullable Direction direction, TileCable cable) {
        long received = 0;
        for (Direction side : this.energySides) {
            if (cable.equals(this) && side.equals(direction) || !canExtractEnergy(side)) continue;
            BlockPos pos = this.pos.offset(side);
            if (direction != null && cable.getPos().offset(direction).equals(pos)) continue;
            TileEntity tile = world.getTileEntity(pos);
            long amount = maxReceive - received;
            if (amount > 0) {
                if (Energy.canReceive(tile, side)) {
                    long net = Math.min(amount, this.energy.getMaxExtract());
                    amount -= net;
                    received += Energy.receive(tile, side, net, simulate);
                    if (maxReceive - received <= 0) {
                        return received;
                    }
                }
            }
        }
        return received;
    }

    public void search(Block block, Direction side) {
        this.proxyMap.get(side).search(block, this, side).clear();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1));
    }

    public boolean canExtractEnergy(Direction side) {
        return true;
    }

}
