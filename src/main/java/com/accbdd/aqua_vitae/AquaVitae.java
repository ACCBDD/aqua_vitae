package com.accbdd.aqua_vitae;

import com.accbdd.aqua_vitae.capability.CupHandler;
import com.accbdd.aqua_vitae.datagen.FluidTagGenerator;
import com.accbdd.aqua_vitae.datagen.Generators;
import com.accbdd.aqua_vitae.item.CupItem;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import com.accbdd.aqua_vitae.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

@Mod(AquaVitae.MODID)
public class AquaVitae {
    public static final String MODID = "aqua_vitae";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceKey<Registry<BrewingIngredient.Flavor>> FLAVOR_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "flavor"));
    public static ResourceKey<Registry<BrewingIngredient>> INGREDIENT_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "brewing_ingredient"));

    public AquaVitae(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTab.CREATIVE_TABS.register(modEventBus);
        ModFluidTypes.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModComponents.COMPONENTS.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        modEventBus.addListener(Generators::onGatherData);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerDatapackRegistries);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            if (player.level().getFluidState(BlockPos.containing(player.getEyePosition())).is(FluidTagGenerator.AQUA_VITAE)) {
                player.addEffect(new MobEffectInstance(ModEffects.TIPSY, 200, 4));
            }
            int bloodAlcohol = player.getData(ModAttachments.BLOOD_ALCOHOL);
            int intoxication = player.getData(ModAttachments.INTOXICATION);
            int hangover = player.getData(ModAttachments.HANGOVER);

            if (bloodAlcohol > 0) {
                bloodAlcohol -= 2 + (bloodAlcohol / 250);
                intoxication += 1 + (bloodAlcohol / 500);
                hangover -= 1;
            } else if (intoxication > 0) {
                intoxication -= 1;
                hangover += 1;
            } else if (hangover > 0) {
                hangover -= 1;
            }

            if (intoxication > 0) {
                player.addEffect(new MobEffectInstance(ModEffects.TIPSY, -1, intoxication / 500));
            } else {
                player.removeEffect(ModEffects.TIPSY);
            }
            player.setData(ModAttachments.BLOOD_ALCOHOL, Math.max(bloodAlcohol, 0));
            player.setData(ModAttachments.INTOXICATION, Math.max(intoxication, 0));
            player.setData(ModAttachments.HANGOVER, Math.max(hangover, 0));
        }
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.KEG.get(),
                (entity, side) -> entity.getFluidHandler()
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.FERMENTER.get(),
                (entity, side) -> entity.getFluidHandler()
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.POT_STILL.get(),
                (entity, side) -> entity.getFluidHandler()
        );

        ModItems.ITEMS.getEntries().stream().filter(holder -> holder.get() instanceof CupItem)
                .map(holder -> (CupItem)holder.get())
                .forEach(cup -> event.registerItem(
                        Capabilities.FluidHandler.ITEM,
                        (c, dir) -> new CupHandler(c, cup.getCapacity()),
                        cup
                    )
                );
    }

    public void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                FLAVOR_REGISTRY,
                BrewingIngredient.Flavor.CODEC,
                BrewingIngredient.Flavor.CODEC
        );

        event.dataPackRegistry(
                INGREDIENT_REGISTRY,
                BrewingIngredient.CODEC,
                BrewingIngredient.CODEC
        );
    }
}
