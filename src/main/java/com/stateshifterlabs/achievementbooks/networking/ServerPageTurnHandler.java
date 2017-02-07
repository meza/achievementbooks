package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.common.NBTUtils;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class ServerPageTurnHandler implements IMessageHandler<PageTurnMessage, IMessage> {

	private AchievementStorage storage;

	public ServerPageTurnHandler(AchievementStorage storage) {
		this.storage = storage;
	}


	@Override
	public IMessage onMessage(final PageTurnMessage message, final MessageContext ctx)
	{

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		String nbttag = AchievementBooksMod.MODID.toLowerCase() + ":" + message.bookName() + ":pageOffset";
		NBTUtils.getTag(player.getHeldItem()).setInteger(nbttag, message.page());

		return null;
	}

}
