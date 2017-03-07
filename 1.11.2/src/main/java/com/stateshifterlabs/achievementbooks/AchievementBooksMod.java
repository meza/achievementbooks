package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.commands.CreateDemoCommand;
import com.stateshifterlabs.achievementbooks.commands.GiveCommand;
import com.stateshifterlabs.achievementbooks.commands.ImportCommand;
import com.stateshifterlabs.achievementbooks.commands.ImportUserSaveCommand;
import com.stateshifterlabs.achievementbooks.commands.ListCommand;
import com.stateshifterlabs.achievementbooks.commands.MainCommand;
import com.stateshifterlabs.achievementbooks.commands.ReloadCommand;
import com.stateshifterlabs.achievementbooks.common.MCThingy;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Books;
import com.stateshifterlabs.achievementbooks.data.GameSave;
import com.stateshifterlabs.achievementbooks.data.Loader;
import com.stateshifterlabs.achievementbooks.data.compatibility.SA.SA;
import com.stateshifterlabs.achievementbooks.facade.MCSound;
import com.stateshifterlabs.achievementbooks.facades.MinecraftStuff;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	private int delay = 160;
	private MinecraftStuff stuff;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configDir =
				new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID);
		networkAgent = new NetworkAgent(storage);
		loader = new Loader(configDir, books, storage, networkAgent, new MCSound());
		stuff = new MCThingy();
		if (books.migration() != null) {
			FMLCommonHandler.instance().bus().register(this);
		}
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
		mainCommand.add(new ImportCommand(loader, networkAgent));
		mainCommand.add(new ImportUserSaveCommand(books, networkAgent));
		mainCommand.add(new CreateDemoCommand(loader));
		mainCommand.add(new GiveCommand(books));
		mainCommand.add(new ListCommand(books));

		ICommandManager server = event.getServer().getCommandManager();
		((ServerCommandManager) server).registerCommand(mainCommand);

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		delay--;
		if(delay>1) {
			return;
		}
		delay = 160;



		Item SAbook = Item.getByNameOrId("simpleachievements:achievement_book");
		final InventoryPlayer inventory = event.player.inventory;
		ItemStack saStack = new ItemStack(SAbook, 1);
		if(inventory.hasItemStack(saStack)) {

			SA importer = new SA();

			Book book = books.migration();

			if(book == null) {
				return;
			}

			AchievementData data = importer.getUserSave(stuff.getPlayer().getDisplayName(), book);
			networkAgent.sendCompletedAchievements(data);
			//event.player.addChatMessage(new ChatComponentText("Imported the save data"));

			Item theBook = Item.getByNameOrId(MODID+":"+book.itemName());

			int inventorySize = inventory.getSizeInventory();

			for(int i = 0; i<inventorySize; i++ ) {
				ItemStack stack = inventory.getStackInSlot(i);
				if (stack == null) {
					continue;
				}
				System.out.println(String.format("Inventory %d, stack name %s", i, stack.getUnlocalizedName()));
				if (stack.getItem() == SAbook) {
					ItemStack newBook = new ItemStack(theBook, 1);
					inventory.setInventorySlotContents(i, newBook);
				}
			}


		}
	}

}
