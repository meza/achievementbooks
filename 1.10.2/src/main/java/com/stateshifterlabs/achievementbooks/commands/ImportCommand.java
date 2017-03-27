package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ImportCommand extends CommandBase {

	private final NetworkAgent networkAgent;
	private String dataDir;
	private Loader loader;

	public ImportCommand(Loader loader, NetworkAgent networkAgent, String dataDir) {

		this.loader = loader;
		this.networkAgent = networkAgent;
		this.dataDir = dataDir;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return Import.PERMISSION;
	}

	@Override
	public String getCommandName() {
		return Import.NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return Import.USAGE;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (sender.getName().equalsIgnoreCase("server")) {
			return false;
		}

		if (sender.getEntityWorld().isRemote) {
			return false;
		}

		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if (!((EntityPlayerMP) sender).mcServer.isSinglePlayer() || sender.getEntityWorld().isRemote ||
			sender.getName().equalsIgnoreCase("server")) {

			sender.addChatMessage(new TextComponentTranslation("ab.command.import.error.multiplayer.error"));
			sender.addChatMessage(new TextComponentTranslation("ab.command.import.error.multiplayer.advice"));
			return;
		}


		SA importer = new SA(dataDir);
		Book newBook = importer.createElementList(importer.parseFormattings());

		importer.saveBook(newBook);
		loader.init();

		importer.parseSaveData(newBook, networkAgent);

		Item item = Item.REGISTRY.getObject(new ResourceLocation(MODID, newBook.itemName()));
		sender.getEntityWorld().getPlayerEntityByName(sender.getName()).inventory
				.addItemStackToInventory(new ItemStack(item, 1));

		sender.addChatMessage(new TextComponentString("Finished importing the achievement book."));
		sender.addChatMessage(new TextComponentString(
				"New book file created in config/achievementbooks/imported_achievement_book.json"));
		sender.addChatMessage(
				new TextComponentString("It's not going to be perfect, but gets the most of the job done."));

	}
}
