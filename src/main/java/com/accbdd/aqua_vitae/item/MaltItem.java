package com.accbdd.aqua_vitae.item;

import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MaltItem extends Item {
    public MaltItem() {
        super(new Properties()
                .component(ModComponents.BREWING_INGREDIENT, BrewingIngredientComponent.DEFAULT));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (stack.has(ModComponents.BREWING_INGREDIENT))
            tooltipComponents.add(Component.literal("Color: " + String.format("%08X", stack.get(ModComponents.BREWING_INGREDIENT).properties().color())));
    }

    public static int getColor(ItemStack stack, int index) {
        if (index == 0 && stack.has(ModComponents.BREWING_INGREDIENT)) {
            return stack.get(ModComponents.BREWING_INGREDIENT).properties().color() | 0xFF000000;
        }
        return -1;
    }
}
