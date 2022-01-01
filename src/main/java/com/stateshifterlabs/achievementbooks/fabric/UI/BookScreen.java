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

@Environment(value= EnvType.CLIENT)
public class BookScreen extends Screen {

    private Book book;
    private World world;
    private PlayerEntity player;
    public static int bookWidth = 417;
    public static int bookHeight = 245;
    public static int pageWidth = bookWidth/2 - 20;
    private int contentLeft = 0;
    private int contentTop = 0;
    public int bookLeft = 0;
    public int bookTop = 0;
    private boolean isOpen = false;
    private int currentPage = 0;
    private ButtonWidget.PressAction nullAction = new ButtonWidget.PressAction() {
        @Override
        public void onPress(ButtonWidget button) {
            return;
        }
    };


    public BookScreen(Book book, World world, PlayerEntity player) {
        super(new LiteralText(book.name()));
        this.book = book;
        this.world = world;
        this.player = player;

    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.bookLeft = (this.width - bookWidth) / 2;
        this.bookTop = (int) ((this.height - bookHeight) / 2.5);
        this.contentLeft = bookLeft + 20;
        this.contentTop = bookTop + 20;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0,  new Identifier(AchievementBooks.MODID, "textures/gui/bookgui_left-"+book.colour()+".png"));
        drawTexture(matrices, bookLeft, bookTop, 0, 0, bookWidth / 2, bookHeight);

        RenderSystem.setShaderTexture(0, new Identifier(AchievementBooks.MODID, "textures/gui/bookgui_right-"+book.colour()+".png"));
        drawTexture(matrices, bookLeft + bookWidth / 2, bookTop, 0, 0, bookWidth / 2, bookHeight);
    }

    private void drawPaginators() {
        this.addDrawableChild(new PageTurnWidget(contentLeft, bookTop+bookHeight - 30, false, nullAction, true));
        this.addDrawableChild(new PageTurnWidget(bookLeft + bookWidth - 50, bookTop+bookHeight - 30, true, nullAction, true));
    }

    private void drawPage(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Page page = book.openPage(0);
        for(PageElement pageElement: page.elements()) {
            if (pageElement.type() == Type.HEADER) {
                HeaderElementComposite hc = new HeaderElementComposite(
                        1,
                        pageElement,
                        contentTop,
                        contentLeft,
                        pageWidth,
                        textRenderer);
                this.addDrawableChild(hc);
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawBackground(matrices, delta, mouseX, mouseY);
        drawPaginators();
        drawPage(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        world.playSound(player, player.getBlockPos(), AchievementBooks.CLOSE_BOOK_SOUND_EVENT, SoundCategory.BLOCKS, 0.25f, 1.0f);
        this.client.setScreen(null);
    }

    @Override
    public void init() {
        if (!isOpen) {
            world.playSound(player, player.getBlockPos(), AchievementBooks.OPEN_BOOK_SOUND_EVENT, SoundCategory.PLAYERS, 0.25f, 1.0f);
            this.isOpen = true;
        }
    }
}
