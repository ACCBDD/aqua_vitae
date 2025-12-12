package com.accbdd.aqua_vitae.item;

import com.accbdd.aqua_vitae.AquaVitae;
import com.accbdd.aqua_vitae.client.ClientUtils;
import com.accbdd.aqua_vitae.component.AlcoholPropertiesComponent;
import com.accbdd.aqua_vitae.component.FluidStackComponent;
import com.accbdd.aqua_vitae.player.PlayerAlcoholManager;
import com.accbdd.aqua_vitae.registry.ModComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class CupItem extends Item {
    private final int useTime;
    private final int capacity;

    public CupItem(int useTime, int stacksTo, int capacity) {
        super(new Properties()
                .component(ModComponents.FLUIDSTACK, new FluidStackComponent(FluidStack.EMPTY))
                .stacksTo(stacksTo));
        this.useTime = useTime;
        this.capacity = capacity;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(ModComponents.FLUIDSTACK) && !stack.get(ModComponents.FLUIDSTACK).stack().isEmpty())
            return Component.translatable("grammar.aqua_vitae.container_of", super.getName(stack), stack.get(ModComponents.FLUIDSTACK.get()).stack().getHoverName());
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        FluidStack fluid = stack.getOrDefault(ModComponents.FLUIDSTACK, FluidStackComponent.EMPTY).stack();
        tooltipComponents.add(fluid.getHoverName().copy().append(": " + fluid.getAmount()));
        tooltipComponents.addAll(ClientUtils.getFluidTooltip(fluid));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return useTime;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        FluidStack fluid = stack.getOrDefault(ModComponents.FLUIDSTACK, FluidStackComponent.EMPTY).stack();
        if (fluid.getAmount() > 0) {
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(stack);
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (level.isClientSide)
            return stack;
        var fluid = stack.remove(ModComponents.FLUIDSTACK).stack();
        if (fluid.has(ModComponents.ALCOHOL_PROPERTIES) && livingEntity instanceof Player player) {
            AlcoholPropertiesComponent props = fluid.get(ModComponents.ALCOHOL_PROPERTIES);
            PlayerAlcoholManager.addBloodAlcohol(player, (int) (props.abb() * fluid.getAmount() / 10));
            props.flavors().stream().map(key -> level.registryAccess().registry(AquaVitae.FLAVOR_REGISTRY).get().get(key))
                    .forEach(flavor -> flavor.effects().forEach(livingEntity::addEffect));
        }
        return stack;
    }

    public int getCapacity() {
        return capacity;
    }
}
