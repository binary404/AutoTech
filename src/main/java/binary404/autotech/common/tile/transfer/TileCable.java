package binary404.autotech.common.tile.transfer;

import binary404.autotech.common.block.transfer.BlockCable;
import binary404.autotech.common.core.logistics.energy.Energy;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.EnergyProxy;
import binary404.autotech.common.tile.core.TileTiered;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.IntStream;

public class TileCable extends TileTiered<BlockCable> {
    public final Map<Direction, EnergyProxy> proxyMap = new HashMap<>();
    public final Set<Direction> energySides = new HashSet<>();

    public TileCable(Tier tier) {
        super(ModTiles.cable, tier);
        for (Direction side : Direction.values()) {
            this.proxyMap.put(side, new EnergyProxy());
        }
    }

    @Override
    public boolean dropNbt() {
        return false;
    }

    public TileCable() {
        this(Tier.LV);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        ListNBT list = compound.getList("linked_cables", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, list.size()).mapToObj(list::getCompound).forEach(nbt -> {
            Direction direction = Direction.values()[nbt.getInt("direction")];
            this.proxyMap.put(direction, new EnergyProxy().read(nbt));
        });
        ListNBT list1 = compound.getList("energy_directions", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, list1.size()).mapToObj(list1::getCompound)
                .map(nbt -> Direction.values()[nbt.getInt("energy_direction")])
                .forEach(this.energySides::add);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
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
        return super.writeSync(compound);
    }

    @Override
    protected long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction direction) {
        if (this.world == null || isRemote() || direction == null || !checkRedstone() || !canReceiveEnergy(direction))
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
    protected long getEnergyCapacity() {
        return 0;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1));
    }
}
