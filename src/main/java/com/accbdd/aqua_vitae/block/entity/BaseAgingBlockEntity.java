package com.accbdd.aqua_vitae.block.entity;

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
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public abstract class BaseAgingBlockEntity extends BaseSingleFluidTankEntity {
    private static final String LAST_SEEN_TAG = "last_seen_time";
    private static final String TICK_TAG = "age_tick";

    private IngredientColor color;
    private int tick;
    private long lastTicked = -1;
    private boolean internalChange;

    public BaseAgingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, IngredientColor color) {
        super(type, pos, blockState, capacity);
        this.color = color;
    }

    @Override
    protected IFluidHandler createFluidHandler(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (!internalChange) {
                    tick = 0;
                }
            }
        };
    }

    private void ageFluid(int amount) {
        internalChange = true;
        if (getFluid().has(ModComponents.ALCOHOL_PROPERTIES)) {
            setFluid(FluidUtils.age(getFluid(), amount, color, List.of()));
        }
        BrewingUtils.determineAlcoholName(getFluid(), getLevel());
        internalChange = false;
        setChanged();
    }

    public void tickServer() {
        if (getLevel() == null) {
            return;
        }

        long currentTime = getLevel().getGameTime();
        long elapsed = lastTicked < 0 ? 1 : Math.max(0, currentTime - lastTicked);
        lastTicked = currentTime;

        if (getBlockState().getValue(BlockStateProperties.OPEN)) {
            return;
        }

        long total = tick + elapsed;
        tick = (int) (total % Config.ageTicks);

        long ageBy = total / Config.ageTicks;
        if (ageBy > 0) {
            ageFluid((int) Math.min(ageBy, Integer.MAX_VALUE));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(TICK_TAG, tick);
        if (lastTicked >= 0) {
            tag.putLong(LAST_SEEN_TAG, lastTicked);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tick = tag.getInt(TICK_TAG);
        lastTicked = tag.contains(LAST_SEEN_TAG) ? tag.getLong(LAST_SEEN_TAG) : -1;
    }

    public IngredientColor getColor() {
        return color;
    }

    public void setColor(IngredientColor color) {
        this.color = color;
    }
}