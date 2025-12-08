package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.IngredientMap;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.registry.ModFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BrewingUtils {

    @Nullable
    public static RegistryAccess registryAccess() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            if (FMLEnvironment.dist.equals(Dist.DEDICATED_SERVER) || Minecraft.getInstance() == null) //datagen
                return null;
            if (Minecraft.getInstance().getConnection() == null) {
                return null;
            } else {
                return Minecraft.getInstance().getConnection().registryAccess();
            }
        } else {
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        }
    }

    @Nullable
    public static Registry<Flavor> flavorRegistry() {
        if (registryAccess() != null) {
            return registryAccess().registry(AquaVitae.FLAVOR_REGISTRY).get();
        }
        return null;
    }

    @Nullable
    public static Registry<BrewingIngredient> ingredientRegistry() {
        if (registryAccess() != null)
            return registryAccess().registry(AquaVitae.INGREDIENT_REGISTRY).get();
        return null;
    }

    @Nullable
    public static BrewingIngredient getIngredient(ResourceKey<BrewingIngredient> key) {
        Registry<BrewingIngredient> brewingIngredients = ingredientRegistry();
        if (brewingIngredients != null)
            return brewingIngredients.get(key);
        return null;
    }

    @Nullable
    public static BrewingIngredient getIngredient(ItemStack stack) {
        if (stack.isEmpty())
            return null;

        if (stack.has(ModComponents.BREWING_INGREDIENT)) {
            BrewingIngredientComponent comp = stack.get(ModComponents.BREWING_INGREDIENT);
            return new BrewingIngredient(Ingredient.of(stack), comp.properties(), comp.maltProperties(), comp.flavors());
        }
        Registry<BrewingIngredient> brewingIngredients = ingredientRegistry();
        return brewingIngredients != null ? brewingIngredients.stream().filter(ing -> ing.itemIngredient() != null && ing.itemIngredient().test(stack)).findFirst().orElse(null) : null;
    }

    @Nullable
    public static BrewingIngredient getIngredient(FluidStack stack) {
        if (stack.has(ModComponents.BREWING_INGREDIENT)) {
            BrewingIngredientComponent comp = stack.get(ModComponents.BREWING_INGREDIENT);
            return new BrewingIngredient(FluidIngredient.of(stack), comp.properties(), comp.maltProperties(), comp.flavors());
        }
        Registry<BrewingIngredient> brewingIngredients = ingredientRegistry();
        return brewingIngredients != null ? ingredientRegistry().stream().filter(ing -> ing.fluidIngredient() != null && ing.fluidIngredient().test(stack)).findFirst().orElse(null) : null;
    }

    @Nullable
    public static ResourceLocation getIngredientKey(BrewingIngredient ingredient) {
        Registry<BrewingIngredient> brewingIngredients = ingredientRegistry();
        if (brewingIngredients != null)
            return brewingIngredients.getKey(ingredient);
        return null;
    }

    @Nullable
    public static Flavor getFlavor(ResourceKey<Flavor> flavorKey) {
        Registry<Flavor> flavors = flavorRegistry();
        if (flavors != null)
            return flavors.get(flavorKey);
        return null;
    }

    public static Component flavorTooltip(Set<ResourceKey<Flavor>> flavors) {
        MutableComponent component = Component.empty();
        Iterator<ResourceKey<Flavor>> iterator = flavors.iterator();
        while (iterator.hasNext()) {
            ResourceKey<Flavor> key = iterator.next();
            component.append(Component.translatable("flavors.aqua_vitae." + key.location()));
            if (iterator.hasNext()) {
                component.append(Component.literal(", "));
            }
        }

        return component;
    }

    public static List<MutableComponent> propertiesTooltip(BrewingIngredient.BrewingProperties properties) {
        List<MutableComponent> tooltips = new ArrayList<>();
        if (properties.sugar() > 0) {
            tooltips.add(Component.translatable("properties.aqua_vitae.sugar", properties.sugar()));
        }
        if (properties.starch() > 0) {
            tooltips.add(Component.translatable("properties.aqua_vitae.starch", properties.starch()));
        }
        if (properties.diastaticPower() > 0) {
            tooltips.add(Component.translatable("properties.aqua_vitae.diastatic_power", properties.diastaticPower()));
        }
        if (properties.yeast() > 0 && properties.yeastTolerance() > 0) {
            tooltips.add(Component.translatable("properties.aqua_vitae.yeast", properties.yeast(), properties.yeastTolerance()));
        }
        tooltips.add(Component.translatable("properties.aqua_vitae.color", Integer.toHexString(properties.color())).withColor(properties.color() | 0xFF000000));
        return tooltips;
    }

    public static Set<ResourceKey<Flavor>> kilnFlavors(Set<ResourceKey<Flavor>> flavors, int kilnCount) {
        Set<ResourceKey<Flavor>> newFlavors = new HashSet<>();
        for (ResourceKey<Flavor> flavorKey : flavors) {
            Flavor flavor = getFlavor(flavorKey);
            if (flavor != null) {
                if (flavor.kiln() == null) {
                    newFlavors.add(flavorKey);
                } else {
                    Flavor.Transition transition = flavor.kiln();
                    transition.flavors().forEach(flavorEntry -> {
                        if (kilnCount >= transition.transitionPoint()) {
                            newFlavors.add(flavorEntry);
                        }
                    });
                }
            }
        }
        return newFlavors;
    }

    public static FluidStack createWort(int amount, ItemStack... ingredients) {
        FluidStack fluid = new FluidStack(ModFluids.WORT, amount);
        IngredientMap inputs = new IngredientMap();
        Set<ResourceKey<Flavor>> flavors = new HashSet<>();
        BrewingIngredient.BrewingProperties properties = BrewingIngredient.BrewingProperties.DEFAULT;
        for (ItemStack stack : ingredients) {
            if (stack.isEmpty())
                continue;
            BrewingIngredient ing = BrewingUtils.getIngredient(stack);
            if (ing == null)
                continue;
            flavors.addAll(ing.flavors());
            if (inputs.isEmpty())
                properties = ing.properties().copy();
            else
                properties = properties.add(ing.properties(), inputs.getIngredientCount());
            inputs.add(stack);
        }
        fluid.set(ModComponents.PRECURSOR_PROPERTIES, new PrecursorPropertiesComponent(inputs, flavors, properties));
        return fluid;
    }
}
