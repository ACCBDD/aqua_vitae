package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.api.IngredientColor;
import com.accbdd.aqua_vitae.config.Config;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import com.accbdd.aqua_vitae.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public abstract class BaseAgingBlockEntity extends BaseSingleFluidTankEntity {
    private static final String LAST_TICKED_TAG = "last_ticked";

    private IngredientColor color;
    private long lastTicked;

    public BaseAgingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, IngredientColor color) {
        super(type, pos, blockState, capacity);
        this.color = color;
    }

    private void ageFluid(int amount) {
        if (getFluid().has(ModComponents.ALCOHOL_PROPERTIES)) {
            setFluid(FluidUtils.age(getFluid(), amount, color, List.of()));
        }
        BrewingUtils.determineAlcoholName(getFluid(), getLevel());
        setChanged();
    }

    public void tickServer() {
        if (getLevel() == null) {
            return;
        }

        long currentTime = getLevel().getGameTime();
        long elapsed = Math.max(0, currentTime - lastTicked);

        if (getBlockState().getValue(BlockStateProperties.OPEN)) {
            return;
        }

        if (elapsed > Config.ageTicks) {
            long ageBy = elapsed / Config.ageTicks;
            if (ageBy > 0) {
                AquaVitae.LOGGER.info("aging by {}. current time {}, last ticked {}, elapsed {}", ageBy, currentTime, lastTicked, elapsed);
                ageFluid((int) Math.min(ageBy, Integer.MAX_VALUE));
            }
            lastTicked = currentTime;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (lastTicked >= 0) {
            tag.putLong(LAST_TICKED_TAG, lastTicked);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        lastTicked = tag.contains(LAST_TICKED_TAG) ? tag.getLong(LAST_TICKED_TAG) : 0;
    }

    public void setLastTicked(long lastTicked) {
        this.lastTicked = lastTicked;
    }

    public long getLastTicked() {
        return lastTicked;
    }

    public IngredientColor getColor() {
        return color;
    }

    public void setColor(IngredientColor color) {
        this.color = color;
    }
}