package com.accbdd.aqua_vitae.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_TABS.register(MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.aqua_vitae"))
                    .icon(Items.WHEAT::getDefaultInstance)
                    .displayItems((parameters, output) -> {
                        ModItems.CREATIVE_TAB_ITEMS.forEach(output::accept);
                    }).build());
}
