package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.common.NBTUtils;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPageTurnHandler implements IMessageHandler<PageTurnMessage, IMessage> {

	private AchievementStorage storage;

	public ServerPageTurnHandler(AchievementStorage storage) {
		this.storage = storage;
	}


	@Override
	public IMessage onMessage(final PageTurnMessage message, final MessageContext ctx)
	{

		EntityPlayer player = ctx.getServerHandler().player;
		String nbttag = AchievementBooksMod.MODID.toLowerCase() + ":" + message.bookName() + ":pageOffset";
		NBTUtils.getTag(player.getHeldItem(EnumHand.MAIN_HAND)).setInteger(nbttag, message.page());

		return null;
	}

}
