package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class ImportUserSaveCommand extends CommandBase {

	private Books books;
	private NetworkAgent networkAgent;
	private String dataDir;

	public ImportUserSaveCommand(Books books, NetworkAgent networkAgent, String dataDir) {
		this.books = books;
		this.networkAgent = networkAgent;
		this.dataDir = dataDir;
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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		SA importer = new SA(dataDir);

		Book book = books.migration();

		if(book == null) {
			return;
		}

		AchievementData data = importer.getUserSave(Minecraft.getMinecraft().thePlayer.getName(), book);
		networkAgent.sendCompletedAchievements(data);

		sender.addChatMessage(new TextComponentString("Imported the save data"));

	}
}
