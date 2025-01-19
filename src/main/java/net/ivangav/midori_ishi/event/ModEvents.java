package net.ivangav.midori_ishi.event;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.ivangav.midori_ishi.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid= MidoriIshiMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onGreaterWitherExpireDie(MobEffectEvent ev) {
        //pretty sure runs on the server only
        if(ev.getEffectInstance() == null) return;
        if((ev instanceof MobEffectEvent.Remove || ev instanceof MobEffectEvent.Expired) && (
                ev.getEffectInstance().getDescriptionId().equals("effect.midori_ishi.greater_wither")
        )) {
//            Level level = Minecraft.getInstance().level;
            LivingEntity e = ev.getEntity();
            Level level = e.level();
            if (level != null) {
                if (!level.isClientSide) {
                    boolean flag = false;
                    BlockPos blockpos = e.blockPosition();
                    BlockState blockstate = Blocks.WITHER_ROSE.defaultBlockState();
                    if (level.isEmptyBlock(blockpos) && blockstate.canSurvive(level, blockpos)) {
                        level.setBlock(blockpos, blockstate, 3);
                        flag = true;
                    }

                    if (!flag) {
                        ItemEntity itementity = new ItemEntity(level, e.getX(), e.getY(), e.getZ(), new ItemStack(Items.WITHER_ROSE));
                        level.addFreshEntity(itementity);
                    }
                }
            }
            //kill the entity which had greater wither effect finish
            ev.getEntity().hurt(ev.getEntity().damageSources().wither(), 200);
        }
    }

//    @SubscribeEvent
//    public static void onGreaterWitherKillSpawnWitherRose(LivingDeathEvent e) {//@Nullable LivingEntity pEntitySource) {
//        Level level = Minecraft.getInstance().level;
//        if(level == null) return;
//        LivingEntity deathSource = e.getSource().getEntity();
//        LivingEntity dyingEntity = e.getEntity();
//        if (!level.isClientSide) {
//            boolean flag = false;
//            if (deathSource.hasEffect(ModEffects.GREATER_WITHER_EFFECT.get())) {
//                BlockPos blockpos = dyingEntity.blockPosition();
//                BlockState blockstate = Blocks.WITHER_ROSE.defaultBlockState();
//                if (level.isEmptyBlock(blockpos) && blockstate.canSurvive(level, blockpos)) {
//                    level.setBlock(blockpos, blockstate, 3);
//                    flag = true;
//                }
//
//                if (!flag) {
//                    ItemEntity itementity = new ItemEntity(level, dyingEntity.getX(), dyingEntity.getY(), dyingEntity.getZ(), new ItemStack(Items.WITHER_ROSE));
//                    level.addFreshEntity(itementity);
//                }
//            }
//        }
//    }
}
