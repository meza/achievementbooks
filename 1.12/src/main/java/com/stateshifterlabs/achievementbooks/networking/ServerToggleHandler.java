package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.facade.MCPlayer;
import com.stateshifterlabs.achievementbooks.facade.Player;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerToggleHandler implements IMessageHandler<ToggleAchievementMessage, IMessage> {

	private AchievementStorage storage;
	private NetworkAgent networkAgent;

	public ServerToggleHandler(AchievementStorage storage, NetworkAgent networkAgent) {
		this.storage = storage;
		this.networkAgent = networkAgent;
	}


	@Override
	public IMessage onMessage(final ToggleAchievementMessage message, final MessageContext ctx)
	{

		Player player = new MCPlayer(ctx.getServerHandler().player);
		storage.forPlayer(player).toggle(message.bookName(), message.achievementId());
		networkAgent.sendAchievementsTo(ctx.getServerHandler().player);
		return null;
	}

}
