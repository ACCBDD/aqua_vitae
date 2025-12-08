package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.capability.AdaptedFluidHandler;
import com.accbdd.aqua_vitae.fluid.CombinedFluidHandler;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.FluidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class PotStillBlockEntity extends AbstractBEWithData {
    public static final String INPUT_TAG = "input";
    public static final String OUTPUT_TAG = "output";
    public static final int CAPACITY = 4000;
    public static final int CYCLE_LENGTH = 20;

    private final FluidTank inputFluidHandler;
    private final FluidTank outputFluidHandler;
    private final IFluidHandler fluidHandler;
    private int tickCount;

    public PotStillBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.POT_STILL.get(), pos, blockState);
        inputFluidHandler = new FluidTank(CAPACITY, FluidUtils.HAS_ALCOHOL) {
            @Override
            protected void onContentsChanged() {
                setChanged();
            }
        };
        outputFluidHandler = new FluidTank(CAPACITY, FluidUtils.HAS_ALCOHOL) {
            @Override
            protected void onContentsChanged() {
                setChanged();
            }
        };
        fluidHandler = new CombinedFluidHandler(
                new AdaptedFluidHandler(inputFluidHandler) {
                    @Override
                    public FluidStack drain(int maxDrain, FluidAction action) {
                        return FluidStack.EMPTY;
                    }

                    @Override
                    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
                        return FluidStack.EMPTY;
                    }
                }, new AdaptedFluidHandler(outputFluidHandler) {
            @Override
            public int fill(FluidStack resource, FluidAction action) {
                return 0;
            }
        }
        );
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!inputFluidHandler.getFluid().isEmpty()) {
            tag.put(INPUT_TAG, inputFluidHandler.getFluid().save(registries));
        }
        if (!outputFluidHandler.getFluid().isEmpty()) {
            tag.put(OUTPUT_TAG, outputFluidHandler.getFluid().save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(INPUT_TAG)) {
            inputFluidHandler.setFluid(FluidStack.parseOptional(registries, tag.getCompound(INPUT_TAG)));
        } else {
            inputFluidHandler.setFluid(FluidStack.EMPTY);
        }
        if (tag.contains(OUTPUT_TAG)) {
            outputFluidHandler.setFluid(FluidStack.parseOptional(registries, tag.getCompound(OUTPUT_TAG)));
        } else {
            outputFluidHandler.setFluid(FluidStack.EMPTY);
        }
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public void tickServer() {
        if (tickCount++ > CYCLE_LENGTH) {
            tickCount = 0;
            if (!inputFluidHandler.isEmpty()) {
                FluidStack drained = inputFluidHandler.drain(500, IFluidHandler.FluidAction.SIMULATE);
                FluidStack distilled = FluidUtils.distill(drained, 0.9f, 4f, 900);
                int filled = outputFluidHandler.fill(distilled, IFluidHandler.FluidAction.EXECUTE);
                inputFluidHandler.drain(filled / distilled.getAmount() * 500, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
}
