package com.accbdd.aqua_vitae.item;

import com.accbdd.aqua_vitae.registry.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class MonocleItem extends Item implements Equipable {
    private static final List<Predicate<Player>> IS_WEARING_PREDICATES = new ArrayList();

    public MonocleItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return this.swapWithEquipmentSlot(this, level, player, usedHand);
    }

    public static boolean isWearingMonocle(Player player) {
        if (player == null)
            return false;
        Iterator<Predicate<Player>> predicates = IS_WEARING_PREDICATES.iterator();

        Predicate<Player> predicate;
        do {
            if (!predicates.hasNext()) {
                return false;
            }

            predicate = predicates.next();
        } while(!predicate.test(player));

        return true;
    }

    public static synchronized void addIsWearingPredicate(Predicate<Player> predicate) {
        IS_WEARING_PREDICATES.add(predicate);
    }

    static {
        addIsWearingPredicate((player) -> player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.BREWMASTER_MONOCLE));
    }
}
