package com.accbdd.aqua_vitae.compat.jade;

import com.accbdd.aqua_vitae.block.PotStillBlock;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    public static final ResourceLocation POT_STILL = ResourceLocation.fromNamespaceAndPath(MODID, "pot_still");

    @Override
    public void register(IWailaCommonRegistration registration) {

    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(PotStillProvider.INSTANCE, PotStillBlock.class);
    }
}
