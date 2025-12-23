package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.IngredientColor;
import com.accbdd.aqua_vitae.registry.Keg;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Map;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.keg;

public class BuiltInKegs {
    public final static Map.Entry<ResourceKey<Keg>, Keg> OAK;
    public final static Map.Entry<ResourceKey<Keg>, Keg> WARPED;

    static {
        OAK = keg("oak",
                new Keg(4000,
                        List.of(new Flavor.Transition(List.of(BuiltInFlavors.BREADY.getKey()), 1000)),
                        new IngredientColor(0xDDFFCC00)));
        WARPED = keg("warped",
                new Keg(4000,
                        List.of(new Flavor.Transition(List.of(BuiltInFlavors.ACRID.getKey()), 200)),
                        new IngredientColor(0xDDFF00FF, 20)));
    }
}
