package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.component.FluidStackComponent;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidStackComponent>> FLUIDSTACK = COMPONENTS.registerComponentType("fluid_stack",
            (builder) -> builder.persistent(FluidStackComponent.CODEC).networkSynchronized(FluidStackComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> STARCH = COMPONENTS.registerComponentType("starch",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
}
