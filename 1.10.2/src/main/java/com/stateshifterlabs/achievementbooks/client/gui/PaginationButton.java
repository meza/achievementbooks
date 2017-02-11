package com.stateshifterlabs.achievementbooks.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class PaginationButton extends GuiButton
{
	private boolean next;

	public PaginationButton(int id, int x, int y, boolean next)
	{
		super(id, x, y, 21, 21, next ? "next" : "prev");
		this.next = next;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.visible)
		{
			par1Minecraft.getTextureManager().bindTexture(AchievementLine.texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			boolean hover = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

			this.drawTexturedModalRect(this.xPosition, this.yPosition, next ? 234 : 203, hover ? 211 : 233, this.width, this.height);

			this.mouseDragged(par1Minecraft, par2, par3);
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
	{
		return super.mousePressed(par1Minecraft, par2, par3);
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {}
}
