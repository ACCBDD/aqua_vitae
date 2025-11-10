package com.accbdd.aqua_vitae.fluid;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import com.accbdd.aqua_vitae.registry.ModFluidTypes;
import com.accbdd.aqua_vitae.registry.ModFluids;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class ModFluidImpl {
    public static final BaseFlowingFluid.Properties AQUA_VITAE_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.AQUA_VITAE_TYPE,
            () -> ModFluids.AQUA_VITAE.get(),
            () -> ModFluids.FLOWING_AQUA_VITAE.get())
            .bucket(ModItems.AQUA_VITAE_BUCKET).block(ModBlocks.AQUA_VITAE);

    public static final BaseFlowingFluid.Properties TEQUILA_BLANCO_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.TEQUILA_BLANCO_TYPE,
            () -> ModFluids.TEQUILA_BLANCO.get(),
            () -> ModFluids.FLOWING_TEQUILA_BLANCO.get())
            .bucket(ModItems.TEQUILA_BLANCO_BUCKET).block(ModBlocks.TEQUILA_BLANCO);

    public static final BaseFlowingFluid.Properties TEQUILA_REPOSADO_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.TEQUILA_REPOSADO_TYPE,
            () -> ModFluids.TEQUILA_REPOSADO.get(),
            () -> ModFluids.FLOWING_TEQUILA_REPOSADO.get())
            .bucket(ModItems.TEQUILA_REPOSADO_BUCKET).block(ModBlocks.TEQUILA_REPOSADO);

    public static final BaseFlowingFluid.Properties TEQUILA_ANEJO_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.TEQUILA_ANEJO_TYPE,
            () -> ModFluids.TEQUILA_ANEJO.get(),
            () -> ModFluids.FLOWING_TEQUILA_ANEJO.get())
            .bucket(ModItems.TEQUILA_ANEJO_BUCKET).block(ModBlocks.TEQUILA_ANEJO);
}
