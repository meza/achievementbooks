package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class MigrationNetworkAgent
{

	private final AchievementStorage storage;
	private SimpleNetworkWrapper wrapper;
	private Book targetBook;
	private NetworkAgent networkAgent;
	private File configDir;
	private int packetId = 10000;
	private int delay = 160;
	private int delayServer = 160;
	private final SA importer;

	public MigrationNetworkAgent(Book targetBook, NetworkAgent networkAgent, File configDir, AchievementStorage mainStorage) {
		importer = new SA(configDir.getAbsolutePath());
		this.storage = mainStorage;
		this.targetBook = targetBook;
		this.networkAgent = networkAgent;
		this.configDir = configDir;
		String channelName = String.format("mezamigr");
		wrapper = new SimpleNetworkWrapper(channelName);
		wrapper.registerMessage(new MigrationClientHandler(mainStorage), MigrationCompletionDetailsMessage.class, packetId++, Side.CLIENT);
	}


	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (player != null && !player.worldObj.isRemote) {
			AchievementData data = importer.getUserSave(player.getName(), targetBook);
			storage.append(data);
			sendMigrationCompletedAchievements((EntityPlayerMP) player, data);
		}

	}

	private void sendMigrationCompletedAchievements(EntityPlayerMP player, AchievementData data) {
		MigrationCompletionDetailsMessage completionDetailsMessage = new MigrationCompletionDetailsMessage();
		completionDetailsMessage.withData(data);
		wrapper.sendTo(completionDetailsMessage, player);
	}

	public Item getBook() {
		return (Item) Item.getByNameOrId("simpleachievements:achievement_book");
	}

	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public void onServerPlayerTick(TickEvent.PlayerTickEvent event) {
		delayServer--;
		if(delayServer>1) {
			return;
		}
		delayServer = 160;
		EntityPlayer player = event.player;

		Item SAbook = getBook();
		final InventoryPlayer inventory = player.inventory;
		final AchievementData userSave = importer.getUserSave(event.player.getName(), targetBook);
		storage.append(userSave);

		updatePlayerInventory(event, SAbook, inventory);

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		delay--;
		if(delay>1) {
			return;
		}
		delay = 160;
		Item SAbook = getBook();
		final InventoryPlayer inventory = event.player.inventory;

		if(!event.player.getEntityWorld().isRemote) {
			final AchievementData userSave = importer.getUserSave(event.player.getName(), targetBook);
			networkAgent.sendCompletedAchievements(userSave);
			updatePlayerInventory(event, SAbook, inventory);
		}
	}

	private void updatePlayerInventory(TickEvent.PlayerTickEvent event, Item SAbook, InventoryPlayer inventory) {
		if(inventory.hasItemStack(new ItemStack(SAbook, 1))) {

			Item theBook = (Item) Item.getByNameOrId(MODID+":"+targetBook.itemName());

			int inventorySize = inventory.getSizeInventory();

			for(int i = 0; i<inventorySize; i++ ) {
				ItemStack stack = inventory.getStackInSlot(i);
				if (stack == null) {
					continue;
				}
				if (stack.getItem() == SAbook) {
					inventory.setInventorySlotContents(i, (ItemStack) null);
					if(!event.player.worldObj.isRemote) {
						networkAgent.sendAchievementsTo((EntityPlayerMP) event.player);
					}
					ItemStack newBook = new ItemStack(theBook, 1);
					inventory.setInventorySlotContents(i, newBook);
				}
			}
		}
	}

}
