package com.accbdd.aqua_vitae.api;

import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IKegEffect {
    FluidStack apply(KegBlockEntity kegBlockEntity, FluidStack input);
}
