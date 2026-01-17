package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.AQUA_VITAE.get(), models().getExistingFile(mcLoc("water")));
        simpleBlock(ModBlocks.CRUSHING_TUB.get(), models().getExistingFile(modLoc("crushing_tub")));
        horizontalBlock(ModBlocks.POT_STILL.get(), models().getExistingFile(modLoc("pot_still")), -90);
        kegState(ModBlocks.OAK_KEG.get(), "oak_keg");
        kegState(ModBlocks.SPRUCE_KEG.get(), "spruce_keg");
        kegState(ModBlocks.JUNGLE_KEG.get(), "jungle_keg");
        getVariantBuilder(ModBlocks.FERMENTER.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(state.getValue(BlockStateProperties.CRAFTING) ? models().getExistingFile(modLoc("fermenter_closed")) : models().getExistingFile(modLoc("fermenter")))
                .build());
        getVariantBuilder(ModBlocks.MALT_KILN.get()).forAllStates(state ->
                ConfiguredModel.builder()
                        .modelFile(state.getValue(BlockStateProperties.LIT) ? models().getExistingFile(modLoc("malt_kiln_on")) : models().getExistingFile(modLoc("malt_kiln")))
                        .rotationY((int) state.getValue(BlockStateProperties.FACING).toYRot())
                        .build());
        getVariantBuilder(ModBlocks.MASH_TUN.get()).forAllStates(state ->
                ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("mash_tun")))
                        .rotationY((int) state.getValue(BlockStateProperties.FACING).toYRot() + 90)
                        .build());
    }

    private void kegState(Block block, String path) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            return ConfiguredModel.builder()
                    .modelFile(state.getValue(BlockStateProperties.OPEN) ? models().getExistingFile(modLoc(path)) : models().getExistingFile(mcLoc("stone")))
                    .rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0))
                    .rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot()) % 360)
                    .build();
        });
    }
}
