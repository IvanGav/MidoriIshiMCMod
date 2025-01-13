package com.example.examplemod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class SpiderEffect extends MobEffect {
    public SpiderEffect(MobEffectCategory cat, int col) {
        super(cat, col);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
        if(e.horizontalCollision) {
            Vec3 initVec = e.getDeltaMovement();
            Vec3 finVec = new Vec3(initVec.x, 0.2d*(pAmplifier+1), initVec.z);
            e.setDeltaMovement(finVec.scale(0.97d));
        }
        super.applyEffectTick(e, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
