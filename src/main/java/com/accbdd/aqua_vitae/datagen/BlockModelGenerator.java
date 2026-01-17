package com.accbdd.aqua_vitae.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockModelGenerator extends BlockModelProvider {
    public BlockModelGenerator(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        cubeBottomTop("oak_keg", modLoc("block/keg/oak_keg_side"), modLoc("block/keg/oak_keg_bottom"), modLoc("block/keg/oak_keg_top"));
        cubeBottomTop("spruce_keg", modLoc("block/keg/spruce_keg_side"), modLoc("block/keg/spruce_keg_bottom"), modLoc("block/keg/spruce_keg_top"));
        cubeBottomTop("jungle_keg", modLoc("block/keg/jungle_keg_side"), modLoc("block/keg/jungle_keg_bottom"), modLoc("block/keg/jungle_keg_top"));
        cubeAll("fermenter_closed", modLoc("block/fermenter_closed"));
        cubeAll("fermenter", modLoc("block/fermenter_open"));
        cube("malt_kiln",
                modLoc("block/malt_kiln_end"),
                modLoc("block/malt_kiln_end"),
                modLoc("block/malt_kiln_front_off"),
                modLoc("block/malt_kiln_side"),
                modLoc("block/malt_kiln_side"),
                modLoc("block/malt_kiln_side")).texture("particle", modLoc("block/malt_kiln_side"));
        cube("malt_kiln_on",
                modLoc("block/malt_kiln_end"),
                modLoc("block/malt_kiln_end"),
                modLoc("block/malt_kiln_front_on"),
                modLoc("block/malt_kiln_side"),
                modLoc("block/malt_kiln_side"),
                modLoc("block/malt_kiln_side")).texture("particle", modLoc("block/malt_kiln_side"));
    }
}
