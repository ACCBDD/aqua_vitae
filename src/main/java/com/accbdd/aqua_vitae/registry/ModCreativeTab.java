package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.item.KegBlockItem;
import com.accbdd.aqua_vitae.util.BrewingUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.Set;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_TABS.register(MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.aqua_vitae"))
                    .icon(Items.WHEAT::getDefaultInstance)
                    .displayItems((parameters, output) -> {
                        ModItems.CREATIVE_TAB_ITEMS.forEach(output::accept);
                        RegistryAccess access = BrewingUtils.registryAccess();
                        if (access != null) {
                            Set<Map.Entry<ResourceKey<Keg>, Keg>> kegSet = access.registry(AquaVitae.KEG_REGISTRY).get().entrySet();
                            for (Map.Entry<ResourceKey<Keg>, Keg> entry : kegSet) {
                                output.accept(KegBlockItem.kegStack(entry.getKey().location()));
                            }
                        }
                    }).build());
}
