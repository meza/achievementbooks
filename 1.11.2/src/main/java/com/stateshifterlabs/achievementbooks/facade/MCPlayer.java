package com.stateshifterlabs.achievementbooks.facade;

import net.minecraft.entity.player.EntityPlayer;

public class MCPlayer implements Player {

	private EntityPlayer player;

	public MCPlayer(EntityPlayer player) {

		this.player = player;
	}

	@Override
	public String getDisplayName() {
		return player.getName();
	}

	public static MCPlayer fromEntity(EntityPlayer player) {
		return new MCPlayer(player);
	}

}
