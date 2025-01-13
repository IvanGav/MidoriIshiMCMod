package net.ivangav.midori_ishi.potion;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.ivangav.midori_ishi.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, MidoriIshiMod.MOD_ID);

    public static final RegistryObject<Potion> MIDORI_VIAL = POTIONS.register("midori_vial",
            ()->new Potion(new MobEffectInstance(ModEffects.MIDAS_TOUCH_EFFECT.get(), 200, 0)));

    public static void register(IEventBus ebus) {
        POTIONS.register(ebus);
    }
}

