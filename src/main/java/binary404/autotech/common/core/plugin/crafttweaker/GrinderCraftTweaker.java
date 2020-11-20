package binary404.autotech.common.core.plugin.crafttweaker;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.ArcFurnaceManager;
import binary404.autotech.common.core.manager.GrinderManager;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.autotech.grinder")
public class GrinderCraftTweaker {

    @ZenCodeType.Method
    public static void addRecipe(IItemStack input, IItemStack output, IItemStack output2, IItemStack output3, int secondChance, int thirdChance, int energy, int tier) {
        CraftTweakerAPI.apply(new Add(input.getInternal(), output.getInternal(), output2.getInternal(), output3.getInternal(), secondChance, thirdChance, energy, Tier.values()[tier]));
    }

    @ZenCodeType.Method
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new Remove(input.getInternal()));
    }

    private static class Add implements IRuntimeAction {
        private ItemStack input, output, output2, output3;
        private int energy, secondChance, thirdChance;
        private Tier tier;

        public Add(ItemStack input, ItemStack output, ItemStack output2, ItemStack output3, int secondChance, int thirdChance, int energy, Tier tier) {
            this.input = input;
            this.energy = energy;
            this.output = output;
            this.output2 = output2;
            this.output3 = output3;
            this.secondChance = secondChance;
            this.thirdChance = thirdChance;
            this.tier = tier;
        }

        @Override
        public void apply() {
            GrinderManager.addRecipe(tier, energy, input, output, output2, output3, secondChance, thirdChance);
        }

        @Override
        public String describe() {
            return "Adding recipe for " + output + " To grinder";
        }
    }

    private static class Remove implements IRuntimeAction {
        private ItemStack input;

        public Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            if (GrinderManager.recipeExists(input)) {
                GrinderManager.removeRecipe(input);
            }
        }

        @Override
        public String describe() {
            return "Removing recipe using " + input + " from grinder";
        }
    }

}
