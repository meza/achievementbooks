package com.stateshifterlabs.achievementbooks.fabric.UI;

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

public class TextLine extends DrawableHelper implements Drawable, Element, Selectable, BookScreenElement {
	private final int top;
	private final int left;
	private final int width;
	private final int pageCenter;
	private final String description;
	private final TextRenderer textRenderer;

	public TextLine(int top, int left, int width, String description, TextRenderer textRenderer) {
		this.top = top;
		this.left = left;
		this.width = width;
		this.description = description;
		this.textRenderer = textRenderer;
		this.pageCenter = left + (width/2);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int i = 0;
		List<OrderedText> lines = textRenderer.wrapLines(new LiteralText(description), width);

		for(OrderedText text: lines) {
			float lineWidth = textRenderer.getWidth(text);
			textRenderer.draw(matrices, text, left , top+(textRenderer.fontHeight * i++), 0);
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
		return textRenderer.getWrappedLinesHeight(description, width);
	}
}
