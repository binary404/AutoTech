package binary404.autotech.common.tile.machine;

import binary404.autotech.client.fx.LaserRenderHandler;
import binary404.autotech.client.fx.particles.EmpowerParticle;
import binary404.autotech.client.fx.particles.FXHelper;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.EmpowererManager;
import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileMachine;
import binary404.autotech.common.tile.device.TileDisplayStand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class TileEmpowerer extends TileMachine {

    TileDisplayStand stand1, stand2, stand3, stand4;
    List<BlockPos> stands = new ArrayList<>();

    EmpowererManager.EmpowererRecipe recipe;

    public TileEmpowerer() {
        super(ModTiles.empowerer, Tier.HV);
        this.inv.set(1);
    }

    @Override
    protected boolean canStart() {
        getStands();

        if (energy.getEnergyStored() <= 0) {
            return false;
        }

        if (stands.size() < 4)
            return false;

        getRecipe();

        if (recipe == null) {
            return false;
        }

        return true;
    }

    @Override
    protected boolean hasValidInput() {
        getStands();

        if (stands.size() < 4)
            return false;

        if (recipe == null) {
            getRecipe();
        }

        if (recipe == null) {
            return false;
        }

        return true;
    }

    @Override
    protected int processTick() {
        if (world.isRemote) {
            getStands();
            for (BlockPos stand : stands) {
                Vector3d start = new Vector3d(stand.getX() + 0.5, stand.getY() + 1.0, stand.getZ() + 0.5);
                Vector3d end = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5);
                LaserRenderHandler.addLaser(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ(), 1, false);
                TileEntity tile = world.getTileEntity(stand);
                if (tile instanceof TileDisplayStand) {
                    for (int i = 0; i <= 3; i++) {
                        EmpowerParticle particle = new EmpowerParticle((ClientWorld) world, start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ(), ((TileDisplayStand) tile).getInventory().getStackInSlot(0));
                        Minecraft.getInstance().particles.addEffect(particle);
                    }
                }
            }
        }
        return super.processTick();
    }

    @Override
    protected void processStart() {
        processMax = recipe.getEnergy();
        processRem = processMax;
    }

    public void getRecipe() {
        if (stand1 == null || stand2 == null || stand3 == null || stand4 == null)
            return;
        this.recipe = EmpowererManager.getRecipe(inv.getStackInSlot(0), stand1.getInventory().getStackInSlot(0), stand2.getInventory().getStackInSlot(0), stand3.getInventory().getStackInSlot(0), stand4.getInventory().getStackInSlot(0));
    }

    @Override
    protected void processFinish() {
        getStands();

        if (recipe == null) {
            getRecipe();
        }

        if (recipe == null) {
            processOff();
            return;
        }

        ItemStack output = recipe.getOutput().copy();

        this.inv.setStackInSlot(0, output);

        stand1.getInventory().getStackInSlot(0).shrink(1);
        stand2.getInventory().getStackInSlot(0).shrink(1);
        stand3.getInventory().getStackInSlot(0).shrink(1);
        stand4.getInventory().getStackInSlot(0).shrink(1);

        sync(4);

        for (int i = 0; i < 36; i++) {
            double x = this.pos.getX() + 0.5 + world.rand.nextGaussian() * 1.8;
            double y = this.pos.getY() + 0.5 + world.rand.nextGaussian() * 1.8;
            double z = this.pos.getZ() + 0.5 + world.rand.nextGaussian() * 1.8;
            FXHelper.sparkle(x, y, z, world.rand.nextGaussian() * 0.08, world.rand.nextGaussian() * 0.08, world.rand.nextGaussian() * 0.08, 1.0F, 0.2F, 0.0F, 0.07F, 0.4F, 0.0F, 30);
        }
    }

    @Override
    protected void clearRecipe() {
        this.recipe = null;
    }

    public void getStands() {
        stands.clear();
        stand1 = stand2 = stand3 = stand4 = null;
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                BlockPos testPos = this.getPos().add(x, 0, z);
                if (world.getTileEntity(testPos) instanceof TileDisplayStand) {
                    if (stand1 == null) {
                        stand1 = ((TileDisplayStand) world.getTileEntity(testPos));
                    } else if (stand2 == null) {
                        stand2 = ((TileDisplayStand) world.getTileEntity(testPos));
                    } else if (stand3 == null) {
                        stand3 = ((TileDisplayStand) world.getTileEntity(testPos));
                    } else if (stand4 == null) {
                        stand4 = ((TileDisplayStand) world.getTileEntity(testPos));
                    }
                    stands.add(testPos);
                }
            }
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        getStands();
        getRecipe();
        return !this.isActive && this.recipe == null;
    }
}
