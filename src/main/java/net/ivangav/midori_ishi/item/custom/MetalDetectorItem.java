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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if(!context.getLevel().isClientSide()) {
            BlockPos pos = context.getClickedPos();
            Direction dir = context.getClickedFace();
            Player player = context.getPlayer();
            boolean found = false;

            context.getLevel().playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSound.MIDORI_SCANNER_USE.get(),
                    SoundSource.BLOCKS, 1f, 1f, 0);

            for(int i = 1; i <= 32; i++) {
                BlockState state = context.getLevel().getBlockState(pos.relative(dir, -i));
                if(isValuable(state)) {
                    found = true;
                    player.sendSystemMessage(Component.literal("Bzzt... "+ I18n.get(state.getBlock().getDescriptionId())));
                    break;
                }
            }

            if(!found) {
                player.sendSystemMessage(Component.literal("Bzzt..."));
            }

            context.getItemInHand().hurtAndBreak(1, context.getPlayer(),
                    p -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.midori_ishi.midori_scanner.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private boolean isValuable(BlockState state) {
        return state.is(Tags.Blocks.ORES);
    }
}
