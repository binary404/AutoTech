package binary404.autotech.common.core.plugin.crafttweaker;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.CentrifugeManager;
import binary404.autotech.common.core.manager.SawMillManager;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.autotech.sawMill")
public class SawMillCraftTweaker {

    @ZenCodeType.Method
    public static void addRecipe(IItemStack input, IItemStack output, IItemStack output2, int secondChance, int energy) {
        CraftTweakerAPI.apply(new Add(input.getInternal(), output.getInternal(), output2.getInternal(), secondChance, energy));
    }

    @ZenCodeType.Method
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new Remove(input.getInternal()));
    }

    private static class Add implements IRuntimeAction {
        private ItemStack input, output, output2;
        private int energy, secondChance;

        public Add(ItemStack input, ItemStack output, ItemStack output2, int secondChance, int energy) {
            this.input = input;
            this.energy = energy;
            this.output = output;
            this.output2 = output2;
            this.secondChance = secondChance;
        }

        @Override
        public void apply() {
            SawMillManager.addRecipe(energy, input, output, output2, secondChance);
        }

        @Override
        public String describe() {
            return "Adding recipe for " + output + " To saw mill";
        }
    }

    private static class Remove implements IRuntimeAction {
        private ItemStack input;

        public Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            if (SawMillManager.recipeExists(input)) {
                SawMillManager.removeRecipe(input);
            }
        }

        @Override
        public String describe() {
            return "Removing recipe using " + input + " from saw mill";
        }
    }


}
