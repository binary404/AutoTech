package binary404.autotech.common.block.multiblock;

import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.util.FluidHelper;
import binary404.autotech.common.tile.multiblock.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class BlockHatch extends BlockMultiBlock {

    public BlockHatch() {
        super();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.getBlock() == ModBlocks.item_input_hatch)
            return new TileItemInputHatch();
        else if (state.getBlock() == ModBlocks.item_output_hatch)
            return new TileItemOutputHatch();
        else if (state.getBlock() == ModBlocks.energy_input_hatch)
            return new TileEnergyInputHatch();
        else if (state.getBlock() == ModBlocks.fluid_input_hatch)
            return new TileFluidInputHatch();
        else if (state.getBlock() == ModBlocks.fluid_output_hatch)
            return new TileFluidOutputHatch();
        return null;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);
        PlayerInteractEvent event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, result.getFace());
        ItemStack item = player.getHeldItem(hand);
        if (FluidHelper.isFluidHandler(item)) {
                if (FluidUtil.interactWithFluidHandler(player, hand, world, pos, result.getFace())) {
                    return ActionResultType.SUCCESS;
                }
        }
        return ActionResultType.SUCCESS;
    }
}
