package com.stateshifterlabs.achievementbooks.client;

import com.stateshifterlabs.achievementbooks.client.gui.Colour;
import com.stateshifterlabs.achievementbooks.facades.MinecraftFacade;

public class DescriptionElement {

	private final MinecraftFacade stuff;
	private final String descriptionText;
	private final int width;
	public static int bottomPadding = 5;

	public DescriptionElement(MinecraftFacade stuff, String descriptionText, int width) {

		this.stuff = stuff;
		this.descriptionText = descriptionText;
		this.width = width;
	}

	public int getButtonHeight() {
		return (stuff.fontRenderer().listFormattedStringToWidth(descriptionText, width - DefaultButtonSettings.textPadding).size())
			   * DefaultButtonSettings.characterHeight;
	}

	public Colour fgColour() {
		return new Colour(0.0f, 0.0f, 0.0f, 1.0f);
	}
}
