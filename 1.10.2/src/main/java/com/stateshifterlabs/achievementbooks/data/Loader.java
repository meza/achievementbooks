package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.facade.Sound;
import com.stateshifterlabs.achievementbooks.items.AchievementBookItem;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import com.stateshifterlabs.achievementbooks.serializers.BookDeserializer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Loader {
	private final AchievementStorage storage;
	private final NetworkAgent networkAgent;
	private Sound sound;
	private File configDir;
	private Books books;
	private Map<String, AchievementBookItem> items = new HashMap<String, AchievementBookItem>();

	public Loader(File configDir, Books books, AchievementStorage storage, NetworkAgent networkAgent, Sound sound) {
		this.configDir = configDir;
		this.books = books;
		this.storage = storage;
		this.networkAgent = networkAgent;
		this.sound = sound;
	}

	public Books init() {
		return init(false);
	}

	public Books init(boolean createDemo) {
		books.empty();
		FilenameFilter fileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					// get last index for '.' char
					int lastIndex = name.lastIndexOf('.');

					// get extension
					String str = name.substring(lastIndex);

					// match path name extension
					if (str.equals(".json")) {
						return true;
					}
				}
				return false;
			}
		};

		if (!configDir.exists()) {
			configDir.mkdirs();
		}

		final File[] files = configDir.listFiles(fileNameFilter);

		if (files.length == 0 || createDemo) {
			File file = new File(configDir.getAbsolutePath() + "/demo.json");

			if (file.exists()) {
				int i = 1;
				do {
					file = new File(configDir.getAbsolutePath() + "/demo" + i + ".json");
				} while (file.exists());
			}

			URL url = AchievementBooksMod.class.getResource("/config/demo.json");
			try {
				FileUtils.copyURLToFile(url, file);
				return init();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (File conf : files) {
			try {


				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(Book.class, new BookDeserializer());
				Gson gson = gsonBuilder.create();

				Book book = gson.fromJson(new FileReader(conf), Book.class);

				books.addBook(book);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		buildItems();

		return books;
	}

	private void buildItems() {
		for (Book book : books) {
			if (items.containsKey(book.itemName())) {
				items.get(book.itemName()).updateBook(book);
			} else {
				AchievementBookItem achievementBook = new AchievementBookItem(book, storage, networkAgent, sound);
				final String name = String.format("book-%s", book.colour());
				AchievementBooksMod.proxy.registerItemRenderer(achievementBook, 0, name);
				GameRegistry.register(achievementBook);

				if (book.isCraftable()) {
					final ItemStack itemStack = new ItemStack(achievementBook);
					GameRegistry.addRecipe(itemStack, "AB", 'A', Items.BOOK, 'B',
										   Item.REGISTRY.getObject(new ResourceLocation(book.material())));
				}
				items.put(book.itemName(), achievementBook);


			}
		}
	}
}
