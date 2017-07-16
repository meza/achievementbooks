package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class PaginationButton extends GuiButton {
	private boolean next;
	private static ResourceLocation
			texture = new ResourceLocation(AchievementBooksMod.MODID.toLowerCase(), "textures/gui/checkboxes.png");


	public PaginationButton(int id, int x, int y, boolean next) {
		super(id, x, y, 21, 21, next ? "next" : "prev");
		this.next = next;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3, float partialTicks) {
		if (this.visible) {
			par1Minecraft.getTextureManager().bindTexture(texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			boolean hover = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width &&
							par3 < this.y + this.height;

			this.drawTexturedModalRect(this.x, this.y, next ? 234 : 203, hover ? 211 : 233, this.width,
									   this.height);

			this.mouseDragged(par1Minecraft, par2, par3);
		}
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
	}
}
