package com.accbdd.aqua_vitae.datagen;

import com.accbdd.aqua_vitae.datagen.loot.BlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LootTableGenerator extends LootTableProvider {

    private static final List<LootTableProvider.SubProviderEntry> entries = List.of(
            new LootTableProvider.SubProviderEntry(
                    BlockLootTables::new,
                    LootContextParamSets.BLOCK
            )
    );
    public LootTableGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), entries, registries);
    }
}
