package binary404.autotech.common.core.manager;

import binary404.autotech.AutoTech;
import binary404.autotech.common.block.ModBlocks;
import binary404.autotech.common.core.logistics.Tier;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import binary404.autotech.common.tags.ModTags;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GrinderManager {

    private static Map<ComparableItemStack, GrinderRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    public static void init() {
        addRecipe(Tier.LV, 40000, new ItemStack(ModBlocks.copper_ore), new ItemStack(ModItems.copper_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.lead_ore_dust), 50, 30);
        addRecipe(Tier.LV, 40000, new ItemStack(ModBlocks.tin_ore), new ItemStack(ModItems.tin_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.copper_ore_dust), 40, 20);
        addRecipe(Tier.LV, 50000, new ItemStack(ModBlocks.lead_ore), new ItemStack(ModItems.lead_ore_dust, 2), new ItemStack(ModItems.silver_ore_dust), ItemStack.EMPTY, 75, 0);
        addRecipe(Tier.LV, 35000, new ItemStack(ModBlocks.silver_ore), new ItemStack(ModItems.silver_ore_dust, 2), new ItemStack(ModItems.lead_ore_dust), new ItemStack(ModItems.gold_ore_dust), 80, 70);
        addRecipe(Tier.HV, 80000, new ItemStack(ModBlocks.uranium_ore, 1), new ItemStack(ModItems.uranium_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 50000, new ItemStack(ModBlocks.nickel_ore), new ItemStack(ModItems.nickel_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.platinum_ore_dust), 60, 10);
        addRecipe(Tier.MV, 40000, new ItemStack(ModBlocks.platinum_ore), new ItemStack(ModItems.platinum_ore_dust, 2), new ItemStack(ModItems.nickel_ore_dust), new ItemStack(ModItems.copper_ore_dust), 70, 70);
        addRecipe(Tier.HV, 65000, new ItemStack(ModBlocks.titanium_ore), new ItemStack(ModItems.titanium_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 40000, new ItemStack(Blocks.IRON_ORE), new ItemStack(ModItems.iron_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 40000, new ItemStack(Blocks.GOLD_ORE), new ItemStack(ModItems.gold_ore_dust, 2), new ItemStack(ModItems.silver_ore_dust), ItemStack.EMPTY, 45, 0);

        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.copper_ingot), new ItemStack(ModItems.copper_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.tin_ingot), new ItemStack(ModItems.tin_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.lead_ingot), new ItemStack(ModItems.lead_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.silver_ingot), new ItemStack(ModItems.silver_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.uranium_ingot), new ItemStack(ModItems.uranium_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.nickel_ingot), new ItemStack(ModItems.nickel_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.platinum_ingot), new ItemStack(ModItems.platinum_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(ModItems.titanium_ingot), new ItemStack(ModItems.titanium_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.iron_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(Tier.LV, 15000, new ItemStack(Items.GOLD_INGOT), new ItemStack(ModItems.gold_dust), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);

        addRecipe(Tier.LV, 25000, new ItemStack(Blocks.STONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), 60, 30);
        addRecipe(Tier.LV, 25000, new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), new ItemStack(Items.FLINT), 50, 50);
        addRecipe(Tier.LV, 25000, new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND), new ItemStack(Items.FLINT), ItemStack.EMPTY, 60, 0);
    }

    public static void initTags() {
        for (Item dust : ModTags.Items.ORE_DUSTS.getAllElements()) {
            String dustName = dust.getRegistryName().getPath();
            dustName = dustName.replace("_dust", "");
            for (Item dust2 : Tags.Items.DUSTS.getAllElements()) {
                if (dustName.contains(dust2.getRegistryName().getPath().replace("_dust", "")) || dustName.contains(dust2.getRegistryName().getPath().replace("dust_", ""))) {
                    if (!recipeExists(new ItemStack(dust)))
                        addRecipe(Tier.LV, 20000, new ItemStack(dust), new ItemStack(dust2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
                }
            }
        }

        GrinderManager.refresh();
    }

    public static GrinderRecipe getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return null;
        }

        GrinderRecipe recipe = recipeMap.get(new ComparableItemStack(input));
        return recipe;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        return recipeExists;
    }

    public static GrinderRecipe[] getRecipeList() {
        return recipeMap.values().toArray(new GrinderRecipe[0]);
    }

    public static GrinderRecipe addRecipe(Tier minTier, int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondOutput, ItemStack thirdOutput, int secondChance, int thirdChance) {
        if (input.isEmpty() || primaryOutput.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }

        GrinderRecipe recipe = new GrinderRecipe(input, primaryOutput, secondOutput, thirdOutput, secondOutput.isEmpty() ? 0 : secondChance, thirdOutput.isEmpty() ? 0 : thirdChance, energy, minTier);
        recipeMap.put(new ComparableItemStack(input), recipe);
        return recipe;
    }

    public static void refresh() {
        Map<ComparableItemStack, GrinderRecipe> tempMap = new Object2ObjectOpenHashMap<>(recipeMap.size());
        GrinderRecipe tempRecipe;

        for (Map.Entry<ComparableItemStack, GrinderRecipe> entry : recipeMap.entrySet()) {
            tempRecipe = entry.getValue();
            tempMap.put(new ComparableItemStack(tempRecipe.input), tempRecipe);
        }
        recipeMap.clear();
        recipeMap = tempMap;
    }

    public static class GrinderRecipe {
        ItemStack input;
        ItemStack primaryOutput;
        ItemStack secondaryOutput;
        ItemStack thirdOutput;
        int secondaryChance;
        int thirdChance;
        int energy;
        Tier minTier;

        GrinderRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, ItemStack thirdOutput, int secondaryChance, int thirdChance, int energy, Tier minTier) {
            this.input = input;
            this.primaryOutput = primaryOutput;
            this.secondaryOutput = secondaryOutput;
            this.thirdOutput = thirdOutput;
            this.secondaryChance = secondaryChance;
            this.thirdChance = thirdChance;
            this.energy = energy;
            this.minTier = minTier;
        }

        public ItemStack getInput() {
            return input;
        }

        public ItemStack getPrimaryOutput() {
            return primaryOutput;
        }

        public ItemStack getSecondaryOutput() {
            return secondaryOutput;
        }

        public ItemStack getThirdOutput() {
            return thirdOutput;
        }

        public int getSecondaryChance() {
            return secondaryChance;
        }

        public int getThirdChance() {
            return thirdChance;
        }

        public int getEnergy() {
            return energy;
        }

        public Tier getMinTier() {
            return minTier;
        }
    }

}
