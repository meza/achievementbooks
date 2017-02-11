package com.stateshifterlabs.achievementbooks.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
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
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>();

		aliases.add("ab");

		return aliases;
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


			sender.addChatMessage(new TextComponentString(txt));
		}

		return "</" + getCommandName() + ">";
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] strings, @Nullable BlockPos pos) {
		List<String> tabOptions = new ArrayList<String>();

		if (strings.length == 1) {
			for (CommandBase command : subCommands) {
				tabOptions.add(command.getCommandName());
			}
		}

		return tabOptions;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		for (CommandBase c : subCommands) {
			if (c.getCommandName().equalsIgnoreCase(args[0])) {
				c.execute(server, sender, args);
				return;
			}
		}
	}

	public void add(CommandBase command) {
		subCommands.add(command);
	}
}
