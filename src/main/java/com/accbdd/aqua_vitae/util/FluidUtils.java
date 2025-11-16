package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
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
        BrewingIngredient.BrewingProperties initial = component.properties();
        BrewingIngredient.BrewingProperties toAdd = ingredient.properties();
        fluid.set(ModComponents.PRECURSOR_PROPERTIES, new PrecursorPropertiesComponent(items, flavors, initial.add(toAdd, component.ingredients().size())));
    }

    public static List<Component> getPrecursorTooltip(FluidStack fluid) {
        PrecursorPropertiesComponent component = fluid.get(ModComponents.PRECURSOR_PROPERTIES);
        if (component == null)
            return List.of();

        BrewingIngredient.BrewingProperties props = component.properties();

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(String.format("color: %s, starch: %s, sugar: %s", Integer.toHexString(props.color()), props.starch(), props.sugar())));
        tooltip.add(Component.literal(String.format("yeast: %s, yeast_tol: %s, dp: %s", props.yeast(), props.yeastTolerance(), props.diastaticPower())));
        tooltip.add(Component.literal(String.format("flavors: %s", component.flavors().stream().map(ResourceKey::location).collect(Collectors.toList()))));
        tooltip.add(Component.literal(String.format("ingredients: %s", component.ingredients())));
        return tooltip;
    }
}
