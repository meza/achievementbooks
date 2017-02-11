package com.stateshifterlabs.achievementbooks.facade;

import net.minecraft.client.Minecraft;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class MCSound implements Sound {

	public void openBook() {
		Minecraft.getMinecraft().thePlayer.playSound(MODID+":open_book", (float) 0.5, 1);
	}

	public void toggle() {
		Minecraft.getMinecraft().thePlayer.playSound(MODID+":tick", (float) 0.5, 1);
	}

	public void nextPage() {
		Minecraft.getMinecraft().thePlayer.playSound(MODID+":page_turn_forward", (float) 0.5, 1);
	}

	public void previousPage() {
		Minecraft.getMinecraft().thePlayer.playSound(MODID+":page_turn_backward", (float) 0.5, 1);
	}

	public void closeBook() {
		Minecraft.getMinecraft().thePlayer.playSound(MODID+":close_book", (float) 0.5, 1);
	}
}
