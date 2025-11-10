package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.registry.ModBlocks;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.AbstractCauldronBlock;
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
        add("effect.aqua_vitae.tipsy", "Tipsy");

        addFluidSet("Aqua Vitae", ModBlocks.AQUA_VITAE , ModItems.AQUA_VITAE_BUCKET);
        addFluidSet("Tequila Blanco", ModBlocks.TEQUILA_BLANCO , ModItems.TEQUILA_BLANCO_BUCKET);
        addFluidSet("Tequila Reposado", ModBlocks.TEQUILA_REPOSADO , ModItems.TEQUILA_REPOSADO_BUCKET);
        addFluidSet("Tequila Añejo", ModBlocks.TEQUILA_ANEJO , ModItems.TEQUILA_ANEJO_BUCKET);
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
