package com.accbdd.aqua_vitae.datagen.builtin;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.api.naming.IngredientPredicate;
import com.accbdd.aqua_vitae.api.naming.MaximumABVPredicate;
import com.accbdd.aqua_vitae.api.naming.MinimumABVPredicate;
import com.accbdd.aqua_vitae.api.naming.NameEntry;
import com.accbdd.aqua_vitae.registry.ModFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BuiltInNames extends DataMapProvider {
    public BuiltInNames(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(AquaVitae.DRINK_NAMES).add(
                ModFluids.ALCOHOL.getKey(),
                List.of(
                        new NameEntry(
                                "name.aqua_vitae.small_beer",
                                List.of(new MinimumABVPredicate(5), new MaximumABVPredicate(30)),
                                10
                        ),
                        new NameEntry(
                                "name.aqua_vitae.beer",
                                List.of(new MinimumABVPredicate(30), new MaximumABVPredicate(80)),
                                10
                        ),
                        new NameEntry(
                                "name.aqua_vitae.brown_beer",
                                List.of(new MinimumABVPredicate(40), new IngredientPredicate("aqua_vitae:wheat.3.malt")),
                                20
                        )
                ),
                false
        );
    }
}
