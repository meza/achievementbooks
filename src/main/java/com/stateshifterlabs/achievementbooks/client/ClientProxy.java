package com.stateshifterlabs.achievementbooks.client;

import com.stateshifterlabs.achievementbooks.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers()
	{
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}
