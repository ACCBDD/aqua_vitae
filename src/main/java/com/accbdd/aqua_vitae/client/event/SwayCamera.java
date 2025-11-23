package com.accbdd.aqua_vitae.client.event;

import com.accbdd.aqua_vitae.registry.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class SwayCamera {
    static float PREV_X_SWAY = 0;
    static float PREV_Y_SWAY = 0;
    static float X_SWAY_SPEED = 0;
    static float Y_SWAY_SPEED = 0;
    static float X_SWAY_PHASE = 0;
    static float Y_SWAY_PHASE = 0;
    static float TIME_SINCE_NEW_SWAY = 0;
    static float SWAY_FACTOR = 0;

    @OnlyIn(Dist.CLIENT)
    public static void swayCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            SWAY_FACTOR = 0;
            PREV_X_SWAY = 0;
            PREV_Y_SWAY = 0;
            return;
        }

        float frameTime = Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();

        if (!Minecraft.getInstance().isPaused() && player.getData(ModAttachments.INTOXICATION) > 0) {
            float targetFactor = player.getData(ModAttachments.INTOXICATION) / 500f * Minecraft.getInstance().options.screenEffectScale().get().floatValue();;

            float interpolationSpeed = 0.05f * frameTime;
            SWAY_FACTOR = SWAY_FACTOR + (targetFactor - SWAY_FACTOR) * interpolationSpeed;

            if (TIME_SINCE_NEW_SWAY > 100 || X_SWAY_SPEED == 0 || Y_SWAY_SPEED == 0) {
                TIME_SINCE_NEW_SWAY = 0;
                X_SWAY_SPEED = (float) (Math.random() * 0.002f + 0.0025f);
                Y_SWAY_SPEED = (float) (Math.random() * 0.002f + 0.0025f);
            }
            TIME_SINCE_NEW_SWAY += frameTime;

            // Blend to the new sway speed
            X_SWAY_PHASE += 2 * Math.PI * frameTime * X_SWAY_SPEED;
            Y_SWAY_PHASE += 2 * Math.PI * frameTime * Y_SWAY_SPEED;

            // Apply the sway speed to a sin function using the smoothed factor
            float xOffs = (float) (Math.sin(X_SWAY_PHASE) * SWAY_FACTOR);
            float yOffs = (float) ((Math.sin(Y_SWAY_PHASE) + Math.cos(Y_SWAY_PHASE / 4) * 2) * SWAY_FACTOR * 3);

            // Apply the sway
            player.setXRot(player.getXRot() + xOffs - PREV_X_SWAY);
            player.setYRot(player.getYRot() + yOffs - PREV_Y_SWAY);

            // Save the previous sway
            PREV_X_SWAY = xOffs;
            PREV_Y_SWAY = yOffs;
        }
    }
}
