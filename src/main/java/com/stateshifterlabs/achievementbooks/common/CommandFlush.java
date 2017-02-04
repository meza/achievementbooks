package com.stateshifterlabs.achievementbooks.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class CommandFlush extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "reloadachievements";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/reloadachievements - Reload Achievement Book config";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{

//		new InputStreamReader();

		//DataManager.INSTANCE.flush();
	}

	@Override
	public int compareTo(Object o)
	{
		if (o instanceof ICommand)
		{
			return this.compareTo((ICommand) o);
		}
		else
		{
			return 0;
		}
	}
}
