package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.client.DefaultButtonSettings;
import com.stateshifterlabs.achievementbooks.client.HeaderElement;
import com.stateshifterlabs.achievementbooks.common.Minecraft111Facade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class HeaderLine extends GuiButton {

	private final HeaderElement button;

	public HeaderLine(int id, int x, int y, int width, String header) {
		super(id, x, y, width, DefaultButtonSettings.buttonHeight, header);
		this.enabled = false;
		button = new HeaderElement(new Minecraft111Facade(), header, width);
		this.height = button.getButtonHeight();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY, float partialTicks) {
		button.drawButton(x, y);
	}

	public int getHeight() {
		return this.height + HeaderElement.bottomPadding;
	}

}
