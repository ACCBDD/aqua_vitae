package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.api.IKegEffect;
import com.accbdd.aqua_vitae.block.KegBlock;
import com.accbdd.aqua_vitae.api.KegType;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class KegBlockEntity extends BaseAgingBlockEntity {
    private final KegType kegType;

    public KegBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, blockState.getBlock() instanceof KegBlock keg ? keg.getKeg() : KegType.NULL);
    }

    public KegBlockEntity(BlockPos pos, BlockState blockState, KegType kegType) {
        super(ModBlockEntities.KEG.get(),
                pos,
                blockState,
                kegType.capacity(),
                kegType.color());
        this.kegType = kegType;
    }

    @Override
    public void tickServer() {
        super.tickServer();
        for (IKegEffect effect : kegType.kegEffects()) {
            setFluid(effect.apply(this, getFluid()));
        }
    }

    @Override
    public IFluidHandler getFluidHandler() {
        return getBlockState().getValue(BlockStateProperties.OPEN) ? super.getFluidHandler() : null;
    }
}
