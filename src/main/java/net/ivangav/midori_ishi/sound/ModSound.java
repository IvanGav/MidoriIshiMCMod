package net.ivangav.midori_ishi.sound;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSound {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MidoriIshiMod.MOD_ID);

    public static final RegistryObject<SoundEvent> MIDORI_SCANNER_USE = registerSoundEvents("midori_scanner_use");

    public static void register(IEventBus ebus) {
        SOUND_EVENTS.register(ebus);
    }

    public static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, ()->SoundEvent.createVariableRangeEvent(new ResourceLocation(MidoriIshiMod.MOD_ID, name)));
    }
}
