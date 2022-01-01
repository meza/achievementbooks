package com.stateshifterlabs.achievementbooks.fabric.UI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public class HeaderLine extends DrawableHelper implements Drawable {

	private final int id;
	private final int top;
	private final int left;
	private final int width;
	private final int pageCenter;
	private final String header;
	private final TextRenderer textRenderer;

	public HeaderLine(int id, int top, int left, int width, String header, TextRenderer textRenderer) {
		this.id = id;
		this.top = top;
		this.left = left;
		this.width = width;
		this.header = header;
		this.textRenderer = textRenderer;
		this.pageCenter = left + (width/2);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int i = 0;
		List<OrderedText> lines = textRenderer.wrapLines(new LiteralText(header), width);

		for(OrderedText text: lines) {
			float lineWidth = textRenderer.getWidth(text);

			textRenderer.draw(matrices, text, pageCenter-(lineWidth/2) , top+(textRenderer.fontHeight * i++), 0);
		}


	}
}
