package com.stateshifterlabs.achievementbooks.client;

import com.stateshifterlabs.achievementbooks.client.gui.Colour;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import com.stateshifterlabs.achievementbooks.facades.MinecraftFacade;
import net.minecraft.util.ResourceLocation;

public class AchievementElement {
	private final ResourceLocation texture;
	private final MinecraftFacade stuff;
	private final PageElement element;
	private int width;

	public AchievementElement(MinecraftFacade stuff, PageElement element, int width) {
		texture = new ResourceLocation(stuff.getModId().toLowerCase(), "textures/gui/checkboxes.png");
		this.stuff = stuff;
		this.element = element;
		this.width = width;
	}

	public int getButtonHeight() {
		return (stuff.fontRenderer().listFormattedStringToWidth(element.formattedAchievement(), width - DefaultButtonSettings.textPadding).size())
			   * DefaultButtonSettings.characterHeight;
	}

	public int getExpectedLines() {
		return stuff.fontRenderer().listFormattedStringToWidth(element.formattedAchievement(), width).size();
	}

	public ResourceLocation texture() {
		return texture;
	}

	public Colour bgColour() {
		return new Colour(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public int yOffset() {
		if (element.checked()) {
			return 20;
		}
		return 0;
	}

	public int xOffset(int mouseX, int mouseY, int xPosition, int yPosition) {
		if (mouseX >= xPosition && mouseX <= xPosition + width && mouseY >= yPosition &&
			mouseY <= yPosition + getButtonHeight()) {
			return 20;
		}
		return 0;
	}

	public int checkboxYPositon(int yPosition) {
		return yPosition + (getButtonHeight() / 2) - 8;
	}

	public int checkboxXPositon(int xPosition) {
		return xPosition;
	}

	public int checkboxDimension() {
		return 20;
	}

	public int textXPosition(int xPosition) {
		return xPosition + 25;
	}

	public int textYPosition(int yPosition) {
		return yPosition + (getButtonHeight() / 2) - getExpectedLines() * 4;
	}

	public int textWrapWidth() {
		return this.width - 25;
	}

	public Colour fgColour() {
		return new Colour(0.0F, 0.0F, 0.0F, 1.0F);
	}
}
