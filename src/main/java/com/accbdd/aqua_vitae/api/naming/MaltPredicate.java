package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.api.IngredientMap;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * checks if any ingredient is malt
 */
public class MaltPredicate implements DrinkPredicate {
    public static final MapCodec<MaltPredicate> CODEC = MapCodec.unit(new MaltPredicate());
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (fluidStack.has(ModComponents.ALCOHOL_PROPERTIES)) {
            IngredientMap ingredientMap = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).inputs();
            for (String key : ingredientMap.getMap().keySet()) {
                if (key.endsWith(".malt"))
                    return true;
            }
        }
        return false;
    }
}
