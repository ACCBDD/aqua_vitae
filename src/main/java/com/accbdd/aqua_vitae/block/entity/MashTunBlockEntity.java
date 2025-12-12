package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.capability.AdaptedFluidHandler;
import com.accbdd.aqua_vitae.capability.AdaptedItemHandler;
import com.accbdd.aqua_vitae.fluid.CombinedFluidHandler;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.List;

public class MashTunBlockEntity extends AbstractBEWithData implements IFluidSyncable {
    public static final int MAX_PROGRESS = 30;
    public static final int MAX_FLUID = 4000;
    public static final String INPUT_TAG = "input";
    public static final String OUTPUT_TAG = "output";

    private final FluidTank inputFluid, outputFluid;
    private final IFluidHandler fluidHandler;
    private final ItemStackHandler inputItems, outputItems;
    private final IItemHandlerModifiable items, itemHandler;
    private final ContainerData data;

    private int progress, maxProgress;

    public MashTunBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MASH_TUN.get(), pos, blockState);
        this.maxProgress = MAX_PROGRESS; //todo fix
        this.inputFluid = new FluidTank(MAX_FLUID) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                sendFluidUpdate(MashTunBlockEntity.this, this.getFluid(), 0);
            }
        };
        this.outputFluid = new FluidTank(MAX_FLUID) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                sendFluidUpdate(MashTunBlockEntity.this, this.getFluid(), 1);
            }
        };

        this.inputItems = new ItemStackHandler(9) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                MashTunBlockEntity.this.setChanged();
            }
        };
        this.outputItems = new ItemStackHandler(1);
        this.items = new CombinedInvWrapper(inputItems, outputItems);

        IFluidHandler inputFluidHandler = new AdaptedFluidHandler(inputFluid) {
            @Override
            public FluidStack drain(int i, FluidAction fluidAction) {
                return FluidStack.EMPTY;
            }
        };

        IFluidHandler outputFluidHandler = new AdaptedFluidHandler(outputFluid) {
            @Override
            public int fill(FluidStack fluidStack, FluidAction fluidAction) {
                return 0;
            }
        };

        this.fluidHandler = new CombinedFluidHandler(inputFluidHandler, outputFluidHandler);

        IItemHandlerModifiable inputItemHandler = new AdaptedItemHandler(inputItems) {
            @Override
            public ItemStack extractItem(int index, int count, boolean simulate) {
                return ItemStack.EMPTY;
            }
        };

        IItemHandlerModifiable outputItemHandler = new AdaptedItemHandler(outputItems) {
            @Override
            public ItemStack insertItem(int i, ItemStack itemStack, boolean simulate) {
                return itemStack;
            }
        };

        this.itemHandler = new CombinedInvWrapper(inputItemHandler, outputItemHandler);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> getBlockState().getValue(BlockStateProperties.LIT) ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int val) {
                switch (i) {
                    case 0 -> progress = val;
                    case 1 -> maxProgress = val;
                    default -> {}
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag input = this.inputItems.serializeNBT(registries);
        CompoundTag output = this.outputItems.serializeNBT(registries);
        if (!this.inputFluid.isEmpty())
            this.inputFluid.writeToNBT(registries, input);
        if (!this.outputFluid.isEmpty())
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
        if (isLit() && canOutput()) {
            if (inputFluid.getFluidAmount() > 0 && !inputItems.getStackInSlot(0).isEmpty()) {
                progress++;
                if (progress >= maxProgress) {
                    progress = 0;
                    List<ItemStack> inputs = new ArrayList<>();
                    for (int i = 0; i < inputItems.getSlots(); i++) {
                        ItemStack stack = inputItems.extractItem(i, 1, true);
                        BrewingIngredient ing = BrewingUtils.getIngredient(stack);
                        if (ing == null)
                            continue;
                        inputs.add(stack);
                        inputItems.extractItem(i, 1, false);
                        outputItems.insertItem(0, Items.BONE_MEAL.getDefaultInstance(), false);
                    }
                    int drained = this.inputFluid.drain(MAX_FLUID, IFluidHandler.FluidAction.EXECUTE).getAmount();
                    FluidStack wort = BrewingUtils.createWort(drained, inputs.toArray(new ItemStack[0]));
                    this.outputFluid.fill(wort, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    public boolean isLit() {
        return getBlockState().getValue(BlockStateProperties.LIT);
    }

    public boolean canOutput() {
        return outputFluid.isEmpty();
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

    @Override
    public void setFluid(FluidStack stack, int tankId) {
        switch (tankId) {
            case 0 -> inputFluid.setFluid(stack);
            case 1 -> outputFluid.setFluid(stack);
            default -> {}
        }
    }
}
