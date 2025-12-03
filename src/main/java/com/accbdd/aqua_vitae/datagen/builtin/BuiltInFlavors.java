package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.recipe.Flavor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Map;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.flavor;

public class BuiltInFlavors {
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> FRUITY =
            flavor("fruity", new Flavor.Builder().effect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 0)));

    public static final Map.Entry<ResourceKey<Flavor>, Flavor> SOUR =
            flavor("sour", new Flavor.Builder().effect(new MobEffectInstance(MobEffects.HARM, 1, 0)));

    public static final Map.Entry<ResourceKey<Flavor>, Flavor> SWEET =
            flavor("sweet", new Flavor.Builder().effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0)));
}
