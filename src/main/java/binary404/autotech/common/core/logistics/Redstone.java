package binary404.autotech.common.core.logistics;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public enum Redstone {

    IGNORE(TextFormatting.DARK_GRAY),
    ON(TextFormatting.RED),
    OFF(TextFormatting.DARK_RED);

    private final TextFormatting color;

    Redstone(TextFormatting color) {
        this.color = color;
    }

    public Redstone next() {
        int i = ordinal() + 1;
        return values()[i > 2 ? 0 : i];
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("info.autotech.redstone.mode." + name().toLowerCase(), this.color).mergeStyle(TextFormatting.GRAY);
    }

}
