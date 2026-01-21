package com.accbdd.aqua_vitae.block;

import com.accbdd.aqua_vitae.api.KegType;
import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class KegBlock extends BaseAgingBlock {

    private final KegType keg;

    public KegBlock(KegType keg) {
        this.keg = keg;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new KegBlockEntity(blockPos, blockState, keg);
    }

    public KegType getKeg() {
        return keg;
    }
}
