package com.stateshifterlabs.achievementbooks.fabric.UI;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Page;
import com.stateshifterlabs.achievementbooks.core.data.PageElement;
import com.stateshifterlabs.achievementbooks.core.data.Type;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Environment(value = EnvType.CLIENT)
public class BookScreen extends Screen {

    private Book book;
    private World world;
    private PlayerEntity player;
    public static int bookWidth = 417;
    public static int bookHeight = 245;
    public static int pageWidth = bookWidth / 2 - 35;
    private float lineSeparatorHeight = 10;
    private float paragraphSeparatorHeight = 20;
    private int contentLeft = 0;
    private int rightPageLeft = 0;
    private int contentTop = 0;
    public int bookLeft = 0;
    public int bookTop = 0;
    private boolean isOpen = false;
    private int currentPage = 0;
    private int cachedPage = 0;
    private ButtonWidget.PressAction turnForward = new ButtonWidget.PressAction() {
        @Override
        public void onPress(ButtonWidget button) {
            currentPage = currentPage+2;
        }
    };
    private ButtonWidget.PressAction turnBackward = new ButtonWidget.PressAction() {
        @Override
        public void onPress(ButtonWidget button) {
            currentPage = currentPage-2;
        }
    };


    public BookScreen(Book book, World world, PlayerEntity player) {
        super(new LiteralText(book.name()));
        this.book = book;
        this.world = world;
        this.player = player;

    }

    protected void drawBackground(MatrixStack matrices) {
        this.bookLeft = (this.width - bookWidth) / 2;
        this.bookTop = (int) ((this.height - bookHeight) / 2.5);
        this.contentLeft = bookLeft + 20;
        this.contentTop = bookTop + 20;
        this.rightPageLeft = (this.width/2) + 15;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, new Identifier(AchievementBooks.MODID, "textures/gui/bookgui_left-" + book.colour() + ".png"));
        drawTexture(matrices, bookLeft, bookTop, 0, 0, bookWidth / 2, bookHeight);

        RenderSystem.setShaderTexture(0, new Identifier(AchievementBooks.MODID, "textures/gui/bookgui_right-" + book.colour() + ".png"));
        drawTexture(matrices, bookLeft + bookWidth / 2, bookTop, 0, 0, bookWidth / 2, bookHeight);
    }

    private void drawPaginators() {
        if (currentPage >= 2) {
            this.addDrawableChild(new PageTurnWidget(contentLeft, bookTop + bookHeight - 30, false, turnBackward, true));
        }
        if (currentPage + 1 < book.pageCount()) {
            this.addDrawableChild(new PageTurnWidget(bookLeft + bookWidth - 50, bookTop + bookHeight - 30, true, turnForward, true));
        }
    }

    private void drawPages() {
        if (cachedPage != currentPage) {
            this.clearChildren();
            cachedPage = currentPage;
        }
        Page leftPage = book.openPage(currentPage);
        drawPage(leftPage, contentLeft);

        if (currentPage + 1 < book.pageCount()) {
            Page rightPage = book.openPage(currentPage+1);
            drawPage(rightPage, rightPageLeft);
        }
    }

    private void drawPage(Page page, int leftMargin) {
        int heightOffset = 0;
        for (PageElement pageElement : page.elements()) {
            if (pageElement.type() == Type.HEADER) {
                CenteredTextLine centeredTextLine = new CenteredTextLine(
                        contentTop,
                        leftMargin,
                        pageWidth,
                        pageElement.formattedHeader(),
                        textRenderer
                );
                this.addDrawableChild(centeredTextLine);
                heightOffset += centeredTextLine.height();

                if (pageElement.hasDescription()) {

                    heightOffset+=lineSeparatorHeight;

                    TextLine textLine = new TextLine(
                            contentTop+heightOffset,
                            leftMargin,
                            pageWidth,
                            pageElement.formattedDescription(),
                            textRenderer
                    );
                    this.addDrawableChild(textLine);
                    heightOffset += textLine.height();
                }

                heightOffset+=paragraphSeparatorHeight;
            }

            if (pageElement.type() == Type.TEXT) {
                TextLine textLine = new TextLine(
                        contentTop+heightOffset,
                        leftMargin,
                        pageWidth,
                        pageElement.formattedDescription(),
                        textRenderer
                );
                this.addDrawableChild(textLine);
                heightOffset += textLine.height()+paragraphSeparatorHeight;
            }

            if (pageElement.type() == Type.ACHIEVEMENT) {
                AchievementLine cb = new AchievementLine(
                        pageElement,
                        contentTop+heightOffset,
                        leftMargin,
                        pageWidth,
                        pageElement.formattedAchievement(),
                        textRenderer);
                this.addDrawableChild(cb);
                heightOffset += cb.height()+paragraphSeparatorHeight;
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawBackground(matrices);
        drawPaginators();
        drawPages();
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        world.playSound(player, player.getBlockPos(), AchievementBooks.CLOSE_BOOK_SOUND_EVENT, SoundCategory.BLOCKS, 0.2f, 1.0f);
        this.client.setScreen(null);
    }

    @Override
    public void init() {
        if (!isOpen) {
            world.playSound(player, player.getBlockPos(), AchievementBooks.OPEN_BOOK_SOUND_EVENT, SoundCategory.PLAYERS, 0.2f, 1.0f);
            this.isOpen = true;
        }
    }
}