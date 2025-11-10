package com.accbdd.aqua_vitae.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends RecipeProvider {
    public RecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }
}
