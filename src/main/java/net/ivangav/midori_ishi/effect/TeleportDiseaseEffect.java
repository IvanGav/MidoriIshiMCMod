package net.ivangav.midori_ishi.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TeleportDiseaseEffect extends MobEffect {
    public TeleportDiseaseEffect(MobEffectCategory cat, int col) {
        super(cat, col);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
        if(!e.level().isClientSide()) {
            double dx = Math.random()*2.0-1.0;
            double dy = Math.random()*3.0-1.0;
            double dz = Math.random()*2.0-1.0;
            e.teleportRelative(dx,dy,dz);
        }
        super.applyEffectTick(e, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
