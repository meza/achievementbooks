package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.DemoAlreadyExistsException;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.items.DemoBook;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class CreateDemoCommand extends CommandBase {


	private Loader loader;

	public CreateDemoCommand(Loader loader) {
		this.loader = loader;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return Init.PERMISSION;
	}

	@Override
	public String getCommandName() {
		return Init.NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return Init.USAGE;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			loader.init(true);
			Item item = Item.REGISTRY.getObject(new ResourceLocation(MODID, DemoBook.NAME));
			sender.getEntityWorld().getPlayerEntityByName(sender.getName()).inventory
					.addItemStackToInventory(new ItemStack(item, 1));
		} catch (DemoAlreadyExistsException e) {
			//TODO translate
			sender.addChatMessage(new TextComponentString(e.getMessage()));
		}
	}

}
