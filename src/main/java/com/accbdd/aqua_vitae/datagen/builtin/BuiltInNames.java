package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.api.naming.IngredientPredicate;
import com.accbdd.aqua_vitae.api.naming.RangeABVPredicate;

import java.util.List;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.nameEntry;

public class BuiltInNames {
    static {
        nameEntry("small_beer", List.of(new RangeABVPredicate(5, 30)), 10);
        nameEntry("beer", List.of(new RangeABVPredicate(30, 80)), 10);
        nameEntry("brown_beer", List.of(new RangeABVPredicate(30, 80), new IngredientPredicate("aqua_vitae:wheat.3.malt")), 20);
    }
}
