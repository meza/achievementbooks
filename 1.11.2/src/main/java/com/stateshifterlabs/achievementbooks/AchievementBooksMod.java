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
import com.stateshifterlabs.achievementbooks.facade.MCSound;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

@Mod(modid = AchievementBooksMod.MODID, version = AchievementBooksMod.VERSION, name = AchievementBooksMod.MODID)
public class AchievementBooksMod {
	public static final String MODID = "achievementbooks";
	public static final String VERSION = "@VERSION@";

	@SidedProxy(serverSide = "com.stateshifterlabs.achievementbooks.CommonProxy", clientSide = "com.stateshifterlabs.achievementbooks.ClientProxy")
	public static CommonProxy proxy;

	private final AchievementStorage storage = new AchievementStorage();
	private Books books = new Books();
	private NetworkAgent networkAgent;
	private Loader loader;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configDir =
				new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID);
		networkAgent = new NetworkAgent(storage);
		loader = new Loader(configDir, books, storage, networkAgent, new MCSound());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		loader.init();
		new GameSave(storage, books, networkAgent);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {

		MainCommand mainCommand = new MainCommand();
		mainCommand.add(new ReloadCommand(loader));
		mainCommand.add(new CreateDemoCommand(loader));
		mainCommand.add(new GiveCommand(books));
		mainCommand.add(new ListCommand(books));

		ICommandManager server = event.getServer().getCommandManager();
		((ServerCommandManager) server).registerCommand(mainCommand);

	}

}
