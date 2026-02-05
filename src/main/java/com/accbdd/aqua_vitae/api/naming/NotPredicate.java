package com.accbdd.aqua_vitae.api.naming;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

public class NotPredicate implements DrinkPredicate {
    public static final MapCodec<NotPredicate> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    DrinkPredicate.CODEC.fieldOf("predicate").forGetter(pred -> pred.predicate)
            ).apply(instance, NotPredicate::new));
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    private final DrinkPredicate predicate;

    public NotPredicate(DrinkPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return !predicate.test(fluidStack);
    }
}
