package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

public class IngredientPredicate implements DrinkPredicate {
    public static final MapCodec<IngredientPredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("min").forGetter(pred -> pred.minABV)
            ).apply(instance, IngredientPredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final int minABV;

    public IngredientPredicate(int minABV) {
        this.minABV = minABV;
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return fluidStack.has(ModComponents.ALCOHOL_PROPERTIES) && fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).abv() >= minABV;
    }
}
