package net.ivangav.midori_ishi.item.custom;

import net.ivangav.midori_ishi.screen.MidoriBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MidoriBookItem extends Item {
    public MidoriBookItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack item = pPlayer.getItemInHand(pHand);

        if(pLevel.isClientSide) {
            Minecraft.getInstance().setScreen(new MidoriBookScreen());
        }

        return InteractionResultHolder.sidedSuccess(item, pLevel.isClientSide());
    }
}
