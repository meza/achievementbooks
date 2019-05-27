package com.crankysupertoon.achievementbooks.networking;

import com.crankysupertoon.achievementbooks.data.AchievementStorage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
