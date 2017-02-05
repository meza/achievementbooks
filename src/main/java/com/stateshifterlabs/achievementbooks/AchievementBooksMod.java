package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.client.items.AchievementBookItem;
import com.stateshifterlabs.achievementbooks.common.CommandFlush;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import com.stateshifterlabs.achievementbooks.data.GameSave;
import com.stateshifterlabs.achievementbooks.data.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.io.File;

@Mod(modid = AchievementBooksMod.MODID, version = AchievementBooksMod.VERSION)
public class AchievementBooksMod {
	public static final String MODID = "achievementbooks";
	public static final String VERSION = "1.0";
	private GameSave saver;

	//    @Mod.Instance("AchievementBooksMod")
	//    public AchievementBooksMod instance;

	//    @SidedProxy(clientSide = "com.stateshifterlabs.achievementbooks.client.ClientProxy", serverSide = "com
	// .stateshifterlabs.achievementbooks.common.CommonProxy")
	//    public static CommonProxy proxy;


	public static CreativeTabs tabName = new CreativeTabs("tabName") {
		public Item getTabIconItem() {
			return Items.gold_nugget;
		}
	};
	private Books books;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configDir =
				new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID);

		Loader loader = new Loader(configDir);
		books = loader.init();
		saver = new GameSave(books);

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		//        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

		for (Book book : books) {
			AchievementBookItem achievementBook = new AchievementBookItem(book);
			GameRegistry.registerItem(achievementBook, book.name(), MODID);
			GameRegistry.addRecipe(new ItemStack(achievementBook),
								   "AB",
								   'A', Items.book,
								   'B', Item.itemRegistry.getObject(book.material()));
		}

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		ICommandManager server = MinecraftServer.getServer().getCommandManager();
		((ServerCommandManager) server).registerCommand(new CommandFlush());
	}

}
