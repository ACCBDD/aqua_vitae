package com.accbdd.aqua_vitae.api.naming;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class AllOfPredicate extends CombiningPredicate {
    public static final MapCodec<AllOfPredicate> CODEC = codec(AllOfPredicate::new);
    public static final DrinkPredicateType DRINK_PREDICATE_TYPE = new DrinkPredicateType(CODEC);

    public AllOfPredicate(List<DrinkPredicate> predicates) {
        super(predicates);
    }

    @Override
    public DrinkPredicateType getType() {
        return DRINK_PREDICATE_TYPE;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        for (DrinkPredicate predicate : predicates) {
            if (!predicate.test(fluidStack)) {
                return false;
            }
        }
        return true;
    }
}
