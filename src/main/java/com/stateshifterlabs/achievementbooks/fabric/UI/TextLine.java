package com.stateshifterlabs.achievementbooks.fabric.UI;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TextLine implements Drawable, Element, Selectable, BookScreenElement {
    private final String description;
    private final int left;
    private final int pageCenter;
    private final TextRenderer textRenderer;
    private final int top;
    private final int width;
    private boolean focused;

    public TextLine(int top, int left, int width, String description, TextRenderer textRenderer) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.description = description;
        this.textRenderer = textRenderer;
        this.pageCenter = left + (width / 2);
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
        return textRenderer.getWrappedLinesHeight(description, width);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = 0;
        List<OrderedText> lines = textRenderer.wrapLines(Text.of(description), width);

        for (OrderedText text : lines) {
            context.drawText(textRenderer, text, left, top + (textRenderer.fontHeight * i++), 0, false);
        }

    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }
}
