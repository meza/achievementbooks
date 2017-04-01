package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.DuplicatePageElementIdException;
import com.stateshifterlabs.achievementbooks.data.JsonParseError;
import com.stateshifterlabs.achievementbooks.data.Loader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class ReloadCommand extends CommandBase {


	private Loader loader;

	public ReloadCommand(Loader loader) {
		this.loader = loader;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return Reload.PERMISSION;
	}

	@Override
	public String getCommandName() {
		return Reload.NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return Reload.USAGE;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] p_71515_2_) {
		try {
			loader.init();
			sender.addChatMessage(new ChatComponentTranslation("ab.command.reload.success"));
		} catch (DuplicatePageElementIdException e) {
			sender.addChatMessage(new ChatComponentText(e.simpleMessage()));
		} catch (JsonParseError e) {
			sender.addChatMessage(new ChatComponentText(e.simpleMessage()));
		}
	}
}
