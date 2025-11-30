package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.capability.WrappedItemHandler;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.RegistryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class MaltKilnBlockEntity extends AbstractBEWithData {
    public static final int MAX_PROGRESS = 200;

    private static final String ITEM_TAG = "items";
    private static final String PROGRESS_TAG = "progress";
    private static final String BURN_TIME_TAG = "burn_time";
    private static final String MAX_BURN_TIME_TAG = "max_burn_time";

    private final ItemStackHandler itemHandler; //0 - input, 1 - fuel, 2 - output
    private final WrappedItemHandler wrappedItemHandler;
    private int progress, burnTime, maxBurnTime;

    private final ContainerData containerData;

    public MaltKilnBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MALT_KILN.get(), pos, blockState);
        this.itemHandler = createItemHandler();

        this.wrappedItemHandler = new WrappedItemHandler(itemHandler) {
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (slot == 2)
                    return stack;
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot < 2)
                    return ItemStack.EMPTY;
                return super.extractItem(slot, amount, simulate);
            }
        };

        this.containerData = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> MaltKilnBlockEntity.this.progress;
                    case 1 -> MaltKilnBlockEntity.this.burnTime;
                    default -> MaltKilnBlockEntity.this.maxBurnTime;
                };
            }


            @Override
            public void set(int i, int val) {
                switch (i) {
                    case 0 -> MaltKilnBlockEntity.this.progress = val;
                    case 1 -> MaltKilnBlockEntity.this.burnTime = val;
                    default -> MaltKilnBlockEntity.this.maxBurnTime = val;
                }
                MaltKilnBlockEntity.this.setChanged();
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @NotNull
    public ItemStackHandler createItemHandler() {
        return new ItemStackHandler(3) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0)
                    return RegistryUtils.getIngredient(stack) != null;
                if (slot == 1)
                    return stack.getBurnTime(RecipeType.SMELTING) > 0;
                return false;
            }

            @Override
            protected void onContentsChanged(int slot) {
                MaltKilnBlockEntity.this.setChanged();
            }
        };
    }

    @NotNull
    public static ItemStackHandler createClientItemHandler() {
        return new ItemStackHandler(3) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0)
                    return RegistryUtils.getIngredient(stack) != null;
                if (slot == 1)
                    return stack.getBurnTime(RecipeType.SMELTING) > 0;
                return false;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEM_TAG, itemHandler.serializeNBT(registries));
        tag.putInt(PROGRESS_TAG, progress);
        tag.putInt(BURN_TIME_TAG, burnTime);
        tag.putInt(MAX_BURN_TIME_TAG, maxBurnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ITEM_TAG))
            itemHandler.deserializeNBT(registries, tag.getCompound(ITEM_TAG));
        if (tag.contains(PROGRESS_TAG))
            progress = tag.getInt(PROGRESS_TAG);
        if (tag.contains(BURN_TIME_TAG))
            burnTime = tag.getInt(BURN_TIME_TAG);
        if (tag.contains(MAX_BURN_TIME_TAG))
            maxBurnTime = tag.getInt(MAX_BURN_TIME_TAG);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IItemHandler getWrappedItemHandler() {
        return wrappedItemHandler;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public void tickServer() {
        if (isLit()) {
            this.burnTime--;
        } else if (this.getBlockState().getValue(BlockStateProperties.LIT) && this.getLevel() != null) {
            this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.LIT, false), 3);
        }

        ItemStack input = itemHandler.getStackInSlot(0);
        ItemStack fuel = itemHandler.getStackInSlot(1);

        BrewingIngredient inputIngredient = RegistryUtils.getIngredient(input);
        if (inputIngredient != null && inputIngredient.maltProperties() != null && canOutput(inputIngredient.maltOutput())) { //we have a maltable ingredient
            tryBurn();
            if (isLit()) {
                this.progress++;
                if (this.progress >= MAX_PROGRESS) {
                    this.progress = 0;
                    input.shrink(1);
                    ItemStack output = itemHandler.getStackInSlot(2);
                    if (output.isEmpty())
                        itemHandler.setStackInSlot(2, inputIngredient.maltOutput());
                    else
                        itemHandler.getStackInSlot(2).grow(1);
                }
            } else {
                decrementProgress();
            }
        } else {
            decrementProgress();
        }
    }

    private boolean canOutput(ItemStack output) {
        return itemHandler.getStackInSlot(2).isEmpty() || ItemStack.isSameItemSameComponents(itemHandler.getStackInSlot(2), output);
    }

    private void decrementProgress() {
        this.progress = Math.clamp(this.progress - 2, 0, this.progress);
    }

    private boolean isLit() {
        return burnTime > 0;
    }

    /**
     * tries to burn a fuel
     */
    private void tryBurn() {
        ItemStack fuel = itemHandler.getStackInSlot(1);
        if (!isLit() && !fuel.isEmpty() && this.getLevel() != null) {
            this.burnTime = fuel.getBurnTime(RecipeType.SMELTING);
            if (this.burnTime > 0) {
                this.maxBurnTime = this.burnTime;
                if (fuel.hasCraftingRemainingItem())
                    itemHandler.setStackInSlot(1, fuel.getCraftingRemainingItem());
                else
                    fuel.shrink(1);
                this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.LIT, true), 3);
            }
        }
    }
}
