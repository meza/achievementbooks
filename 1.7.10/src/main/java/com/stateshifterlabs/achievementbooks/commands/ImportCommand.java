package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ImportCommand extends CommandBase {

	private Loader loader;
	private NetworkAgent networkAgent;
	private String dataDir;


	public ImportCommand(Loader loader, NetworkAgent networkAgent, String dataDir) {

		this.loader = loader;
		this.networkAgent = networkAgent;
		this.dataDir = dataDir;
	}

	@Override
	public String getCommandName() {
		return "import";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if (sender.getCommandSenderName().equalsIgnoreCase("server")) {
			return false;
		}

		if (sender.getEntityWorld().isRemote) {
			return false;
		}

		return true;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "import";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		if (!((EntityPlayerMP) sender).mcServer.isSinglePlayer() || sender.getEntityWorld().isRemote || sender.getCommandSenderName().equalsIgnoreCase("server")) {
			sender.addChatMessage(
					new ChatComponentText("Please run the import process in a single player world to avoid problems" +
										  "."));
			sender.addChatMessage(new ChatComponentText("When you have the new book config, add it to the modpack"));
			return;
		}

		SA importer = new SA(dataDir);
		Book newBook = importer.createElementList(importer.parseFormattings());

		importer.saveBook(newBook);
		loader.init();

		importer.parseSaveData(newBook, networkAgent);

		Item item = GameRegistry.findItem(MODID, newBook.itemName());
		sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName()).inventory
				.addItemStackToInventory(new ItemStack(item, 1));

		sender.addChatMessage(new ChatComponentText("Finished importing the achievement book."));
		sender.addChatMessage(new ChatComponentText(
				"New book file created in config/achievementbooks/imported_achievement_book.json"));
		sender.addChatMessage(
				new ChatComponentText("It's not going to be perfect, but gets the most of the job done."));

	}
}
