package com.accbdd.aqua_vitae.item;

import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.stream.Collectors;

public class MaltItem extends Item {
    public MaltItem() {
        super(new Properties()
                .component(ModComponents.BREWING_INGREDIENT, BrewingIngredientComponent.DEFAULT));
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(ModComponents.BREWING_INGREDIENT) && stack.get(ModComponents.BREWING_INGREDIENT).origin() != null)
            return Component.translatable("grammar.aqua_vitae.malt", stack.get(ModComponents.BREWING_INGREDIENT).originDescriptionId(), super.getName(stack));
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        if (stack.has(ModComponents.BREWING_INGREDIENT))
            tooltip.addAll(stack.get(ModComponents.BREWING_INGREDIENT).flavors().stream().map(key -> Component.translatable("flavor.aqua_vitae."+key.location())).toList());
        if (stack.has(ModComponents.ROAST_COUNTER))
            stack.addToTooltip(ModComponents.ROAST_COUNTER, context, tooltip::add, tooltipFlag);
    }

    public static int getColor(ItemStack stack, int index) {
        if (index == 0 && stack.has(ModComponents.BREWING_INGREDIENT)) {
            return stack.get(ModComponents.BREWING_INGREDIENT).properties().color() | 0xFF000000;
        }
        return -1;
    }
}
