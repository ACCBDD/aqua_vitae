package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.IngredientColor;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class AbstractAgingBlockEntity extends BaseSingleFluidTankEntity {
    private final List<Flavor.Transition> flavors;
    private final IngredientColor color;
    private final String LAST_TICKED_TAG = "last_tick";

    private int tick;
    private long lastTicked;

    public AbstractAgingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, List<Flavor.Transition> flavors, IngredientColor color) {
        super(type, pos, blockState, capacity);
        this.flavors = flavors;
        this.color = color;
        this.tick = 0;
    }

    public void tickServer() {
        if (getLevel() == null)
            return;

        if (tick++ >= 200) {
            this.tick = 0;
            int ageBy = (int) (Math.max(getLevel().getGameTime() - this.lastTicked, 200));
            this.lastTicked = getLevel().getGameTime();
            if (getFluid().has(ModComponents.ALCOHOL_PROPERTIES)) {
                setFluid(FluidUtils.age(getFluid(), ageBy, color, flavors));
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong(LAST_TICKED_TAG, this.lastTicked);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(LAST_TICKED_TAG))
            this.lastTicked = tag.getLong(LAST_TICKED_TAG);
    }
}
