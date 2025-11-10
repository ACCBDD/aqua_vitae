package com.accbdd.aqua_vitae.block;

import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import com.accbdd.aqua_vitae.capability.CupHandler;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class KegBlock extends BaseEntityBlock implements LiquidBlockContainer {
    public static final MapCodec<KegBlock> CODEC = simpleCodec((prop) -> new KegBlock());

    public KegBlock() {
        super(Properties.of()
                .sound(SoundType.WOOD)
                .mapColor(MapColor.WOOD)
                .strength(2)
                .explosionResistance(3));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new KegBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        Optional<KegBlockEntity> keg = blockGetter.getBlockEntity(blockPos, ModBlockEntities.KEG.get());
        return keg.filter(kegBlockEntity -> kegBlockEntity.getTank().fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.SIMULATE) == 1000).isPresent();
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        Optional<KegBlockEntity> keg = levelAccessor.getBlockEntity(blockPos, ModBlockEntities.KEG.get());
        if (keg.isPresent()) {
            keg.get().getTank().fill(new FluidStack(fluidState.getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        KegBlockEntity keg = level.getBlockEntity(pos, ModBlockEntities.KEG.get()).orElse(null);
        if (keg == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        boolean stacked = false;
        IFluidHandlerItem itemCap = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (stack.getCount() > 1) {
            itemCap = stack.copyWithCount(1).getCapability(Capabilities.FluidHandler.ITEM);
            stacked = true;
        }

        if (itemCap != null) {
            boolean transfer = false;
            if (itemCap instanceof CupHandler cupHandler) {
                cupHandler.interactWith(keg.getTank());
            } else {
                FluidStack available = keg.getTank().drain(KegBlockEntity.CAPACITY, IFluidHandler.FluidAction.SIMULATE);
                int filled = itemCap.fill(available, IFluidHandler.FluidAction.EXECUTE);
                if (filled > 0) { //if there is space in the item
                    keg.getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    transfer = true;
                } else { //item can't be filled
                    FluidStack simulatedExtract = itemCap.drain(KegBlockEntity.CAPACITY, IFluidHandler.FluidAction.SIMULATE);
                    int accepted = keg.getTank().fill(simulatedExtract, IFluidHandler.FluidAction.EXECUTE);
                    if (accepted > 0) {
                        itemCap.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                        transfer = true;
                    }
                }
            }

            ItemStack newContainer = itemCap.getContainer();
            if (stacked) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                    player.addItem(newContainer);
                }
            } else if (!ItemStack.isSameItemSameComponents(stack, newContainer)) {
                if (!player.isCreative()) {
                    player.setItemInHand(hand, newContainer);
                }
            }

            if (transfer) {
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
        } else {
            if (stack.is(Items.STICK)) {
                FluidStack fluid = keg.getTank().getFluid();
                fluid.set(DataComponents.DAMAGE, fluid.getOrDefault(DataComponents.DAMAGE, 0) + 1);
            } else if (stack.is(Items.APPLE)) {
                FluidStack fluid = keg.getTank().getFluid();
                fluid.set(DataComponents.DAMAGE, fluid.getOrDefault(DataComponents.DAMAGE, 0) - 1);
            }
        }

        if (player.isShiftKeyDown()) {
            player.displayClientMessage(keg.getTank().getFluid().getHoverName().copy().append(": " + keg.getTank().getFluidAmount()), true);
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
}
