package com.stateshifterlabs.achievementbooks.client;

import com.stateshifterlabs.achievementbooks.client.gui.Colour;
import com.stateshifterlabs.achievementbooks.facades.MinecraftFacade;
import net.minecraft.client.gui.FontRenderer;

import java.util.List;

public class HeaderElement {

	public static int bottomPadding = 10;
	private final MinecraftFacade stuff;
	private final String headerText;
	private final int width;

	public HeaderElement(MinecraftFacade stuff, String headerText, int width) {

		this.stuff = stuff;
		this.headerText = headerText;
		this.width = width;
	}

	public int getButtonHeight() {
		return (stuff.fontRenderer().listFormattedStringToWidth(headerText, width - DefaultButtonSettings.textPadding)
					 .size()) * DefaultButtonSettings.characterHeight;
	}

	private Colour fgColour() {
		return new Colour(0.0f, 0.0f, 0.0f, 1.0f);
	}

	private int getExpectedLines() {
		return stuff.fontRenderer().listFormattedStringToWidth(headerText, width).size();
	}

	public void drawButton(int xPosition, int yPosition) {
		FontRenderer fnt = stuff.fontRenderer();
		int lineNum = getExpectedLines();
		int height = getButtonHeight();

		List<String> lines = fnt.listFormattedStringToWidth(headerText, width);
		for (int i = 0; i < lines.size(); i++) {
			String s = lines.get(i);

			fnt.drawString(s, xPosition + (BookSettings.bookWidth / 4) - 20 - (fnt.getStringWidth(s) / 2),
						   yPosition + (height / 2) - lineNum * 4 + i * DefaultButtonSettings.characterHeight,
						   fgColour().rgb(), false);
		}

	}
}
