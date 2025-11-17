package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.FermentingPropertiesComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient.BrewingProperties;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FluidUtils {
    public static boolean handleInteraction(Player player, InteractionHand hand, ItemStack itemStack, IFluidHandler other) {
        if (itemStack.getCapability(Capabilities.FluidHandler.ITEM) == null) {
            return false;
        } else {
            ItemStack copyStack = itemStack.copyWithCount(1);
            IFluidHandlerItem handler = copyStack.getCapability(Capabilities.FluidHandler.ITEM);
            FluidStack fluidInOther = other.getFluidInTank(0);
            if (handler != null) {
                boolean transfer = false;
                FluidStack fluidInItem = handler.getFluidInTank(0);
                if (fluidInItem.isEmpty()) { //handler is empty, try filling it
                    int filled = handler.fill(fluidInOther, IFluidHandler.FluidAction.EXECUTE);
                    if (filled > 0) { //if there is space in the item
                        other.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                        transfer = true;
                    }
                } else { //item has fluid, try emptying it then filling it
                    FluidStack simulatedExtract = handler.drain(handler.getTankCapacity(0), IFluidHandler.FluidAction.SIMULATE);
                    int accepted = other.fill(simulatedExtract, IFluidHandler.FluidAction.EXECUTE);
                    if (accepted > 0) { //other was able to take fluid in item
                        handler.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                        transfer = true;
                    } else {
                        int filled = handler.fill(fluidInOther, IFluidHandler.FluidAction.EXECUTE);
                        if (filled > 0) { //if there is space in the item
                            other.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                            transfer = true;
                        }
                    }
                }

                ItemStack newContainer = handler.getContainer();
                if (!player.isCreative()) {
                    if (itemStack.getCount() > 1) {
                        itemStack.shrink(1);
                        player.addItem(newContainer);
                    } else {
                        player.setItemInHand(hand, newContainer);
                    }
                    player.getInventory().setChanged();
                }

                return transfer;
            }

            return false;
        }
    }

    /**
     * Modifies a FluidStack {@param stack}'s precursor component according to {@param ingredient}
     * @param fluid the FluidStack to modify
     * @param ingredient the ingredient to modify by
     * @param itemStack the itemStack to modify by
     * @see com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent
     */
    public static void modifyPrecursor(FluidStack fluid, BrewingIngredient ingredient, ItemStack itemStack) {
        PrecursorPropertiesComponent component = fluid.getOrDefault(ModComponents.PRECURSOR_PROPERTIES, PrecursorPropertiesComponent.EMPTY);
        List<ItemStack> items = new ArrayList<>(component.ingredients());
        Set<ResourceKey<BrewingIngredient.Flavor>> flavors = new HashSet<>();
        flavors.addAll(ingredient.flavors());
        flavors.addAll(component.flavors());
        items.add(itemStack.copyWithCount(1));
        BrewingProperties initial = component.properties();
        BrewingProperties toAdd = ingredient.properties();
        fluid.set(ModComponents.PRECURSOR_PROPERTIES, new PrecursorPropertiesComponent(items, flavors, initial.add(toAdd, component.ingredients().size())));
    }

    public static List<Component> getPrecursorTooltip(FluidStack fluid) {
        PrecursorPropertiesComponent component = fluid.get(ModComponents.PRECURSOR_PROPERTIES);
        if (component == null)
            return List.of();

        BrewingProperties props = component.properties();

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(String.format("color: %s, starch: %s, sugar: %s", Integer.toHexString(props.color()), props.starch(), props.sugar())));
        tooltip.add(Component.literal(String.format("yeast: %s, yeast_tol: %s, dp: %s", props.yeast(), props.yeastTolerance(), props.diastaticPower())));
        tooltip.add(Component.literal(String.format("flavors: %s", component.flavors().stream().map(ResourceKey::location).collect(Collectors.toList()))));
        tooltip.add(Component.literal(String.format("ingredients: %s", component.ingredients())));
        return tooltip;
    }

    /**
     * @param fluid a fluid with fermenting properties already applied
     * @return a fluid fermented for one tick
     */
    public static FluidStack ferment(FluidStack fluid) {
        //todo: flavors changing
        FermentingPropertiesComponent ferment = fluid.get(ModComponents.FERMENTING_PROPERTIES);
        if (ferment == null)
            return fluid;

        FluidStack newFluid = fluid.copy();
        AlcoholPropertiesComponent alcohol = fluid.getOrDefault(ModComponents.ALCOHOL_PROPERTIES, AlcoholPropertiesComponent.EMPTY);
        BrewingProperties brewing = ferment.properties();

        double batchPenalty = Math.min(Math.max(1.0 / Math.pow(fluid.getAmount() / 1000d, 0.1), 0.25), 1); //bigger batches ferment slower, to a point
        double abbFactor = Math.max(1.0 - (double) alcohol.abb() / Math.max(brewing.yeastTolerance(), 1), 0); // closer to abb tolerance means slower ferment
        double yeastFactor = (double) brewing.yeast() / Math.max(brewing.sugar(), 1); // more yeast per sugar means faster ferment
        double conversionRate = 5 * yeastFactor * abbFactor * batchPenalty;
        double abbDelta = conversionRate / fluid.getAmount() * 500; //500 means sugar/bucket is converted to alcohol at 2:1

        FermentingPropertiesComponent newFerment = new FermentingPropertiesComponent(ferment.stress(),
                ferment.flavors(),
                new BrewingProperties(brewing.color(),
                        brewing.starch(),
                        Math.max(0, (int) (brewing.sugar() - conversionRate)),
                        brewing.yeast(),
                        brewing.yeastTolerance(),
                        brewing.diastaticPower()));

        AlcoholPropertiesComponent newAlcohol = new AlcoholPropertiesComponent(alcohol.color(),
                (float) (alcohol.abb() + abbDelta),
                alcohol.age(),
                alcohol.flavors(),
                alcohol.items());

        AquaVitae.LOGGER.debug("conversion rate: {} (batch: {}, abb: {}, yeast: {}), abb delta: {}", conversionRate, batchPenalty, abbFactor, yeastFactor, abbDelta);
        AquaVitae.LOGGER.debug("sugar {} -> {}; abb {} -> {}", brewing.sugar(), newFerment.properties().sugar(), alcohol.abb(), newAlcohol.abb());

        if (newFerment.properties().sugar() == 0 || conversionRate == 0)
            newFluid.remove(ModComponents.FERMENTING_PROPERTIES);
        else
            newFluid.set(ModComponents.FERMENTING_PROPERTIES, newFerment);
        newFluid.set(ModComponents.ALCOHOL_PROPERTIES, newAlcohol);
        return newFluid;
    }

    public static FluidStack stress(FluidStack fluid) {
        FermentingPropertiesComponent ferment = fluid.get(ModComponents.FERMENTING_PROPERTIES);
        if (ferment == null)
            return fluid;

        FluidStack newFluid = fluid.copy();
        fluid.set(ModComponents.FERMENTING_PROPERTIES, new FermentingPropertiesComponent(ferment.stress() + 1, ferment.flavors(), ferment.properties()));
        return newFluid;
    }

    public static List<Component> getAlcoholTooltip(FluidStack fluid) {
        AlcoholPropertiesComponent component = fluid.get(ModComponents.ALCOHOL_PROPERTIES);
        if (component == null)
            return List.of();

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(String.format("color: %s, abb: %s, age: %s", Integer.toHexString(component.color()), component.abb(), component.age())));
        tooltip.add(Component.literal(String.format("flavors: %s", component.flavors().stream().map(ResourceKey::location).collect(Collectors.toList()))));
        tooltip.add(Component.literal(String.format("ingredients: %s", component.items())));
        return tooltip;
    }
}
