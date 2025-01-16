package net.ivangav.midori_ishi.item.custom;

import net.ivangav.midori_ishi.block.entity.MidoriInfusionTableEntity;
import net.ivangav.midori_ishi.block.entity.ModBlockEntities;
import net.ivangav.midori_ishi.screen.ModMenuTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class MidoriBookItem extends Item {
    public MidoriBookItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack item = pPlayer.getItemInHand(pHand);
//        pPlayer.openItemGui(item, pHand);
//        NetworkHooks.openScreen((ServerPlayer) pPlayer, (MidoriInfusionTableEntity) blockentity, pPos);
        NetworkHooks.openScreen((ServerPlayer) pPlayer, (MenuProvider) ModBlockEntities.MIDORI_INFUSION_TABLE_ENTITY.get());
//        new MenuProvider();
        return InteractionResultHolder.sidedSuccess(item, pLevel.isClientSide());
    }
}
