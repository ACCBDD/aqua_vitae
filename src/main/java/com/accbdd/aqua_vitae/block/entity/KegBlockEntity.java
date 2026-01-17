package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.block.KegBlock;
import com.accbdd.aqua_vitae.registry.KegType;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class KegBlockEntity extends BaseAgingBlockEntity {

    public KegBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, blockState.getBlock() instanceof KegBlock keg ? keg.getKeg() : KegType.NULL);
    }

    public KegBlockEntity(BlockPos pos, BlockState blockState, KegType kegType) {
        super(ModBlockEntities.KEG.get(),
                pos,
                blockState,
                kegType.capacity(),
                kegType.flavorAdds(),
                kegType.color());
    }
}
