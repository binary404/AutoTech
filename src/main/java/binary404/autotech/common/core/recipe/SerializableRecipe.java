package binary404.autotech.common.core.recipe;

import binary404.autotech.common.container.core.BlankIInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class SerializableRecipe implements IRecipe<BlankIInventory> {

    protected final ResourceLocation recipeId;

    protected SerializableRecipe(ResourceLocation recipeId) {

        this.recipeId = recipeId;
    }

    // region IRecipe
    @Override
    public boolean matches(BlankIInventory inv, World worldIn) {

        return true;
    }

    @Override
    public ItemStack getCraftingResult(BlankIInventory inv) {

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {

        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {

        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic() {

        return true;
    }

    @Override
    public ResourceLocation getId() {

        return recipeId;
    }

    @Override
    public abstract IRecipeSerializer<?> getSerializer();

    @Override
    public abstract IRecipeType<?> getType();

}
