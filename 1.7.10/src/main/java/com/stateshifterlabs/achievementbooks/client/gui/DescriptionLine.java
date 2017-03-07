package com.stateshifterlabs.achievementbooks.client.gui;


import com.stateshifterlabs.achievementbooks.client.DefaultButtonSettings;
import com.stateshifterlabs.achievementbooks.client.DescriptionElement;
import com.stateshifterlabs.achievementbooks.common.MCThingy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;


public class DescriptionLine extends GuiButton {

	private final MCThingy stuff;
	private final DescriptionElement button;
	private String description;

	public DescriptionLine(int id, int x, int y, int width, String description) {
		super(id, x, y, width, DefaultButtonSettings.buttonHeight, description);

		this.enabled = false;
		this.description = description;
		stuff = new MCThingy();
		button = new DescriptionElement(stuff, description, width);

		this.height = button.getButtonHeight();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY) {
		Colour fgColour = button.fgColour();
		GL11.glColor4f(fgColour.red(), fgColour.green(), fgColour.blue(), fgColour.alpha());

		stuff.fontRenderer().drawSplitString(description, xPosition, yPosition, this.width, fgColour.rgb());

	}

	public int getHeight() {
		return this.height + DescriptionElement.bottomPadding;
	}

}
