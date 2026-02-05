package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * maximum abv, inclusive
 */
public class MaximumABVPredicate implements DrinkPredicate {
    public static final MapCodec<MaximumABVPredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("value").forGetter(pred -> pred.maxABV)
            ).apply(instance, MaximumABVPredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final int maxABV;

    public MaximumABVPredicate(int maxABV) {
        this.maxABV = maxABV;
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return fluidStack.has(ModComponents.ALCOHOL_PROPERTIES) && fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).abv() <= maxABV;
    }
}
