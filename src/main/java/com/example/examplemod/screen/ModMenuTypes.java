package com.example.examplemod.screen;

import com.example.examplemod.MidoriIshiMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, MidoriIshiMod.MOD_ID);

    public static final RegistryObject<MenuType<MidoriInfusionTableMenu>> MIDORI_INFUSION_TABLE_MENU = MENUS.register(
            "midori_infusion_table_menu",
            ()->IForgeMenuType.create(MidoriInfusionTableMenu::new)
    );

//    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
//        return MENUS.register(name, () -> IForgeMenuType.create(factory));
//    }

    public static void register(IEventBus ebus) {
        MENUS.register(ebus);
    }
}
