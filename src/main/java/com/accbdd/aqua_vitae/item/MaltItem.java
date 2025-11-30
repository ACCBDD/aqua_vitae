package com.accbdd.aqua_vitae.item;

import com.accbdd.aqua_vitae.component.BrewingIngredientComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.world.item.Item;

public class MaltItem extends Item {
    public MaltItem() {
        super(new Properties()
                .component(ModComponents.BREWING_INGREDIENT, BrewingIngredientComponent.DEFAULT));
    }
}
