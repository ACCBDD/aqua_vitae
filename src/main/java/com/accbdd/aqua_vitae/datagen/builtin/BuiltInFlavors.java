package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.api.Flavor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import java.util.Map;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.flavor;

public class BuiltInFlavors {
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> BREADY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> FRUITY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> LUMINOUS;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> FLORAL;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> SULFURIC;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> EARTHY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> FUNKY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> STARCHY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> SOUR;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> SWEET;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> CRISP;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> HEAVY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> OZONE;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> WISPY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> NUTTY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> TOASTY;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> COCOA;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> LICORICE;
    public static final Map.Entry<ResourceKey<Flavor>, Flavor> ACRID;

    static {
        FRUITY = flavor("fruity", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 0))
                .kiln(List.of(), 1)
                .build());
        LUMINOUS = flavor("luminous", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0))
                .effect(new MobEffectInstance(MobEffects.GLOWING, 600, 0))
                .build());
        FLORAL = flavor("floral", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0))
                .build());
        SULFURIC = flavor("sulfuric", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0))
                .build());
        EARTHY = flavor("earthy", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 0))
                .build());
        FUNKY = flavor("funky", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.INVISIBILITY, 400, 0))
                .build());
        STARCHY = flavor("starchy", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0))
                .build());
        SOUR = flavor("sour", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.HARM, 1, 0))
                .build());
        SWEET = flavor("sweet", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.HEAL, 1, 0))
                .age(List.of(), 2)
                .build());
        CRISP = flavor("crisp", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0))
                .build());
        HEAVY = flavor("heavy", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 0))
                .build());
        OZONE = flavor("ozone", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.LEVITATION, 300, 0))
                .build());
        WISPY = flavor("wispy", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 0))
                .build());
        ACRID = flavor("acrid", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.HARM, 1, 0))
                .age(List.of(), 2)
                .build());
        LICORICE = flavor("licorice", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 2))
                .build());
        COCOA = flavor("cocoa", new Flavor.Builder()
                .kiln(List.of(LICORICE.getKey(), ACRID.getKey()), 4)
                .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 2))
                .build());
        TOASTY = flavor("toasty", new Flavor.Builder()
                .kiln(List.of(COCOA.getKey()), 3)
                .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1))
                .build());
        NUTTY = flavor("nutty", new Flavor.Builder()
                .kiln(List.of(TOASTY.getKey()), 2)
                .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 300, 1))
                .build());
        BREADY = flavor("bready", new Flavor.Builder()
                .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0))
                .malt(List.of(NUTTY.getKey(), FRUITY.getKey()), 0)
                .build());
    }
}
