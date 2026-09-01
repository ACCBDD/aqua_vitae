package com.accbdd.aqua_vitae.item;

import com.accbdd.aqua_vitae.registry.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class BreathalyzerItem extends Item {
    public BreathalyzerItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && !level.isClientSide) {
            player.getCooldowns().addCooldown(this, 40);
            double reach = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
            Vec3 eyePos = player.getEyePosition();
            Vec3 viewVec = player.getViewVector(1.0F);
            Vec3 reachVec = eyePos.add(viewVec.x * reach, viewVec.y * reach, viewVec.z * reach);
            AABB searchBox = player.getBoundingBox().expandTowards(viewVec.scale(reach)).inflate(1.0D);

            EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                    level, player, eyePos, reachVec, searchBox, entity -> entity instanceof Player
            );

            Player target = (hitResult != null && hitResult.getEntity() instanceof Player hitPlayer)
                    ? hitPlayer
                    : player;

            player.displayClientMessage(Component.literal(String.format("%s -> Undigested: %d, BAC: %3.3f, Hangover: %d",
                    target == player ? "Self" : target.getName().getString(),
                    target.getData(ModAttachments.UNDIGESTED_ALCOHOL),
                    target.getData(ModAttachments.BLOOD_ALCOHOL).floatValue() / 1000000f,
                    target.getData(ModAttachments.HANGOVER))), true);
            //todo add noises
            //todo add obscuration for undigested and hangover (HIGH, etc)
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
