package net.ivangav.midori_ishi.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GreaterWitherEffect extends MobEffect {
    public GreaterWitherEffect(MobEffectCategory cat, int col) {
        super(cat, col);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
//        if(!e.level().isClientSide()) {
//            e.setItemInHand(e.getUsedItemHand(), new ItemStack(Items.WITHER_ROSE, e.getMainHandItem().getCount()));
//        }
        super.applyEffectTick(e, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
