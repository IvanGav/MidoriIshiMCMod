package net.ivangav.midori_ishi.screen;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
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
    public static final RegistryObject<MenuType<MidoriTransmogrificationTableMenu>> MIDORI_TRANSMOGRIFICATION_TABLE_MENU = MENUS.register(
            "midori_transmogrification_table_menu",
            ()->IForgeMenuType.create(MidoriTransmogrificationTableMenu::new)
    );

//    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
//        return MENUS.register(name, () -> IForgeMenuType.create(factory));
//    }

    public static void register(IEventBus ebus) {
        MENUS.register(ebus);
    }
}
