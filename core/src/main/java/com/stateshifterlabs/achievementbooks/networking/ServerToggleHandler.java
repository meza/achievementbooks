package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class ServerToggleHandler implements IMessageHandler<ToggleAchievementMessage, IMessage> {

	private AchievementStorage storage;

	public ServerToggleHandler(AchievementStorage storage) {
		this.storage = storage;
	}


	@Override
	public IMessage onMessage(final ToggleAchievementMessage message, final MessageContext ctx)
	{

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		storage.forPlayer(player).toggle(message.bookName(), message.achievmenetId());

		return null;
	}

}
