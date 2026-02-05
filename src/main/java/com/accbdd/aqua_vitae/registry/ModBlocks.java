package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.api.IngredientColor;
import com.accbdd.aqua_vitae.api.KegType;
import com.accbdd.aqua_vitae.block.*;
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

    public static final DeferredBlock<Block> OAK_KEG = registerKeg("oak", 0xFFFFBF00);
    public static final DeferredBlock<Block> SPRUCE_KEG = registerKeg("spruce", 0xFF664D28);
    public static final DeferredBlock<Block> BIRCH_KEG = registerKeg("birch", 0xFFF4E8AF);
    public static final DeferredBlock<Block> JUNGLE_KEG = registerKeg("jungle", 0xFFE07A5F);
    public static final DeferredBlock<Block> ACACIA_KEG = registerKeg("acacia", 0xFFB35C35);
    public static final DeferredBlock<Block> DARK_OAK_KEG = registerKeg("dark_oak", 0xFF5C3A21);
    public static final DeferredBlock<Block> CRIMSON_KEG = registerKeg("crimson", 0xFF974666);
    public static final DeferredBlock<Block> WARPED_KEG = registerKeg("warped", 0xFF167E7E);
    //mangrove = 0xFF7A2B2B, cherry = 0xFFFFB7C5, bamboo = 0xFFE3E475
    public static final DeferredBlock<Block> FERMENTER = registerBlock("fermenter", FermenterBlock::new);
    public static final DeferredBlock<Block> CRUSHING_TUB = registerBlock("crushing_tub", CrushingTubBlock::new);
    public static final DeferredBlock<Block> POT_STILL = registerBlock("pot_still", PotStillBlock::new);
    public static final DeferredBlock<Block> MALT_KILN = registerBlock("malt_kiln", MaltKilnBlock::new);
    public static final DeferredBlock<Block> MASH_TUN = registerBlock("mash_tun", MashTunBlock::new);

    private static DeferredBlock<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }

    private static DeferredBlock<Block> registerKeg(String name, int color) {
        return registerBlock(name + "_keg", () -> new KegBlock(new KegType(8000, List.of(), new IngredientColor(color, 1))));
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
