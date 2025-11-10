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

    public static final BaseFlowingFluid.Properties TEQUILA_BLANCO_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.TEQUILA_BLANCO_TYPE::value,
            ModFluids.TEQUILA_BLANCO::value,
            ModFluids.FLOWING_TEQUILA_BLANCO::value)
            .bucket(ModItems.TEQUILA_BLANCO_BUCKET).block(ModBlocks.TEQUILA_BLANCO);

    public static final BaseFlowingFluid.Properties TEQUILA_REPOSADO_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.TEQUILA_REPOSADO_TYPE::value,
            ModFluids.TEQUILA_REPOSADO::value,
            ModFluids.FLOWING_TEQUILA_REPOSADO::value)
            .bucket(ModItems.TEQUILA_REPOSADO_BUCKET).block(ModBlocks.TEQUILA_REPOSADO);

    public static final BaseFlowingFluid.Properties TEQUILA_ANEJO_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.TEQUILA_ANEJO_TYPE::value,
            ModFluids.TEQUILA_ANEJO::value,
            ModFluids.FLOWING_TEQUILA_ANEJO::value)
            .bucket(ModItems.TEQUILA_ANEJO_BUCKET).block(ModBlocks.TEQUILA_ANEJO);
}
