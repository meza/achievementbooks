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

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class GiveCommand extends CommandBase {


	private Books books;

	public GiveCommand(Books books) {
		this.books = books;
	}

	@Override
	public String getCommandName() {
		return "give";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "give";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		for(Book book: books) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(MODID, book.itemName()));
			sender.getEntityWorld().getPlayerEntityByName(sender.getName()).inventory.addItemStackToInventory(new ItemStack(item, 1));
		}
	}

}
