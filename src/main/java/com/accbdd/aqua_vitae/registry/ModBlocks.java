package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.block.*;
import com.accbdd.aqua_vitae.recipe.IngredientColor;
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

import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<LiquidBlock> AQUA_VITAE = registerLiquidBlock("aqua_vitae", ModFluids.AQUA_VITAE);

    public static final DeferredBlock<Block> OAK_KEG = registerBlock("oak_keg", () -> new KegBlock(new KegType(4000, List.of(), new IngredientColor(0xFFFFFF00, 1))));
    public static final DeferredBlock<Block> SPRUCE_KEG = registerBlock("spruce_keg", () -> new KegBlock(new KegType(4000, List.of(), new IngredientColor(0xFFFF0000, 1))));
    public static final DeferredBlock<Block> JUNGLE_KEG = registerBlock("jungle_keg", () -> new KegBlock(new KegType(4000, List.of(), new IngredientColor(0xFF00FF00, 1))));
    public static final DeferredBlock<Block> FERMENTER = registerBlock("fermenter", FermenterBlock::new);
    public static final DeferredBlock<Block> CRUSHING_TUB = registerBlock("crushing_tub", CrushingTubBlock::new);
    public static final DeferredBlock<Block> POT_STILL = registerBlock("pot_still", PotStillBlock::new);
    public static final DeferredBlock<Block> MALT_KILN = registerBlock("malt_kiln", MaltKilnBlock::new);
    public static final DeferredBlock<Block> MASH_TUN = registerBlock("mash_tun", MashTunBlock::new);

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
