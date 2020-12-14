package binary404.autotech.common.core.plugin.crafttweaker;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.AssemblerManager;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.autotech.assembler")
public class AssemblerCraftTweaker {

    @ZenCodeType.Method
    public static void addRecipe(IItemStack[] input, IItemStack output, int energy, int time, int tier) {
        ItemStack[] itemStacks = new ItemStack[input.length];
        for (int i = 0; i < input.length; i++) {
            itemStacks[i] = input[i].getInternal();
        }
        NonNullList<ItemStack> inputList = NonNullList.from(ItemStack.EMPTY, itemStacks);
        CraftTweakerAPI.apply(new Add(inputList, output.getInternal(), energy, time, Tier.values()[tier]));
    }

    @ZenCodeType.Method
    public static void removeRecipe(IItemStack[] input) {
        ItemStack[] itemStacks = new ItemStack[input.length];
        for (int i = 0; i < input.length; i++) {
            itemStacks[i] = input[i].getInternal();
        }
        NonNullList<ItemStack> inputList = NonNullList.from(ItemStack.EMPTY, itemStacks);
        CraftTweakerAPI.apply(new Remove(inputList));
    }

    private static class Add implements IRuntimeAction {
        private ItemStack output;
        private NonNullList<ItemStack> input;
        private int energy;
        private Tier minTier;
        private int time;

        public Add(NonNullList<ItemStack> input, ItemStack output, int energy, int time, Tier minTier) {
            this.input = input;
            this.output = output;
            this.energy = energy;
            this.time = time;
            this.minTier = minTier;
        }

        @Override
        public void apply() {
        }

        @Override
        public String describe() {
            return "Adding assembler recipe for " + output;
        }
    }

    private static class Remove implements IRuntimeAction {
        private NonNullList<ItemStack> list;

        public Remove(NonNullList<ItemStack> input) {
            this.list = input;
        }

        @Override
        public void apply() {
        }

        @Override
        public String describe() {
            return "Removing assembler recipe with input " + list;
        }
    }

}
