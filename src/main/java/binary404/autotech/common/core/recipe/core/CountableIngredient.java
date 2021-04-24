package binary404.autotech.common.core.recipe.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class CountableIngredient implements Predicate<ItemStack> {

    public static CountableIngredient from(ItemStack stack) {
        return new CountableIngredient(Ingredient.fromStacks(stack), stack.getCount());
    }

    public static CountableIngredient from(ItemStack stack, int amount) {
        return new CountableIngredient(Ingredient.fromStacks(stack), amount);
    }

    public static CountableIngredient from(ITag.INamedTag<Item> tag, int amount) {
        return new CountableIngredient(Ingredient.fromTag(tag), amount);
    }

    private Ingredient ingredient;
    private int count;

    public CountableIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean test(ItemStack stack) {
        if (stack.getCount() != this.getCount())
            return false;
        else
            return this.ingredient.test(stack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountableIngredient that = (CountableIngredient) o;
        return count == that.count &&
                Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, count);
    }

    @Override
    public String toString() {
        return "CountableIngredient{" +
                "ingredient=" + Arrays.toString(ingredient.getMatchingStacks()) +
                ", count=" + count +
                '}';
    }

}
