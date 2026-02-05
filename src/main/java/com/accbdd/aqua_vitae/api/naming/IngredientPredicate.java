package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.api.IngredientMap;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;


/**
 * checks if ingredient is present in IngredientMap
 */
public class IngredientPredicate implements DrinkPredicate {
    public static final MapCodec<IngredientPredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("ingredient").forGetter(pred -> pred.ingredientKey)
            ).apply(instance, IngredientPredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final String ingredientKey;

    public IngredientPredicate(String ingredientKey) {
        this.ingredientKey = ingredientKey;
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (fluidStack.has(ModComponents.ALCOHOL_PROPERTIES)) {
            IngredientMap ingredientMap = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).inputs();
            for (String key : ingredientMap.getMap().keySet()) {
                if (key.equals(this.ingredientKey))
                    return true;
            }
        }
        return false;
    }
}
