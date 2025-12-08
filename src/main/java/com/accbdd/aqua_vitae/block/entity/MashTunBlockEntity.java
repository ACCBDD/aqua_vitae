package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.capability.AdaptedFluidHandler;
import com.accbdd.aqua_vitae.capability.AdaptedItemHandler;
import com.accbdd.aqua_vitae.fluid.CombinedFluidHandler;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

public class MashTunBlockEntity extends AbstractBEWithData {
    public static final int MAX_PROGRESS = 10;
    public static final int MAX_FLUID = 4000;
    public static final String INPUT_TAG = "input";
    public static final String OUTPUT_TAG = "output";

    private final FluidTank inputFluid, outputFluid;
    private final IFluidHandler inputFluidHandler, outputFluidHandler, fluidHandler;
    private final ItemStackHandler inputItems, outputItems;
    private final IItemHandlerModifiable items, inputItemHandler, outputItemHandler, itemHandler;

    private final ContainerData data;

    public MashTunBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MASH_TUN.get(), pos, blockState);
        this.inputFluid = new FluidTank(MAX_FLUID);
        this.outputFluid = new FluidTank(MAX_FLUID);
        this.inputItems = new ItemStackHandler(9);
        this.outputItems = new ItemStackHandler(1);
        this.items = new CombinedInvWrapper(inputItems, outputItems);

        this.inputFluidHandler = new AdaptedFluidHandler(inputFluid) {
            @Override
            public FluidStack drain(int i, FluidAction fluidAction) {
                return FluidStack.EMPTY;
            }
        };

        this.outputFluidHandler = new AdaptedFluidHandler(outputFluid) {
            @Override
            public int fill(FluidStack fluidStack, FluidAction fluidAction) {
                return 0;
            }
        };

        this.fluidHandler = new CombinedFluidHandler(this.inputFluidHandler, this.outputFluidHandler);

        this.inputItemHandler = new AdaptedItemHandler(inputItems) {
            @Override
            public ItemStack extractItem(int index, int count, boolean simulate) {
                return ItemStack.EMPTY;
            }
        };

        this.outputItemHandler = new AdaptedItemHandler(outputItems) {
            @Override
            public ItemStack insertItem(int i, ItemStack itemStack, boolean simulate) {
                return itemStack;
            }
        };

        this.itemHandler = new CombinedInvWrapper(inputItemHandler, outputItemHandler);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return 0;
            }

            @Override
            public void set(int i, int i1) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag input = this.inputItems.serializeNBT(registries);
        CompoundTag output = this.outputItems.serializeNBT(registries);
        this.inputFluid.writeToNBT(registries, input);
        this.outputFluid.writeToNBT(registries, output);
        tag.put(INPUT_TAG, input);
        tag.put(OUTPUT_TAG, output);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains(INPUT_TAG)) {
            CompoundTag input = tag.getCompound(INPUT_TAG);
            this.inputFluid.readFromNBT(registries, input);
            this.inputItems.deserializeNBT(registries, input);
        }
        if (tag.contains(OUTPUT_TAG)) {
            CompoundTag output = tag.getCompound(OUTPUT_TAG);
            this.outputFluid.readFromNBT(registries, output);
            this.outputItems.deserializeNBT(registries, output);
        }
    }

    public void tickServer() {

    }

    public IFluidHandler getFluidHandler() {
        return this.fluidHandler;
    }

    public IItemHandlerModifiable getItemHandler() {
        return this.itemHandler;
    }

    public IItemHandlerModifiable getItems() {
        return this.items;
    }

    public ContainerData getContainerData() {
        return this.data;
    }
}
