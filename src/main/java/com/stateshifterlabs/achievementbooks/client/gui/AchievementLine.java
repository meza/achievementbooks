package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class AchievementLine extends GuiButton {
	static final ResourceLocation texture =
			new ResourceLocation(AchievementBooksMod.MODID.toLowerCase(), "textures/gui/checkboxes.png");
	private static final int buttonHeight = 30;
	private String achievement;
	private boolean checked = false;

	public AchievementLine(int id, int x, int y, int width, String achievement) {
		super(id, x, y, width, buttonHeight, achievement);
		this.achievement = achievement;
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(String.format("shit")));

		this.height =
				(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(achievement, width - 25).size()) * 8;
	}

	public static int getExpectedLines(String text, int width) {
		return Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, width).size();
	}

	private static Offset getOffsetForPlayer(EntityPlayer player) {
		return new Offset(0, 0);
	}

	public void toggle() {
		checked = !checked;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft par1Minecraft, int mouseX, int mouseY) {
		par1Minecraft.renderEngine.bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int offsetX = 0, offsetY = 0;

		if (checked) {
			offsetY = 20;
		}

		if (mouseX >= xPosition && mouseX <= xPosition + width && mouseY >= yPosition && mouseY <= yPosition +
																								   height) {
			offsetX = 20;
		}

		Offset offset = getOffsetForPlayer(Minecraft.getMinecraft().thePlayer);
		offsetX += offset.x * 40;
		offsetY += offset.y * 40;

		drawTexturedModalRect(xPosition, yPosition + (height / 2) - 8, offsetX, offsetY, 20, 20);

		FontRenderer fnt = Minecraft.getMinecraft().fontRenderer;
		int lineNum = getExpectedLines(achievement, width);

		// render the text according to alignment

		fnt.drawSplitString(achievement, xPosition + 25, yPosition + (height / 2) - lineNum * 4, this.width - 25,
							0x000000);

	}

	public int getHeight() {
		return this.height + 8;
	}

	// don't allow the element to be clickable if not an achievement
	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		return super.mousePressed(par1Minecraft, par2, par3);
	}

}
