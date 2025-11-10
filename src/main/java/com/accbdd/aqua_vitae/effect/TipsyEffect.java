package com.accbdd.aqua_vitae.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class TipsyEffect extends MobEffect {
    public TipsyEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFCC00);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
