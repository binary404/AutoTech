package binary404.autotech.common.core.manager;

import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class CentrifugeManager {

    private static Map<ComparableItemStack, CentrifugeRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    public static void init() {
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.copper_ore_dust, 2), new ItemStack(ModItems.copper_dust, 3), new ItemStack(ModItems.iron_dust, 2), 50);
        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.tin_ore_dust, 2), new ItemStack(ModItems.tin_dust, 3), new ItemStack(ModItems.copper_dust, 2), 60);

        addRecipe(Tier.MV, 100000, new ItemStack(ModItems.iron_ore_dust, 2), new ItemStack(ModItems.iron_dust, 3), new ItemStack(ModItems.lead_dust), 70);

        addRecipe(Tier.MV, 150000, new ItemStack(ModItems.netherite_ore_dust, 2), new ItemStack(ModItems.netherite_dust, 3), ItemStack.EMPTY, 0);

        refresh();
    }

    public static CentrifugeRecipe getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return null;
        }
        CentrifugeRecipe recipe = recipeMap.get(new ComparableItemStack(input));
        return recipe;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;

        return recipeExists;
    }

    public static CentrifugeRecipe[] getRecipeList() {
        return recipeMap.values().toArray(new CentrifugeRecipe[0]);
    }

    public static CentrifugeRecipe addRecipe(Tier minTier, int energy, ItemStack input, ItemStack output1, ItemStack output2, int secondChance) {
        if (input.isEmpty() || output1.isEmpty() || energy <= 0 || recipeExists(input))
            return null;

        CentrifugeRecipe recipe = new CentrifugeRecipe(input, output1, output2, secondChance, energy, minTier);
        recipeMap.put(new ComparableItemStack(input), recipe);
        return recipe;
    }

    public static void refresh() {
        Map<ComparableItemStack, CentrifugeRecipe> tempMap = new Object2ObjectOpenHashMap<>(recipeMap.size());
        CentrifugeRecipe tempRecipe;

        for (Map.Entry<ComparableItemStack, CentrifugeRecipe> entry : recipeMap.entrySet()) {
            tempRecipe = entry.getValue();
            tempMap.put(new ComparableItemStack(tempRecipe.input), tempRecipe);
        }
        recipeMap.clear();
        recipeMap = tempMap;
    }

    public static class CentrifugeRecipe {
        ItemStack input;
        ItemStack output1;
        ItemStack output2;
        int secondChance;
        int energy;
        Tier minTier;

        CentrifugeRecipe(ItemStack input, ItemStack output1, ItemStack output2, int secondChance, int energy, Tier minTier) {
            this.input = input;
            this.output1 = output1;
            this.output2 = output2;
            this.energy = energy;
            this.minTier = minTier;
            this.secondChance = secondChance;
        }

        @Override
        public String toString() {
            return input + " " + output1;
        }

        public ItemStack getInput() {
            return input;
        }

        public ItemStack getOutput1() {
            return output1;
        }

        public ItemStack getOutput2() {
            return output2;
        }

        public int getEnergy() {
            return energy;
        }

        public Tier getMinTier() {
            return minTier;
        }

        public int getSecondChance() {
            return secondChance;
        }
    }

}
