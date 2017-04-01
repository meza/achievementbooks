package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class GiveCommand extends CommandBase {


	private final LanguageRegistry t;
	private Books books;


	public GiveCommand(Books books) {
		this.books = books;
		t = LanguageRegistry.instance();
	}

	@Override
	public int getRequiredPermissionLevel() {
		return Give.PERMISSION;
	}

	@Override
	public String getCommandName() {
		return Give.NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return Give.USAGE;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender.getCommandSenderName().equalsIgnoreCase("server")) {
			sender.addChatMessage(new ChatComponentText(t.getStringLocalization("ab.command.give.error.console")));
			return;
		}
		for (Book book : books) {
			Item item = GameRegistry.findItem(MODID, book.itemName());
			sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName()).inventory
					.addItemStackToInventory(new ItemStack(item, 1));
		}

	}
}
