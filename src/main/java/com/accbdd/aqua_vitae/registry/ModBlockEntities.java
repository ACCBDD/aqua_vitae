package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.block.entity.FermentingBlockEntity;
import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<KegBlockEntity>> KEG = BLOCK_ENTITY_TYPES.register("keg",
            () -> BlockEntityType.Builder.of(KegBlockEntity::new, ModBlocks.KEG.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FermentingBlockEntity>> FERMENTER = BLOCK_ENTITY_TYPES.register("fermenter",
            () -> BlockEntityType.Builder.of(FermentingBlockEntity::new, ModBlocks.FERMENTER.get()).build(null));
}
