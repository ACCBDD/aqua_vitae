package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.fluid.ModFluidImpl;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, MODID);

    public static final DeferredHolder<Fluid, FlowingFluid> TEST_FLUID = FLUIDS.register("test", () -> new BaseFlowingFluid.Source(ModFluidImpl.TEST_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_TEST_FLUID = FLUIDS.register("test_flowing", () -> new BaseFlowingFluid.Flowing(ModFluidImpl.TEST_FLUID_PROPERTIES));


}
