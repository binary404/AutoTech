package binary404.autotech.common.block;

import binary404.autotech.common.tile.TileCore;
import binary404.autotech.common.tile.util.IBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockTile extends Block {

    public Class<? extends TileEntity> tileEntity;

    public BlockTile(Properties properties, Class<? extends TileEntity> tileEntity) {
        super(properties);

        this.tileEntity = tileEntity;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        try {
            return tileEntity.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onAdded(world, state, oldState, isMoving);
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onRemoved(world, state, newState, isMoving);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onPlaced(world, state, placer, stack);
        }
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof TileCore) {
            TileCore tile = (TileCore) te;
            ItemStack stack1 = tile.storeToStack(new ItemStack(this));
            spawnAsEntity(world, pos, stack1);
            player.addStat(Stats.BLOCK_MINED.get(this));
            player.addExhaustion(0.005F);
        } else {
            super.harvestBlock(world, player, pos, state, te, stack);
        }
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tile = worldIn.getTileEntity(pos);
        return tile != null && tile.receiveClientEvent(id, param);
    }


    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCore) {
            INamedContainerProvider provider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new ItemStack(BlockTile.this).getDisplayName();
                }

                @Nullable
                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return getContainer(i, playerInventory, (TileCore) tile, result);
                }
            };
                if (player instanceof ServerPlayerEntity) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, provider, buffer -> {
                        buffer.writeBlockPos(pos);
                        additionalGuiData(buffer, state, world, pos, player, hand, result);
                    });
                }
                return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Nullable
    public <T extends TileCore> Container getContainer(int id, PlayerInventory inventory, TileCore te, BlockRayTraceResult result) {
        return null;
    }

    protected void additionalGuiData(PacketBuffer buffer, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    }

}
