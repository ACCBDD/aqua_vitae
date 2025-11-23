package com.accbdd.aqua_vitae.player;

import com.accbdd.aqua_vitae.network.AlcoholSyncPacket;
import com.accbdd.aqua_vitae.registry.ModAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerAlcoholManager {
    public static void tickPlayer(Player player) {
        int bloodAlcohol = player.getData(ModAttachments.BLOOD_ALCOHOL);
        int intoxication = player.getData(ModAttachments.INTOXICATION);
        int hangover = player.getData(ModAttachments.HANGOVER);

        if (bloodAlcohol > 0) {
            bloodAlcohol -= 2 + (bloodAlcohol / 250);
            intoxication += 1 + (bloodAlcohol / 500);
            hangover -= 2;
        } else if (intoxication > 0) {
            intoxication -= 1;
            hangover += 1;
        } else if (hangover > 0) {
            hangover -= 1;
        }

        player.setData(ModAttachments.BLOOD_ALCOHOL, Math.max(bloodAlcohol, 0));
        player.setData(ModAttachments.INTOXICATION, Math.max(intoxication, 0));
        player.setData(ModAttachments.HANGOVER, Math.max(hangover, 0));
    }

    public static void addBloodAlcohol(Player player, int toAdd) {
        player.setData(ModAttachments.BLOOD_ALCOHOL, player.getData(ModAttachments.BLOOD_ALCOHOL) + toAdd);
        syncAlcohol(player);
    }

    public static void syncAlcohol(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer,
                    new AlcoholSyncPacket(player.getData(ModAttachments.BLOOD_ALCOHOL),
                            player.getData(ModAttachments.INTOXICATION),
                            player.getData(ModAttachments.HANGOVER)));
        }
    }
}
