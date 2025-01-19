package net.ivangav.midori_ishi.effect;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.ivangav.midori_ishi.sound.ModSound;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class AnvilEffect extends MobEffect {
    private int fallTicks = 0;

    public AnvilEffect(MobEffectCategory cat, int col) {
        super(cat, col);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
        if(e.onGround()) {
            if(!e.level().isClientSide) {
                for (Entity toDamage : e.level().getEntities(e, e.getBoundingBox().inflate(3))) {
                    toDamage.hurt(e.damageSources().anvil(e), this.fallTicks * 1.3f);
                }
                e.level().playSeededSound(null, e.getX(), e.getY(), e.getZ(), SoundEvents.ANVIL_LAND,
                        SoundSource.BLOCKS, 1f, 1f, 0);
                e.removeEffect(this);
                fallTicks = 0;
            }
        } else {
            Vec3 initVec = e.getDeltaMovement();
            Vec3 finVec = new Vec3(initVec.x, initVec.y-0.1, initVec.z); //Math.min(initVec.y * 0.1, 0.1)
            e.setDeltaMovement(finVec);
            this.fallTicks++;
        }
        super.applyEffectTick(e, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

}
