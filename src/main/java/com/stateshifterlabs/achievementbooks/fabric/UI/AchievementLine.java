package com.stateshifterlabs.achievementbooks.fabric.UI;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.data.PageElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

interface ToggleCallback {
    void onToggleCallback(PageElement achievement);
}

@Environment(EnvType.CLIENT)
public class AchievementLine extends DrawableHelper implements Element, Selectable, BookScreenElement, Drawable {

    private static final Identifier TEXTURE = new Identifier(AchievementBooks.MODID, "textures/gui/checkboxes.png");
    private final PageElement achievement;
    private final TextLine descriptionLine;
    private final int left;
    private final TextRenderer textRenderer;
    private final int top;
    private final int width;
    private int checkboxOffset = 0;
    private Consumer<PageElement> toggleCallback;

    public AchievementLine(
            PageElement achievement,
            int top,
            int left,
            int width,
            String description,
            TextRenderer textRenderer
    ) {
        this.achievement = achievement;

        this.top = top;
        this.left = left;
        this.width = width;
        this.textRenderer = textRenderer;
        this.descriptionLine = new TextLine(top, left + 30, width - 30, description, textRenderer);

        checkboxOffset = descriptionLine.height() / 2 - 10;
        if (checkboxOffset < 0) {
            checkboxOffset = 0;
        }

    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public int height() {
        return descriptionLine.height() > 20 ? descriptionLine.height() : 20;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isValidClickButton(button) && clicked(mouseX, mouseY)) {

            this.achievement.toggleState();
            SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
            toggleCallback.accept(this.achievement);
            if (this.achievement.checked()) {
                soundManager.play(PositionedSoundInstance.master(AchievementBooks.TICK_SOUND_EVENT, 1.0f));
            } else {
                soundManager.play(PositionedSoundInstance.master(AchievementBooks.RUB_SOUND_EVENT, 1.0f));
            }
            return true;
        }
        return false;
    }

    public void onToggle(Consumer<PageElement> callback) {
        toggleCallback = callback;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        if (!achievement.checked()) {
            drawTexture(matrices, left, top + checkboxOffset, 20, 20, 0, 0, 20, 20, 256, 256);
        } else {
            drawTexture(matrices, left, top + checkboxOffset, 20, 20, 0, 20, 20, 20, 256, 256);
        }
        descriptionLine.render(matrices, mouseX, mouseY, delta);
    }

    protected boolean clicked(double mouseX, double mouseY) {
        return mouseX >= (double) this.left && mouseY >= (double) this.top && mouseX < (double) (this.left + this.width) && mouseY < (double) (this.top + this.height());
    }

    protected boolean isValidClickButton(int button) {
        return button == 0;
    }
}
