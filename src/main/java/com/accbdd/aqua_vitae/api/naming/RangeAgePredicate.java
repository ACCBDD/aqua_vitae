package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * range age, exclusive min, inclusive max
 */
public class RangeAgePredicate implements DrinkPredicate {
    public static final MapCodec<RangeAgePredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("min").forGetter(pred -> pred.minAge),
                    Codec.INT.fieldOf("max").forGetter(pred -> pred.maxAge)
            ).apply(instance, RangeAgePredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final int minAge;
    private final int maxAge;

    public RangeAgePredicate(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (!fluidStack.has(ModComponents.ALCOHOL_PROPERTIES))
            return false;
        float age = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).age();
        return age > minAge &&
                age <= maxAge;
    }
}
