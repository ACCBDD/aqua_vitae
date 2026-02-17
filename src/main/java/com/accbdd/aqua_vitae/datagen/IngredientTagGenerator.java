package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.api.BrewingIngredient;
import com.accbdd.aqua_vitae.datagen.builtin.BuiltInIngredients;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class IngredientTagGenerator extends TagsProvider<BrewingIngredient> {
    public static final TagKey<BrewingIngredient> GRAINS = create("grain");

    public IngredientTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, AquaVitae.INGREDIENT_REGISTRY, provider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GRAINS).add(
                BuiltInIngredients.WHEAT.getKey()
        );
    }

    private static TagKey<BrewingIngredient> create(String name) {
        return TagKey.create(AquaVitae.INGREDIENT_REGISTRY, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }
}
