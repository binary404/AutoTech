package binary404.autotech.common.core;

import binary404.autotech.common.block.ModBlocks;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Map;

public class GrinderManager {

    private static Map<ComparableItemStack, GrinderRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    public static void init() {
        addRecipe(25000, new ItemStack(ModBlocks.copper_ore), new ItemStack(ModItems.copper_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.lead_ore_dust), 50, 30);
        addRecipe(25000, new ItemStack(ModBlocks.tin_ore), new ItemStack(ModItems.tin_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.copper_ore_dust), 40, 20);
        addRecipe(30000, new ItemStack(ModBlocks.lead_ore), new ItemStack(ModItems.lead_ore_dust, 2), new ItemStack(ModItems.silver_ore_dust), ItemStack.EMPTY, 75, 0);
        addRecipe(20000, new ItemStack(ModBlocks.silver_ore), new ItemStack(ModItems.silver_ore_dust, 2), new ItemStack(ModItems.lead_ore_dust), new ItemStack(ModItems.gold_ore_dust), 80, 70);
        addRecipe(23000, new ItemStack(ModBlocks.uranium_ore), new ItemStack(ModItems.uranium_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(30000, new ItemStack(ModBlocks.nickel_ore), new ItemStack(ModItems.nickel_ore_dust, 2), new ItemStack(ModItems.iron_ore_dust), new ItemStack(ModItems.platinum_ore_dust), 60, 10);
        addRecipe(40000, new ItemStack(ModBlocks.platinum_ore), new ItemStack(ModItems.platinum_ore_dust, 2), new ItemStack(ModItems.nickel_ore_dust), new ItemStack(ModItems.copper_ore_dust), 70, 70);
        addRecipe(65000, new ItemStack(ModBlocks.titanium_ore), new ItemStack(ModItems.titanium_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(30000, new ItemStack(Blocks.IRON_ORE), new ItemStack(ModItems.iron_ore_dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
        addRecipe(20000, new ItemStack(Blocks.GOLD_ORE), new ItemStack(ModItems.gold_ore_dust, 2), new ItemStack(ModItems.silver_ore_dust), ItemStack.EMPTY, 45, 0);

        for (Block ore : Tags.Blocks.ORES.getAllElements()) {
            for (Item dust : ModTags.Items.ORE_DUSTS.getAllElements()) {
                String dustName = dust.getRegistryName().getPath();
                dustName = dustName.replace("_dust", "");
                String oreName = ore.getRegistryName().getPath();
                if (oreName.contains(dust.getRegistryName().getPath().replace("_dust", "")) || oreName.contains(dust.getRegistryName().getPath().replace("dust_", ""))) {
                    addRecipe(20000, new ItemStack(ore), new ItemStack(dust, 2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
                }
                for (Item dust2 : Tags.Items.DUSTS.getAllElements()) {
                    if (dustName.contains(dust2.getRegistryName().getPath().replace("_dust", "")) || dustName.contains(dust2.getRegistryName().getPath().replace("dust_", ""))) {
                        addRecipe(30000, new ItemStack(dust), new ItemStack(dust2), ItemStack.EMPTY, ItemStack.EMPTY, 0, 0);
                    }
                }
            }
        }

    }

    public static GrinderRecipe getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return null;
        }

        GrinderRecipe recipe = recipeMap.get(new ComparableItemStack(input));
        return recipe;
    }

    public static boolean recipeExists(ItemStack input) {
        return getRecipe(input) != null;
    }

    public static GrinderRecipe addRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondOutput, ItemStack thirdOutput, int secondChance, int thirdChance) {
        if (input.isEmpty() || primaryOutput.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }

        GrinderRecipe recipe = new GrinderRecipe(input, primaryOutput, secondOutput, thirdOutput, secondOutput.isEmpty() ? 0 : secondChance, thirdOutput.isEmpty() ? 0 : thirdChance, energy);
        recipeMap.put(new ComparableItemStack(input), recipe);
        return recipe;
    }

    public static class GrinderRecipe {
        ItemStack input;
        ItemStack primaryOutput;
        ItemStack secondaryOutput;
        ItemStack thirdOutput;
        int secondaryChance;
        int thirdChance;
        int energy;

        GrinderRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, ItemStack thirdOutput, int secondaryChance, int thirdChance, int energy) {
            this.input = input;
            this.primaryOutput = primaryOutput;
            this.secondaryOutput = secondaryOutput;
            this.thirdOutput = thirdOutput;
            this.secondaryChance = secondaryChance;
            this.thirdChance = thirdChance;
            this.energy = energy;
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

    }

    public static class ComparableItemStack {
        public Item item = Items.AIR;

        public ComparableItemStack(ItemStack stack) {
            this.item = stack.getItem();
        }

        public boolean isEqual(ComparableItemStack other) {
            if (other == null) {
                return false;
            }

            if (item == other.item) {
                return true;
            }

            return false;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ComparableItemStack && isEqual((ComparableItemStack) obj);
        }
    }

}
