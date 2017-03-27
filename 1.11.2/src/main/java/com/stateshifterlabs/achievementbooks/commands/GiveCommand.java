package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class GiveCommand extends CommandBase {


	private Books books;

	public GiveCommand(Books books) {
		this.books = books;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return Give.PERMISSION;
	}

	@Override
	public String getName() {
		return Give.NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return Give.USAGE;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender.getName().equalsIgnoreCase("server")) {
			sender.sendMessage(new TextComponentTranslation("ab.command.give.error.console"));
			return;
		}
		for(Book book: books) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(MODID, book.itemName()));
			sender.getEntityWorld().getPlayerEntityByName(sender.getName()).inventory.addItemStackToInventory(new ItemStack(item, 1));
		}
	}

}
