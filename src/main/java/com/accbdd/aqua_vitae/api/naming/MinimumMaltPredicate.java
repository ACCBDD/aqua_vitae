package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.api.IngredientMap;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * checks for a minimum number of malt ingredients
 */
public class MinimumMaltPredicate implements DrinkPredicate {
    public static final MapCodec<MinimumMaltPredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("count", 1).forGetter(pred -> pred.maltCount)
            ).apply(instance, MinimumMaltPredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final int maltCount;

    public MinimumMaltPredicate(int maltCount) {
        this.maltCount = maltCount;
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        int testCount = 0;
        if (fluidStack.has(ModComponents.ALCOHOL_PROPERTIES)) {
            IngredientMap ingredientMap = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).inputs();
            for (String key : ingredientMap.getMap().keySet()) {
                if (key.endsWith(".malt")) {
                    if (++testCount >= maltCount) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
