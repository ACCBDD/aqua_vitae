package com.accbdd.aqua_vitae.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModDamageSources {
    public static final ResourceKey<DamageType> ALCOHOL_POISONING = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "alcohol_poisoning"));

    public static DamageSource alcoholPoisoning(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ALCOHOL_POISONING));
    }
}
