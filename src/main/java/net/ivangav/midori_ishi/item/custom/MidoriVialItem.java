package net.ivangav.midori_ishi.item.custom;

import net.ivangav.midori_ishi.effect.ModEffects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Function3;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;

public class MidoriVialItem extends Item {
    private static final int DRINK_DURATION = 32;

    private static final ImmutableSet<String> vialRecipes = ImmutableSet.of(
            "item.midori_ishi.midori_ishi",
            "item.minecraft.enchanted_golden_apple",
            "item.minecraft.string",
            "block.minecraft.cobblestone",
            "item.minecraft.ender_pearl",
            "block.minecraft.wither_rose",
            "block.minecraft.wither_skeleton_skull",
            "item.minecraft.gold_ingot"
    );
    private static final ImmutableMap<String,VialEffectDanger> vialDanger = ImmutableMap.of(
            "item.midori_ishi.midori_ishi", VialEffectDanger.Safe,
            "item.minecraft.enchanted_golden_apple", VialEffectDanger.Safe,
            "item.minecraft.string", VialEffectDanger.Safe,
            "block.minecraft.cobblestone", VialEffectDanger.Harmful,
            "item.minecraft.ender_pearl", VialEffectDanger.Harmful,
            "block.minecraft.wither_rose", VialEffectDanger.Deadly,
            "block.minecraft.wither_skeleton_skull", VialEffectDanger.Deadly,
            "item.minecraft.gold_ingot", VialEffectDanger.Destructive
    );
    private static final ImmutableMap<String,String> vialMessage = ImmutableMap.of(
            "item.midori_ishi.midori_ishi", "You feel relieved and rejuvinated",
            "item.minecraft.enchanted_golden_apple", "Tastes like apple juice and honey, with a slight metallic aftertaste",
            "item.minecraft.string", "Spooder",
            "block.minecraft.cobblestone", "Get cobblestoned",
            "item.minecraft.ender_pearl", "Tastes like ender pearls",
            "block.minecraft.wither_rose", "An immensly dry potion withers your insides",
            "block.minecraft.wither_skeleton_skull", "You don't feel well... You feel your insides being turned to dust",
            "item.minecraft.gold_ingot", "Greed"
    );
    private static final ImmutableMap<String, Function3<Level, LivingEntity, String, Nullable>> vialEffect = ImmutableMap.of(
            "item.midori_ishi.midori_ishi", (level, e, infused_item)-> {
                e.addEffect(new MobEffectInstance(MobEffects.HEAL, 20, 3));
                e.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 1));
                return null;
            },
            "item.minecraft.enchanted_golden_apple", (level, e, infused_item)-> {
                e.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
                e.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3000, 0));
                e.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3000, 0));
                e.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3));
                return null;
            },
            "item.minecraft.string", (level, e, infused_item)-> {
                e.addEffect(new MobEffectInstance(ModEffects.SPIDER_EFFECT.get(), 2400, 0));
                return null;
            },
            "block.minecraft.cobblestone", (level, e, infused_item)-> {
                BlockPos pos = new BlockPos((int) Math.round(e.getX()-0.5), (int) Math.round(e.getY()+1f), (int) Math.round(e.getZ()-0.5));
                level.playSound(e, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState newState = Blocks.COBBLESTONE.defaultBlockState();
                if(level.getBlockState(pos).canBeReplaced())
                    level.setBlock(pos, newState, 11);
                if(level.getBlockState(pos.below()).canBeReplaced())
                    level.setBlock(pos.below(), newState, 11);
                level.gameEvent(e, GameEvent.BLOCK_PLACE, pos);
                return null;
            },
            "item.minecraft.ender_pearl", (level, e, infused_item)-> {
                e.addEffect(new MobEffectInstance(ModEffects.TELEPORT_DISEASE_EFFECT.get(), 60, 0));
                return null;
            },
            "block.minecraft.wither_rose", (level, e, infused_item)-> {
                e.addEffect(new MobEffectInstance(MobEffects.WITHER, 1200, 6));
                e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 0));
                return null;
            },
            "block.minecraft.wither_skeleton_skull", (level, e, infused_item)-> {
                e.addEffect(new MobEffectInstance(MobEffects.WITHER, 1200, 0));
                e.addEffect(new MobEffectInstance(ModEffects.GREATER_WITHER_EFFECT.get(), 1200, 0));
                return null;
            },
            "item.minecraft.gold_ingot", (level, e, infused_item)-> {
                e.addEffect(new MobEffectInstance(ModEffects.MIDAS_TOUCH_EFFECT.get(), 100, 0));
                return null;
            }
    );

    private enum VialEffectDanger {
        Safe, Harmful, Deadly, Destructive, Apocalypse;

        public ChatFormatting chatFormatting() {
            switch(this) {
                case Safe -> {return ChatFormatting.GREEN;}
                case Harmful -> {return ChatFormatting.YELLOW;}
                case Deadly -> {return ChatFormatting.RED;}
                case Destructive -> {return ChatFormatting.DARK_PURPLE;}
                case Apocalypse -> {return ChatFormatting.BLACK;}
                default -> {return ChatFormatting.GRAY;} //impossible; java wtf why are you making me do this?
            }
        }
    }

    public MidoriVialItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public static void giveNBT(ItemStack currentStack, ItemStack infusedItem) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("infused_item", infusedItem.getItem().getDescriptionId());
        currentStack.setTag(nbt);
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack finishUsingItem(ItemStack pStack, Level level, LivingEntity e) {
        if (!level.isClientSide) {
            if(pStack.hasTag() && pStack.getTag().contains("infused_item")) {
                String infused_item = pStack.getTag().getString("infused_item");
                if(vialRecipes.contains(infused_item)) {
                    e.sendSystemMessage(Component.literal(vialMessage.get(infused_item)));
                    vialEffect.get(infused_item).apply(level, e, infused_item);
                } else {
                    //for unimplemented items
                    e.sendSystemMessage(Component.literal("Doesn't taste well..."));
                    e.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 2));
                    e.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
                }
                //start effects
//                switch(infused_item) {
//                    case "item.minecraft.gold_ingot": {
//                        e.addEffect(new MobEffectInstance(ModEffects.MIDAS_TOUCH_EFFECT.get(), 100, 0));
//                        break;
//                    }
//                    case "item.minecraft.string": {
//                        e.addEffect(new MobEffectInstance(ModEffects.SPIDER_EFFECT.get(), 2400, 0));
//                        break;
//                    }
//                    case "block.minecraft.cobblestone": {
//                        BlockPos pos = new BlockPos((int) Math.round(e.getX()-0.5), (int) Math.round(e.getY()+1f), (int) Math.round(e.getZ()-0.5));
//                        level.playSound(e, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
//                        BlockState newstate = Blocks.COBBLESTONE.defaultBlockState();
//                        if(level.getBlockState(pos).canBeReplaced())
//                            level.setBlock(pos, newstate, 11);
//                        if(level.getBlockState(pos.below()).canBeReplaced())
//                            level.setBlock(pos.below(), newstate, 11);
//                        level.gameEvent(e, GameEvent.BLOCK_PLACE, pos);
//                        break;
//                    }
//                    case "block.minecraft.wither_rose": {
//                        e.addEffect(new MobEffectInstance(MobEffects.WITHER, 1200, 6));
//                        e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 0));
//                        break;
//                    }
//                    case "block.minecraft.wither_skeleton_skull": {
//                        e.addEffect(new MobEffectInstance(MobEffects.WITHER, 1200, 0));
//                        e.addEffect(new MobEffectInstance(ModEffects.GREATER_WITHER_EFFECT.get(), 1200, 0));
//                        break;
//                    }
//                    case "item.midori_ishi.midori_ishi": {
//                        e.addEffect(new MobEffectInstance(MobEffects.HEAL, 20, 3));
//                        e.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 1));
//                        break;
//                    }
//                    case "item.minecraft.enchanted_golden_apple": {
//                        e.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
//                        e.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3000, 0));
//                        e.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3000, 0));
//                        e.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3));
//                        break;
//                    }
//                    default: {
//                        e.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 2));
//                        e.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
//                    }
//                }
                //end effects
            } else {
                e.sendSystemMessage(Component.literal("no cheating, brew the vial yourself"));
                e.die(e.damageSources().magic());
            }
        }
        //give stats == useless for me; can be found in `Item/MilkBucketItem`
        if (e instanceof Player) {
//            pStack.shrink(1);
//            Inventory i = ((Player) e).getInventory();
//            int slot = i.getSlotWithRemainingSpace(new ItemStack(Items.GLASS_BOTTLE));
//            if(slot == Inventory.NOT_FOUND_INDEX)
//                slot = i.getFreeSlot();
//
//            if(slot == Inventory.NOT_FOUND_INDEX)
//                ((Player) e).drop(new ItemStack(Items.GLASS_BOTTLE), false);
//            else
//                i.add(slot, new ItemStack(Items.GLASS_BOTTLE));
            //^^^ my own implementation that is useless because of this boi below
            return ItemUtils.createFilledResult(pStack, (Player) e, new ItemStack(Items.GLASS_BOTTLE));
        }
        return pStack;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pMiningEntity) {
        Player p = (Player) pMiningEntity;
        giveNBT(pStack, p.getOffhandItem());
        return super.mineBlock(pStack, pLevel, pState, pPos, pMiningEntity);
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return DRINK_DURATION;
    }

    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    /**
     * Called to trigger the item's "innate" right click behavior.
     */
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pStack.hasTag() && pStack.getTag().contains("infused_item")) {
            String infused_item = pStack.getTag().getString("infused_item");
            VialEffectDanger ved = vialDanger.getOrDefault(infused_item, VialEffectDanger.Harmful);
            pTooltipComponents.add(Component.translatable(infused_item).withStyle(ved.chatFormatting()));
            pTooltipComponents.add(Component.literal(infused_item).withStyle(ChatFormatting.GRAY));
        } else {
            //no nbt data
            pTooltipComponents.add(Component.literal("JEI won't help with recipes").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}

/*
public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
      ItemStack itemstack = pPlayer.getItemInHand(pHand);
      BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, this.content == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
      InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, blockhitresult);
      if (ret != null) return ret;
      if (blockhitresult.getType() == HitResult.Type.MISS) {
         return InteractionResultHolder.pass(itemstack);
      } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
         return InteractionResultHolder.pass(itemstack);
      } else {
         BlockPos blockpos = blockhitresult.getBlockPos();
         Direction direction = blockhitresult.getDirection();
         BlockPos blockpos1 = blockpos.relative(direction);
         if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
            if (this.content == Fluids.EMPTY) {
               BlockState blockstate1 = pLevel.getBlockState(blockpos);
               if (blockstate1.getBlock() instanceof BucketPickup) {
                  BucketPickup bucketpickup = (BucketPickup)blockstate1.getBlock();
                  ItemStack itemstack1 = bucketpickup.pickupBlock(pLevel, blockpos, blockstate1);
                  if (!itemstack1.isEmpty()) {
                     pPlayer.awardStat(Stats.ITEM_USED.get(this));
                     bucketpickup.getPickupSound(blockstate1).ifPresent((p_150709_) -> {
                        pPlayer.playSound(p_150709_, 1.0F, 1.0F);
                     });
                     pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, blockpos);
                     ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, pPlayer, itemstack1);
                     if (!pLevel.isClientSide) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)pPlayer, itemstack1);
                     }

                     return InteractionResultHolder.sidedSuccess(itemstack2, pLevel.isClientSide());
                  }
               }

               return InteractionResultHolder.fail(itemstack);
            } else {
               BlockState blockstate = pLevel.getBlockState(blockpos);
               BlockPos blockpos2 = canBlockContainFluid(pLevel, blockpos, blockstate) ? blockpos : blockpos1;
               if (this.emptyContents(pPlayer, pLevel, blockpos2, blockhitresult, itemstack)) {
                  this.checkExtraContent(pPlayer, pLevel, itemstack, blockpos2);
                  if (pPlayer instanceof ServerPlayer) {
                     CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)pPlayer, blockpos2, itemstack);
                  }

                  pPlayer.awardStat(Stats.ITEM_USED.get(this));
                  return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemstack, pPlayer), pLevel.isClientSide());
               } else {
                  return InteractionResultHolder.fail(itemstack);
               }
            }
         } else {
            return InteractionResultHolder.fail(itemstack);
         }
      }
   }
 */



/*
replace block combo:
if (!pLevel.isClientSide && flag && !blockstate.liquid()) {
   pLevel.destroyBlock(pPos, true);
}
if (!pLevel.setBlock(pPos, this.content.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
   return false;
}
 */