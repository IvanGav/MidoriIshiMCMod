package net.ivangav.midori_ishi.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MidasTouchEffect extends MobEffect {
    public MidasTouchEffect(MobEffectCategory cat, int col) {
        super(cat, col);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
        //https://stackoverflow.com/questions/53745339/is-there-a-need-to-close-resource-if-we-use-try-with-resource
        if(!e.level().isClientSide()) {
            if(!e.getMainHandItem().is(Items.GOLD_INGOT))
                e.setItemInHand(e.getUsedItemHand(), new ItemStack(Items.GOLD_INGOT, e.getMainHandItem().getCount()));
            }
//            if(!e.getFeetBlockState().is(Blocks.GOLD_BLOCK)) {
//                e.sendSystemMessage(Component.literal("mmmm"));
//            }
//            int blockX = (int) Math.floor(e.position().x);
//            int blockY = ((int) Math.floor(e.getBoundingBox().minY))-1;
//            int blockZ = (int) Math.floor(e.position().z);
//
////            BlockState newState = Blocks.COBBLESTONE.defaultBlockState();
////            level.setBlock(p, newState, 11);
        super.applyEffectTick(e, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
