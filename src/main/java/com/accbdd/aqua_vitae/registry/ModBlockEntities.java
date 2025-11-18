package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.block.entity.CrushingTubBlockEntity;
import com.accbdd.aqua_vitae.block.entity.FermenterBlockEntity;
import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import com.accbdd.aqua_vitae.block.entity.PotStillBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<KegBlockEntity>> KEG = BLOCK_ENTITY_TYPES.register("keg",
            () -> BlockEntityType.Builder.of(KegBlockEntity::new, ModBlocks.KEG.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FermenterBlockEntity>> FERMENTER = BLOCK_ENTITY_TYPES.register("fermenter",
            () -> BlockEntityType.Builder.of(FermenterBlockEntity::new, ModBlocks.FERMENTER.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CrushingTubBlockEntity>> CRUSHING_TUB = BLOCK_ENTITY_TYPES.register("crushing_tub",
            () -> BlockEntityType.Builder.of(CrushingTubBlockEntity::new, ModBlocks.CRUSHING_TUB.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PotStillBlockEntity>> POT_STILL = BLOCK_ENTITY_TYPES.register("pot_still",
            () -> BlockEntityType.Builder.of(PotStillBlockEntity::new, ModBlocks.POT_STILL.get()).build(null));
}
