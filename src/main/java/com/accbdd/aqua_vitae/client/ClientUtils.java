package com.accbdd.aqua_vitae.client;

import com.accbdd.aqua_vitae.api.BrewingIngredient;
import com.accbdd.aqua_vitae.api.Flavor;
import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.FermentingPropertiesComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.config.Config;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import com.accbdd.aqua_vitae.util.Constants;
import com.accbdd.aqua_vitae.util.GuiUtils;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ClientUtils {
    public static List<Component> flavorTooltip(Set<ResourceKey<Flavor>> flavors) {
        if (flavors.isEmpty())
            return List.of(Component.translatable("flavor.aqua_vitae.none"));
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("flavor.aqua_vitae.label").withStyle(ChatFormatting.GOLD));
        for (ResourceKey<Flavor> key : flavors) {
            MutableComponent flavorName = Component.translatable("flavor.aqua_vitae." + key.location());
            Component flavorEffects = ComponentUtils.formatList(BrewingUtils.getFlavor(key).effects().stream().map(effect -> {
                MutableComponent component = Component.translatable(effect.getDescriptionId());
                if (effect.getAmplifier() > 0) {
                    component = Component.translatable(
                            "potion.withAmplifier", component, Component.translatable("potion.potency." + effect.getAmplifier())
                    );
                }

//                if (!effect.endsWithin(20)) {
//                    component = Component.translatable(
//                            "potion.withDuration", component, MobEffectUtil.formatDuration(effect, 1, 20)
//                    );
//                }
                return component.withStyle(effect.getEffect().value().getCategory().getTooltipFormatting());
            }).toList(), Component.literal(","));
            components.add(Component.translatable("grammar.aqua_vitae.list_item", Component.translatable("grammar.aqua_vitae.label", flavorName, Component.translatable("grammar.aqua_vitae.parenthesis", flavorEffects))));
        }

        return components;
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

    public static List<Component> getTooltip(BrewingIngredient ingredient) {
        return getTooltip(ingredient.flavors(), ingredient.properties());
    }

    public static List<Component> getTooltip(Set<ResourceKey<Flavor>> flavors, BrewingIngredient.BrewingProperties properties) {
        List<Component> flavorsTooltip = flavorTooltip(flavors);
        List<Component> propertiesTooltip = propertiesTooltip(properties);
        List<Component> tooltip = new ArrayList<>();

        if (!flavorsTooltip.isEmpty() && ModKeyMappings.isKeyDown(ModKeyMappings.FLAVORS_MAPPING.get())) {
            tooltip.addAll(flavorsTooltip);
        } else {
            tooltip.add(Constants.COMPONENT_FLAVORS.withStyle(ChatFormatting.DARK_GRAY));
        }

        if (!propertiesTooltip.isEmpty() && ModKeyMappings.isKeyDown(ModKeyMappings.PROPERTIES_MAPPING.get())) {
            tooltip.addAll(propertiesTooltip);
        } else {
            tooltip.add(Constants.COMPONENT_PROPERTIES.withStyle(ChatFormatting.DARK_GRAY));
        }

        return tooltip;
    }

    public static List<Component> effectTooltip(List<MobEffectInstance> effects, float ticksPerSecond) {
        List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
        List<Component> tooltip = new ArrayList<>();

        for (MobEffectInstance mobeffectinstance : effects) {
            MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
            Holder<MobEffect> holder = mobeffectinstance.getEffect();
            holder.value().createModifiers(mobeffectinstance.getAmplifier(), (p_331556_, p_330860_) -> list.add(new Pair<>(p_331556_, p_330860_)));
            if (mobeffectinstance.getAmplifier() > 0) {
                mutablecomponent = Component.translatable(
                        "potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier())
                );
            }

            if (!mobeffectinstance.endsWithin(20)) {
                mutablecomponent = Component.translatable(
                        "potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, 1, ticksPerSecond)
                );
            }

            tooltip.add(mutablecomponent.withStyle(holder.value().getCategory().getTooltipFormatting()));
        }
        return tooltip;
    }

    /**
     * @param fluidStack
     * @return a list of components that make up a fluid's tooltip
     */
    public static List<Component> getFluidTooltip(FluidStack fluidStack) {
        List<Component> tooltips = new ArrayList<>();
        List<Component> flavorsTooltip = new ArrayList<>();
        List<Component> ingredientsTooltip = new ArrayList<>();
        List<Component> propertiesTooltip = new ArrayList<>();

        if (fluidStack.has(ModComponents.PRECURSOR_PROPERTIES)) {
            PrecursorPropertiesComponent precursorComponent = fluidStack.get(ModComponents.PRECURSOR_PROPERTIES);
            flavorsTooltip = flavorTooltip(precursorComponent.flavors());
            ingredientsTooltip.add(precursorComponent.ingredients().getTooltipComponent());
            propertiesTooltip.addAll(propertiesTooltip(precursorComponent.properties()));
        }

        if (fluidStack.has(ModComponents.FERMENTING_PROPERTIES)) {
            FermentingPropertiesComponent fermentingComponent = fluidStack.get(ModComponents.FERMENTING_PROPERTIES);
            propertiesTooltip.addAll(propertiesTooltip(fermentingComponent.properties()));
        }

        if (fluidStack.has(ModComponents.ALCOHOL_PROPERTIES)) {
            AlcoholPropertiesComponent alcoholComponent = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES);
            flavorsTooltip = flavorTooltip(alcoholComponent.flavors());
            ingredientsTooltip.add(alcoholComponent.inputs().getTooltipComponent());
            propertiesTooltip.add(Component.translatable("properties.aqua_vitae.abv", String.format("%.2f%%", alcoholComponent.abv() / 10)));
            propertiesTooltip.add(Component.translatable("properties.aqua_vitae.age", String.format("%.2f", (alcoholComponent.age() * Config.ageTicks) / 24000f)));
            propertiesTooltip.add(Component.translatable("properties.aqua_vitae.color", Integer.toHexString(alcoholComponent.color().color()).toUpperCase()).withColor(alcoholComponent.color().color() | 0xFF000000));
        }

        if (ingredientsTooltip.isEmpty() && propertiesTooltip.isEmpty())
            return List.of();

        if (!flavorsTooltip.isEmpty() && ModKeyMappings.isKeyDown(ModKeyMappings.FLAVORS_MAPPING.get())) {
            tooltips.addAll(flavorsTooltip);
        } else {
            tooltips.add(Constants.COMPONENT_FLAVORS.withStyle(ChatFormatting.DARK_GRAY));
        }

        if (!ingredientsTooltip.isEmpty() && ModKeyMappings.isKeyDown(ModKeyMappings.INGREDIENTS_MAPPING.get())) {
            tooltips.addAll(ingredientsTooltip);
        } else {
            tooltips.add(Constants.COMPONENT_INGREDIENTS.withStyle(ChatFormatting.DARK_GRAY));
        }

        if (!propertiesTooltip.isEmpty() && ModKeyMappings.isKeyDown(ModKeyMappings.PROPERTIES_MAPPING.get())) {
            tooltips.addAll(propertiesTooltip);
        } else {
            tooltips.add(Constants.COMPONENT_PROPERTIES.withStyle(ChatFormatting.DARK_GRAY));
        }

        return tooltips;
    }
}
