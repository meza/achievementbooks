package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ImportCommand extends CommandBase {

	private final NetworkAgent networkAgent;
	private Loader loader;

	public ImportCommand(Loader loader, NetworkAgent networkAgent) {

		this.loader = loader;
		this.networkAgent = networkAgent;
	}

	@Override
	public String getCommandName() {
		return "import";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "import";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		SA importer = new SA(Minecraft.getMinecraft().mcDataDir.getAbsolutePath());
		Book newBook = importer.createElementList(importer.parseFormattings());

		importer.saveBook(newBook);
		loader.init();

		importer.parseSaveData(newBook, networkAgent);

		Item item = Item.REGISTRY.getObject(new ResourceLocation(MODID, newBook.itemName()));
		sender.getEntityWorld().getPlayerEntityByName(sender.getName()).inventory.addItemStackToInventory(new ItemStack(item, 1));

		sender.addChatMessage(new TextComponentString("Finished importing the achievement book."));
		sender.addChatMessage(new TextComponentString("New book file created in config/achievementbooks/imported_achievement_book.json"));
		sender.addChatMessage(new TextComponentString("It's not going to be perfect, but gets the most of the job done."));



	}
}
