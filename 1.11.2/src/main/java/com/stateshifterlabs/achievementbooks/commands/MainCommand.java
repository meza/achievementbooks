package com.stateshifterlabs.achievementbooks.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MainCommand extends CommandBase {

	private List<CommandBase> subCommands = new ArrayList<CommandBase>();

	@Override
	public int getRequiredPermissionLevel() {
		return Main.PERMISSION;
	}

	@Override
	public String getName() {
		return Main.NAME;
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();

		aliases.add(Main.ALIAS1);

		return aliases;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		String txt = "";
		for (int i = 0; i < subCommands.size(); i++) {
			CommandBase c = subCommands.get(i);
			txt += c.getName();
			if (i < subCommands.size() - 1) {
				txt += "|";
			}
		}
		if (subCommands.size() == 0) {
			return "/" + getName();
		}

		return "/" + getName() + " <" + txt + ">";
	}

	@Override
	public List<String> getTabCompletions(
			MinecraftServer server, ICommandSender sender, String[] strings, @Nullable BlockPos pos
	) {
		List<String> tabOptions = new ArrayList<String>();

		if (strings.length == 1) {
			for (CommandBase command : subCommands) {
				tabOptions.add(command.getName());
			}
		}

		return tabOptions;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		for (CommandBase c : subCommands) {
			if (c.getName().equalsIgnoreCase(args[0])) {
				c.execute(server, sender, args);
				return;
			}
		}
	}

	public void add(CommandBase command) {
		subCommands.add(command);
	}
}
