package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MashTunBlockEntity extends AbstractBEWithData {
    public static final int MAX_PROGRESS = 10;
    public static final int MAX_FLUID = 4000;

    public MashTunBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MASH_TUN.get(), pos, blockState);
    }

    public void tickServer() {

    }
}
