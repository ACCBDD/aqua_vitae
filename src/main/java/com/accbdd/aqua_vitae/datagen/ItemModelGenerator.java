package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import com.accbdd.aqua_vitae.registry.ModFluids;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModFluids.REGISTERED.forEach(this::bucketModel);
        simpleBlockItem(ModBlocks.KEG.get());
        simpleBlockItem(ModBlocks.FERMENTER.get());
        simpleBlockItem(ModBlocks.CRUSHING_TUB.get());
        simpleBlockItem(ModBlocks.POT_STILL.get());
        simpleBlockItem(ModBlocks.MALT_KILN.get());
        simpleBlockItem(ModBlocks.MASH_TUN.get());
        basicItem(ModItems.CUP.get());
        basicItem(ModItems.SHOOTER.get());
        basicItem(ModItems.METER.get());
        basicItem(ModItems.BREW_BUCKET.get());
        basicItem(ModItems.MALT.get());
        getBuilder(ModItems.EYEBALL.getKey().location().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", modLoc("item/eyeball_base")).texture("layer1", modLoc("item/eyeball_overlay"));
    }

    private void bucketModel(DeferredHolder<Fluid, FlowingFluid> fluid) {
        getBuilder(fluid.get().getBucket().builtInRegistryHolder().getRegisteredName())
                .parent(getExistingFile(ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .applyFluidLuminosity(true)
                .coverIsMask(false)
                .flipGas(false)
                .fluid(fluid.get());
    }
}
