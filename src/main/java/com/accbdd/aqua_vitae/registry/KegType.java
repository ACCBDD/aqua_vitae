package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.IngredientColor;

import java.util.List;

public record KegType(int capacity, List<Flavor.Transition> flavorAdds, IngredientColor color) {
    public static KegType NULL = new KegType(1, List.of(), new IngredientColor(0, 0));
}
