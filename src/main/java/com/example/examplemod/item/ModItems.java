package com.example.examplemod.item;

import com.example.examplemod.MidoriIshiMod;
import com.example.examplemod.item.custom.MetalDetectorItem;
import com.example.examplemod.item.custom.MidoriVialItem;
import net.minecraft.world.item.Item;
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

    public static void register(IEventBus ebus) {
        ITEMS.register(ebus);
    }
}
