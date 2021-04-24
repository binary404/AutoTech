package binary404.autotech.common.core.plugin.crafttweaker;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("mods.autotech.recipe.ChancedEntry")
@ZenRegister
public class ChancedEntry {

    private final IItemStack output;
    private final int chance;
    private final int boostPerTier;

    public ChancedEntry(IItemStack output, int chance, int boostPerTier) {
        this.output = output;
        this.chance = chance;
        this.boostPerTier = boostPerTier;
    }

    @ZenCodeType.Getter("output")
    public IItemStack getOutput() {
        return output;
    }

    @ZenCodeType.Getter("chance")
    public int getChance() {
        return chance;
    }

    @ZenCodeType.Getter("boostPerTier")
    public int getBoostPerTier() {
        return boostPerTier;
    }

}
