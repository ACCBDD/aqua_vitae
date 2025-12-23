package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.IngredientColor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Keg(int capacity, List<Flavor.Transition> flavorAdds, IngredientColor color) {
    public static final Codec<Keg> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("capacity").forGetter(Keg::capacity),
                    Flavor.Transition.CODEC.listOf().fieldOf("flavor_adds").forGetter(Keg::flavorAdds),
                    IngredientColor.CODEC.fieldOf("color").forGetter(Keg::color)
            ).apply(instance, Keg::new));

    public static final Keg NULL = new Keg(1, List.of(), new IngredientColor(0, 0));
}
