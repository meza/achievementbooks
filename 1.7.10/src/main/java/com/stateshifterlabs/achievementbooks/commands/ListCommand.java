package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.UTF8Utils;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ListCommand extends CommandBase {


	private Books books;

	public ListCommand(Books books) {
		this.books = books;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return com.stateshifterlabs.achievementbooks.commands.List.PERMISSION;
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add(com.stateshifterlabs.achievementbooks.commands.List.ALIAS1);
		return aliases;
	}

	@Override
	public String getCommandName() {
		return com.stateshifterlabs.achievementbooks.commands.List.NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return com.stateshifterlabs.achievementbooks.commands.List.USAGE;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		String txt;
		sender.addChatMessage(new ChatComponentTranslation("ab.command.list.title").appendText(":"));

		for (Book book : books) {
			Item item = GameRegistry.findItem(MODID, book.itemName());
			txt = UTF8Utils.utf8String(" - ", book.name(), " ", EnumChatFormatting.BLUE.toString(), "(",
									   String.valueOf(Item.getIdFromItem(item)), ")");
			if (sender.getCommandSenderName().equalsIgnoreCase("server")) {
				txt = String.format(" - %s (%d)", book.name(), Item.getIdFromItem(item));
			}

			sender.addChatMessage(new ChatComponentText(txt));
		}
	}
}
