package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.api.BrewingIngredient;
import com.accbdd.aqua_vitae.api.Flavor;
import com.accbdd.aqua_vitae.api.IngredientMap;
import com.accbdd.aqua_vitae.api.naming.NameEntry;
import com.accbdd.aqua_vitae.component.AlcoholNameComponent;
import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.registry.ModFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

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
        RegistryAccess registryAccess = registryAccess();
        if (registryAccess != null) {
            return registryAccess.registry(AquaVitae.FLAVOR_REGISTRY).orElseThrow();
        }
        return null;
    }

    @Nullable
    public static Registry<BrewingIngredient> ingredientRegistry() {
        RegistryAccess registryAccess = registryAccess();
        if (registryAccess != null)
            return registryAccess.registry(AquaVitae.INGREDIENT_REGISTRY).orElseThrow();
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
        if (stack == null || stack.isEmpty())
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
            BrewingIngredientComponent comp = stack.getOrDefault(ModComponents.BREWING_INGREDIENT, BrewingIngredientComponent.DEFAULT);
            return new BrewingIngredient(FluidIngredient.of(stack), comp.properties(), comp.maltProperties(), comp.flavors());
        }
        Registry<BrewingIngredient> brewingIngredients = ingredientRegistry();
        return brewingIngredients != null ? ingredientRegistry().stream().filter(ing -> ing.fluidIngredient() != null && ing.fluidIngredient().test(stack)).findFirst().orElse(null) : null;
    }

    @Nullable
    public static ResourceLocation getIngredientLoc(BrewingIngredient ingredient) {
        Registry<BrewingIngredient> brewingIngredients = ingredientRegistry();
        if (brewingIngredients != null)
            return brewingIngredients.getKey(ingredient);
        return null;
    }

    @Nullable
    public static ResourceLocation getIngredientLoc(ItemStack stack) {
        return getIngredientLoc(getIngredient(stack));
    }

    @Nullable
    public static ResourceLocation getIngredientLoc(FluidStack stack) {
        return getIngredientLoc(getIngredient(stack));
    }

    @Nullable
    public static Flavor getFlavor(ResourceKey<Flavor> flavorKey) {
        Registry<Flavor> flavors = flavorRegistry();
        if (flavors != null)
            return flavors.get(flavorKey);
        return null;
    }

    public static Component flavorTooltip(Set<ResourceKey<Flavor>> flavors) {
        if (flavors.isEmpty())
            return Component.translatable("flavor.aqua_vitae.none");
        MutableComponent component = Component.empty();
        Iterator<ResourceKey<Flavor>> iterator = flavors.iterator();
        while (iterator.hasNext()) {
            ResourceKey<Flavor> key = iterator.next();
            MutableComponent flavorName = Component.translatable("flavor.aqua_vitae." + key.location());
            if (iterator.hasNext()) {
                component.append(Component.translatable("grammar.aqua_vitae.list_combine", flavorName));
            } else {
                component.append(flavorName);
            }
        }

        return Component.translatable("grammar.aqua_vitae.label", Component.translatable("flavor.aqua_vitae.label").withStyle(ChatFormatting.AQUA), component);
    }

    public static List<Component> propertiesTooltip(BrewingIngredient.BrewingProperties properties) {
        List<Component> tooltips = new ArrayList<>();
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
            tooltips.add(Component.translatable("properties.aqua_vitae.yeast", properties.yeast(), String.format("%.2f%%", (float)properties.yeastTolerance() / 10)));
        }
        tooltips.add(Component.translatable("properties.aqua_vitae.color", Integer.toHexString(properties.color().color()).toUpperCase()).withColor(properties.color().color() | 0xFF000000));
        return tooltips;
    }

    /**
     * transitions flavors according to a flavor transition
     * @param flavors
     * @param transitionFunction
     * @param transitionPoint
     * @return
     */
    public static Set<ResourceKey<Flavor>> transitionFlavors(Set<ResourceKey<Flavor>> flavors, Function<Flavor, Flavor.Transition> transitionFunction, int transitionPoint) {
        Set<ResourceKey<Flavor>> newFlavors = new HashSet<>();
        for (ResourceKey<Flavor> flavorKey : flavors) {
            Flavor flavor = getFlavor(flavorKey);
            if (flavor != null) {
                if (transitionFunction.apply(flavor) == null) {
                    newFlavors.add(flavorKey);
                } else {
                    Flavor.Transition transition = transitionFunction.apply(flavor);
                    transition.flavors().forEach(flavorEntry -> {
                        if (transitionPoint >= transition.transitionPoint()) {
                            newFlavors.add(flavorEntry);
                        } else {
                            newFlavors.add(flavorKey);
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
                properties = properties.add(ing.properties());
            inputs.add(stack);
        }
        fluid.set(ModComponents.PRECURSOR_PROPERTIES, new PrecursorPropertiesComponent(inputs, flavors, properties));
        return fluid;
    }

    public static FluidStack mashToWort(int amount, ItemStack... ingredients) {
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
                properties = properties.add(ing.properties());
            inputs.add(stack);
        }
        fluid.set(ModComponents.PRECURSOR_PROPERTIES, new PrecursorPropertiesComponent(inputs, flavors, properties.mash()));
        fluid.set(ModComponents.ALCOHOL_NAME, new AlcoholNameComponent(Component.translatable("alcohol.aqua_vitae.wort")));
        return fluid;
    }

    public static void determineAlcoholName(FluidStack alcohol, Level level) {
        if (level != null && alcohol.has(ModComponents.ALCOHOL_PROPERTIES)) {
            AlcoholPropertiesComponent props = alcohol.get(ModComponents.ALCOHOL_PROPERTIES);
            Component name = null;
            for (Map.Entry<ResourceKey<NameEntry>, NameEntry> entry : level.registryAccess().registry(AquaVitae.NAME_REGISTRY).orElseThrow().entrySet()) {
                if (entry.getValue().test(alcohol)) {
                    name = entry.getValue().getComponent();
                    break;
                }
            }

            if (name == null)
                name = Component.translatable("alcohol.aqua_vitae.generic");
            alcohol.set(ModComponents.ALCOHOL_NAME, new AlcoholNameComponent(name));
        }
    }
}
