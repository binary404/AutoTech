package binary404.autotech.common.tile.device;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.fluid.ModFluids;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileEnergy;
import binary404.autotech.common.tile.util.ITank;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileOilPump extends TileEnergy implements ITank {

    public int oilY;
    public boolean foundOil;
    List<BlockPos> oilFound;

    public TileOilPump() {
        super(ModTiles.oil_pump);
        this.tank.setCapacity(10000);
    }

    @Override
    protected int postTick(World world) {
        if (this.ticks % 60 == 0) {
            if (!foundOil) {
                oilY -= 1;
                BlockPos testPos = new BlockPos(this.pos.getX(), oilY, this.pos.getZ());
                BlockState testState = world.getBlockState(testPos);
                if (testState.getBlock() == ModBlocks.crude_oil) {
                    foundOil = true;
                    fillOilList();
                }
            } else {
                if (this.oilFound.isEmpty())
                    fillOilList();
                if (this.tank.getFluidAmount() <= 9000) {
                    fillOil();
                }
            }
        }

        return super.postTick(world);
    }

    private void fillOilList() {
        List<BlockPos> tempList = new ArrayList<>();
        for (int x = -25; x <= 25; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -25; z <= 25; z++) {
                    BlockPos tempPos = new BlockPos(this.pos.getX(), this.oilY, this.pos.getZ()).add(x, y, z);
                    BlockState tempState = world.getBlockState(tempPos);
                    if (tempState.getBlock() == ModBlocks.crude_oil) {
                        tempList.add(tempPos);
                    }
                }
            }
        }
        this.oilFound = tempList;
    }

    private void fillOil() {
        BlockPos oilPos = oilFound.get(new Random().nextInt(oilFound.size()));
        BlockState oilState = world.getBlockState(oilPos);
        if (oilState.getBlock() == ModBlocks.crude_oil) {
            world.setBlockState(oilPos, Blocks.AIR.getDefaultState());
            this.tank.fill(new FluidStack(ModFluids.crude_oil, 1000), IFluidHandler.FluidAction.EXECUTE);
        }
        oilFound.remove(oilPos);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putInt("oilY", this.oilY);
        nbt.putBoolean("foundOil", this.foundOil);
        return super.writeSync(nbt);
    }

    @Override
    public void onPlaced(World world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, state, placer, stack);
        this.oilY = this.getPos().getY();
    }
}
