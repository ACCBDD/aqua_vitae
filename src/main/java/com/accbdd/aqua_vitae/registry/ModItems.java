package com.accbdd.aqua_vitae.registry;

import com.accbdd.aqua_vitae.item.CupItem;
import com.accbdd.aqua_vitae.item.MeterItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.aqua_vitae.AquaVitae.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final List<DeferredItem<? extends Item>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static final DeferredItem<BucketItem> AQUA_VITAE_BUCKET = registerBucket("aqua_vitae_bucket", ModFluids.AQUA_VITAE);

    public static final DeferredItem<BlockItem> KEG = registerSimpleBlockItem("keg", ModBlocks.KEG);
    public static final DeferredItem<BlockItem> FERMENTER = registerSimpleBlockItem("fermenter", ModBlocks.FERMENTER);
    public static final DeferredItem<BlockItem> CRUSHING_TUB = registerSimpleBlockItem("crushing_tub", ModBlocks.CRUSHING_TUB);
    public static final DeferredItem<BlockItem> POT_STILL = registerSimpleBlockItem("pot_still", ModBlocks.POT_STILL);

    public static final DeferredItem<CupItem> CUP = registerWithTab("cup", () -> new CupItem(40, 1, 250));
    public static final DeferredItem<CupItem> SHOOTER = registerWithTab("shooter", () -> new CupItem(20, 4, 50));
    public static final DeferredItem<CupItem> EYEBALL = registerWithTab("eyeball", () -> new CupItem(20, 1, 10));
    public static final DeferredItem<CupItem> BREW_BUCKET = registerWithTab("brew_bucket", () -> new CupItem(120, 1, 1000));
    public static final DeferredItem<MeterItem> METER = registerWithTab("meter", MeterItem::new);

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

    private static <T extends Fluid> DeferredItem<BucketItem> registerBucket(String name, Supplier<? extends Fluid> fluid) {
        return registerWithTab(name, () -> new BucketItem(fluid.get(), (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1)));
    }
}
