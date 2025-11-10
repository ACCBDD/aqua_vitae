package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.component.FluidStackComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidStackComponent>> FLUIDSTACK = COMPONENTS.registerComponentType("fluid_stack",
            (builder) -> builder.persistent(FluidStackComponent.CODEC).networkSynchronized(FluidStackComponent.STREAM_CODEC));
}
