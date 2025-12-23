package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.Set;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.brewingIngredient;

public class BuiltInIngredients {
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> APPLE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> LAPIS;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> SUGAR;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> WHEAT;

    static {
        APPLE = brewingIngredient("apple",
                new BrewingIngredient(
                        Ingredient.of(Items.APPLE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCFF3300, 10)
                                .starch(2)
                                .sugar(60)
                                .yeast(5)
                                .yeastTolerance(80)
                                .build(),
                        Set.of(BuiltInFlavors.FRUITY.getKey(), BuiltInFlavors.SWEET.getKey())));
        LAPIS = brewingIngredient("lapis",
                new BrewingIngredient(
                        Ingredient.of(Items.LAPIS_LAZULI),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF0000FF, 10)
                                .build(),
                        Set.of()));
        SUGAR = brewingIngredient("sugar",
                new BrewingIngredient(
                        Ingredient.of(Items.SUGAR),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0x00FFFFFF)
                                .sugar(200)
                                .build(),
                        Set.of()));
        WHEAT = brewingIngredient("wheat",
                new BrewingIngredient(
                        Ingredient.of(Items.WHEAT),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0x00FFFFFF)
                                .yeast(200)
                                .yeastTolerance(80)
                                .build(),
                        new BrewingIngredient.BrewingProperties(0xCCDCBB65, 100, 10, 50, 80, 50),
                        Set.of(BuiltInFlavors.BREADY.getKey())));
    }
}
