package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.datagen.builtin.BuiltInFlavors;
import com.accbdd.aqua_vitae.datagen.builtin.BuiltInIngredients;
import com.accbdd.aqua_vitae.api.BrewingIngredient;
import com.accbdd.aqua_vitae.api.Flavor;
import com.accbdd.aqua_vitae.registry.ModBlocks;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Map;
import java.util.function.Supplier;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class LanguageGenerator extends LanguageProvider {
    public LanguageGenerator(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("item_group." + MODID, "Aqua Vitae");
        add("block.aqua_vitae.wort", "Wort");
        add("config.jade.plugin_aqua_vitae.pot_still", "Pot Still");

        addFluidSet("Aqua Vitae", ModBlocks.AQUA_VITAE, ModItems.AQUA_VITAE_BUCKET);

        addBlock(ModBlocks.OAK_KEG, "Oak Keg");
        addBlock(ModBlocks.SPRUCE_KEG, "Spruce Keg");
        addBlock(ModBlocks.BIRCH_KEG, "Birch Keg");
        addBlock(ModBlocks.JUNGLE_KEG, "Jungle Keg");
        addBlock(ModBlocks.ACACIA_KEG, "Acacia Keg");
        addBlock(ModBlocks.DARK_OAK_KEG, "Dark Oak Keg");
        addBlock(ModBlocks.CRIMSON_KEG, "Crimson Keg");
        addBlock(ModBlocks.WARPED_KEG, "Warped Keg");
        addBlock(ModBlocks.CRUSHING_TUB, "Crushing Tub");
        addBlock(ModBlocks.POT_STILL, "Pot Still");
        addBlock(ModBlocks.MALT_KILN, "Malt Kiln");
        addBlock(ModBlocks.MASH_TUN, "Mash Tun");

        addItem(ModItems.CUP, "Cup");
        addItem(ModItems.SHOOTER, "Shooter");
        addItem(ModItems.METER, "Meter");
        addItem(ModItems.EYEBALL, "Eyeball");
        addItem(ModItems.BREW_BUCKET, "Brew Bucket");
        addItem(ModItems.MALT, "Malt");

        addIngredient(BuiltInIngredients.WHEAT, "Wheat");

        addFlavor(BuiltInFlavors.BREADY, "Bready");
        addFlavor(BuiltInFlavors.NUTTY, "Nutty");
        addFlavor(BuiltInFlavors.TOASTY, "Toasty");
        addFlavor(BuiltInFlavors.COCOA, "Cocoa");
        addFlavor(BuiltInFlavors.LICORICE, "Licorice");
        addFlavor(BuiltInFlavors.FRUITY, "Fruity");
        addFlavor(BuiltInFlavors.ACRID, "Acrid");
        addFlavor(BuiltInFlavors.SOUR, "Sour");
        addFlavor(BuiltInFlavors.SWEET, "Sweet");

        addGrammar("container_of", "%1$s of %2$s");
        addGrammar("malt", "%1$s %2$s %3$s");
        addGrammar("fluid_amount", "%s mB");
        addGrammar("list_combine", "%s, ");
        addGrammar("label", "%1$s %2$s");

        addAlcohol("generic", "Ferment");
        addAlcohol("liquor", "Liquor");
        addAlcohol("beer", "Beer");
        addAlcohol("small_beer", "Small Beer");
        addAlcohol("wort", "Wort");

        add("flavor.aqua_vitae.none", "None");
        add("flavor.aqua_vitae.label", "Flavors:");
        add("ingredient.aqua_vitae.roast.1", "Pale");
        add("ingredient.aqua_vitae.roast.2", "Amber");
        add("ingredient.aqua_vitae.roast.3", "Brown");
        add("ingredient.aqua_vitae.roast.4", "Chocolate");
        add("ingredient.aqua_vitae.roast.5", "Black");
        add("ingredient.aqua_vitae.no_ingredients", "The ingredient list has been lost...");
        add("ingredient.aqua_vitae.label", "Contains:");
        add("properties.aqua_vitae.sugar", "Sugar: %s");
        add("properties.aqua_vitae.starch", "Starch: %s");
        add("properties.aqua_vitae.diastatic_power", "DP: %s");
        add("properties.aqua_vitae.yeast", "Yeast: %1$s, Tolerance: %2$s");
        add("properties.aqua_vitae.color", "#%s");
        add("properties.aqua_vitae.abv", "%s ABV");
        add("properties.aqua_vitae.age", "Aged for %s days");
        add("properties.aqua_vitae.properties", "Hold %s for properties.");
        add("properties.aqua_vitae.ingredients", "Hold %s for ingredients.");
        add("key.categories.aqua_vitae", "Aqua Vitae");
        add("key.aqua_vitae.properties", "Show Fluid Properties");
        add("key.aqua_vitae.ingredients", "Show Ingredients");
        add("tooltip.aqua_vitae.heated", "Heated");
        add("tooltip.aqua_vitae.needs_heat", "Needs heat from below");

    }

    private void addFluidSet(String name, Supplier<? extends LiquidBlock> block, Supplier<? extends BucketItem> bucket) {
        if (block != null) {
            addBlock(block, name);
        }
        if (bucket != null) {
            addItem(bucket, "Bucket of " + name);
        }
    }

    private void addIngredient(Map.Entry<ResourceKey<BrewingIngredient>, BrewingIngredient> ingredient, String name) {
        add("ingredient.aqua_vitae." + ingredient.getKey().location(), name);
    }

    private void addFlavor(Map.Entry<ResourceKey<Flavor>, Flavor> flavor, String name) {
        add("flavor.aqua_vitae." + flavor.getKey().location(), name);
    }

    private void addGrammar(String name, String text) {
        add("grammar.aqua_vitae." + name, text);
    }

    private void addAlcohol(String name, String text) {
        add("alcohol.aqua_vitae." + name, text);
    }
}
