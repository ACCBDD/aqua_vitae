package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KegBlockEntity>> KEG = BLOCK_ENTITY_TYPES.register("keg",
            () -> BlockEntityType.Builder.of(KegBlockEntity::new,
                    ModBlocks.OAK_KEG.get(),
                    ModBlocks.SPRUCE_KEG.get(),
                    ModBlocks.JUNGLE_KEG.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FermenterBlockEntity>> FERMENTER = BLOCK_ENTITY_TYPES.register("fermenter",
            () -> BlockEntityType.Builder.of(FermenterBlockEntity::new, ModBlocks.FERMENTER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrushingTubBlockEntity>> CRUSHING_TUB = BLOCK_ENTITY_TYPES.register("crushing_tub",
            () -> BlockEntityType.Builder.of(CrushingTubBlockEntity::new, ModBlocks.CRUSHING_TUB.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PotStillBlockEntity>> POT_STILL = BLOCK_ENTITY_TYPES.register("pot_still",
            () -> BlockEntityType.Builder.of(PotStillBlockEntity::new, ModBlocks.POT_STILL.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MaltKilnBlockEntity>> MALT_KILN = BLOCK_ENTITY_TYPES.register("malt_kiln",
            () -> BlockEntityType.Builder.of(MaltKilnBlockEntity::new, ModBlocks.MALT_KILN.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MashTunBlockEntity>> MASH_TUN = BLOCK_ENTITY_TYPES.register("mash_tun",
            () -> BlockEntityType.Builder.of(MashTunBlockEntity::new, ModBlocks.MASH_TUN.get()).build(null));
}
