package com.accbdd.aqua_vitae.compat.curios;

import com.accbdd.aqua_vitae.item.MonocleItem;
import com.accbdd.aqua_vitae.registry.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.Optional;

public class Curios {
    private static Optional<Map<String, ICurioStacksHandler>> resolveCuriosMap(LivingEntity entity) {
        return Optional.ofNullable(entity.getCapability(CuriosCapability.INVENTORY)).map(ICuriosItemHandler::getCurios);
    }

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(Curios::onClientSetup);
        MonocleItem.addIsWearingPredicate((player) -> resolveCuriosMap(player).map((curiosMap) -> {

            for (ICurioStacksHandler stacksHandler : curiosMap.values()) {
                int slots = stacksHandler.getSlots();

                for (int slot = 0; slot < slots; ++slot) {
                    if (stacksHandler.getStacks().getStackInSlot(slot).is(ModItems.BREWMASTER_MONOCLE)) {
                        return true;
                    }
                }
            }

            return false;
        }).orElse(false));
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        CuriosRenderers.register();
    }
}
