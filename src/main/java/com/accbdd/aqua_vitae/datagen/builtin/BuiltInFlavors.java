package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.recipe.Flavor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import java.util.Map;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.flavor;

public class BuiltInFlavors {
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> BREADY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> NUTTY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> TOASTY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> COCOA;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> LICORICE;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> ACRID;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> FRUITY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> SOUR;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> SWEET;

    static {
        FRUITY = flavor("fruity", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 0))
                .kiln(List.of(), 1)
                .build());
        SOUR = flavor("sour", new Flavor.Builder().effect(new MobEffectInstance(MobEffects.HARM, 1, 0))
                .build());
        SWEET = flavor("sweet", new Flavor.Builder().effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0))
                .build());
        ACRID = flavor("acrid", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.HARM, 1, 0))
                .age(List.of(), 2)
                .build());
        LICORICE = flavor("licorice", new Flavor.Builder()
                .build());
        COCOA = flavor("cocoa", new Flavor.Builder()
                .kiln(List.of(LICORICE.getKey(), ACRID.getKey()), 4)
                .build());
        TOASTY = flavor("toasty", new Flavor.Builder()
                .kiln(List.of(COCOA.getKey()), 3)
                .build());
        NUTTY = flavor("nutty", new Flavor.Builder()
                .kiln(List.of(TOASTY.getKey()), 2)
                .build());
        BREADY = flavor("bready", new Flavor.Builder()
                .kiln(List.of(NUTTY.getKey(), FRUITY.getKey()), 1)
                .build());
    }
}
