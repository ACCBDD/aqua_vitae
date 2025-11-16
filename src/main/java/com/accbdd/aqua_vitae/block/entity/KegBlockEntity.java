package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class KegBlockEntity extends BaseSingleFluidTankEntity {
    public static final int CAPACITY = 4000;

    public KegBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.KEG.get(), pos, blockState, CAPACITY);
    }
}
