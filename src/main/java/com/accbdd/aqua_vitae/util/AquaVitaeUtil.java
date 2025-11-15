package com.accbdd.aqua_vitae.util;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.recipe.BrewingIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class AquaVitaeUtil {

    @Nullable
    public static RegistryAccess registryAccess() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            if (FMLEnvironment.dist.equals(Dist.DEDICATED_SERVER) || Minecraft.getInstance() == null) //datagen
                return null;
            if (Minecraft.getInstance().getConnection() == null) {
                return null;
            } else {
                return Minecraft.getInstance().getConnection().registryAccess();
            }
        } else {
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        }
    }

    @Nullable
    public static Registry<BrewingIngredient.Flavor> flavorRegistry() {
        if (registryAccess() != null)
            return registryAccess().registry(AquaVitae.FLAVOR_REGISTRY).get();
        return null;
    }
}
