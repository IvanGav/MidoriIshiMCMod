package net.ivangav.midori_ishi.item;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.ivangav.midori_ishi.item.custom.Maxwell;
import net.ivangav.midori_ishi.item.custom.MetalDetectorItem;
import net.ivangav.midori_ishi.item.custom.MidoriBookItem;
import net.ivangav.midori_ishi.item.custom.MidoriVialItem;
import net.ivangav.midori_ishi.sound.ModSound;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MidoriIshiMod.MOD_ID);

    public static final RegistryObject<Item> MIDORI_ISHI = ITEMS.register("midori_ishi",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("midori_scanner",
            () -> new MetalDetectorItem(new Item.Properties().durability(256)));

    public static final RegistryObject<Item> MIDORI_VIAL = ITEMS.register("midori_vial",
            () -> new MidoriVialItem(new Item.Properties()));

    public static final RegistryObject<Item> MAXWELL = ITEMS.register("maxwell",
            () -> new Maxwell(new Item.Properties().stacksTo(1)));

//    public static final RegistryObject<Item> MIDORI_BOOK_ITEM = ITEMS.register("midori_book",
//            () -> new MidoriBookItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus ebus) {
        ITEMS.register(ebus);
    }
}
