package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.common.MCThingy;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;

public class NetworkAgent
{

	private SimpleNetworkWrapper wrapper;
	private AchievementStorage storage;
	private int packetId = 0;

	public NetworkAgent(AchievementStorage storage) {
		this.storage = storage;
		String channelName = String.format("meza");
		wrapper = new SimpleNetworkWrapper(channelName);

		wrapper.registerMessage(new ClientHandler(storage), CompletionDetailsMessage.class, packetId++, Side.CLIENT);
		wrapper.registerMessage(new ClientHandler(storage), CompletionDetailsMessage.class, packetId++, Side.SERVER);
		wrapper.registerMessage(new ServerToggleHandler(storage), ToggleAchievementMessage.class, packetId++, Side.SERVER);
		wrapper.registerMessage(new ClientToggleHandler(storage, new MCThingy()), ToggleAchievementMessage.class, packetId++, Side.CLIENT);
		wrapper.registerMessage(new ServerPageTurnHandler(storage), PageTurnMessage.class, packetId++, Side.SERVER);
	}

	public void sendPageNumber(Book book, int pageOffset) {
		PageTurnMessage msg = new PageTurnMessage();
		msg.withData(book.itemName(), pageOffset);
		wrapper.sendToServer(msg);
	}

	public void toggle(Book book, int id) {
		ToggleAchievementMessage msg = new ToggleAchievementMessage();
		msg.withData(book.itemName(), id);
		wrapper.sendToServer(msg);
	}

	public void sendAchievementsTo(EntityPlayerMP player) {
		CompletionDetailsMessage msg = new CompletionDetailsMessage();
		msg.withData(storage.forPlayer(player.getDisplayName()));
		wrapper.sendTo(msg, player);
	}

	public void sendCompletedAchievements(AchievementData data) {
		CompletionDetailsMessage msg = new CompletionDetailsMessage();
		msg.withData(data);
		wrapper.sendToServer(msg);
	}
}
