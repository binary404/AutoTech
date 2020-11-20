package binary404.autotech.common.core.manager;

import binary404.autotech.common.container.machine.SawMillContainer;
import binary404.autotech.common.core.util.ComparableItemStack;
import binary404.autotech.common.item.ModItems;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SawMillManager {

    private static Map<ComparableItemStack, SawMillRecipe> recipeMap = new Object2ObjectOpenHashMap<>();

    public static void init() {
    }

    public static void initTags() {
        for (Block log : BlockTags.LOGS.getAllElements()) {
            for (Block plank : BlockTags.PLANKS.getAllElements()) {
                String logName = log.getRegistryName().getPath().replace("_log", "");
                String plankName = plank.getRegistryName().getPath().replace("_planks", "");
                if (logName.equals(plankName)) {
                    addRecipe(30000, new ItemStack(log), new ItemStack(plank, 6), new ItemStack(ModItems.saw_dust), 55);
                }
                addRecipe(30000, new ItemStack(plank), new ItemStack(Items.STICK, 6), new ItemStack(ModItems.saw_dust), 68);
            }
        }
        addRecipe(30000, new ItemStack(Items.STICK), new ItemStack(ModItems.saw_dust, 2), new ItemStack(ModItems.saw_dust), 90);
        SawMillManager.refresh();
    }

    public static SawMillRecipe getRecipe(ItemStack input) {
        if (input.isEmpty()) {
            return null;
        }

        SawMillRecipe recipe = recipeMap.get(new ComparableItemStack(input));
        return recipe;
    }

    public static boolean recipeExists(ItemStack input) {
        boolean recipeExists = getRecipe(input) != null;
        return recipeExists;
    }

    public static SawMillRecipe removeRecipe(ItemStack input) {
        return recipeMap.remove(ComparableItemStack.convert(input));
    }

    public static SawMillRecipe[] getRecipeList() {
        return recipeMap.values().toArray(new SawMillRecipe[0]);
    }

    public static SawMillRecipe addRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondOutput, int secondChance) {
        if (input.isEmpty() || primaryOutput.isEmpty() || energy <= 0 || recipeExists(input)) {
            return null;
        }

        SawMillRecipe recipe = new SawMillRecipe(input, primaryOutput, secondOutput, secondOutput.isEmpty() ? 0 : secondChance, energy);
        recipeMap.put(new ComparableItemStack(input), recipe);
        return recipe;
    }

    public static void refresh() {
        Map<ComparableItemStack, SawMillRecipe> tempMap = new Object2ObjectOpenHashMap<>(recipeMap.size());
        SawMillRecipe tempRecipe;

        for (Map.Entry<ComparableItemStack, SawMillRecipe> entry : recipeMap.entrySet()) {
            tempRecipe = entry.getValue();
            tempMap.put(new ComparableItemStack(tempRecipe.input), tempRecipe);
        }
        recipeMap.clear();
        recipeMap = tempMap;
    }

    public static class SawMillRecipe {

        final ItemStack input;
        final ItemStack primaryOutput;
        final ItemStack secondaryOutput;
        final int secondaryChance;
        final int energy;

        SawMillRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance, int energy) {

            this.input = input;
            this.primaryOutput = primaryOutput;
            this.secondaryOutput = secondaryOutput;
            this.secondaryChance = secondaryChance;
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

        public int getSecondaryOutputChance() {

            return secondaryChance;
        }

        public int getEnergy() {

            return energy;
        }
    }
}
