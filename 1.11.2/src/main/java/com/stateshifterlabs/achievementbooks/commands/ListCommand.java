package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class ListCommand extends CommandBase {


	private Books books;

	public ListCommand(Books books) {
		this.books = books;
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("ls");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String txt = "Current Achievement Books:";
		sender.sendMessage(new TextComponentString(txt));

		for(Book book: books) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(MODID, book.itemName()));

			txt = String.format(" - §r%s§r §9(%d)§r", book.name(), Item.getIdFromItem(item));
			sender.sendMessage(new TextComponentString(txt));

		}
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "list";
	}

}
