package binary404.autotech.common.core.plugin.crafttweaker;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.manager.ArcFurnaceManager;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeGlobals;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.autotech.arcFurnace")
public class ArcFurnaceCraftTweaker {

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
        private Tier minTier;

        public Add(ItemStack input, ItemStack output, int energy, Tier minTier) {
            this.input = input;
            this.energy = energy;
            this.output = output;
            this.minTier = minTier;
        }

        @Override
        public void apply() {
            ArcFurnaceManager.addRecipe(minTier, energy, input, output);
        }

        @Override
        public String describe() {
            return "Adding recipe for " + output + " To arc furnace";
        }
    }

    private static class Remove implements IRuntimeAction {
        private ItemStack input;

        public Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            if (ArcFurnaceManager.recipeExists(input)) {
                ArcFurnaceManager.removeRecipe(input);
            }
        }

        @Override
        public String describe() {
            return "Removing recipe using " + input + " from arc furnace";
        }
    }

}
