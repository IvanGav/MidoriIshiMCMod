package net.ivangav.midori_ishi.screen;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.ivangav.midori_ishi.block.entity.MidoriTransmogrificationTableEntity.BREW_DURATION;
import static net.ivangav.midori_ishi.block.entity.MidoriTransmogrificationTableEntity.MAX_FUEL;

@OnlyIn(Dist.CLIENT)
public class MidoriTransmogrificationTableScreen extends AbstractContainerScreen<MidoriTransmogrificationTableMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MidoriIshiMod.MOD_ID, "textures/gui/midori_transmogrification_table_gui.png");
    private static final int[] BUBBLE_LENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};

    public MidoriTransmogrificationTableScreen(MidoriTransmogrificationTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int fuel = this.menu.getFuel();
        int fuelHeight = Mth.clamp((18 * fuel + MAX_FUEL - 1) / MAX_FUEL, 0, 18);
        if (fuelHeight > 0) {
            pGuiGraphics.blit(TEXTURE, i + 60, j + 44, 176, 29, fuelHeight, 4);
        }

        int brewTime = this.menu.getBrewingTicks();
        if (brewTime > 0) {
            int brewTimeHeight = (int)(28.0F * (1.0F - (float)brewTime / BREW_DURATION));
            if (brewTimeHeight > 0) {
                pGuiGraphics.blit(TEXTURE, i + 97, j + 16, 176, 0, 9, brewTimeHeight);
            }

            brewTimeHeight = BUBBLE_LENGTHS[brewTime / 2 % 7];
            if (brewTimeHeight > 0) {
//                pGuiGraphics.blit(TEXTURE, i + 63, j + 14 + 29 - brewTimeHeight, 185, 29 - brewTimeHeight, 12, brewTimeHeight);
                pGuiGraphics.blit(TEXTURE, i + 63, j + 14, 185, 0, 12, brewTimeHeight);
            }
        }

    }
}
