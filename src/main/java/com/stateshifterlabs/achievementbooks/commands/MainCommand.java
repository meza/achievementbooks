package com.stateshifterlabs.achievementbooks.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends CommandBase {

	private List<CommandBase> subCommands = new ArrayList<CommandBase>();

	public MainCommand() {

	}

	@Override
	public String getCommandName() {
		return "achievementbooks";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		for (int i = 0; i < subCommands.size(); i++) {
			String txt = "";

			CommandBase c = subCommands.get(i);
			txt += "/ " + getCommandName() + " " + c.getCommandName();

			if (i < subCommands.size() - 1) {
				txt += ", ";
			}

			sender.addChatMessage(new ChatComponentTranslation(txt));
		}

		return "</" + getCommandName() + ">";
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
