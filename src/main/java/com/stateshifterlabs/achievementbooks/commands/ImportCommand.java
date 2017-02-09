package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ImportCommand extends CommandBase {

	private Loader loader;

	public ImportCommand(Loader loader) {

		this.loader = loader;
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
	public void processCommand(ICommandSender sender, String[] args) {

		SA importer = new SA();
		Book newBook = importer.createElementList(importer.parseFormattings());

		importer.saveBook(newBook);

		loader.init();

		Item item = GameRegistry.findItem(MODID, newBook.itemName());
		sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName()).inventory.addItemStackToInventory(new ItemStack(item, 1));

		sender.addChatMessage(new ChatComponentText("Finished importing the achievement book."));
		sender.addChatMessage(new ChatComponentText("New book file created in config/achievementbooks/imported_achievement_book.json"));
		sender.addChatMessage(new ChatComponentText("It's not going to be perfect, but gets the most of the job done."));

	}
}
