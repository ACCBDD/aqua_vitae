package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.datagen.builtin.BuiltInIngredients;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
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

        addBlock(ModBlocks.KEG, "Keg");
        addBlock(ModBlocks.CRUSHING_TUB, "Crushing Tub");
        addBlock(ModBlocks.POT_STILL, "Pot Still");
        addBlock(ModBlocks.MALT_KILN, "Malt Kiln");

        addItem(ModItems.CUP, "Cup");
        addItem(ModItems.SHOOTER, "Shooter");
        addItem(ModItems.METER, "Meter");
        addItem(ModItems.EYEBALL, "Eyeball");
        addItem(ModItems.BREW_BUCKET, "Brew Bucket");
        addItem(ModItems.MALT, "Malt");

        addIngredient(BuiltInIngredients.WHEAT, "Wheat");

        addGrammar("container_of", "%1$s of %2$s");
        addGrammar("malt", "%1$s %2$s");

        add("ingredient.aqua_vitae.roast.1", "Pale");
        add("ingredient.aqua_vitae.roast.2", "Amber");
        add("ingredient.aqua_vitae.roast.3", "Brown");
        add("ingredient.aqua_vitae.roast.4", "Chocolate");
        add("ingredient.aqua_vitae.roast.5", "Black");
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

    private void addGrammar(String name, String text) {
        add("grammar.aqua_vitae." + name, text);
    }
}
