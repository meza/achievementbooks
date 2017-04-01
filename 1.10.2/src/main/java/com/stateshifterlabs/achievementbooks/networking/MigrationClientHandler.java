package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MigrationClientHandler implements IMessageHandler<MigrationCompletionDetailsMessage, IMessage> {

	private AchievementStorage storage;

	public MigrationClientHandler(AchievementStorage storage) {

		this.storage = storage;
	}

	@Override
	public IMessage onMessage(final MigrationCompletionDetailsMessage message, MessageContext ctx)
	{

		storage.append(message.achievementData());
		return null;
	}
}
