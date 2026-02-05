package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.registry.ModComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * range abv, exclusive min, inclusive max
 */
public class RangeABVPredicate implements DrinkPredicate {
    public static final MapCodec<RangeABVPredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("min").forGetter(pred -> pred.minABV),
                    Codec.INT.fieldOf("max").forGetter(pred -> pred.maxABV)
            ).apply(instance, RangeABVPredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final int minABV;
    private final int maxABV;

    public RangeABVPredicate(int minABV, int maxABV) {
        this.minABV = minABV;
        this.maxABV = maxABV;
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (!fluidStack.has(ModComponents.ALCOHOL_PROPERTIES))
            return false;
        float abv = fluidStack.get(ModComponents.ALCOHOL_PROPERTIES).abv();
        return abv > minABV &&
                abv <= maxABV;
    }
}
