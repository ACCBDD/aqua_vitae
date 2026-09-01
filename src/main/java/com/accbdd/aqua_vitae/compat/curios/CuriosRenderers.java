package com.accbdd.aqua_vitae.compat.curios;

import com.accbdd.aqua_vitae.registry.ModItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@OnlyIn(Dist.CLIENT)
public class CuriosRenderers {

    public static void register() {
        CuriosRendererRegistry.register(ModItems.BREWMASTER_MONOCLE.get(), MonocleCurioRenderer::new);
    }
}
