package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import net.minecraft.data.PackOutput;
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
        directionalBlock(ModBlocks.KEG.get(), models().getExistingFile(modLoc("keg")));

        getVariantBuilder(ModBlocks.FERMENTER.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(state.getValue(BlockStateProperties.CRAFTING) ? models().getExistingFile(modLoc("fermenter_closed")) : models().getExistingFile(modLoc("fermenter")))
                .build());

        getVariantBuilder(ModBlocks.MALT_KILN.get()).forAllStates(state ->
                ConfiguredModel.builder()
                        .modelFile(state.getValue(BlockStateProperties.LIT) ? models().getExistingFile(modLoc("malt_kiln_on")) : models().getExistingFile(modLoc("malt_kiln")))
                        .rotationY((int) state.getValue(BlockStateProperties.FACING).toYRot())
                        .build());
    }
}
