package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ListCommand extends CommandBase {


	private Books books;

	public ListCommand(Books books) {
		this.books = books;
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("ls");
		return aliases;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 1;
	}

	@Override
	public String getCommandName() {
		return "list";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "list";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		String txt = "Current Achievement Books:";
		sender.addChatMessage(new ChatComponentText(txt));

		for(Book book: books) {
			Item item = GameRegistry.findItem(MODID, book.itemName());

			txt = String.format(" - §r%s§r §9(%d)§r", book.name(), Item.getIdFromItem(item));
			if(sender.getCommandSenderName().equalsIgnoreCase("server")) {
				txt = String.format(" - %s (%d)", book.name(), Item.getIdFromItem(item));
			}

			sender.addChatMessage(new ChatComponentText(txt));

		}

	}
}
