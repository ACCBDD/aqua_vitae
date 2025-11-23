package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.fluid.AlcoholFluidType;
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

    public static final DeferredHolder<FluidType, FluidType> AQUA_VITAE_TYPE = register("aqua_vitae", 0xAAFFFFFF);
    public static final DeferredHolder<FluidType, FluidType> ALCOHOL_TYPE = register("alcohol", 0xEEDDDDDD);
    public static final DeferredHolder<FluidType, FluidType> WORT_TYPE = register("wort", 0xEE4F2500);

    public static DeferredHolder<FluidType, FluidType> register(String name, int color) {
        DeferredHolder<FluidType, FluidType> registered = FLUID_TYPES.register(name, () -> new AlcoholFluidType(name));
        REGISTERED.put("block.aqua_vitae." + name, new FluidSet(name, registered, color));
        return registered;
    }

    public record FluidSet(String name, DeferredHolder<FluidType, FluidType> type, int color) {
    }
}
