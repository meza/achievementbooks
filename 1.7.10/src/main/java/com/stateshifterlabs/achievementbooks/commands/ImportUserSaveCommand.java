package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ImportUserSaveCommand extends CommandBase {

	private Books books;
	private NetworkAgent networkAgent;

	public ImportUserSaveCommand(Books books, NetworkAgent networkAgent) {
		this.books = books;
		this.networkAgent = networkAgent;
	}

	@Override
	public String getCommandName() {
		return "import_user";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "import_user";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(!sender.getEntityWorld().isRemote) {
			SA importer = new SA(Minecraft.getMinecraft().mcDataDir.getAbsolutePath());

			Book book = books.migration();

			if (book == null) {
				return;
			}

			AchievementData data = importer.getUserSave(Minecraft.getMinecraft().thePlayer.getDisplayName(), book);
			networkAgent.sendCompletedAchievements(data);

			sender.addChatMessage(new ChatComponentText("Imported the save data"));
		}

	}
}
