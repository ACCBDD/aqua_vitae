package com.accbdd.aqua_vitae.datagen;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class Generators {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // If it's a server resource (goes in the data folder) include the server.
        generator.addProvider(event.includeServer(), new RecipeGenerator(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new FluidTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTableGenerator(packOutput, lookupProvider));

        // If it's a client resource (goes in the assets folder) include the client.
        generator.addProvider(event.includeClient(), new BlockModelGenerator(packOutput, MODID, existingFileHelper));
        generator.addProvider(event.includeClient(), new ItemModelGenerator(packOutput, MODID, existingFileHelper));
        generator.addProvider(event.includeClient(), new BlockStateGenerator(packOutput, MODID, existingFileHelper));
        generator.addProvider(event.includeClient(), new LanguageGenerator(packOutput, MODID, Locale.US.toString().toLowerCase()));
    }
}
