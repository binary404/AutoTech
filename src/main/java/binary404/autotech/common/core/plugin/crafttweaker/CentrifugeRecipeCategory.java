package binary404.autotech.common.core.plugin.crafttweaker;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.ArcFurnaceManager;
import binary404.autotech.common.core.manager.CentrifugeManager;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.autotech.centrifuge")
public class CentrifugeRecipeCategory {

    @ZenCodeType.Method
    public static void addRecipe(IItemStack input, IItemStack output, IItemStack output2, int secondChance, int energy, int tier) {
        CraftTweakerAPI.apply(new Add(input.getInternal(), output.getInternal(), output2.getInternal(), secondChance, energy, Tier.values()[tier]));
    }

    @ZenCodeType.Method
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new Remove(input.getInternal()));
    }

    private static class Add implements IRuntimeAction {
        private ItemStack input, output, output2;
        private int energy, secondChance;
        private Tier minTier;

        public Add(ItemStack input, ItemStack output, ItemStack output2, int secondChance, int energy, Tier tier) {
            this.input = input;
            this.energy = energy;
            this.output = output;
            this.output2 = output2;
            this.secondChance = secondChance;
            this.minTier = tier;
        }

        @Override
        public void apply() {
            CentrifugeManager.addRecipe(minTier, energy, input, output, output2, secondChance);
        }

        @Override
        public String describe() {
            return "Adding recipe for " + output + " To centrifuge";
        }
    }

    private static class Remove implements IRuntimeAction {
        private ItemStack input;

        public Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            if (CentrifugeManager.recipeExists(input)) {
                CentrifugeManager.removeRecipe(input);
            }
        }

        @Override
        public String describe() {
            return "Removing recipe using " + input + " from centrifuge";
        }
    }

}
