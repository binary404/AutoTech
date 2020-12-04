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
    public static void addRecipe(IItemStack input, IItemStack input2, IItemStack output, int energy, int tier) {
        CraftTweakerAPI.apply(new Add(input.getInternal(), input2.getInternal(), output.getInternal(), energy, Tier.values()[tier]));
    }

    @ZenCodeType.Method
    public static void removeRecipe(IItemStack input, IItemStack input2) {
        CraftTweakerAPI.apply(new Remove(input.getInternal(), input2.getInternal()));
    }

    private static class Add implements IRuntimeAction {
        private ItemStack input, input2, output;
        private int energy;
        private Tier minTier;

        public Add(ItemStack input, ItemStack input2, ItemStack output, int energy, Tier minTier) {
            this.input = input;
            this.input2 = input2;
            this.energy = energy;
            this.output = output;
            this.minTier = minTier;
        }

        @Override
        public void apply() {
            ArcFurnaceManager.addRecipe(minTier, energy, input, input2, output);
        }

        @Override
        public String describe() {
            return "Adding recipe for " + output + " To arc furnace";
        }
    }

    private static class Remove implements IRuntimeAction {
        private ItemStack input, input2;

        public Remove(ItemStack input, ItemStack input2) {
            this.input = input;
            this.input2 = input2;
        }

        @Override
        public void apply() {
            if (ArcFurnaceManager.recipeExists(input, input2)) {
                ArcFurnaceManager.removeRecipe(input, input2);
            }
        }

        @Override
        public String describe() {
            return "Removing recipe using " + input + " from arc furnace";
        }
    }

}
