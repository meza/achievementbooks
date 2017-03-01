package com.stateshifterlabs.achievementbooks.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import java.util.List;

import static com.stateshifterlabs.achievementbooks.client.gui.GUI.bookWidth;

public class HeaderLine extends GuiButton {

	private static final int buttonHeight = 30;
	private String header;

	public HeaderLine(int id, int x, int y, int width, String header) {
		super(id, x, y, width, buttonHeight, header);

		this.enabled = false;
		this.header = header;

		this.height = (Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(header, width).size())
					   * 8;

	}

	public static int getExpectedLines(String text, int width) {
		return Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, width).size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY) {

		FontRenderer fnt = Minecraft.getMinecraft().fontRenderer;

		int lineNum = getExpectedLines(header, width);


		List<String> lines = fnt.listFormattedStringToWidth(header, this.width);
		for (int i = 0; i < lines.size(); i++)
		{
			String s = lines.get(i);

			fnt.drawString(s, xPosition + (bookWidth / 4) - 20 - (fnt.getStringWidth(s) / 2), yPosition + (height / 2) - lineNum * 4 + i * 8,
						   0x000000, false);
		}
	}

	public int getHeight() {
		return this.height + 10;
	}

	// don't allow the element to be clickable if not an achievement
	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		return super.mousePressed(par1Minecraft, par2, par3);
	}

}
