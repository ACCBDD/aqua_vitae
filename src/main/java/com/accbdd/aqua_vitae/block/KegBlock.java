package com.accbdd.aqua_vitae.block;

import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.util.FluidUtils;
import com.accbdd.aqua_vitae.util.RegistryUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
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

        if (FluidUtils.handleInteraction(player, hand, stack, keg.getTank())) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            player.getInventory().setChanged();
            return ItemInteractionResult.SUCCESS;
        } else {
            BrewingIngredient brewStats = RegistryUtils.ingredientRegistry().stream().filter(ing -> ing.itemIngredient().test(stack)).findFirst().orElse(null);
            if (brewStats != null) {
                FluidStack fluid = keg.getTank().getFluid();
                if (!fluid.isEmpty()) {
                    fluid.set(ModComponents.STARCH, fluid.getOrDefault(ModComponents.STARCH, 0).floatValue() + brewStats.starch());
                    stack.shrink(1);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        if (player.isShiftKeyDown()) {
            player.displayClientMessage(keg.getTank().getFluid().getHoverName().copy().append(": " + keg.getTank().getFluidAmount()), true);
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
}
