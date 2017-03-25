package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.DuplicatePageElementIdException;
import com.stateshifterlabs.achievementbooks.data.JsonParseError;
import com.stateshifterlabs.achievementbooks.data.Loader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ReloadCommand extends CommandBase {


	private Loader loader;

	public ReloadCommand(Loader loader) {
		this.loader = loader;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 1;
	}

	@Override
	public String getCommandName() {
		return "reload";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "reload //Reloads all books in the conifg";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] p_71515_2_) {
		try {
			loader.init();
			String message = "Achievement Books Data reloaded. Textures may not appear correctly until a full game restart";
			sender.addChatMessage(new ChatComponentText(message));
		} catch (DuplicatePageElementIdException e) {
			sender.addChatMessage(new ChatComponentText(e.simpleMessage()));
		} catch (JsonParseError e) {
			sender.addChatMessage(new ChatComponentText(e.simpleMessage()));
		}
	}
}
