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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class MidoriVialItem extends Item {
    private static final int DRINK_DURATION = 32;
    public static final String NBT_TAG_NAME = "infused_item";

    private static final ImmutableSet<String> vialRecipes = ImmutableSet.of(
            "item.midori_ishi.midori_ishi",
            "item.midori_ishi.maxwell",

            "item.minecraft.enchanted_golden_apple",
            "item.minecraft.string",
            "block.minecraft.cobblestone",
            "item.minecraft.ender_pearl",
            "block.minecraft.wither_rose",
            "block.minecraft.wither_skeleton_skull",
            "item.minecraft.gold_ingot"
//            "item.minecraft.lava_bucket"

//            "block.tconstruct.grout",
//            "block.tconstruct.nether_grout",
//
//            "item.born_in_chaos_v1.dark_metal_nugget",
//            "item.born_in_chaos_v1.pieceofdarkmetal",
//            "item.born_in_chaos_v1.creepy_gift",
//
//            "item.alexscaves.uranium",
//            "block.alexscaves.nuclear_bomb",
//
//            "item.scalinghealth.heart_crystal",
//
//            "item.hexerei.infused_fabric",
//            "item.hexerei.blood_bottle",
//
//            "item.graveyard.corruption",
//
//            "item.galosphere.silver_ingot",
//
//            "item.easy_villagers.villager",
//
//            "item.mysticalagriculture.inferium_essence",
//
//            "item.create.goggles",
//            "item.create.andesite_alloy",
//
//            "item.dragonsurvival.star_bone",
//            "item.dragonsurvival.elder_dragon_dust",
//            "item.dragonsurvival.elder_dragon_bone",
//            "item.dragonsurvival.heart_element",
//            "item.dragonsurvival.weak_dragon_heart",
//            "item.dragonsurvival.elder_dragon_heart"
    );
    private static final ImmutableMap<String,VialEffectDanger> vialDanger = ImmutableMap.of(
            "item.midori_ishi.midori_ishi", VialEffectDanger.Safe,
            "item.midori_ishi.maxwell", VialEffectDanger.Apocalypse,

            "item.minecraft.enchanted_golden_apple", VialEffectDanger.Safe,
            "item.minecraft.string", VialEffectDanger.Safe,
            "block.minecraft.cobblestone", VialEffectDanger.Harmful,
            "item.minecraft.ender_pearl", VialEffectDanger.Harmful,
            "block.minecraft.wither_rose", VialEffectDanger.Deadly,
            "block.minecraft.wither_skeleton_skull", VialEffectDanger.Deadly,
            "item.minecraft.gold_ingot", VialEffectDanger.Destructive
//            "item.minecraft.lava_bucket",
    );
    private static final ImmutableMap<String,String> vialMessage = ImmutableMap.of(
            "item.midori_ishi.midori_ishi", "You feel relieved and rejuvinated",
            "item.midori_ishi.maxwell", "You are a monster",
            "item.minecraft.enchanted_golden_apple", "Tastes like apple juice and honey, with a slight metallic aftertaste",
            "item.minecraft.string", "Spooder",
            "block.minecraft.cobblestone", "Get cobblestoned",
            "item.minecraft.ender_pearl", "Tastes like ender pearls",
            "block.minecraft.wither_rose", "An immensly dry potion withers your insides",
            "block.minecraft.wither_skeleton_skull", "You don't feel well... You feel your insides being turned to dust",
            "item.minecraft.gold_ingot", "Greed"
    );
    private static final ImmutableMap<String, Function3<Level, LivingEntity, String, Object>> vialEffect = ImmutableMap.of(
            "item.midori_ishi.midori_ishi", (level, e, infused_item)-> {
                e.addEffect(new MobEffectInstance(MobEffects.HEAL, 20, 3));
                e.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 1));
                return null;
            },
            "item.midori_ishi.maxwell", (level, e, infused_item)-> {
                e.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CACTUS));
                e.teleportTo(0d,-67d, 0d);
                e.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100000, 0));
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
                BlockPos[] poss = {pos, pos.above(), pos.below(), pos.below(2),
                        pos.east(), pos.west(), pos.north(), pos.south(),
                        pos.below().east(), pos.below().west(), pos.below().north(), pos.below().south()};
                for(BlockPos p: poss) {
                    if (level.getBlockState(p).canBeReplaced())
                        level.setBlock(p, newState, 11);
                }
//                level.gameEvent(e, GameEvent.BLOCK_PLACE, pos);
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
        nbt.putString(NBT_TAG_NAME, infusedItem.getItem().getDescriptionId());
        currentStack.setTag(nbt);
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack finishUsingItem(ItemStack pStack, Level level, LivingEntity e) {
        if (!level.isClientSide) {
            if(pStack.hasTag() && pStack.getTag().contains(NBT_TAG_NAME)) {
                String infused_item = pStack.getTag().getString(NBT_TAG_NAME);
                if(vialRecipes.contains(infused_item)) {
                    e.sendSystemMessage(Component.literal(vialMessage.get(infused_item)));
                    vialEffect.get(infused_item).apply(level, e, infused_item);
                } else {
                    //for unimplemented items
                    e.sendSystemMessage(Component.literal("Doesn't taste well..."));
                    e.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 2));
                    e.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
                }
            } else {
                e.sendSystemMessage(Component.literal("no cheating, brew the vial yourself"));
                e.die(e.damageSources().magic());
            }
        }
        if (e instanceof Player) {
            return ItemUtils.createFilledResult(pStack, (Player) e, new ItemStack(Items.GLASS_BOTTLE));
        }
        return pStack;
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
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pStack.hasTag() && pStack.getTag().contains("infused_item")) {
            String infused_item = pStack.getTag().getString("infused_item");
            VialEffectDanger ved = vialDanger.getOrDefault(infused_item, VialEffectDanger.Harmful);
            pTooltipComponents.add(Component.translatable(infused_item).withStyle(ved.chatFormatting()));
//            pTooltipComponents.add(Component.literal(infused_item).withStyle(ChatFormatting.GRAY));
        } else {
            //no nbt data
            pTooltipComponents.add(Component.literal("JEI won't help with recipes").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}