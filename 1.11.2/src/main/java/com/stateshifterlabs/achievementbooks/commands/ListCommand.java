package com.stateshifterlabs.achievementbooks.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.stateshifterlabs.achievementbooks.UTF8Utils;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

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
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add(com.stateshifterlabs.achievementbooks.commands.List.ALIAS1);
		return aliases;
	}

	@Override
	public String getName() {
		return com.stateshifterlabs.achievementbooks.commands.List.NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return com.stateshifterlabs.achievementbooks.commands.List.USAGE;
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String txt;
		sender.sendMessage(new TextComponentTranslation("ab.command.list.title").appendText(":"));

		for(Book book: books) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(MODID, book.itemName()));

			txt = UTF8Utils.utf8String(" - ", book.name(), " ", ChatFormatting.BLUE.toString(), "(",
									   String.valueOf(Item.getIdFromItem(item)), ")");
			if (sender.getName().equalsIgnoreCase("server")) {
				txt = String.format(" - %s (%d)", book.name(), Item.getIdFromItem(item));
			}
			sender.sendMessage(new TextComponentString(txt));

		}
	}
}
