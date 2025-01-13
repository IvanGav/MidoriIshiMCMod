package com.example.examplemod.event;

import com.example.examplemod.MidoriIshiMod;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= MidoriIshiMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onGreaterWitherExpireDie(MobEffectEvent ev) {
        if((ev instanceof MobEffectEvent.Remove || ev instanceof MobEffectEvent.Expired) && ev.getEffectInstance().getDescriptionId().equals("effect.midori_ishi.greater_wither")) {
            ev.getEntity().hurt(ev.getEntity().damageSources().wither(), 100);
        }
    }
}
