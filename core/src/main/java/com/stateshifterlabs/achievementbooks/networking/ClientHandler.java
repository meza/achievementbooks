package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientHandler implements IMessageHandler<CompletionDetailsMessage, IMessage> {

	private AchievementStorage storage;

	public ClientHandler(AchievementStorage storage) {

		this.storage = storage;
	}

	@Override
	public IMessage onMessage(final CompletionDetailsMessage message, MessageContext ctx)
	{

		storage.append(message.achievementData());
		return null;
	}
}
