package com.accbdd.aqua_vitae.player;

import com.accbdd.aqua_vitae.network.AlcoholSyncPacket;
import com.accbdd.aqua_vitae.registry.ModAttachments;
import com.accbdd.aqua_vitae.registry.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerAlcoholManager {
    public static void tickPlayer(Player player) {
        int undigested = player.getData(ModAttachments.UNDIGESTED_ALCOHOL);
        int bloodAlcohol = player.getData(ModAttachments.BLOOD_ALCOHOL);
        int hangover = player.getData(ModAttachments.HANGOVER);

        float saturationFactor = Math.max(0.3f, 1.0f - (player.getFoodData().getSaturationLevel() / 20.0f) * 0.7f);
        if (undigested > 0) {
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
            hangover = (int) Math.max(0, hangover - 5 / saturationFactor);
        }

        player.setData(ModAttachments.UNDIGESTED_ALCOHOL, Math.max(undigested, 0));
        player.setData(ModAttachments.BLOOD_ALCOHOL, Math.max(bloodAlcohol, 0));
        player.setData(ModAttachments.HANGOVER, Math.max(hangover, 0));
        //todo add actual effects for hangover and bac
        if (bloodAlcohol > 500000) { //0.50 bac
            setPlayerEffectLevel(player, ModEffects.INTOXICATED, 5);
        } else if (bloodAlcohol > 320000) {
            setPlayerEffectLevel(player, ModEffects.INTOXICATED, 4);
        } else if (bloodAlcohol > 200000) {
            setPlayerEffectLevel(player, ModEffects.INTOXICATED, 3);
        } else if (bloodAlcohol > 130000) {
            setPlayerEffectLevel(player, ModEffects.INTOXICATED, 2);
        } else if (bloodAlcohol > 80000) {
            setPlayerEffectLevel(player, ModEffects.INTOXICATED, 1);
        } else if (bloodAlcohol > 5000) {
            setPlayerEffectLevel(player, ModEffects.INTOXICATED, 0);
        } else {
            setPlayerEffectLevel(player, ModEffects.INTOXICATED, -1);
        }
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

    /**
     * Sets a player's effect level of an infinite duration effect. Does not change data if the effect already exists.
     * @param player the player to change the effect of
     * @param effect the effect to change
     * @param amplifier the amplifier to set the effect level to (-1 clears the effect)
     */
    private static void setPlayerEffectLevel(Player player, Holder<MobEffect> effect, int amplifier) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null && amplifier != -1) {
            player.addEffect(new MobEffectInstance(effect, -1, amplifier));
            return;
        }
        if (current != null && current.getAmplifier() != amplifier) {
            player.removeEffect(effect);
            if (amplifier != -1) {
                player.addEffect(new MobEffectInstance(effect, -1, amplifier));
            }
        }
    }
}
