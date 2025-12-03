package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
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

public class RegistryUtils {

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
}
