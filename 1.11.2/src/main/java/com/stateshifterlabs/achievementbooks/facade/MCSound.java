package com.stateshifterlabs.achievementbooks.facade;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class MCSound implements Sound {

	public void openBook() {
		SoundEvent s = new SoundEvent(new ResourceLocation(MODID + ":open_book"));
		Minecraft.getMinecraft().player.playSound(s, (float) 0.5, 1);
	}

	public void toggle() {
		SoundEvent s = new SoundEvent(new ResourceLocation(MODID + ":tick"));
		Minecraft.getMinecraft().player.playSound(s, (float) 0.5, 1);
	}

	public void nextPage() {
		SoundEvent s = new SoundEvent(new ResourceLocation(MODID + ":page_turn_forward"));
		Minecraft.getMinecraft().player.playSound(s, (float) 0.5, 1);
	}

	public void previousPage() {
		SoundEvent s = new SoundEvent(new ResourceLocation(MODID + ":page_turn_backward"));
		Minecraft.getMinecraft().player.playSound(s, (float) 0.5, 1);
	}

	public void closeBook() {
		SoundEvent s = new SoundEvent(new ResourceLocation(MODID + ":close_book"));
		Minecraft.getMinecraft().player.playSound(s, (float) 0.5, 1);
	}
}
