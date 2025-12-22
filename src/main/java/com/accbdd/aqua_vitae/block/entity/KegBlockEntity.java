package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.datagen.builtin.BuiltInFlavors;
import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.IngredientColor;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class KegBlockEntity extends AbstractAgingBlockEntity {
    public KegBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.KEG.get(),
                pos,
                blockState,
                4000,
                List.of(new Flavor.Transition(List.of(BuiltInFlavors.SOUR.getKey()), 1000)),
                new IngredientColor(0xDDFFCC00));
    }
}
