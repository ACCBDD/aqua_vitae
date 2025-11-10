package com.accbdd.aqua_vitae.fluid;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import com.accbdd.aqua_vitae.registry.ModFluidTypes;
import com.accbdd.aqua_vitae.registry.ModFluids;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class ModFluidImpl {
    public static final BaseFlowingFluid.Properties TEST_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(ModFluidTypes.TEST_FLUID_TYPE, ModFluids.TEST_FLUID, ModFluids.FLOWING_TEST_FLUID).bucket(ModItems.TEST_BUCKET_ITEM).block(ModBlocks.TEST_FLUID);
}
