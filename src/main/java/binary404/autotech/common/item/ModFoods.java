package binary404.autotech.common.item;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {

    public static Food applePie = new Food.Builder().hunger(10).saturation(1.0F).build();
    public static Food goldenApplePie = new Food.Builder().hunger(14).saturation(1.4F).effect(new EffectInstance(Effects.REGENERATION, 100, 1), 1.0F).effect(new EffectInstance(Effects.ABSORPTION, 2400, 0), 1.0F).build();
    public static Food enchantedGoldenApplePie = new Food.Builder().hunger(18).saturation(2.0F).effect(new EffectInstance(Effects.REGENERATION, 400, 1), 1.0F).effect(new EffectInstance(Effects.RESISTANCE, 6000, 0), 1.0F).effect(new EffectInstance(Effects.FIRE_RESISTANCE, 6000, 0), 1.0F).effect(new EffectInstance(Effects.ABSORPTION, 2400, 3), 1.0F).effect(new EffectInstance(Effects.SATURATION, 8, 10), 1.0F).build();

}