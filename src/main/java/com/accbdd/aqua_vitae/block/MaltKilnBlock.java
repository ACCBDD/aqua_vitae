package com.accbdd.aqua_vitae.block;

import com.accbdd.aqua_vitae.block.entity.MaltKilnBlockEntity;
import com.accbdd.aqua_vitae.screen.MaltKilnMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MaltKilnBlock extends BaseEntityBlock {
    public static final MapCodec<MaltKilnBlock> CODEC = simpleCodec((prop) -> new MaltKilnBlock());

    public MaltKilnBlock() {
        super(Properties.ofFullCopy(Blocks.FURNACE)
                .mapColor(MapColor.TERRACOTTA_PINK));
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
        return new MaltKilnBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.FACING).add(BlockStateProperties.LIT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, context.getHorizontalDirection()).setValue(BlockStateProperties.LIT, false);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof MaltKilnBlockEntity kiln) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, plr) -> new MaltKilnMenu(id, inv, ContainerLevelAccess.create(level, pos), kiln.getItemHandler(), kiln.getContainerData()),
                    Component.translatable("block.aqua_vitae.malt_kiln")
            ));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
