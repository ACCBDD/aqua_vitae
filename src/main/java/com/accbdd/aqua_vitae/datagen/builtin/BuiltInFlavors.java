package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Map;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.flavor;

public class BuiltInFlavors {
    public static final Map.Entry<ResourceKey<BrewingIngredient.Flavor>, BrewingIngredient.Flavor> FRUITY =
            flavor("fruity", new BrewingIngredient.Flavor.Builder().effect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 0)));

    public static final Map.Entry<ResourceKey<BrewingIngredient.Flavor>, BrewingIngredient.Flavor> SOUR =
            flavor("sour", new BrewingIngredient.Flavor.Builder().effect(new MobEffectInstance(MobEffects.HARM, 1, 0)));

    public static final Map.Entry<ResourceKey<BrewingIngredient.Flavor>, BrewingIngredient.Flavor> SWEET =
            flavor("sweet", new BrewingIngredient.Flavor.Builder().effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0)));
}
