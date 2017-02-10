package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Loader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CreateDemoCommand extends CommandBase {


	private Loader loader;

	public CreateDemoCommand(Loader loader) {
		this.loader = loader;
	}

	@Override
	public String getCommandName() {
		return "init";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "init";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		loader.init(true);
	}
}
