package net.ivangav.midori_ishi;

import net.ivangav.midori_ishi.block.ModBlocks;
import net.ivangav.midori_ishi.block.entity.ModBlockEntities;
import net.ivangav.midori_ishi.effect.ModEffects;
import net.ivangav.midori_ishi.tabs.ModCreativeModTabs;
import net.ivangav.midori_ishi.item.ModItems;
import net.ivangav.midori_ishi.screen.MidoriInfusionTableScreen;
import net.ivangav.midori_ishi.screen.MidoriTransmogrificationTableScreen;
import net.ivangav.midori_ishi.screen.ModMenuTypes;
import net.ivangav.midori_ishi.sound.ModSound;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MidoriIshiMod.MOD_ID)
public class MidoriIshiMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "midori_ishi";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public MidoriIshiMod() { //FMLJavaModLoadingContext context
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModCreativeModTabs.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModSound.register(modEventBus);
        ModEffects.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.debug("--MidoriIshi.commonSetup has executed the enqueued work.");
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.MIDORI_INFUSION_TABLE_MENU.get(), MidoriInfusionTableScreen::new);
            MenuScreens.register(ModMenuTypes.MIDORI_TRANSMOGRIFICATION_TABLE_MENU.get(), MidoriTransmogrificationTableScreen::new);
        }
    }

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.debug("--MidoriIshi.init has executed the enqueued work.");
        });
    }
}
