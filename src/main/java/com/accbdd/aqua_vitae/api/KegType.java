package com.accbdd.aqua_vitae.api;

import java.util.List;

public record KegType(int capacity, List<IKegEffect> kegEffects, IngredientColor color) {
    public static KegType NULL = new KegType(1, List.of(), new IngredientColor(0, 0));
}
