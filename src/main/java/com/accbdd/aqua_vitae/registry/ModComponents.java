package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.FermentingPropertiesComponent;
import com.accbdd.aqua_vitae.component.FluidStackComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidStackComponent>> FLUIDSTACK = COMPONENTS.registerComponentType("fluid_stack",
            (builder) -> builder.persistent(FluidStackComponent.CODEC).networkSynchronized(FluidStackComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PrecursorPropertiesComponent>> PRECURSOR_PROPERTIES = COMPONENTS.registerComponentType("precursor_properties",
            builder -> builder.persistent(PrecursorPropertiesComponent.CODEC).networkSynchronized(PrecursorPropertiesComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FermentingPropertiesComponent>> FERMENTING_PROPERTIES = COMPONENTS.registerComponentType("fermenting_properties",
            builder -> builder.persistent(FermentingPropertiesComponent.CODEC).networkSynchronized(FermentingPropertiesComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AlcoholPropertiesComponent>> ALCOHOL_PROPERTIES = COMPONENTS.registerComponentType("alcohol_properties",
            builder -> builder.persistent(AlcoholPropertiesComponent.CODEC).networkSynchronized(AlcoholPropertiesComponent.STREAM_CODEC));
}
