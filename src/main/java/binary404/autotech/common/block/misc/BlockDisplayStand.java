package binary404.autotech.common.block.misc;

import binary404.autotech.common.block.BlockTile;
import binary404.autotech.common.core.logistics.item.Inventory;
import binary404.autotech.common.tile.device.TileDisplayStand;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDisplayStand extends BlockTile {

    private static VoxelShape shape = VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);

    public BlockDisplayStand() {
        super();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileDisplayStand) {
            TileDisplayStand stand = (TileDisplayStand) tileEntity;
            Inventory inventory = stand.getInventory();
            if (inventory.getStackInSlot(0).isEmpty()) {
                ItemStack stack = player.getHeldItem(hand);
                ItemStack insertStack = stack.copy();
                insertStack.setCount(1);
                stack.shrink(1);
                inventory.setStackInSlot(0, insertStack);
                return ActionResultType.SUCCESS;
            } else {
                player.addItemStackToInventory(inventory.getStackInSlot(0));
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileDisplayStand();
    }
}
