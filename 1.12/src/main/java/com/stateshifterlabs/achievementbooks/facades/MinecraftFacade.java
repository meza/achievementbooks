package com.stateshifterlabs.achievementbooks.facades;

import com.stateshifterlabs.achievementbooks.facade.Player;
import net.minecraft.client.gui.FontRenderer;

public interface MinecraftFacade {
	public String getModId();
	public  FontRenderer fontRenderer();
	public Player getPlayer();
}
