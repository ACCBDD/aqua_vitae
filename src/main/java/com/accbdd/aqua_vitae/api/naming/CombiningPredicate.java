package com.accbdd.aqua_vitae.api.naming;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Function;

public abstract class CombiningPredicate implements DrinkPredicate {
    public static <T extends CombiningPredicate> MapCodec<T> codec(Function<List<DrinkPredicate>, T> factory) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(DrinkPredicate.CODEC.listOf().fieldOf("predicates").forGetter(inst -> inst.predicates)).apply(instance, factory));
    }

    protected final List<DrinkPredicate> predicates;

    public CombiningPredicate(List<DrinkPredicate> predicates) {
        this.predicates = predicates;
    }
}
