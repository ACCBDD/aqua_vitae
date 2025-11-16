package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class BuiltIn {
    public static final Map<ResourceKey<BrewingIngredient.Flavor>, BrewingIngredient.Flavor> FLAVORS = new HashMap<>();
    public static final Map<ResourceKey<BrewingIngredient>, BrewingIngredient> BREWING_INGREDIENTS = new HashMap<>();

    static Map.Entry<ResourceKey<BrewingIngredient.Flavor>, BrewingIngredient.Flavor> flavor(String path, BrewingIngredient.Flavor.Builder flavorBuilder) {
        BrewingIngredient.Flavor flavor = flavorBuilder.build();
        ResourceKey<BrewingIngredient.Flavor> key = ResourceKey.create(AquaVitae.FLAVOR_REGISTRY, loc(path));
        FLAVORS.put(key, flavor);
        return new AbstractMap.SimpleEntry<>(key, flavor);
    }

    static Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> brewingIngredient(String path, BrewingIngredient ingredient) {
        ResourceKey<BrewingIngredient> key = ResourceKey.create(AquaVitae.INGREDIENT_REGISTRY, loc(path));
        BREWING_INGREDIENTS.put(key, ingredient);
        return new AbstractMap.SimpleEntry<>(key, ingredient);
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
