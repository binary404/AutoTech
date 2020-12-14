package binary404.autotech.common.tile.device;

import binary404.autotech.common.tile.ModTiles;
import binary404.autotech.common.tile.core.TileCore;
import binary404.autotech.common.tile.core.TileTickable;
import binary404.autotech.common.tile.util.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public class TileDisplayStand extends TileTickable implements IInventory {

    public TileDisplayStand() {
        super(ModTiles.display_stand);
        this.inv.set(1);
    }


    @Override
    protected int postTick(World world) {
        this.sync(4);
        return super.postTick(world);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
