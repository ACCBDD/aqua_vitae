package com.accbdd.aqua_vitae.block;

import com.accbdd.aqua_vitae.block.entity.CrushingTubBlockEntity;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CrushingTubBlock extends BaseEntityBlock {
    public static final MapCodec<CrushingTubBlock> CODEC = simpleCodec((prop) -> new CrushingTubBlock());

    public CrushingTubBlock() {
        super(Properties.of()
                .strength(3)
                .explosionResistance(3)
                .sound(SoundType.WOOD)
                .mapColor(MapColor.WOOD)
                .noOcclusion());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Stream.of(
                Block.box(2, 0, 2, 14, 1, 14),
                Block.box(1, 1, 2, 2, 9, 14),
                Block.box(14, 1, 2, 15, 9, 14),
                Block.box(2, 1, 1, 14, 9, 2),
                Block.box(2, 1, 14, 14, 9, 15),
                Block.box(2, 1, 14, 14, 9, 15)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrushingTubBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        CrushingTubBlockEntity tub = level.getBlockEntity(pos, ModBlockEntities.CRUSHING_TUB.get()).orElse(null);
        if (tub == null || level.isClientSide)
            return InteractionResult.SUCCESS;
        IItemHandler handler = tub.getItemHandler();
        for (int srcIndex = handler.getSlots(); srcIndex > 0; srcIndex--) {
            ItemStack sourceStack = handler.extractItem(srcIndex - 1, Integer.MAX_VALUE, false);
            if (sourceStack.isEmpty()) {
                continue;
            }
            ItemHandlerHelper.giveItemToPlayer(player, sourceStack);
            return InteractionResult.CONSUME;
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        CrushingTubBlockEntity tub = level.getBlockEntity(pos, ModBlockEntities.CRUSHING_TUB.get()).orElse(null);
        if (tub == null || stack.isEmpty() || level.isClientSide)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        IItemHandler handler = tub.getItemHandler();
        ItemStack remainder = ItemHandlerHelper.insertItem(handler, stack.copyWithCount(1), false);
        if (remainder.isEmpty()) {
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
            stack.shrink(1);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
