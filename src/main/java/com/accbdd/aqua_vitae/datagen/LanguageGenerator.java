package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.common.data.LanguageProvider;

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

        add("grammar.aqua_vitae.container_of", "%s of %s");
    }

    private void addFluidSet(String name, Supplier<? extends LiquidBlock> block, Supplier<? extends BucketItem> bucket) {
        if (block != null) {
            addBlock(block, name);
        }
        if (bucket != null) {
            addItem(bucket, "Bucket of " + name);
        }
    }
}
