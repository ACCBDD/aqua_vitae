package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.screen.MaltKilnMenu;
import com.accbdd.aqua_vitae.screen.MashTunMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MODID);

    public static final Supplier<MenuType<MaltKilnMenu>> MALT_KILN = MENU_TYPES.register("malt_kiln",
            () -> new MenuType<>(MaltKilnMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<MashTunMenu>> MASH_TUN = MENU_TYPES.register("mash_tun",
            () -> new MenuType<>(MashTunMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
