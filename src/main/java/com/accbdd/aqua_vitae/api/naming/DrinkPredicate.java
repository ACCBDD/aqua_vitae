package com.accbdd.aqua_vitae.api.naming;

import com.accbdd.aqua_vitae.registry.ModDrinkPredicateType;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.function.Predicate;

public interface DrinkPredicate extends Predicate<FluidStack> {
    Codec<DrinkPredicate> CODEC = ModDrinkPredicateType.DRINK_PREDICATE_TYPES.byNameCodec()
            .dispatch(
                    "type",
                    DrinkPredicate::getType,
                    DrinkPredicateType::codec
            );

    DrinkPredicateType getType();
}
