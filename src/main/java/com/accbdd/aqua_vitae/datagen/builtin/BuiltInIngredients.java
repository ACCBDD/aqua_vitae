package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.brewingIngredient;

public class BuiltInIngredients {
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> APPLE = brewingIngredient("apple",
            new BrewingIngredient(
                    Ingredient.of(Items.APPLE),
                    new BrewingIngredient.BrewingProperties.Builder()
                            .color(0xCCFF3300)
                            .starch(2)
                            .sugar(60)
                            .yeast(5)
                            .yeastTolerance(80)
                            .build(),
                    List.of(BuiltInFlavors.FRUITY.getKey(), BuiltInFlavors.SWEET.getKey())));

    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> LAPIS = brewingIngredient("lapis",
            new BrewingIngredient(
                    Ingredient.of(Items.LAPIS_LAZULI),
                    new BrewingIngredient.BrewingProperties.Builder()
                            .color(0xFF0000FF)
                            .build(),
                    List.of()));
}
