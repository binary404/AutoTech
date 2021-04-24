package binary404.autotech.common.tile.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IBlockEntity {

    default void onPlaced(World world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    }

    default void onAdded(World world, BlockState state, BlockState oldState, boolean isMoving) {
    }

    default void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
    }

    default boolean onRightClick(PlayerEntity player, Hand hand, Direction direction, BlockRayTraceResult hitResult) {
        return false;
    }

}
