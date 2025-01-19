package net.ivangav.midori_ishi.effect;

import com.mojang.blaze3d.shaders.Effect;
import net.ivangav.midori_ishi.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

import static net.ivangav.midori_ishi.item.custom.MidoriVialItem.NBT_TAG_NAME;

public class GreaterWitherEffect extends MobEffect {
    public GreaterWitherEffect(MobEffectCategory cat, int col) {
        super(cat, col);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
        super.applyEffectTick(e, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }

    // Should this function be enabled, in event/ModEvents/onGreaterWitherExpireDie,
    // the check for `ev instanceof MobEffectEvent.Remove` should also be removed
//    @Override
//    public List<ItemStack> getCurativeItems() {
//        ItemStack cure = new ItemStack(ModItems.MIDORI_VIAL.get());
//        CompoundTag nbt = new CompoundTag();
//        nbt.putString(NBT_TAG_NAME, Items.ENCHANTED_GOLDEN_APPLE.getDescriptionId());
//        cure.setTag(nbt);
//        return List.of(cure);
//    }
}
