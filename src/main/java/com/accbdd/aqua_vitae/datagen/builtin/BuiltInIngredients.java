package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.api.BrewingIngredient;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.Set;

import static com.accbdd.aqua_vitae.datagen.builtin.BuiltIn.brewingIngredient;

public class BuiltInIngredients {
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> APPLE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> HONEY;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> NETHER_WART;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> SWEET_BERRY;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> BROWN_MUSHROOM;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> RED_MUSHROOM;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> CHORUS_FRUIT;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> CHORUS_FLOWER;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> MELON;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> PUMPKIN;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> CARROT;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> POTATO;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> BEETROOT;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> WHEAT;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> SUGAR;

    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> WHITE_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> LIGHT_GRAY_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> GRAY_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> BLACK_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> BROWN_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> RED_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> ORANGE_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> YELLOW_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> LIME_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> GREEN_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> CYAN_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> LIGHT_BLUE_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> BLUE_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> PURPLE_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> MAGENTA_DYE;
    public static final Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> PINK_DYE;


    static {
        APPLE = brewingIngredient("apple",
                new BrewingIngredient(
                        Ingredient.of(Items.APPLE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCFF3300, 10)
                                .sugar(24)
                                .yeast(10)
                                .yeastTolerance(90)
                                .build(),
                        Set.of(BuiltInFlavors.FRUITY.getKey(), BuiltInFlavors.SWEET.getKey())));
        HONEY = brewingIngredient("honey",
                new BrewingIngredient(
                        Ingredient.of(Items.HONEY_BOTTLE, Items.HONEYCOMB),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCFFB900, 4)
                                .sugar(36)
                                .yeast(15)
                                .yeastTolerance(120)
                                .starch(0)
                                .build(),
                        Set.of(BuiltInFlavors.FLORAL.getKey(), BuiltInFlavors.SWEET.getKey())
                ));
        NETHER_WART = brewingIngredient("nether_wart",
                new BrewingIngredient(
                        Ingredient.of(Items.NETHER_WART, Items.NETHER_WART_BLOCK),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCC7F000, 5)
                                .sugar(4)
                                .yeast(5)
                                .yeastTolerance(180)
                                .starch(20)
                                .build(),
                        Set.of(BuiltInFlavors.SULFURIC.getKey(), BuiltInFlavors.ACRID.getKey())
                ));
        SWEET_BERRY = brewingIngredient("sweet_berry",
                new BrewingIngredient(
                        Ingredient.of(Items.SWEET_BERRIES),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCB30000, 5)
                                .sugar(20)
                                .yeast(8)
                                .yeastTolerance(100)
                                .build(),
                        Set.of(BuiltInFlavors.FRUITY.getKey(), BuiltInFlavors.SWEET.getKey())
                ));
        BROWN_MUSHROOM = brewingIngredient("brown_mushroom",
                new BrewingIngredient(
                        Ingredient.of(Items.BROWN_MUSHROOM, Items.BROWN_MUSHROOM_BLOCK),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCC5D4037, 1)
                                .sugar(2)
                                .yeast(60)
                                .yeastTolerance(60)
                                .diastaticPower(20)
                                .build(),
                        Set.of(BuiltInFlavors.EARTHY.getKey())
                ));
        RED_MUSHROOM = brewingIngredient("red_mushroom",
                new BrewingIngredient(
                        Ingredient.of(Items.RED_MUSHROOM, Items.RED_MUSHROOM_BLOCK),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCED1C24, 1)
                                .sugar(4)
                                .yeast(45)
                                .yeastTolerance(120)
                                .diastaticPower(30)
                                .build(),
                        Set.of(BuiltInFlavors.FUNKY.getKey())
                ));
        CHORUS_FRUIT = brewingIngredient("chorus_fruit",
                new BrewingIngredient(
                        Ingredient.of(Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCC896789, 1)
                                .sugar(10)
                                .yeast(20)
                                .yeastTolerance(150)
                                .diastaticPower(50)
                                .starch(120)
                                .build(),
                        Set.of(BuiltInFlavors.WISPY.getKey())
                ));
        CHORUS_FLOWER = brewingIngredient("chorus_flower",
                new BrewingIngredient(
                        Ingredient.of(Items.CHORUS_FLOWER),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCE7BCE7, 5)
                                .sugar(30)
                                .yeast(40)
                                .yeastTolerance(150)
                                .build(),
                        Set.of(BuiltInFlavors.OZONE.getKey())
                ));
        MELON = brewingIngredient("melon",
                new BrewingIngredient(
                        Ingredient.of(Items.MELON, Items.MELON_SLICE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCFF6347, 3)
                                .sugar(18)
                                .yeast(5)
                                .yeastTolerance(60)
                                .build(),
                        Set.of(BuiltInFlavors.CRISP.getKey(), BuiltInFlavors.FRUITY.getKey())
                ));
        PUMPKIN = brewingIngredient("pumpkin",
                new BrewingIngredient(
                        Ingredient.of(Items.PUMPKIN),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCFF7518, 2)
                                .sugar(10)
                                .yeast(5)
                                .yeastTolerance(80)
                                .starch(80)
                                .build(),
                        Set.of(BuiltInFlavors.EARTHY.getKey(), BuiltInFlavors.HEAVY.getKey(), BuiltInFlavors.STARCHY.getKey())
                ));
        CARROT = brewingIngredient("carrot",
                new BrewingIngredient(
                        Ingredient.of(Items.CARROT),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCFF9100, 5)
                                .sugar(12)
                                .yeast(5)
                                .yeastTolerance(80)
                                .starch(40)
                                .build(),
                        Set.of(BuiltInFlavors.CRISP.getKey())
                ));
        POTATO = brewingIngredient("potato",
                new BrewingIngredient(
                        Ingredient.of(Items.POTATO, Items.BAKED_POTATO),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xBBF5F5DC)
                                .sugar(4)
                                .yeast(2)
                                .yeastTolerance(80)
                                .starch(160)
                                .build(),
                        Set.of(BuiltInFlavors.STARCHY.getKey())
                ));
        BEETROOT = brewingIngredient("beetroot",
                new BrewingIngredient(
                        Ingredient.of(Items.BEETROOT),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCC910022, 6)
                                .sugar(18)
                                .yeast(5)
                                .yeastTolerance(90)
                                .starch(80)
                                .build(),
                        Set.of(BuiltInFlavors.STARCHY.getKey(), BuiltInFlavors.SWEET.getKey())
                ));
        WHEAT = brewingIngredient("wheat",
                new BrewingIngredient(
                        Ingredient.of(Items.WHEAT),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xCCFDCC80)
                                .sugar(2)
                                .yeast(25)
                                .yeastTolerance(80)
                                .diastaticPower(5)
                                .starch(100)
                                .build(),
                        new BrewingIngredient.BrewingProperties(0xCCDCBB65, 100, 10, 50, 80, 50),
                        Set.of(BuiltInFlavors.BREADY.getKey())));
        SUGAR = brewingIngredient("sugar",
                new BrewingIngredient(
                        Ingredient.of(Items.SUGAR),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0x33FFFFFF)
                                .sugar(50)
                                .build(),
                        Set.of(BuiltInFlavors.SWEET.getKey())
                ));
        WHITE_DYE = brewingIngredient("white_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.WHITE_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFFFFFFFF, 10)
                                .build(),
                        Set.of()
                ));
        LIGHT_GRAY_DYE = brewingIngredient("light_gray_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.LIGHT_GRAY_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFFC0C0C0, 10)
                                .build(),
                        Set.of()
                ));
        GRAY_DYE = brewingIngredient("gray_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.GRAY_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF505050, 10)
                                .build(),
                        Set.of()
                ));
        BLACK_DYE = brewingIngredient("black_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.BLACK_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF0A0A0A, 10)
                                .build(),
                        Set.of()
                ));
        BROWN_DYE = brewingIngredient("brown_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.BROWN_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF703810, 10)
                                .build(),
                        Set.of()
                ));
        RED_DYE = brewingIngredient("red_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.RED_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFFFF0000, 10)
                                .build(),
                        Set.of()
                ));
        ORANGE_DYE = brewingIngredient("orange_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.ORANGE_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFFFF8C00, 10)
                                .build(),
                        Set.of()
                ));
        YELLOW_DYE = brewingIngredient("yellow_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.YELLOW_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFFFFFF00, 10)
                                .build(),
                        Set.of()
                ));
        LIME_DYE = brewingIngredient("lime_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.LIME_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF32CD32, 10)
                                .build(),
                        Set.of()
                ));
        GREEN_DYE = brewingIngredient("green_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.GREEN_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF008000, 10)
                                .build(),
                        Set.of()
                ));
        CYAN_DYE = brewingIngredient("cyan_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.CYAN_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF00FFFF, 10)
                                .build(),
                        Set.of()
                ));
        LIGHT_BLUE_DYE = brewingIngredient("light_blue_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.LIGHT_BLUE_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF00BFFF, 10)
                                .build(),
                        Set.of()
                ));
        BLUE_DYE = brewingIngredient("blue_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.BLUE_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF0000FF, 10)
                                .build(),
                        Set.of()
                ));
        PURPLE_DYE = brewingIngredient("purple_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.PURPLE_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFF800080, 10)
                                .build(),
                        Set.of()
                ));
        MAGENTA_DYE = brewingIngredient("magenta_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.MAGENTA_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFFFF00FF, 10)
                                .build(),
                        Set.of()
                ));
        PINK_DYE = brewingIngredient("pink_dye",
                new BrewingIngredient(
                        Ingredient.of(Items.PINK_DYE),
                        new BrewingIngredient.BrewingProperties.Builder()
                                .color(0xFFFF69B4, 10)
                                .build(),
                        Set.of()
                ));
    }
}
