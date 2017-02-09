package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.commands.CreateDemoCommand;
import com.stateshifterlabs.achievementbooks.commands.GiveCommand;
import com.stateshifterlabs.achievementbooks.commands.ListCommand;
import com.stateshifterlabs.achievementbooks.commands.MainCommand;
import com.stateshifterlabs.achievementbooks.commands.ReloadCommand;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Books;
import com.stateshifterlabs.achievementbooks.data.GameSave;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

import java.io.File;

@Mod(modid = AchievementBooksMod.MODID, version = AchievementBooksMod.VERSION)
public class AchievementBooksMod {
	public static final String MODID = "achievementbooks";
	public static final String VERSION = "@VERSION@";
	public static CreativeTabs tabName = new CreativeTabs("tabName") {
		public Item getTabIconItem() {
			return Items.gold_nugget;
		}
	};

	private final AchievementStorage storage = new AchievementStorage();
	private Books books = new Books();
	private NetworkAgent networkAgent;
	private Loader loader;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configDir =
				new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID);
		networkAgent = new NetworkAgent(storage);
		loader = new Loader(configDir, books, storage, networkAgent);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		loader.init();
		new GameSave(storage, books, networkAgent);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {

		MainCommand mainCommand = new MainCommand();
		mainCommand.add(new ReloadCommand(loader));
		mainCommand.add(new CreateDemoCommand(loader));
		mainCommand.add(new GiveCommand(books));
		mainCommand.add(new ListCommand(books));

		ICommandManager server = MinecraftServer.getServer().getCommandManager();
		((ServerCommandManager) server).registerCommand(mainCommand);
	}

}
