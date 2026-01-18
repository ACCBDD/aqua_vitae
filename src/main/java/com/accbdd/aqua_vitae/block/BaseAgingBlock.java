package com.accbdd.aqua_vitae.block;

import com.accbdd.aqua_vitae.block.entity.BaseAgingBlockEntity;
import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.FluidUtils;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseAgingBlock extends BaseEntityBlock {
    public BaseAgingBlock() {
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
        builder.add(BlockStateProperties.OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite())
                .setValue(BlockStateProperties.OPEN, true);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        KegBlockEntity keg = level.getBlockEntity(pos, ModBlockEntities.KEG.get()).orElse(null);
        if (keg == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (state.getValue(BlockStateProperties.OPEN) && FluidUtils.handleInteraction(player, hand, stack, keg.getFluidHandler())) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            player.getInventory().setChanged();
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            boolean flag = state.getValue(BlockStateProperties.OPEN);
            if (flag == level.hasNeighborSignal(pos)) {
                level.setBlock(pos, state.cycle(BlockStateProperties.OPEN), 2);
                level.invalidateCapabilities(pos);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof BaseAgingBlockEntity be) {
                    be.tickServer();
                }
            };
        }
        return null;
    }
}
