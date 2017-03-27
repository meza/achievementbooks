package com.stateshifterlabs.achievementbooks.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends CommandBase {

	private List<CommandBase> subCommands = new ArrayList<CommandBase>();

	@Override
	public int getRequiredPermissionLevel() {
		return Main.PERMISSION;
	}

	@Override
	public String getCommandName() {
		return Main.NAME;
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();

		aliases.add(Main.ALIAS1);

		return aliases;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		String txt = "";
		for (int i = 0; i < subCommands.size(); i++) {
			CommandBase c = subCommands.get(i);

			if (c.canCommandSenderUseCommand(sender)) {
				txt += c.getCommandName();
			}

			if (i < subCommands.size() - 1) {
				txt += "|";
			}
		}
		if (subCommands.size() == 0) {
			return "/" + getCommandName();
		}

		return "/" + getCommandName() + " <" + txt + ">";
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] strings) {
		List<String> tabOptions = new ArrayList<String>();

		if (strings.length == 1) {
			for (CommandBase command : subCommands) {
				tabOptions.add(command.getCommandName());
			}
		}

		return tabOptions;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		for (CommandBase c : subCommands) {
			if (c.getCommandName().equalsIgnoreCase(args[0])) {
				c.processCommand(sender, args);
				return;
			}
		}
	}

	public void add(CommandBase command) {
		subCommands.add(command);
	}
}
