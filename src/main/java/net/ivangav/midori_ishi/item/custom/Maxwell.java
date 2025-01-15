package net.ivangav.midori_ishi.item.custom;

import net.ivangav.midori_ishi.sound.ModSound;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Maxwell extends RecordItem {
    public Maxwell(Properties properties) {
        super(1, ModSound.MAXWELL_THEME, properties, 6320);
    }

//    @Override
//    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
//        pTooltipComponents.add(Component.translatable("tooltip.midori_ishi.midori_scanner.tooltip").withStyle(ChatFormatting.GRAY));
//        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
//    }
}
