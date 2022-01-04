package com.stateshifterlabs.achievementbooks.minecraftdependent.UI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.stateshifterlabs.achievementbooks.AchievementBooks.MODID;

public class CloseButton extends DrawableHelper implements Element, Selectable, BookScreenElement, Drawable {
    private static final Logger LOGGER = LogManager.getLogger(CloseButton.class);
    private final int height = 11;
    private final int width = 10;
    private final int left;
    private final int top;
    private Runnable onClicked;
    private MatrixStack matrices;

    public CloseButton(int left, int top) {
        this(left, top, null);
    }

    public CloseButton(int left, int top, Runnable onClicked) {
        this.left = left;
        this.top = top;
        this.onClicked = onClicked;
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
        return this.height;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.matrices = matrices;
        RenderSystem.setShaderTexture(0, new Identifier(MODID, "textures/gui/checkboxes.png"));
        if (isOver(mouseX, mouseY)) {
            drawTexture(matrices, this.left, this.top, 91, 100, this.width, this.height, 256, 256);
        } else {
            drawTexture(matrices, this.left, this.top, 106, 100, this.width, this.height, 256, 256);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        LOGGER.debug("Mouse clicked");
        if(this.onClicked != null) {
            LOGGER.debug("Click handler is set");
            if(isOver(mouseX, mouseY)) {
                LOGGER.debug("Calling close handler");
                this.onClicked.run();
                return true;
            }
        } else {
            LOGGER.debug("Click handler is NOT set");
        }
        return false;
    }

    public boolean isOver(double mouseX, double mouseY) {
        return mouseX >= (double) this.left && mouseY >= (double) this.top && mouseX < (double) (this.left + this.width) && mouseY < (double) (this.top + this.height());
    }
}
