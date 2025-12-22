package com.accbdd.aqua_vitae.block.entity;

import com.accbdd.aqua_vitae.capability.AdaptedItemHandler;
import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.component.RoastCountComponent;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.recipe.Flavor;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class MaltKilnBlockEntity extends AbstractBEWithData implements IFluidSyncable {
    public static final int MAX_PROGRESS = 10;
    public static final int MAX_FLUID = 4000;
    public static final int WATER_USAGE = 100;

    private static final String ITEM_TAG = "items";
    private static final String FLUID_TAG = "fluid";
    private static final String PROGRESS_TAG = "progress";
    private static final String BURN_TIME_TAG = "burn_time";
    private static final String MAX_BURN_TIME_TAG = "max_burn_time";

    private final ItemStackHandler itemHandler; //0 - input, 1 - fuel, 2 - output
    private final AdaptedItemHandler adaptedItemHandler; //
    private final FluidTank fluidHandler;
    private int progress, burnTime, maxBurnTime, fluidAmount;

    private final ContainerData containerData;

    public MaltKilnBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MALT_KILN.get(), pos, blockState);
        this.itemHandler = createItemHandler();
        this.fluidHandler = new FluidTank(MAX_FLUID, fluidStack -> fluidStack.is(Fluids.WATER)) {
            @Override
            protected void onContentsChanged() {
                MaltKilnBlockEntity.this.setChanged();
                sendFluidUpdate(MaltKilnBlockEntity.this, this.getFluid(), 0);
            }
        };

        this.adaptedItemHandler = new AdaptedItemHandler(itemHandler) {
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
                    case 2 -> MaltKilnBlockEntity.this.maxBurnTime;
                    default -> 0;
                };
            }


            @Override
            public void set(int i, int val) {
                switch (i) {
                    case 0 -> MaltKilnBlockEntity.this.progress = val;
                    case 1 -> MaltKilnBlockEntity.this.burnTime = val;
                    case 2 -> MaltKilnBlockEntity.this.maxBurnTime = val;
                    default -> {}
                }
                MaltKilnBlockEntity.this.setChanged();
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @NotNull
    public ItemStackHandler createItemHandler() {
        return new ItemStackHandler(3) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0)
                    return BrewingUtils.getIngredient(stack) != null;
                if (slot == 1)
                    return stack.getBurnTime(RecipeType.SMELTING) > 0;
                return true;
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
                    return BrewingUtils.getIngredient(stack) != null;
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
        fluidHandler.writeToNBT(registries, tag);
        tag.putInt(PROGRESS_TAG, progress);
        tag.putInt(BURN_TIME_TAG, burnTime);
        tag.putInt(MAX_BURN_TIME_TAG, maxBurnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fluidHandler.readFromNBT(registries, tag);
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
        return adaptedItemHandler;
    }

    public FluidTank getFluidHandler() {
        return fluidHandler;
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
        ItemStack output = getRecipeOutput();
        if (!output.isEmpty() && canOutput(output)) { //we have a maltable ingredient
            tryBurn();
            if (isLit() && fluidHandler.getFluidAmount() >= WATER_USAGE) {
                this.progress++;
                if (this.progress >= MAX_PROGRESS) {
                    this.progress = 0;
                    fluidHandler.drain(WATER_USAGE, IFluidHandler.FluidAction.EXECUTE);
                    input.shrink(1);
                    ItemStack outputSlot = itemHandler.getStackInSlot(2);
                    if (outputSlot.isEmpty())
                        itemHandler.setStackInSlot(2, output);
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

    /**
     * @return malt output or empty itemstack if no recipe found
     */
    private ItemStack getRecipeOutput() {
        ItemStack input = itemHandler.getStackInSlot(0);
        if (input.isEmpty())
            return ItemStack.EMPTY;

        BrewingIngredient inputIngredient = BrewingUtils.getIngredient(input);
        if (inputIngredient == null)
            return ItemStack.EMPTY;

        ItemStack output = inputIngredient.maltOutput();
        if (!output.isEmpty())
            return output;

        if (input.has(ModComponents.ROAST_COUNTER) && input.get(ModComponents.ROAST_COUNTER).roast() < 5) {
            output = input.copyWithCount(1);
            int roastCount = output.get(ModComponents.ROAST_COUNTER).roast() + 1;
            if (output.has(ModComponents.BREWING_INGREDIENT)) {
                BrewingIngredientComponent old = output.get(ModComponents.BREWING_INGREDIENT);
                BrewingIngredient.BrewingProperties oldProps = old.properties();
                Set<ResourceKey<Flavor>> newFlavors = BrewingUtils.transitionFlavors(old.flavors(), Flavor::kiln, roastCount);
                output.set(ModComponents.BREWING_INGREDIENT, new BrewingIngredientComponent(oldProps.kiln(), null, newFlavors, old.origin()));
            }
            output.set(ModComponents.ROAST_COUNTER, new RoastCountComponent(roastCount));
            return output;
        }

        return ItemStack.EMPTY;
    }

    private boolean canOutput(ItemStack output) {
        return itemHandler.insertItem(2, output, true).isEmpty();
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

    @Override
    public void setFluid(FluidStack stack, int tankId) {
        fluidHandler.setFluid(stack);
    }
}
