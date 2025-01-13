package net.ivangav.midori_ishi.item;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.ivangav.midori_ishi.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MidoriIshiMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MIDORI_ISHI_TAB = CREATIVE_MODE_TABS.register("midori_ishi_tab",
            () -> CreativeModeTab.builder()
                    .icon(()->new ItemStack(ModItems.MIDORI_ISHI.get()))
                    .title(Component.translatable("creativetab.midori_ishi_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.MIDORI_ISHI.get());

                        output.accept(ModBlocks.MIDORI_ISHI_ORE.get());

                        output.accept(ModItems.METAL_DETECTOR.get());

                        output.accept(ModItems.MIDORI_VIAL.get());

                        output.accept(ModBlocks.MIDORI_INFUSION_TABLE.get());
                    }))
                    .build());

    public static void register(IEventBus ebus) {
        CREATIVE_MODE_TABS.register(ebus);
    }
}
