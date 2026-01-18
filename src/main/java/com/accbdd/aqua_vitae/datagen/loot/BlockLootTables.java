package com.accbdd.aqua_vitae.datagen.loot;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class BlockLootTables extends BlockLootSubProvider {
    public BlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(block -> (Block) block.get()).toList();
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.OAK_KEG.get());
        dropSelf(ModBlocks.SPRUCE_KEG.get());
        dropSelf(ModBlocks.BIRCH_KEG.get());
        dropSelf(ModBlocks.JUNGLE_KEG.get());
        dropSelf(ModBlocks.ACACIA_KEG.get());
        dropSelf(ModBlocks.DARK_OAK_KEG.get());
        dropSelf(ModBlocks.CRIMSON_KEG.get());
        dropSelf(ModBlocks.WARPED_KEG.get());
        dropSelf(ModBlocks.FERMENTER.get());
        dropSelf(ModBlocks.CRUSHING_TUB.get());
        dropSelf(ModBlocks.POT_STILL.get());
        dropSelf(ModBlocks.MALT_KILN.get());
        dropSelf(ModBlocks.MASH_TUN.get());
    }
}
