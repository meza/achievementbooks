package com.stateshifterlabs.achievementbooks.commands;

import com.stateshifterlabs.achievementbooks.data.DemoAlreadyExistsException;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.items.DemoBook;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

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
	public void processCommand(ICommandSender sender, String[] p_71515_2_) {
		try {
			loader.init(true);
			Item item = GameRegistry.findItem(MODID, DemoBook.NAME);
			if (!sender.getCommandSenderName().equalsIgnoreCase("server")) {
				sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName()).inventory
						.addItemStackToInventory(new ItemStack(item, 1));
			} else {
				sender.addChatMessage(new ChatComponentTranslation("ab.command.init.success"));
			}
		} catch (DemoAlreadyExistsException e) {
			//TODO translate
			sender.addChatMessage(new ChatComponentText(e.getMessage()));
		}

	}
}
