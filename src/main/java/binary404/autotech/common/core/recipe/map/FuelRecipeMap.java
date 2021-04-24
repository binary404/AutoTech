package binary404.autotech.common.core.recipe.map;

import binary404.autotech.common.core.recipe.core.FuelRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

public class FuelRecipeMap {

    private static final List<FuelRecipeMap> RECIPE_MAPS = new ArrayList<>();

    public final String unlocalizedName;

    private final Map<FluidKey, FuelRecipe> recipeFluidMap = new HashMap<>();
    private final List<FuelRecipe> recipeList = new ArrayList<>();

    public FuelRecipeMap(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        RECIPE_MAPS.add(this);
    }

    public static List<FuelRecipeMap> getRecipeMaps() {
        return RECIPE_MAPS;
    }

    public static FuelRecipeMap getByName(String unlocalizedName) {
        return RECIPE_MAPS.stream()
                .filter(map -> map.unlocalizedName.equals(unlocalizedName))
                .findFirst().orElse(null);
    }

    public void addRecipe(FuelRecipe fuelRecipe) {
        FluidKey fluidKey = new FluidKey(fuelRecipe.getRecipeFluid());
        if (recipeFluidMap.containsKey(fluidKey)) {
            FuelRecipe oldRecipe = recipeFluidMap.remove(fluidKey);
            recipeList.remove(oldRecipe);
        }
        recipeFluidMap.put(fluidKey, fuelRecipe);
        recipeList.add(fuelRecipe);
    }

    public boolean removeRecipe(FuelRecipe recipe) {
        if (recipeList.contains(recipe)) {
            this.recipeList.remove(recipe);
            this.recipeFluidMap.remove(new FluidKey(recipe.getRecipeFluid()));
            return true;
        }
        return false;
    }

    public FuelRecipe findRecipe(long maxVoltage, FluidStack inputFluid) {
        if (inputFluid == null)
            return null;
        FluidKey fluidKey = new FluidKey(inputFluid);
        FuelRecipe fuelRecipe = recipeFluidMap.get(fluidKey);
        return fuelRecipe != null && fuelRecipe.matches(maxVoltage, inputFluid) ? fuelRecipe : null;
    }

    public List<FuelRecipe> getRecipeList() {
        return Collections.unmodifiableList(recipeList);
    }
}
