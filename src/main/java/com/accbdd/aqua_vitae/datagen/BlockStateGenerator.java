package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.AQUA_VITAE.get(), models().getExistingFile(mcLoc("water")));

        directionalBlock(ModBlocks.KEG.get(), models().getExistingFile(modLoc("keg")));
    }
}
