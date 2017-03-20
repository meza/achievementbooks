package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.facade.Player;
import com.stateshifterlabs.achievementbooks.facades.MinecraftStuff;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ClientToggleHandler implements IMessageHandler<ToggleAchievementMessage, IMessage> {

	private AchievementStorage storage;
	private MinecraftStuff stuff;

	public ClientToggleHandler(AchievementStorage storage, MinecraftStuff stuff) {
		this.storage = storage;
		this.stuff = stuff;
	}


	@Override
	public IMessage onMessage(final ToggleAchievementMessage message, final MessageContext ctx)
	{

		Player player = stuff.getPlayer();
		storage.forPlayer(player).toggle(message.bookName(), message.achievementId());

		return null;
	}

}