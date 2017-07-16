package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.facade.Player;
import com.stateshifterlabs.achievementbooks.facades.MinecraftFacade;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ClientToggleHandler implements IMessageHandler<ToggleAchievementMessage, IMessage> {

	private AchievementStorage storage;
	private MinecraftFacade minecraft;

	public ClientToggleHandler(AchievementStorage storage, MinecraftFacade minecraft) {
		this.storage = storage;
		this.minecraft = minecraft;
	}


	@Override
	public IMessage onMessage(final ToggleAchievementMessage message, final MessageContext ctx)
	{

		Player player = minecraft.getPlayer();
		storage.forPlayer(player).toggle(message.bookName(), message.achievementId());

		return null;
	}

}
