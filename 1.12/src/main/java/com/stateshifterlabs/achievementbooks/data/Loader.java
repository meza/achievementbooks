package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.CommonProxy;
import com.stateshifterlabs.achievementbooks.facade.Sound;
import com.stateshifterlabs.achievementbooks.items.AchievementBookItem;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import com.stateshifterlabs.achievementbooks.serializers.BookSerializer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.io.FileUtils;
import com.google.common.io.Files;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Loader {
	private final AchievementStorage storage;
	private final NetworkAgent networkAgent;
	private Sound sound;
	private CommonProxy proxy;
	private File configDir;
	private Books books;
	private Map<String, AchievementBookItem> items = new HashMap<String, AchievementBookItem>();

	public Loader(File configDir, Books books, AchievementStorage storage, NetworkAgent networkAgent, Sound sound, CommonProxy proxy) {
		this.configDir = configDir;
		this.books = books;
		this.storage = storage;
		this.networkAgent = networkAgent;
		this.sound = sound;
		this.proxy = proxy;
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
				throw new DemoAlreadyExistsException();
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
				gsonBuilder.registerTypeAdapter(Book.class, new BookSerializer(conf));
				Gson gson = gsonBuilder.create();
				try {
					Book book = gson.fromJson(new BufferedReader(Files.newReader(conf, Charset.defaultCharset())), Book.class);
					books.addBook(book);
				} catch (JsonSyntaxException e) {
					throw new JsonParseError("There is an error in the book config. Use http://jsonlint.com/ to find it", conf);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		buildItems();

		return books;
	}

	public List<AchievementBookItem> items() {
		List<AchievementBookItem> z = new ArrayList<>();
		for(Map.Entry<String, AchievementBookItem> entry : items.entrySet()) {
			z.add(entry.getValue());
		}
		return z;
	};

	private void buildItems() {
		for (Book book : books) {
			if (items.containsKey(book.itemName())) {
				items.get(book.itemName()).updateBook(book);
			} else {
				AchievementBookItem achievementBook = new AchievementBookItem(book, storage, networkAgent, sound);
				final String name = String.format("book-%s", book.colour());
				proxy.registerItemRenderer(achievementBook, 0, name);

//				GameRegistry.register(achievementBook);
//				if (book.isCraftable()) {
//					final ItemStack itemStack = new ItemStack(achievementBook);
//					GameRegistry.addRecipe(itemStack, "AB", 'A', Items.BOOK, 'B',
//										   Item.getByNameOrId(book.material()));
//				}
				items.put(book.itemName(), achievementBook);
			}
		}
	}
}
