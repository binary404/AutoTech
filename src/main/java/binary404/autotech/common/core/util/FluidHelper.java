package binary404.autotech.common.core.util;

import binary404.autotech.common.core.logistics.fluid.IMultipleTankHandler;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractList;
import java.util.List;
import java.util.function.Predicate;

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

    public static List<FluidStack> fluidHandlerToList(IMultipleTankHandler fluidTanks) {
        List<IFluidTank> backedList = fluidTanks.getFluidTanks();
        return new AbstractList<FluidStack>() {
            @Override
            public FluidStack set(int index, FluidStack element) {
                IFluidTank fluidTank = backedList.get(index);
                FluidStack oldStack = fluidTank.getFluid();
                if (!(fluidTank instanceof FluidTank))
                    return oldStack;
                ((FluidTank) backedList.get(index)).setFluid(element);
                return oldStack;
            }

            @Override
            public FluidStack get(int index) {
                return backedList.get(index).getFluid();
            }

            @Override
            public int size() {
                return backedList.size();
            }
        };
    }

    public static boolean isFluidEqual(@Nullable FluidStack first, @Nullable FluidStack other) {
        return first != null && other != null && first.getFluid() == other.getFluid() && isFluidStackTagEqual(first, other);
    }

    private static boolean isFluidStackTagEqual(FluidStack first, FluidStack other) {
        return first.getTag() == null ? other.getTag() == null : other.getTag() == null ? false : first.getTag().equals(other.getTag());
    }

    public static List<FluidStack> copyFluidList(List<FluidStack> fluidStacks) {
        FluidStack[] stacks = new FluidStack[fluidStacks.size()];
        for (int i = 0; i < fluidStacks.size(); i++) stacks[i] = fluidStacks.get(i).copy();
        return Lists.newArrayList(stacks);
    }

}
