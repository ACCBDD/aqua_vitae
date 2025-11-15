package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.fluid.ModFluidImpl;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, MODID);
    public static final List<DeferredHolder<Fluid, FlowingFluid>> REGISTERED = new ArrayList<>();

    public static final DeferredHolder<Fluid, FlowingFluid> AQUA_VITAE = registerSource("aqua_vitae", () -> ModFluidImpl.AQUA_VITAE_PROPERTIES);
    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_AQUA_VITAE = registerFlowing("flowing_aqua_vitae", () -> ModFluidImpl.AQUA_VITAE_PROPERTIES);

    public static final DeferredHolder<Fluid, FlowingFluid> ALCOHOL = registerSource("alcohol", () -> ModFluidImpl.ALCOHOL_PROPERTIES);
    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_ALCOHOL = registerFlowing("flowing_alcohol", () -> ModFluidImpl.ALCOHOL_PROPERTIES);

    private static DeferredHolder<Fluid, FlowingFluid> registerSource(String name, Supplier<BaseFlowingFluid.Properties> properties) {
        DeferredHolder<Fluid, FlowingFluid> register = FLUIDS.register(name, () -> new BaseFlowingFluid.Source(properties.get()));
        REGISTERED.add(register);
        return register;
    }

    private static DeferredHolder<Fluid, FlowingFluid> registerFlowing(String name, Supplier<BaseFlowingFluid.Properties> properties) {
        DeferredHolder<Fluid, FlowingFluid> register = FLUIDS.register(name, () -> new BaseFlowingFluid.Flowing(properties.get()));
        REGISTERED.add(register);
        return register;
    }
}
