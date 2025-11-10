package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.registry.ModFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class FluidTagGenerator extends FluidTagsProvider {
    public static TagKey<Fluid> HARD_LIQUOR = TagKey.create(Registries.FLUID, loc("hard_liquor"));

    public FluidTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(HARD_LIQUOR).add(
                ModFluids.AQUA_VITAE.get(),
                ModFluids.FLOWING_AQUA_VITAE.get(),
                ModFluids.TEQUILA_ANEJO.get(),
                ModFluids.FLOWING_TEQUILA_ANEJO.get(),
                ModFluids.TEQUILA_REPOSADO.get(),
                ModFluids.FLOWING_TEQUILA_REPOSADO.get(),
                ModFluids.TEQUILA_BLANCO.get(),
                ModFluids.FLOWING_TEQUILA_BLANCO.get()
        );
    }

    private static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
