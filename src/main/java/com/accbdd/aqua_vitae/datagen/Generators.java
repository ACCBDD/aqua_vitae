package com.accbdd.aqua_vitae.datagen;


import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.datagen.builtin.BuiltIn;
import com.accbdd.aqua_vitae.datagen.builtin.BuiltInFlavors;
import com.accbdd.aqua_vitae.datagen.builtin.BuiltInIngredients;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
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

        //utility classes for datagen
        AquaVitae.LOGGER.info("flavors: {}, ingredients: {}", new BuiltInFlavors(), new BuiltInIngredients());
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, new RegistrySetBuilder().add(
                AquaVitae.FLAVOR_REGISTRY,
                bootstrap -> {
                    for (Map.Entry<ResourceKey<BrewingIngredient.Flavor>, BrewingIngredient.Flavor> entry : BuiltIn.FLAVORS.entrySet())
                        bootstrap.register(
                                entry.getKey(),
                                entry.getValue()
                        );
                }
        ).add(
                AquaVitae.INGREDIENT_REGISTRY,
                bootstrap -> {
                    for (Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> entry : BuiltIn.BREWING_INGREDIENTS.entrySet())
                        bootstrap.register(
                                entry.getKey(),
                                entry.getValue()
                        );
                }
        ), Set.of(MODID)));
    }
}
