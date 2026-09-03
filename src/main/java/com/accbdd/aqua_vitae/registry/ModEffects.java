package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.effect.HungoverEffect;
import com.accbdd.aqua_vitae.effect.IntoxicatedEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);

    public static final Holder<MobEffect> INTOXICATED = EFFECTS.register("intoxicated", IntoxicatedEffect::new);
    public static final Holder<MobEffect> HUNGOVER = EFFECTS.register("hungover", HungoverEffect::new);
}
