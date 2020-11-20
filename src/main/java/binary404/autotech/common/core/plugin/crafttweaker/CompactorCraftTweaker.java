package binary404.autotech.common.core.plugin.crafttweaker;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.ArcFurnaceManager;
import binary404.autotech.common.core.manager.CompactorManager;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.autotech.compactor")
public class CompactorCraftTweaker {

    @ZenCodeType.Method
    public static void addRecipe(IItemStack input, IItemStack output, int energy, int tier) {
        CraftTweakerAPI.apply(new Add(input.getInternal(), output.getInternal(), energy, Tier.values()[tier]));
    }

    @ZenCodeType.Method
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new Remove(input.getInternal()));
    }

    private static class Add implements IRuntimeAction {
        private ItemStack input, output;
        private int energy;
        private Tier tier;

        public Add(ItemStack input, ItemStack output, int energy, Tier tier) {
            this.input = input;
            this.energy = energy;
            this.output = output;
            this.tier = tier;
        }

        @Override
        public void apply() {
            CompactorManager.addRecipe(tier, energy, input, output);
        }

        @Override
        public String describe() {
            return "Adding recipe for " + output + " To compactor";
        }
    }

    private static class Remove implements IRuntimeAction {
        private ItemStack input;

        public Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            if (CompactorManager.recipeExists(input)) {
                CompactorManager.removeRecipe(input);
            }
        }

        @Override
        public String describe() {
            return "Removing recipe using " + input + " from compactor";
        }
    }

}
