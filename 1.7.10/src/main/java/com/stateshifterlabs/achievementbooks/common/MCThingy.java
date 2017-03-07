package com.stateshifterlabs.achievementbooks.common;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.facade.MCPlayer;
import com.stateshifterlabs.achievementbooks.facades.MinecraftStuff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class MCThingy implements MinecraftStuff {


	public String getModId() {
		return AchievementBooksMod.MODID;
	}


	public FontRenderer fontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public MCPlayer getPlayer() {
		return new MCPlayer(Minecraft.getMinecraft().thePlayer);
	}
}
