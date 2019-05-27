package com.crankysupertoon.achievementbooks.facades;

import com.crankysupertoon.achievementbooks.facade.Player;
import net.minecraft.client.gui.FontRenderer;

public interface MinecraftFacade {
	public String getModId();
	public  FontRenderer fontRenderer();
	public Player getPlayer();
}
