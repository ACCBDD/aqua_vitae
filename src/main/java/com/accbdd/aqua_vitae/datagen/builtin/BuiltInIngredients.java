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
            new BrewingIngredient.Builder(Ingredient.of(Items.APPLE))
                    .color(0xCCFF3300)
                    .starch(0.02f)
                    .sugar(0.6f)
                    .yeast(0.05f)
                    .yeastTolerance(0.08f)
                    .flavors(List.of(BuiltInFlavors.FRUITY.getKey(), BuiltInFlavors.SWEET.getKey())));
}
