package com.stateshifterlabs.achievementbooks.common;

import com.stateshifterlabs.achievementbooks.client.gui.GUI;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler
{
	public void registerRenderers()
	{
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == GUI.GUI_ID)
		{
			return new GUI(player, null);
		}
		return null;
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}
}
