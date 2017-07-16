package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.client.ItemMashes;
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
import com.stateshifterlabs.achievementbooks.items.AchievementBookItem;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.File;
import java.util.List;

@Mod(modid = AchievementBooksMod.MODID, version = AchievementBooksMod.VERSION, name = AchievementBooksMod.MODID)
@Mod.EventBusSubscriber(modid = AchievementBooksMod.MODID)
public class AchievementBooksMod {
	public static final String MODID = "achievementbooks";
	public static final String VERSION = "@VERSION@";
	protected static IForgeRegistry<Item> registry;

	@SidedProxy(serverSide = "com.stateshifterlabs.achievementbooks.CommonProxy", clientSide = "com.stateshifterlabs.achievementbooks.ClientProxy")
	public static CommonProxy proxy;

	private final AchievementStorage storage = new AchievementStorage();
	private Books books = new Books();
	private NetworkAgent networkAgent;
	private Loader loader;
	private static Loader l2;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configDir =
				new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID);
		networkAgent = new NetworkAgent(storage);
		loader = new Loader(configDir, books, storage, networkAgent, new MCSound(), proxy);
		l2 = loader;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		new GameSave(storage, books, networkAgent);
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		l2.init();
		registry = event.getRegistry();

		for (final AchievementBookItem item : l2.items()) {
			registry.register(item);
		}
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		List<AchievementBookItem> items = l2.items();
		for (final AchievementBookItem item : items) {

			System.out.println(String.format("Setting %s to %s", item.getUnlocalizedName(), item.getModelLocation().toString()));

			ModelLoader.setCustomModelResourceLocation(item, 0, item.getModelLocation());
		}

	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {

		MainCommand mainCommand = new MainCommand();
		mainCommand.add(new ReloadCommand(loader, registry));
		mainCommand.add(new CreateDemoCommand(loader));
		mainCommand.add(new GiveCommand(books));
		mainCommand.add(new ListCommand(books));

		ICommandManager server = event.getServer().getCommandManager();
		((ServerCommandManager) server).registerCommand(mainCommand);

	}

}
