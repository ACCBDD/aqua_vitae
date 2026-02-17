package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.api.BrewingIngredient;
import com.accbdd.aqua_vitae.api.Flavor;
import com.accbdd.aqua_vitae.api.naming.DrinkPredicate;
import com.accbdd.aqua_vitae.api.naming.NameEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class BuiltIn {
    public static final Map<ResourceKey<Flavor>, Flavor> FLAVORS = new HashMap<>();
    public static final Map<ResourceKey<BrewingIngredient>, BrewingIngredient> BREWING_INGREDIENTS = new HashMap<>();
    public static final Map<ResourceKey<NameEntry>, NameEntry> NAMES = new HashMap<>();

    static Map.Entry<ResourceKey<Flavor>, Flavor> flavor(String path, Flavor flavor) {
        ResourceKey<Flavor> key = ResourceKey.create(AquaVitae.FLAVOR_REGISTRY, loc(path));
        FLAVORS.put(key, flavor);
        return new AbstractMap.SimpleEntry<>(key, flavor);
    }

    static Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> brewingIngredient(String path, BrewingIngredient ingredient) {
        ResourceKey<BrewingIngredient> key = ResourceKey.create(AquaVitae.INGREDIENT_REGISTRY, loc(path));
        BREWING_INGREDIENTS.put(key, ingredient);
        return new AbstractMap.SimpleEntry<>(key, ingredient);
    }

    static Map.Entry<ResourceKey<NameEntry>, NameEntry> nameEntry(String path, List<DrinkPredicate> predicates, int priority) {
        ResourceKey<NameEntry> key = ResourceKey.create(AquaVitae.NAME_REGISTRY, loc(path));
        NameEntry entry = new NameEntry("name.aqua_vitae." + path, predicates, priority);
        NAMES.put(key, entry);
        return new AbstractMap.SimpleEntry<>(key, entry);
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
