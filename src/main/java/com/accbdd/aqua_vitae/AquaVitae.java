package com.accbdd.aqua_vitae;

import com.accbdd.aqua_vitae.capability.CupHandler;
import com.accbdd.aqua_vitae.datagen.FluidTagGenerator;
import com.accbdd.aqua_vitae.datagen.Generators;
import com.accbdd.aqua_vitae.item.CupItem;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;

@Mod(AquaVitae.MODID)
public class AquaVitae {
    public static final String MODID = "aqua_vitae";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AquaVitae(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTab.CREATIVE_TABS.register(modEventBus);
        ModFluidTypes.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModComponents.COMPONENTS.register(modEventBus);

        modEventBus.addListener(Generators::onGatherData);
        modEventBus.addListener(this::registerCapabilities);

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

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.KEG.get(),
                (entity, side) -> entity.getTank()
        );

        ModItems.ITEMS.getEntries().stream().filter(holder -> holder.get() instanceof CupItem).map(holder -> (CupItem)holder.get()).forEach(cup -> {
            event.registerItem(
                    Capabilities.FluidHandler.ITEM,
                    (c, dir) -> new CupHandler(c, cup.getCapacity()),
                    cup
            );
        });
    }
}
