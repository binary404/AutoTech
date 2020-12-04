package binary404.autotech.common.core.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FluidHelper {

    public static boolean isFluidHandler(ItemStack stack) {
        return !stack.isEmpty() && stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }

    public static boolean drainItemToHandler(ItemStack stack, IFluidHandler handler, PlayerEntity player, Hand hand) {

        if (stack.isEmpty() || handler == null || player == null) {
            return false;
        }
        IItemHandler playerInv = new InvWrapper(player.inventory);
        FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(stack, handler, playerInv, Integer.MAX_VALUE, player, true);
        if (result.isSuccess()) {
            player.setHeldItem(hand, result.getResult());
            return true;
        }
        return false;
    }

    public static boolean fillItemFromHandler(ItemStack stack, IFluidHandler handler, PlayerEntity player, Hand hand) {
        if (stack.isEmpty() || handler == null || player == null) {
            return false;
        }
        IItemHandler playerInv = new InvWrapper(player.inventory);
        FluidActionResult result = FluidUtil.tryFillContainerAndStow(stack, handler, playerInv, Integer.MAX_VALUE, player, true);
        if (result.isSuccess()) {
            player.setHeldItem(hand, result.getResult());
            return true;
        }
        return false;
    }

    public static boolean interactWithHandler(ItemStack stack, IFluidHandler handler, PlayerEntity player, Hand hand) {
        return fillItemFromHandler(stack, handler, player, hand) || drainItemToHandler(stack, handler, player, hand);
    }

}
