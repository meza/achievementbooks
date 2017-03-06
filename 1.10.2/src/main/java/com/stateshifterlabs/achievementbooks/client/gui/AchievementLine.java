package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.client.AchievementElement;
import com.stateshifterlabs.achievementbooks.client.DefaultButtonSettings;
import com.stateshifterlabs.achievementbooks.common.MCThingy;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;


public class AchievementLine extends GuiButton {

	private final AchievementElement button;
	private final MCThingy stuff;
	private PageElement element;

	public AchievementLine(int id, int x, int y, int width, PageElement element) {
		super(id, x, y, width, DefaultButtonSettings.buttonHeight, element.formattedAchievement());
		this.element = element;
		stuff = new MCThingy();
		button = new AchievementElement(stuff, element, width);
		this.height = button.getButtonHeight();
	}

	public void toggle() {
		element.toggleState();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY) {
		par1Minecraft.getTextureManager().bindTexture(button.texture());

		final Colour bgColour = button.bgColour();
		GL11.glColor4f(bgColour.red(), bgColour.green(), bgColour.blue(), bgColour.alpha());

		int offsetX = button.xOffset(mouseX, mouseY, xPosition, yPosition);
		int offsetY = button.yOffset();

		drawTexturedModalRect(button.checkboxXPositon(xPosition), button.checkboxYPositon(yPosition), offsetX, offsetY,
							  button.checkboxDimension(), button.checkboxDimension());


		final Colour fgColor = button.fgColour();
		GL11.glColor4f(fgColor.red(), fgColor.green(), fgColor.blue(), fgColor.alpha());
		FontRenderer fnt = stuff.fontRenderer();

		fnt.drawSplitString(element.formattedAchievement(), button.textXPosition(xPosition), button.textYPosition(yPosition),
							button.textWrapWidth(), fgColor.rgb());

	}

	public int getHeight() {
		return this.height + 8;
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {}

}
