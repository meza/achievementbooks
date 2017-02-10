package com.stateshifterlabs.achievementbooks.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;


public class DescriptionLine extends GuiButton {

	private static final int buttonHeight = 30;
	private String description;

	public DescriptionLine(int id, int x, int y, int width, String description) {
		super(id, x, y, width, buttonHeight, description);

		this.enabled = false;

		this.description = "§o" + description;

		this.height = (Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(description, width).size())
					   * 8;

	}

	public static int getExpectedLines(String text, int width) {
		return Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, width).size();
	}

	private static Offset getOffsetForPlayer(EntityPlayer player) {
		return new Offset(0, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY) {

		FontRenderer fnt = Minecraft.getMinecraft().fontRenderer;

		int lineNum = getExpectedLines(description, width);

		fnt.drawSplitString(description, xPosition, yPosition,
							this.width, 0x000000);

	}

	public int getHeight() {
		return this.height + 5;
	}

	// don't allow the element to be clickable if not an achievement
	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		return super.mousePressed(par1Minecraft, par2, par3);
	}

}
