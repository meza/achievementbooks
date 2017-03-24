package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.Loader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

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
		Item item = Item.REGISTRY.getObject(new ResourceLocation(MODID, "book_demo"));
		sender.getEntityWorld().getPlayerEntityByName(sender.getName()).inventory.addItemStackToInventory(new ItemStack(item, 1));
	}

}
