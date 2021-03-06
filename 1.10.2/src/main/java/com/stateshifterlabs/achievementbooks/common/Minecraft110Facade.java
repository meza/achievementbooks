package com.stateshifterlabs.achievementbooks.common;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.facade.MCPlayer;
import com.stateshifterlabs.achievementbooks.facades.MinecraftFacade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Minecraft110Facade implements MinecraftFacade {


	public String getModId() {
		return AchievementBooksMod.MODID;
	}


	public FontRenderer fontRenderer() {
		return Minecraft.getMinecraft().fontRendererObj;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public MCPlayer getPlayer() {
		return MCPlayer.fromEntity(Minecraft.getMinecraft().thePlayer);
	}

}
