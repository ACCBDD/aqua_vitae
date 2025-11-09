package com.accbdd.aqua_vitae.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final List<DeferredItem<? extends Item>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = registerSimpleBlockItem("example_block", ModBlocks.EXAMPLE_BLOCK);

    private static <T extends Item> DeferredItem<T> register(String name, Supplier<T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }

    private static <T extends Item> DeferredItem<T> registerWithTab(String name, Supplier<T> itemSupplier) {
        DeferredItem<T> register = register(name, itemSupplier);
        CREATIVE_TAB_ITEMS.add(register);
        return register;
    }

    private static DeferredItem<Item> registerSimpleItem(String name) {
        return registerWithTab(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Block> DeferredItem<BlockItem> registerSimpleBlockItem(String name, Supplier<T> block) {
        return registerWithTab(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
