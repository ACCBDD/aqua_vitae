package com.accbdd.aqua_vitae.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class MeterItem extends Item {
    public MeterItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide)
            return InteractionResult.SUCCESS;

        return InteractionResult.CONSUME;
    }
}
