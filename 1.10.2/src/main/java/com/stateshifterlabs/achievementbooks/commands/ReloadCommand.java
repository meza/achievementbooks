package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.DuplicatePageElementIdException;
import com.stateshifterlabs.achievementbooks.data.JsonParseError;
import com.stateshifterlabs.achievementbooks.data.Loader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		try {
			loader.init();
			sender.addChatMessage(new TextComponentTranslation("ab.command.reload.success"));
		} catch (DuplicatePageElementIdException e) {
			sender.addChatMessage(new TextComponentString(e.simpleMessage()));
		} catch (JsonParseError e) {
			sender.addChatMessage(new TextComponentString(e.simpleMessage()));
		}
	}

}
