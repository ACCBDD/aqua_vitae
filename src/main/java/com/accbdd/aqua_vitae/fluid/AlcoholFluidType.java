package com.accbdd.aqua_vitae.fluid;

import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public class AlcoholFluidType extends FluidType {
    public AlcoholFluidType(String name) {
        super(FluidType.Properties.create()
                .descriptionId("block.aqua_vitae." + name)
                .fallDistanceModifier(0)
                .canConvertToSource(false)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(false)
                .viscosity(500));
    }

    @Override
    public Component getDescription(FluidStack stack) {
        //todo: actual data-driven naming, custom name component
        if (stack.has(ModComponents.ALCOHOL_PROPERTIES)) {
            AlcoholPropertiesComponent props = stack.get(ModComponents.ALCOHOL_PROPERTIES);
            if (props.abb() > 200)
                return Component.literal("Liquor");
            else if (props.abb() > 100)
                return Component.literal("Wine");
            else if (props.abb() > 50)
                return Component.literal("Beer");
            else
                return Component.literal("Small Beer");

        }
        return super.getDescription(stack);
    }
}
