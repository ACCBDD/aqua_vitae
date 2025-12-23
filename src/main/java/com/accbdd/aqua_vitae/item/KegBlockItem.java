package com.accbdd.aqua_vitae.item;

import com.accbdd.aqua_vitae.block.entity.KegBlockEntity;
import com.accbdd.aqua_vitae.registry.ModComponents;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class KegBlockItem extends BlockItem {
    public KegBlockItem(Block block) {
        super(block, new Properties());
    }

    public static ItemStack kegStack(ResourceLocation keg) {
        ItemStack stack = new ItemStack(ModItems.KEG.get());
        stack.set(ModComponents.KEG, keg);
        return stack;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean superResult = super.updateCustomBlockEntityTag(pos, level, player, stack, state);

        if (level.getBlockEntity(pos) instanceof KegBlockEntity keg) {
            ResourceLocation kegType = stack.get(ModComponents.KEG);
            if (kegType != null) {
                keg.setKegLoc(kegType);
                return true;
            }
        }

        return superResult;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(ModComponents.KEG))
            return Component.translatable("keg.aqua_vitae." + stack.get(ModComponents.KEG));
        return super.getName(stack);
    }
}
