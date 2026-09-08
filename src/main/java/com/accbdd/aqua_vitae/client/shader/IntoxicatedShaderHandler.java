package com.accbdd.aqua_vitae.client.shader;

import com.accbdd.aqua_vitae.registry.ModAttachments;
import com.accbdd.aqua_vitae.registry.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class IntoxicatedShaderHandler {
    private static final ResourceLocation SHADER_LOCATION = ResourceLocation.fromNamespaceAndPath("aqua_vitae", "shaders/post/intoxicated.json");

    private static PostChain intoxicatedShader;
    private static int lastWidth = 0;
    private static int lastHeight = 0;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.player.hasEffect(ModEffects.INTOXICATED)) {
            return;
        }

        int width = minecraft.getWindow().getWidth();
        int height = minecraft.getWindow().getHeight();

        if (intoxicatedShader == null || lastWidth != width || lastHeight != height) {
            loadShader(minecraft, width, height);
        }

        if (intoxicatedShader != null) {
            int bac = minecraft.player.getData(ModAttachments.BLOOD_ALCOHOL);

            float time = (minecraft.level.getGameTime() + event.getPartialTick().getGameTimeDeltaPartialTick(true)) * 0.05f;

            float focusX = 0.5f + (float) Math.sin(time * 0.8f) * 0.25f;
            float focusY = 0.5f + (float) Math.cos(time * 0.6f) * 0.25f;

            intoxicatedShader.passes.forEach(pass -> {
                pass.getEffect().safeGetUniform("PlayerBAC").set(bac);
                pass.getEffect().safeGetUniform("FocusCenter").set(focusX, focusY);
            });

            intoxicatedShader.process(event.getPartialTick().getGameTimeDeltaPartialTick(true));
            minecraft.getMainRenderTarget().bindWrite(true);
        }
    }

    private static void loadShader(Minecraft minecraft, int width, int height) {
        if (intoxicatedShader != null) {
            intoxicatedShader.close();
        }
        try {
            intoxicatedShader = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getMainRenderTarget(), SHADER_LOCATION);
            intoxicatedShader.resize(width, height);
            lastWidth = width;
            lastHeight = height;
        } catch (Exception e) {
            intoxicatedShader = null;
        }
    }
}
