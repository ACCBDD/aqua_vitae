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
        kegModel("oak");
        kegModel("spruce");
        kegModel("birch");
        kegModel("jungle");
        kegModel("acacia");
        kegModel("dark_oak");
        kegModel("crimson");
        kegModel("warped");
        cubeBottomTop("fermenter_closed", modLoc("block/fermenter_side"), modLoc("block/fermenter_bottom"), modLoc("block/fermenter_top_closed"));
        cubeBottomTop("fermenter", modLoc("block/fermenter_side"), modLoc("block/fermenter_bottom"), modLoc("block/fermenter_top_open"));
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

    private void kegModel(String name) {
        cubeBottomTop(name + "_keg", modLoc("block/keg/"+name+"_keg_side"), modLoc("block/keg/"+name+"_keg_bottom"), modLoc("block/keg/"+name+"_keg_top"));
        cubeBottomTop(name + "_keg_open", modLoc("block/keg/"+name+"_keg_side"), modLoc("block/keg/"+name+"_keg_bottom"), modLoc("block/keg/"+name+"_keg_top_open"));
    }
}
