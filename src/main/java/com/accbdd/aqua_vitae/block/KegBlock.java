package com.accbdd.aqua_vitae.block;

import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import com.accbdd.aqua_vitae.util.FluidUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KegBlock extends BaseEntityBlock {
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        KegBlockEntity keg = level.getBlockEntity(pos, ModBlockEntities.KEG.get()).orElse(null);
        if (keg == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (FluidUtils.handleInteraction(player, hand, stack, keg.getFluidHandler())) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            player.getInventory().setChanged();
            return ItemInteractionResult.SUCCESS;
        } else {
            BrewingIngredient ingredient = BrewingUtils.getIngredient(stack);
            if (ingredient != null) {
                FluidStack fluid = keg.getFluid();
                if (!fluid.isEmpty()) {
                    FluidUtils.modifyPrecursor(fluid, ingredient, stack);
                    stack.shrink(1);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        if (player.isShiftKeyDown()) {
            player.displayClientMessage(keg.getFluid().getHoverName().copy().append(": " + keg.getFluid().getAmount()), true);
            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
