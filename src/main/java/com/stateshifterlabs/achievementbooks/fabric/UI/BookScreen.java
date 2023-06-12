package com.stateshifterlabs.achievementbooks.fabric.UI;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Page;
import com.stateshifterlabs.achievementbooks.core.data.PageElement;
import com.stateshifterlabs.achievementbooks.core.data.Type;
import com.stateshifterlabs.achievementbooks.core.events.BookEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(value = EnvType.CLIENT)
public class BookScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger(BookScreen.class);
    public static int bookHeight = 245;
    public static int bookWidth = 417;
    public static int pageWidth = bookWidth / 2 - 35;
    private final Book book;
    private final float lineSeparatorHeight = 10;
    private final float paragraphSeparatorHeight = 20;
    private final PlayerEntity player;
    private final World world;
    public int bookLeft = 0;
    public int bookTop = 0;
    private int cachedPage = -1;
    private int contentLeft = 0;
    private int contentTop = 0;
    private int currentPage = 0;
    private PageTurnWidget nextPageButton;
    private PageTurnWidget previousPageButton;
    private final ButtonWidget.PressAction turnForward = new ButtonWidget.PressAction() {
        @Override
        public void onPress(ButtonWidget button) {
            currentPage = currentPage + 2;
            BookEvents.PAGE_TURN.invoker().onPageTurn(currentPage, book);
            onPageTurn();
        }
    };
    private final ButtonWidget.PressAction turnBackward = new ButtonWidget.PressAction() {
        @Override
        public void onPress(ButtonWidget button) {
            currentPage = currentPage - 2;
            onPageTurn();
        }
    };
    private boolean isOpen = false;
    private int rightPageLeft = 0;

    public BookScreen(Book book, World world, PlayerEntity player) {
        super(Text.of(book.name()));
        this.book = book;
        this.world = world;
        this.player = player;
        this.currentPage = getCurrentPage();
    }

    @Override
    public void init() {
        if (!isOpen) {
            world.playSound(player, player.getBlockPos(), AchievementBooks.OPEN_BOOK_SOUND_EVENT, SoundCategory.PLAYERS, 0.2f, 1.0f);
            this.isOpen = true;
            drawCloseButton();
        }
    }

    private void onPageTurn() {
        drawPages();
        this.updatePageButtons();
        // Fire event for client comms
        BookEvents.PAGE_TURN.invoker().onPageTurn(currentPage, book);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        if (this.cachedPage < 0) {
            drawPages();
        }
        super.render(context, mouseX, mouseY, delta);
    }

    private int getCurrentPage() {
        ItemStack itemStack = player.getStackInHand(player.getActiveHand());
        int pageOffset = itemStack.getOrCreateNbt().getInt(AchievementBooks.MODID + ":" + book.itemName() + ":pageOffset");
        if (pageOffset < 0) {
            pageOffset = 0;
        }
        return pageOffset;
    }

    private void saveCurrentPage() {
        ItemStack itemStack = player.getStackInHand(player.getActiveHand());
        itemStack.getOrCreateNbt().putInt(AchievementBooks.MODID + ":" + book.itemName() + ":pageOffset", this.currentPage);
    }

    @Override
    public void close() {
        if (this.client != null) {
            if (this.client.currentScreen != null) {
                saveCurrentPage();
                world.playSound(player, player.getBlockPos(), AchievementBooks.CLOSE_BOOK_SOUND_EVENT, SoundCategory.BLOCKS, 0.2f, 1.0f);
                this.client.setScreen(null);
            }
        }
    }

    private void drawPage(Page page, int leftMargin) {
        int heightOffset = 0;
        for (PageElement pageElement : page.elements()) {
            if (pageElement.type() == Type.HEADER) {
                CenteredTextLine centeredTextLine = new CenteredTextLine(
                        contentTop + heightOffset,
                        leftMargin,
                        pageWidth,
                        pageElement.formattedHeader(),
                        textRenderer
                );
                this.addDrawableChild(centeredTextLine);
                heightOffset += centeredTextLine.height();

                if (pageElement.hasDescription()) {

                    heightOffset += lineSeparatorHeight;

                    TextLine textLine = new TextLine(
                            contentTop + heightOffset,
                            leftMargin,
                            pageWidth,
                            pageElement.formattedDescription(),
                            textRenderer
                    );
                    this.addDrawableChild(textLine);
                    heightOffset += textLine.height();
                }

                heightOffset += paragraphSeparatorHeight;
            }

            if (pageElement.type() == Type.TEXT) {
                TextLine textLine = new TextLine(
                        contentTop + heightOffset,
                        leftMargin,
                        pageWidth,
                        pageElement.formattedDescription(),
                        textRenderer
                );
                this.addDrawableChild(textLine);
                heightOffset += textLine.height() + paragraphSeparatorHeight;
            }

            if (pageElement.type() == Type.ACHIEVEMENT) {
                AchievementLine cb = new AchievementLine(
                        pageElement,
                        contentTop + heightOffset,
                        leftMargin,
                        pageWidth,
                        pageElement.formattedAchievement(),
                        textRenderer);

                cb.onToggle((toggled) -> {
                    BookEvents.ACHIEVEMENT_TOGGLE.invoker().onAchievementToggled(pageElement.id(), book);
                });

                this.addDrawableChild(cb);
                heightOffset += cb.height() + paragraphSeparatorHeight;
            }
        }
    }

    private void drawPages() {
        if (cachedPage == currentPage) {
            return;
        }
        cachedPage = currentPage;
        this.clearChildren();

        Page leftPage = book.openPage(currentPage);
        drawPage(leftPage, contentLeft);

        if (currentPage + 1 < book.pageCount()) {
            Page rightPage = book.openPage(currentPage + 1);
            drawPage(rightPage, rightPageLeft);
        }

        this.drawPaginators();
        this.drawCloseButton();

    }

    private void drawCloseButton() {
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).dimensions(this.width / 2 - 100, bookTop + bookHeight, 200, 20).build());
    }

    private void drawPaginators() {
        int originalHeight = bookTop + bookHeight - 30;
        int originalLeftA = contentLeft;
        int originalLeftB = bookLeft + bookWidth - 50;
        int top = originalHeight;
        this.previousPageButton = this.addDrawableChild(
                new PageTurnWidget(originalLeftA, top, false, turnBackward, true)
        );
        this.nextPageButton = this.addDrawableChild(
                new PageTurnWidget(originalLeftB, top, true, turnForward, true)
        );
        this.updatePageButtons();
    }

    private void updatePageButtons() {
        this.nextPageButton.visible = this.currentPage < this.book.pageCount() - 1;
        this.previousPageButton.visible = this.currentPage > 0;
    }

    protected void drawBackground(DrawContext context) {
        this.renderBackground(context);
        this.bookLeft = (this.width - bookWidth) / 2;
        this.bookTop = (int) ((this.height - bookHeight) / 2.5);
        this.contentLeft = bookLeft + 20;
        this.contentTop = bookTop + 20;
        this.rightPageLeft = (this.width / 2) + 15;
        Identifier bookLeftCover = new Identifier(AchievementBooks.MODID, "textures/gui/bookgui_left-" + book.colour() + ".png");
        Identifier bookRightCover = new Identifier(AchievementBooks.MODID, "textures/gui/bookgui_right-" + book.colour() + ".png");
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        context.drawTexture(bookLeftCover, bookLeft, bookTop, 0, 0, bookWidth / 2, bookHeight);
        context.drawTexture(bookRightCover, bookLeft + bookWidth / 2, bookTop, 0, 0, bookWidth / 2, bookHeight);
    }

}
