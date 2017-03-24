package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

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
	public void processCommand(ICommandSender sender, String[] p_71515_2_) {
		loader.init(true);
		Item item = GameRegistry.findItem(MODID, "book_demo");
		sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName()).inventory
				.addItemStackToInventory(new ItemStack(item, 1));

	}
}
