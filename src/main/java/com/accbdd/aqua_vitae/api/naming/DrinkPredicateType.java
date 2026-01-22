package com.accbdd.aqua_vitae.api.naming;

import com.mojang.serialization.MapCodec;

public record DrinkPredicateType(MapCodec<? extends DrinkPredicate> codec) {
}
