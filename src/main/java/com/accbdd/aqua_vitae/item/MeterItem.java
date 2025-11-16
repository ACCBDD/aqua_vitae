package com.accbdd.aqua_vitae.item;

import com.accbdd.aqua_vitae.registry.ModBlockEntities;
import com.accbdd.aqua_vitae.util.FluidUtils;
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

        context.getLevel().getBlockEntity(context.getClickedPos(), ModBlockEntities.KEG.get()).ifPresent(keg ->
                FluidUtils.getPrecursorTooltip(keg.getTank().getFluid()).forEach(component ->
                        context.getPlayer().sendSystemMessage(component)));

        return InteractionResult.CONSUME;
    }
}
