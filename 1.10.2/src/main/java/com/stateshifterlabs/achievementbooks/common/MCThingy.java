package com.stateshifterlabs.achievementbooks.common;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.facades.MinecraftStuff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class MCThingy implements MinecraftStuff {


	public String getModId() {
		return AchievementBooksMod.MODID;
	}


	public FontRenderer fontRenderer() {
		return Minecraft.getMinecraft().fontRendererObj;
	}
}
