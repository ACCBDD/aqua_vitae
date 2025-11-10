package com.accbdd.aqua_vitae;

import com.accbdd.aqua_vitae.datagen.FluidTagGenerator;
import com.accbdd.aqua_vitae.datagen.Generators;
import com.accbdd.aqua_vitae.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.slf4j.Logger;

@Mod(AquaVitae.MODID)
public class AquaVitae {
    public static final String MODID = "aqua_vitae";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AquaVitae(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTab.CREATIVE_TABS.register(modEventBus);
        ModFluidTypes.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);

        modEventBus.addListener(Generators::onGatherData);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            if (player.level().getFluidState(BlockPos.containing(player.getEyePosition())).is(FluidTagGenerator.HARD_LIQUOR)) {
                player.addEffect(new MobEffectInstance(ModEffects.TIPSY, 200, 4));
            }
        }
    }
}
