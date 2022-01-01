package com.stateshifterlabs.achievementbooks.fabric.UI;

import com.stateshifterlabs.achievementbooks.core.data.PageElement;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.util.math.MatrixStack;

public class HeaderElementComposite extends DrawableHelper implements Drawable, Element, Selectable {

	private final int padding = 10;
	private int id;
	private final PageElement element;
	private final int top;
	private final int left;
	private final int maxWidth;
	private TextRenderer textRenderer;
	private int height = 0;

	public HeaderElementComposite(int id, PageElement element, int top, int left, int maxWidth, TextRenderer textRenderer) {
		this.id = id;
		this.element = element;
		this.top = top;
		this.left = left;
		this.maxWidth = maxWidth;
		this.textRenderer = textRenderer;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		HeaderLine header = new HeaderLine(id, top, left, maxWidth, element.formattedHeader(), textRenderer);
		header.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public SelectionType getType() {
		return SelectionType.NONE;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}
}
