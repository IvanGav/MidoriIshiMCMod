package net.ivangav.midori_ishi.screen;

import net.ivangav.midori_ishi.MidoriIshiMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class MidoriBookScreen extends Screen {
    public static final List<String> all_pages = List.of(
            "Hi! Don't think too hard about how you get a nice and comprehensive guide out of a book and a little piece of Midori Ishi. You know, we're quite magical.",
            "Anyway, you can craft 3 things using us!\n- Stick + Midori Ishi = Scanner\n- Crafting Table + Midori Ishi = Cool Green Table\n- Brewing Stand + Midori Ishi = Cooler Brewing Stand",
            "Midori Transmogrification Table (aka the Cool Green Table) will let you morph some amount of Midori Ishi into anything you want! Pretty cool, right? But we can't be made into containers, sadly. So no Shulker Boxes or Backpacks please!",
            "Midori Infusion Table (aka the Cooler Brewing Stand) will let you make a potion out of ANY ITEM YOU WANT! Exciting! Just make sure not to put anything too dangerous... Some effects might be... deadly.",
            "In fact, you can easily notice if you should drink it or not! Here:\n- Green = Nice and Pleasant\n- Orange = Will hurt a bit\n- Red = Try to get better armor\n- Purple = Don't drink near dear places or items\n- Black = Oh uh... How?",
            "Many infused items are just gonna give you a stomachache. But some will have cool effects! If you wanna discover them by yourself - nice! If not - go to the next page.",
            "=(",
            "Alright alright, here's the cool items list:",
            "Midori Ishi, Cobblestone, Anvil, Gold Ingot, Bone Meal, Ender Pearl, Wither Rose, Enchanted Golden Apple, String, Slime Block, Wither Skelly Skull"
    );

    public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
    public static final int PAGE_TEXT_X_OFFSET = 36;
    public static final int PAGE_TEXT_Y_OFFSET = 30;
    public static final ResourceLocation BOOK_LOCATION = new ResourceLocation(MidoriIshiMod.MOD_ID, "textures/gui/midori_book_gui.png");
    protected static final int TEXT_WIDTH = 114;
    protected static final int TEXT_HEIGHT = 128;
    protected static final int IMAGE_WIDTH = 192;
    protected static final int IMAGE_HEIGHT = 192;
    private final BookAccess bookAccess;
    private int currentPage;
    private List<FormattedCharSequence> cachedPageComponents;
    private int cachedPage;
    private Component pageMsg;
    private PageButton forwardButton;
    private PageButton backButton;
    private final boolean playTurnSound;

    public MidoriBookScreen() {
        super(Component.translatable("item.midori_ishi.midori_book"));
        this.cachedPageComponents = Collections.emptyList();
        this.cachedPage = -1;
        this.pageMsg = CommonComponents.EMPTY;
        this.bookAccess = new BookAccess();
        this.playTurnSound = true;

//        this.createMenuControls();
//        this.createPageControlButtons();
    }

    @Override
    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
        MidoriIshiMod.LOGGER.debug("--Midori: this.width={}",this.width);
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).bounds(this.width / 2 - 100, 196, 200, 20).build());
    }

    protected void createPageControlButtons() {
        int $$0 = (this.width - 192) / 2;
        this.forwardButton = this.addRenderableWidget(new PageButton($$0 + 116, 159, true, (p_98297_) -> this.pageForward(), this.playTurnSound));
        this.backButton = this.addRenderableWidget(new PageButton($$0 + 43, 159, false, (p_98287_) -> this.pageBack(), this.playTurnSound));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        return this.bookAccess.getPageCount();
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }
        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }
        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        int $$4 = (this.width - 192) / 2;
        Objects.requireNonNull(this.font);
        pGuiGraphics.blit(BOOK_LOCATION, $$4, 2, 0, 0, 192, 192);
        if (this.cachedPage != this.currentPage) {
            FormattedText $$6 = this.bookAccess.getPage(this.currentPage);
            this.cachedPageComponents = this.font.split($$6, 114);
            this.pageMsg = Component.translatable("book.pageIndicator", new Object[]{this.currentPage + 1, Math.max(this.getNumPages(), 1)});
        }

        this.cachedPage = this.currentPage;
        int $$7 = this.font.width(this.pageMsg);
        pGuiGraphics.drawString(this.font, this.pageMsg, $$4 - $$7 + 192 - 44, 18, 0, false);
        int $$8 = Math.min(128 / 9, this.cachedPageComponents.size());

        for(int $$9 = 0; $$9 < $$8; ++$$9) {
            FormattedCharSequence $$10 = this.cachedPageComponents.get($$9);
            Font var10001 = this.font;
            int var10003 = $$4 + 36;
            pGuiGraphics.drawString(var10001, $$10, var10003, 32 + $$9 * 9, 0, false);
        }
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @OnlyIn(Dist.CLIENT)
    public static class BookAccess {
        private final List<String> pages;

        public BookAccess() {
            this.pages = all_pages;
        }

        public FormattedText getPage(int pPage) {
            return pPage >= 0 && pPage < this.getPageCount() ? this.getPageRaw(pPage) : FormattedText.EMPTY;
        }

        public int getPageCount() {
            return this.pages.size();
        }

        public FormattedText getPageRaw(int pIndex) {
            String $$1 = this.pages.get(pIndex);
            return FormattedText.of($$1);
        }
    }
}
