package com.stateshifterlabs.achievementbooks.fabric.UI;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BookScreen extends Screen {

    private Book book;
    private World world;
    private PlayerEntity player;
    public static int bookWidth = 417;
    public static int bookHeight = 245;
    private int oldLeft = 0;
    private int oldTop = 0;

    public BookScreen(Book book, World world, PlayerEntity player) {
        super(new LiteralText(book.name()));
        this.book = book;
        this.world = world;
        this.player = player;
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int bookLeft = (this.width - bookWidth) / 2;
        int bookTop = (int) ((this.height - bookHeight) / 2.5);

        if (oldLeft != bookLeft || oldTop != bookTop) {
            oldLeft = bookLeft;
            oldTop = bookTop;
            //initGui(bookTop, bookLeft);
        }

        RenderSystem.setShaderTexture(0,  new Identifier(AchievementBooks.MODID, "textures/gui/bookgui_left-"+book.colour()+".png"));
        drawTexture(matrices, bookLeft, bookTop, 0, 0, bookWidth / 2, bookHeight);

        RenderSystem.setShaderTexture(0, new Identifier(AchievementBooks.MODID, "textures/gui/bookgui_right-"+book.colour()+".png"));
        drawTexture(matrices, bookLeft + bookWidth / 2, bookTop, 0, 0, bookWidth / 2, bookHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawBackground(matrices, delta, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        world.playSound(player, player.getBlockPos(), AchievementBooks.CLOSE_BOOK_SOUND_EVENT, SoundCategory.BLOCKS, 0.25f, 1.0f);
        this.client.setScreen(null);
    }
    @Override
    public void init() {
        world.playSound(player, player.getBlockPos(), AchievementBooks.OPEN_BOOK_SOUND_EVENT, SoundCategory.PLAYERS, 0.25f, 1.0f);
    }
}
