package com.accbdd.aqua_vitae.config;

import com.accbdd.aqua_vitae.AquaVitae;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = AquaVitae.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue AGE_CYCLE_TICKS = BUILDER.comment("The number of ticks one unit of age counts as").defineInRange("ageTicks", 6000, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int ageTicks;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        ageTicks = AGE_CYCLE_TICKS.get();
    }
}
