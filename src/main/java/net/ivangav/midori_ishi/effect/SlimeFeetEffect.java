package net.ivangav.midori_ishi.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SlimeFeetEffect extends MobEffect {
    public SlimeFeetEffect(MobEffectCategory cat, int col) {
        super(cat, col);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
        if(e.verticalCollisionBelow) {
            Vec3 initVec = e.getDeltaMovement();
            Vec3 finVec = new Vec3(initVec.x, 2d, initVec.z);
            e.setDeltaMovement(finVec.scale(0.97d));
        } else {
            e.resetFallDistance();
        }
        super.applyEffectTick(e, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
