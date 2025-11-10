package com.accbdd.aqua_vitae.registry;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, MODID);
    public static final Map<String, FluidSet> REGISTERED = new HashMap<>();

    public static final DeferredHolder<FluidType, FluidType> AQUA_VITAE_TYPE = register("aqua_vitae", 0xEEFFFFFF);
    public static final DeferredHolder<FluidType, FluidType> TEQUILA_BLANCO_TYPE = register("tequila_blanco", 0xEEE8F1FF);
    public static final DeferredHolder<FluidType, FluidType> TEQUILA_REPOSADO_TYPE = register("tequila_reposado", 0xDDFFE291);
    public static final DeferredHolder<FluidType, FluidType> TEQUILA_ANEJO_TYPE = register("tequila_anejo", 0xCCFCBA03);

    public static DeferredHolder<FluidType, FluidType> register(String name, int color) {
        DeferredHolder<FluidType, FluidType> registered = FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()
                .descriptionId("block.aqua_vitae."+name)
                .fallDistanceModifier(0)
                .canExtinguish(true)
                .canConvertToSource(false)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(false)
                .density(0)
                .temperature(0)
                .viscosity(0)));
        REGISTERED.put("block.aqua_vitae."+name, new FluidSet(name, registered, color));
        return registered;
    }

    public record FluidSet(String name, DeferredHolder<FluidType, FluidType> type, int color) {}
}
