package com.accbdd.aqua_vitae.player;

import com.accbdd.aqua_vitae.network.AlcoholSyncPacket;
import com.accbdd.aqua_vitae.registry.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerAlcoholManager {
    public static void tickPlayer(Player player) {
        int undigested = player.getData(ModAttachments.UNDIGESTED_ALCOHOL);
        int bloodAlcohol = player.getData(ModAttachments.BLOOD_ALCOHOL);
        int hangover = player.getData(ModAttachments.HANGOVER);

        if (undigested > 0) {
            float saturationFactor = Math.max(0.3f, 1.0f - (player.getFoodData().getSaturationLevel() / 20.0f) * 0.7f);
            float undigestedFactor = Math.max(1.0f, undigested / 10000f);

            int baseDigest = 30; // at base, digest 30 units of alcohol/tick. slows down w/ higher saturation, speeds up w/ more alcohol
            int scaledDigest = Math.min(undigested, Math.round(baseDigest * saturationFactor * undigestedFactor));

            undigested -= scaledDigest;
            bloodAlcohol += scaledDigest;

            undigested -= scaledDigest;
            bloodAlcohol += scaledDigest;
            if (hangover > 0) {
                hangover -= 20; // hair of the dog
            }
        } else if (bloodAlcohol > 0) {
            int fadePerTick = 20;

            int cleared = Math.min(bloodAlcohol, fadePerTick);
            bloodAlcohol -= cleared;
            hangover += cleared / 2;
        } else if (hangover > 0) {
            hangover = Math.max(0, hangover - 5);
        }

        player.setData(ModAttachments.UNDIGESTED_ALCOHOL, Math.max(undigested, 0));
        player.setData(ModAttachments.BLOOD_ALCOHOL, Math.max(bloodAlcohol, 0));
        player.setData(ModAttachments.HANGOVER, Math.max(hangover, 0));
    }

    public static void addAlcohol(Player player, int toAdd) {
        player.setData(ModAttachments.UNDIGESTED_ALCOHOL, player.getData(ModAttachments.UNDIGESTED_ALCOHOL) + toAdd);
        syncAlcohol(player);
    }

    public static void syncAlcohol(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer,
                    new AlcoholSyncPacket(player.getData(ModAttachments.UNDIGESTED_ALCOHOL),
                            player.getData(ModAttachments.BLOOD_ALCOHOL),
                            player.getData(ModAttachments.HANGOVER)));
        }
    }
}
