package com.accbdd.aqua_vitae.block;

import com.accbdd.aqua_vitae.block.entity.PotStillBlockEntity;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.FluidUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PotStillBlock extends BaseEntityBlock {
    public static final MapCodec<PotStillBlock> CODEC = simpleCodec((prop) -> new PotStillBlock());

    public PotStillBlock() {
        super(Properties.of()
                .strength(3)
                .explosionResistance(4)
                .sound(SoundType.METAL)
                .noOcclusion());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST || state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST)
            return Block.box(4, 0, 1, 12, 12, 15);
        else
            return Block.box(1, 0, 4, 15, 12, 12);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PotStillBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof PotStillBlockEntity be) {
                    be.tickServer();
                }
            };
        }
        return null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        PotStillBlockEntity still = level.getBlockEntity(pos, ModBlockEntities.POT_STILL.get()).orElse(null);
        if (FluidUtils.handleInteraction(player, hand, stack, still.getFluidHandler())) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            player.getInventory().setChanged();
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
