package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Loader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CreateDemoCommand extends CommandBase {


	private Loader loader;

	public CreateDemoCommand(Loader loader) {
		this.loader = loader;
	}

	@Override
	public String getName() {
		return "init";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "init";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		loader.init(true);
	}

}
