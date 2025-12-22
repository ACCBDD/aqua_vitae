package com.accbdd.aqua_vitae.client;

import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.FermentingPropertiesComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import com.accbdd.aqua_vitae.util.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ClientUtils {
    /**
     * @param fluidStack
     * @return a list of components that make up a fluid's tooltip
     */
    public static List<Component> getFluidTooltip(FluidStack fluidStack) {
        List<Component> tooltips = new ArrayList<>();
        List<Component> ingredientsTooltip = new ArrayList<>();
        List<Component> propertiesTooltip = new ArrayList<>();

        if (fluidStack.has(ModComponents.PRECURSOR_PROPERTIES)) {
            PrecursorPropertiesComponent precursorComponent = fluidStack.get(ModComponents.PRECURSOR_PROPERTIES);
            ingredientsTooltip.add(BrewingUtils.flavorTooltip(precursorComponent.flavors()));
            ingredientsTooltip.add(precursorComponent.ingredients().getTooltipComponent());
            propertiesTooltip.addAll(BrewingUtils.propertiesTooltip(precursorComponent.properties()));
        }

        if (fluidStack.has(ModComponents.FERMENTING_PROPERTIES)) {
            FermentingPropertiesComponent fermentingComponent = fluidStack.get(ModComponents.FERMENTING_PROPERTIES);
            propertiesTooltip.addAll(BrewingUtils.propertiesTooltip(fermentingComponent.properties()));
        }

        if (fluidStack.has(ModComponents.ALCOHOL_PROPERTIES)) {
            AlcoholPropertiesComponent alcoholComponent = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES);
            ingredientsTooltip.add(BrewingUtils.flavorTooltip(alcoholComponent.flavors()));
            ingredientsTooltip.add(alcoholComponent.inputs().getTooltipComponent());
            propertiesTooltip.add(Component.translatable("properties.aqua_vitae.abb", String.format("%.2f%%", alcoholComponent.abb() / 10)));
            propertiesTooltip.add(Component.translatable("properties.aqua_vitae.age", alcoholComponent.age()));
            propertiesTooltip.add(Component.translatable("properties.aqua_vitae.color", Integer.toHexString(alcoholComponent.color().color()).toUpperCase()).withColor(alcoholComponent.color().color() | 0xFF000000));
        }

        if (ingredientsTooltip.isEmpty() && propertiesTooltip.isEmpty())
            return List.of();

        if (!ingredientsTooltip.isEmpty() && ModKeyMappings.isKeyDown(ModKeyMappings.INGREDIENTS_MAPPING.get()))
            tooltips.addAll(ingredientsTooltip);
        else
            tooltips.add(Constants.COMPONENT_INGREDIENTS.withStyle(ChatFormatting.DARK_GRAY));

        if (!propertiesTooltip.isEmpty() && ModKeyMappings.isKeyDown(ModKeyMappings.PROPERTIES_MAPPING.get()))
            tooltips.addAll(propertiesTooltip);
        else
            tooltips.add(Constants.COMPONENT_PROPERTIES.withStyle(ChatFormatting.DARK_GRAY));

        return tooltips;
    }
}
