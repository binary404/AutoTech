package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

import java.util.ArrayList;
import java.util.List;

public class EmpowererManager {

    private static List<EmpowererRecipe> recipeMap = new ArrayList<>();

    public static void init() {
        addRecipe(30000, new ItemStack(ModItems.copper_plate), new ItemStack(ModItems.steel_ingot), new ItemStack(ModItems.steel_ingot), new ItemStack(ModItems.steel_ingot), new ItemStack(ModItems.steel_ingot), new ItemStack(ModItems.steel_rod));
    }

    public static EmpowererRecipe getRecipe(ItemStack input, ItemStack stand1, ItemStack stand2, ItemStack stand3, ItemStack stand4) {
        for (EmpowererRecipe recipe : recipeMap) {
            if (recipe.recipeMatches(input, stand1, stand2, stand3, stand4)) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean recipeExists(ItemStack input, ItemStack stand1, ItemStack stand2, ItemStack stand3, ItemStack stand4) {
        boolean recipeExists = getRecipe(input, stand1, stand2, stand3, stand4) != null;

        return recipeExists;
    }

    public static EmpowererRecipe[] getRecipeList() {
        return recipeMap.toArray(new EmpowererRecipe[0]);
    }

    public static EmpowererRecipe addRecipe(int energy, ItemStack input, ItemStack stand1, ItemStack stand2, ItemStack stand3, ItemStack stand4, ItemStack output) {
        if (input.isEmpty() || stand1.isEmpty() || stand2.isEmpty() || stand3.isEmpty() || stand4.isEmpty() || output.isEmpty() || energy <= 0 || recipeExists(input, stand1, stand2, stand3, stand4))
            return null;

        EmpowererRecipe recipe = new EmpowererRecipe(input, stand1, stand2, stand3, stand4, energy, output);
        recipeMap.add(recipe);
        return recipe;
    }

    public static void removeRecipe(ItemStack input, ItemStack stand1, ItemStack stand2, ItemStack stand3, ItemStack stand4) {
        EmpowererRecipe recipe = getRecipe(input, stand1, stand2, stand3, stand4);
        if (recipe != null)
            recipeMap.remove(recipe);
    }

    public static class EmpowererRecipe implements IMachineRecipe {
        ItemStack input;
        ItemStack stand1;
        ItemStack stand2;
        ItemStack stand3;
        ItemStack stand4;
        int energy;
        ItemStack output;

        public EmpowererRecipe(ItemStack input, ItemStack stand1, ItemStack stand2, ItemStack stand3, ItemStack stand4, int energy, ItemStack output) {
            this.input = input;
            this.stand1 = stand1;
            this.stand2 = stand2;
            this.stand3 = stand3;
            this.stand4 = stand4;
            this.energy = energy;
            this.output = output;
        }

        @Override
        public ItemStack getInput() {
            return input;
        }

        @Override
        public ItemStack getOutput() {
            return output;
        }

        @Override
        public Tier getMinTier() {
            return Tier.LV;
        }

        @Override
        public int getEnergy() {
            return energy;
        }

        @Override
        public ITag.INamedTag<Item> getInputTag() {
            return null;
        }

        @Override
        public int getInputCount() {
            return input.getCount();
        }

        public boolean recipeMatches(ItemStack input, ItemStack stand1, ItemStack stand2, ItemStack stand3, ItemStack stand4) {
            if (this.input.getItem() != input.getItem())
                return false;
            List<ItemStack> matches = new ArrayList<>();
            ItemStack[] stacks = {stand1, stand2, stand3, stand4};
            boolean[] check = {true, true, true, true};
            for (ItemStack s : stacks) {
                if (check[0] && this.stand1.getItem() == s.getItem()) {
                    matches.add(this.stand1);
                    check[0] = false;
                } else if (check[1] && this.stand2.getItem() == s.getItem()) {
                    matches.add(this.stand1);
                    check[1] = false;
                } else if (check[2] && this.stand3.getItem() == s.getItem()) {
                    matches.add(this.stand3);
                    check[2] = false;
                } else if (check[3] && this.stand4.getItem() == s.getItem()) {
                    matches.add(this.stand4);
                    check[3] = false;
                }
            }
            return matches.size() == 4;
        }
    }

}
