package com.accbdd.aqua_vitae.fluid;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import com.accbdd.aqua_vitae.registry.ModFluidTypes;
import com.accbdd.aqua_vitae.registry.ModFluids;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class ModFluidImpl {
    public static final BaseFlowingFluid.Properties AQUA_VITAE_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.AQUA_VITAE_TYPE::value,
            ModFluids.AQUA_VITAE::value,
            ModFluids.FLOWING_AQUA_VITAE::value)
            .bucket(ModItems.AQUA_VITAE_BUCKET).block(ModBlocks.AQUA_VITAE);

    public static final BaseFlowingFluid.Properties ALCOHOL_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.ALCOHOL_TYPE::value,
            ModFluids.ALCOHOL::value,
            ModFluids.FLOWING_ALCOHOL::value);
}
