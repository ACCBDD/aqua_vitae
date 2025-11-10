package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.block.KegBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<LiquidBlock> AQUA_VITAE = registerLiquidBlock("aqua_vitae", ModFluids.AQUA_VITAE);
    public static final DeferredBlock<LiquidBlock> TEQUILA_BLANCO = registerLiquidBlock("tequila_blanco", ModFluids.TEQUILA_BLANCO);
    public static final DeferredBlock<LiquidBlock> TEQUILA_REPOSADO = registerLiquidBlock("tequila_reposado", ModFluids.TEQUILA_REPOSADO);
    public static final DeferredBlock<LiquidBlock> TEQUILA_ANEJO = registerLiquidBlock("tequila_anejo", ModFluids.TEQUILA_ANEJO);

    public static final DeferredBlock<Block> KEG = registerBlock("keg", KegBlock::new);

    private static DeferredBlock<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }


    private static DeferredBlock<LiquidBlock> registerLiquidBlock(String name, DeferredHolder<Fluid, FlowingFluid> fluid) {
        return BLOCKS.register(name, () -> new LiquidBlock(fluid.get(), BlockBehaviour.Properties.of()
                .mapColor(MapColor.WATER)
                .replaceable()
                .noCollission()
                .strength(100)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(SoundType.EMPTY)));
    }
}
