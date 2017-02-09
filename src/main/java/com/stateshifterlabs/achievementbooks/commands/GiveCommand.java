package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
	public void processCommand(ICommandSender sender, String[] args) {

		for(Book book: books) {
			Item item = GameRegistry.findItem(MODID, book.itemName());
			sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName()).inventory.addItemStackToInventory(new ItemStack(item, 1));
		}

	}
}
