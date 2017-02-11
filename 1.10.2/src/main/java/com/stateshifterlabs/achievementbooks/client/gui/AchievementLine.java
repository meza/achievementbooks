package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class AchievementLine extends GuiButton {
	static ResourceLocation texture = new ResourceLocation(AchievementBooksMod.MODID.toLowerCase(), "textures/gui/checkboxes.png");

	private static final int buttonHeight = 30;
	private PageElement element;

	public AchievementLine(int id, int x, int y, int width, PageElement element) {
		super(id, x, y, width, buttonHeight, element.formattedAchievement());
		this.element = element;

		this.height = (Minecraft.getMinecraft().fontRendererObj
							   .listFormattedStringToWidth(element.formattedAchievement(), width - 25).size()) * 8;
	}

	public static int getExpectedLines(String text, int width) {
		return Minecraft.getMinecraft().fontRendererObj.listFormattedStringToWidth(text, width).size();
	}

	public void toggle() {
		element.toggleState();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY) {
		par1Minecraft.getTextureManager().bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int offsetX = 0, offsetY = 0;

		if (element.checked()) {
			offsetY = 20;
		}

		if (mouseX >= xPosition && mouseX <= xPosition + width && mouseY >= yPosition && mouseY <= yPosition +
																								   height) {
			offsetX = 20;
		}

		drawTexturedModalRect(xPosition, yPosition + (height / 2) - 8, offsetX, offsetY, 20, 20);

		GL11.glColor4f(0F, 0F, 0F, 1.0F);
		FontRenderer fnt = Minecraft.getMinecraft().fontRendererObj;

		int lineNum = getExpectedLines(element.formattedAchievement(), width);

		// render the text according to alignment

		fnt.drawSplitString(element.formattedAchievement(), xPosition + 25, yPosition + (height / 2) - lineNum * 4,
							this.width - 25, 0x000000);

	}

	public int getHeight() {
		return this.height + 8;
	}

	// don't allow the element to be clickable if not an achievement
	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		return super.mousePressed(par1Minecraft, par2, par3);
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {}

}
