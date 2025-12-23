package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.recipe.IngredientColor;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.util.Constants;
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
    private final String LAST_TICKED_TAG = "last_tick";

    private List<Flavor.Transition> flavors;
    private IngredientColor color;
    private int tick;
    private long lastTicked;

    public BaseAgingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, List<Flavor.Transition> flavors, IngredientColor color) {
        super(type, pos, blockState, capacity);
        this.flavors = flavors;
        this.color = color;
        this.tick = 0;
    }

    @Override
    protected IFluidHandler createFluidHandler(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                tick = 0;
            }
        };
    }

    public void tickServer() {
        if (getLevel() == null || getBlockState().getValue(BlockStateProperties.OPEN))
            return;

        long currentTime = getLevel().getGameTime();
        if (lastTicked == 0) {
            lastTicked = currentTime;
            return;
        }

        if (tick++ >= Constants.AGING_STEP) {
            this.tick = 0;
            int ageBy = (int) (getLevel().getGameTime() - this.lastTicked);
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

    public void setFlavors(List<Flavor.Transition> flavors) {
        this.flavors = flavors;
    }

    public void setColor(IngredientColor color) {
        this.color = color;
    }

    public List<Flavor.Transition> getFlavors() {
        return flavors;
    }

    public IngredientColor getColor() {
        return color;
    }
}
