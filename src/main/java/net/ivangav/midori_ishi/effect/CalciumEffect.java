package net.ivangav.midori_ishi.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CalciumEffect extends MobEffect {
    public CalciumEffect(MobEffectCategory cat, int col) {
            super(cat, col);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
        if(!e.level().isClientSide()) {
            e.resetFallDistance();
        }
        super.applyEffectTick(e, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

}
