package com.stateshifterlabs.achievementbooks.fabric.UI;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CenteredTextLine extends DrawableHelper implements Drawable, Element, Selectable, BookScreenElement {

    private final int top;
    private final int left;
    private final int width;
    private final int pageCenter;
    private final String header;
    private final TextRenderer textRenderer;

    public CenteredTextLine(int top, int left, int width, String header, TextRenderer textRenderer) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.header = header;
        this.textRenderer = textRenderer;
        this.pageCenter = left + (width / 2);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int i = 0;
        List<OrderedText> lines = textRenderer.wrapLines(new LiteralText(header), width);

        for (OrderedText text : lines) {
            float lineWidth = textRenderer.getWidth(text);

            textRenderer.draw(matrices, text, pageCenter - (lineWidth / 2), top + (textRenderer.fontHeight * i++), 0);
        }


    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public int height() {
        return textRenderer.getWrappedLinesHeight(header, width);
    }
}
