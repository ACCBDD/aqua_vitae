package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class BlockTagGenerator extends BlockTagsProvider {
    public static TagKey<Block> HEAT_SOURCES = TagKey.create(BuiltInRegistries.BLOCK.key(), modLoc("heat_source"));

    public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                ModBlocks.OAK_KEG.get(),
                ModBlocks.SPRUCE_KEG.get(),
                ModBlocks.BIRCH_KEG.get(),
                ModBlocks.JUNGLE_KEG.get(),
                ModBlocks.ACACIA_KEG.get(),
                ModBlocks.DARK_OAK_KEG.get(),
                ModBlocks.CRIMSON_KEG.get(),
                ModBlocks.WARPED_KEG.get(),
                ModBlocks.CRUSHING_TUB.get()
        );

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.FERMENTER.get(),
                ModBlocks.POT_STILL.get(),
                ModBlocks.MALT_KILN.get(),
                ModBlocks.MASH_TUN.get()
        );

        tag(HEAT_SOURCES).add(
                Blocks.MAGMA_BLOCK,
                Blocks.LAVA_CAULDRON,
                Blocks.LAVA
        ).addTags(
                BlockTags.CAMPFIRES,
                BlockTags.FIRE
        ).addOptionalTag(
                loc("farmersdelight:heat_sources")
        );
    }

    private static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private static ResourceLocation loc(String path) {
        return ResourceLocation.parse(path);
    }
}
