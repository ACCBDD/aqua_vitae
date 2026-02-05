package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.api.BrewingIngredient;
import com.accbdd.aqua_vitae.api.BrewingIngredient.BrewingProperties;
import com.accbdd.aqua_vitae.api.Flavor;
import com.accbdd.aqua_vitae.api.IngredientColor;
import com.accbdd.aqua_vitae.api.IngredientMap;
import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.FermentingPropertiesComponent;
import com.accbdd.aqua_vitae.component.PrecursorPropertiesComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class FluidUtils {
    public static final Predicate<FluidStack> HAS_ALCOHOL = stack -> stack.has(ModComponents.ALCOHOL_PROPERTIES);
    public static final Predicate<FluidStack> HAS_FERMENT = stack -> stack.has(ModComponents.FERMENTING_PROPERTIES);
    public static final Predicate<FluidStack> HAS_PRECURSOR = stack -> stack.has(ModComponents.PRECURSOR_PROPERTIES);

    public static boolean handleInteraction(Player player, InteractionHand hand, ItemStack itemStack, IFluidHandler other) {
        if (itemStack.getCapability(Capabilities.FluidHandler.ITEM) == null) {
            return false;
        } else {
            ItemStack copyStack = itemStack.copyWithCount(1);
            IFluidHandlerItem handler = copyStack.getCapability(Capabilities.FluidHandler.ITEM);
            if (handler == null) return false;

            boolean transfer = false;

            // Attempt to fill item from 'other' if item is empty
            for (int itemTank = 0; itemTank < handler.getTanks(); itemTank++) {
                FluidStack fluidInItem = handler.getFluidInTank(itemTank);
                if (fluidInItem.isEmpty()) {
                    FluidStack fluidInOther = other.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                    if (!fluidInOther.isEmpty()) {
                        int filled = handler.fill(fluidInOther, IFluidHandler.FluidAction.EXECUTE);
                        if (filled > 0) {
                            other.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                            transfer = true;
                        }
                    }
                }
            }

            // Attempt to empty item into 'other' if item contains fluid
            if (!transfer) {
                for (int itemTank = 0; itemTank < handler.getTanks(); itemTank++) {
                    FluidStack fluidInItem = handler.getFluidInTank(itemTank);
                    if (!fluidInItem.isEmpty()) {
                        int remaining = fluidInItem.getAmount();
                        for (int otherTank = 0; otherTank < other.getTanks() && remaining > 0; otherTank++) {
                            FluidStack toFill = fluidInItem.copyWithAmount(remaining);
                            int filled = other.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                            if (filled > 0) {
                                handler.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                                remaining -= filled;
                                transfer = true;
                            }
                        }
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
    }

    /**
     * Modifies a FluidStack {@param stack}'s precursor component according to {@param ingredient}
     *
     * @param fluid      the FluidStack to modify
     * @param ingredient the ingredient to modify by
     * @param itemStack  the itemStack to modify by
     * @see PrecursorPropertiesComponent
     */
    public static void modifyPrecursor(FluidStack fluid, BrewingIngredient ingredient, ItemStack itemStack) {
        PrecursorPropertiesComponent component = fluid.getOrDefault(ModComponents.PRECURSOR_PROPERTIES, PrecursorPropertiesComponent.EMPTY);
        IngredientMap items = component.ingredients();
        Set<ResourceKey<Flavor>> flavors = new HashSet<>();
        flavors.addAll(ingredient.flavors());
        flavors.addAll(component.flavors());
        items.add(itemStack);
        BrewingProperties initial = component.properties();
        BrewingProperties toAdd = ingredient.properties();
        fluid.set(ModComponents.PRECURSOR_PROPERTIES, new PrecursorPropertiesComponent(items, flavors, initial.add(toAdd)));
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
        double abvFactor = Math.max(1.0 - (double) alcohol.abv() / Math.max(brewing.yeastTolerance(), 1), 0.01); // closer to abv tolerance means slower ferment
        double yeastFactor = (double) brewing.yeast() / Math.max(brewing.sugar(), 1); // more yeast per sugar means faster ferment
        double conversionRate = 5 * yeastFactor * abvFactor * batchPenalty;
        double converted = conversionRate - Math.max(conversionRate - brewing.sugar(), 0);
        double abvDelta = converted / 2; //sugar is converted to alcohol at 2:1

        FermentingPropertiesComponent newFerment = new FermentingPropertiesComponent(ferment.stress(),
                ferment.flavors(),
                new BrewingProperties(brewing.color(),
                        brewing.starch(),
                        Math.max(0, (int) (brewing.sugar() - converted)),
                        brewing.yeast(),
                        brewing.yeastTolerance(),
                        brewing.diastaticPower()));

        AlcoholPropertiesComponent newAlcohol = new AlcoholPropertiesComponent(alcohol.color(),
                Math.min((float) (alcohol.abv() + abvDelta), brewing.yeastTolerance()),
                alcohol.age(),
                alcohol.flavors(),
                alcohol.inputs());

        AquaVitae.LOGGER.debug("conversion rate: {} (batch: {}, abv: {}, yeast: {}), abv delta: {}", conversionRate, batchPenalty, abvFactor, yeastFactor, abvDelta);
        AquaVitae.LOGGER.debug("sugar {} -> {}; abv {} -> {}", brewing.sugar(), newFerment.properties().sugar(), alcohol.abv(), newAlcohol.abv());

        if (newFerment.properties().sugar() == 0 || abvDelta == 0 || alcohol.abv() >= brewing.yeastTolerance())
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
        newFluid.set(ModComponents.FERMENTING_PROPERTIES, new FermentingPropertiesComponent(ferment.stress() + 1, ferment.flavors(), ferment.properties()));
        return newFluid;
    }

    /**
     * distills a fluid with an {@link AlcoholPropertiesComponent}
     * @param fluid the fluid to distill
     * @param lossFactor the amount of alcohol kept in distilling (complement of angel's share)
     * @param distillFactor the factor to distill by - higher values mean less passes needed to hit max
     * @param maxAbv the maximum abv this distillation can hit
     * @return a new FluidStack representing the distilled fluid, with color and properties changed
     */
    public static FluidStack distill(FluidStack fluid, float lossFactor, float distillFactor, float maxAbv) {
        AlcoholPropertiesComponent alcohol = fluid.get(ModComponents.ALCOHOL_PROPERTIES);
        if (alcohol == null )
            return fluid;

        float currentAbv = alcohol.abv();
        if (currentAbv <= 0 || currentAbv >= maxAbv)
            return fluid;

        float x = currentAbv / 1000f;
        float numerator = distillFactor * x;
        float denominator = 1.0f + (distillFactor - 1.0f) * x;
        float y = numerator / denominator;
        float potentialAbv = y * 1000f;
        float newAbv = Math.min(potentialAbv, maxAbv);

        int newVolume;
        float totalAlcoholUnits = fluid.getAmount() * currentAbv;
        float retainedAlcoholUnits = totalAlcoholUnits * lossFactor;
        newVolume = (int) (retainedAlcoholUnits / newAbv);
        newVolume = Math.clamp(newVolume, 1, fluid.getAmount());

        float improvementRatio = (newAbv - currentAbv) / (maxAbv - currentAbv + 1e-6f);
        int newColor = NumUtils.lightenColor(alcohol.color().color(), improvementRatio);

        FluidStack newFluid = fluid.copyWithAmount(newVolume);
        newFluid.set(ModComponents.ALCOHOL_PROPERTIES, new AlcoholPropertiesComponent(
                new IngredientColor(newColor, (int) (alcohol.color().influence() * improvementRatio)),
                Math.round(newAbv),
                alcohol.age(),
                BrewingUtils.transitionFlavors(alcohol.flavors(), Flavor::distill, Math.round(newAbv)),
                alcohol.inputs()));

        return newFluid;
    }

    /**
     * ages a fluid with an {@link AlcoholPropertiesComponent}
     * @param fluid the fluid to distill
     * @param ageBy the amount of ticks to age the alcohol by
     * @param colorToAdd the color to add (from the aging vessel)
     * @return a new FluidStack representing the aged fluid
     */
    public static FluidStack age(FluidStack fluid, int ageBy, IngredientColor colorToAdd, List<Flavor.Transition> flavorsToAdd) {
        if (!fluid.has(ModComponents.ALCOHOL_PROPERTIES))
            return fluid;

        AlcoholPropertiesComponent props = fluid.get(ModComponents.ALCOHOL_PROPERTIES);
        int newAge = props.age() + ageBy;
        IngredientColor ageIntensifiedColor = new IngredientColor(colorToAdd.color(), Math.max(colorToAdd.influence() * ageBy, 1));
        IngredientColor newColor = IngredientColor.blendColor(props.color(), colorToAdd);
        Set<ResourceKey<Flavor>> newFlavors = BrewingUtils.transitionFlavors(props.flavors(), Flavor::age, newAge);
        if (!flavorsToAdd.isEmpty()) {
            for (Flavor.Transition transition : flavorsToAdd) {
                if (newAge > transition.transitionPoint()) {
                    newFlavors.addAll(transition.flavors());
                }
            }
        }
        FluidStack newFluid = fluid.copy();
        newFluid.set(ModComponents.ALCOHOL_PROPERTIES, new AlcoholPropertiesComponent(newColor, props.abv(), newAge, newFlavors, props.inputs()));
        return newFluid;
    }

    public static int getColorOrInvisible(FluidStack stack) {
        if (stack.has(ModComponents.ALCOHOL_PROPERTIES))
            return stack.getOrDefault(ModComponents.ALCOHOL_PROPERTIES, AlcoholPropertiesComponent.EMPTY).color().color();
        if (stack.has(ModComponents.FERMENTING_PROPERTIES))
            return stack.getOrDefault(ModComponents.FERMENTING_PROPERTIES, FermentingPropertiesComponent.EMPTY).properties().color().color();
        if (stack.has(ModComponents.PRECURSOR_PROPERTIES))
            return stack.getOrDefault(ModComponents.PRECURSOR_PROPERTIES, PrecursorPropertiesComponent.EMPTY).properties().color().color();
        return 0x00000000;
    }
}
