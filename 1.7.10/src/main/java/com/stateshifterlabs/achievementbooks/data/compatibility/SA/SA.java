package com.stateshifterlabs.achievementbooks.data.compatibility.SA;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.SA.Formatting;
import com.stateshifterlabs.achievementbooks.SA.FormattingDeserializer;
import com.stateshifterlabs.achievementbooks.SA.FormattingList;
import com.stateshifterlabs.achievementbooks.SA.NoSuchFormattingException;
import com.stateshifterlabs.achievementbooks.SA.SaveDataDeserializer;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Page;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import com.stateshifterlabs.achievementbooks.serializers.BookSerializer;
import net.minecraftforge.common.DimensionManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SA {
	private static String achievementList = "/SimpleAchievements/achievementList.txt";
	private static String formatConfig = "/SimpleAchievements/divConfig.json";
	private static String saveData = "/SimpleAchievements/achievements.json";
	private final String configDir;
	private Gson gson;

	public SA(String configDirPath) {
		configDir = configDirPath + "/config";

		GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
		builder.registerTypeAdapter(FormattingList.class, new FormattingDeserializer());
		builder.registerTypeAdapter(Book.class, new BookSerializer(null));
		gson = builder.create();
	}

	public FormattingList parseFormattings() {
		try {
			return gson.fromJson(new FileReader(configDir + formatConfig), FormattingList.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new FormattingList();
	}

	public Book createElementList(FormattingList formattingList) {
		File achievements = new File(configDir + achievementList);
		Book book = new Book();
		book.withName("Achievement Book");
		book.withItemName("imported_achievement_book");
		final String endStr = "::";

		int id = 0;
		int itemsOnPage = 0;
		try {
			Scanner scan = new Scanner(achievements);

			Page page = new Page();


			while (scan.hasNextLine()) {
				String s = scan.nextLine().trim();
				if (s.isEmpty()) {
					continue;
				}

				String[] args = s.split(endStr);
				if (args.length != 2) {
					scan.close();
					throw new IllegalArgumentException(
							"Illegal format \"" + s + "\". Format must be [text]" + endStr + "[divClass]");
				}

				String text = args[0].trim().replaceAll("[|]", "\n");

				if (text.isEmpty()) {
					continue;
				}

				int formattingId = Integer.parseInt(args[1].trim());

				try {
					Formatting formatting = formattingList.formattingFor(formattingId);
					PageElement element = new PageElement(id++);
					if (formatting.isAchievement()) {

						Map<String, String> x = achievementText(text);

						element.withAchievement(x.get("achievement"));
						if(x.containsKey("mod")) {
							element.withMod(x.get("mod"));
						}
					} else if (formatting.isHeader()) {
						element.withHeader(text);
					} else {
						element.withDescription(text);
					}
					page.addElement(element);
					if (itemsOnPage++ > 4) {
						book.addPage(page);
						page = new Page();
						itemsOnPage = 0;
					}
				} catch (NoSuchFormattingException e) {
					//TODO add logging
				}

			}

			scan.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return book;
	}

	public void saveBook(Book book) {

		try {
			FileWriter writer = new FileWriter(configDir + "/achievementbooks/imported_achievement_book.json");
			BufferedWriter buffer = new BufferedWriter(writer);
			String json = gson.toJson(book);
			buffer.write(json);
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public AchievementStorage parseSaveData(Book book, NetworkAgent networkAgent) {
		GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
		builder.registerTypeAdapter(AchievementStorage.class, new SaveDataDeserializer(book));
		Gson gson = builder.create();

		String worldDir = DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath();
		String saveFile = worldDir + saveData;

		File save = new File(saveFile);

		if(save.exists()) {
			try {
				AchievementStorage result = gson.fromJson(new FileReader(save), AchievementStorage.class);

				for (String player : result.players()) {
					AchievementData playerData = result.forPlayer(player);
					if (networkAgent != null) {
						networkAgent.sendCompletedAchievements(playerData);
						;
					}
				}

				return result;

			} catch (FileNotFoundException e) {

			}
		}

		return new AchievementStorage();
	}

	public AchievementData getUserSave(String displayName, Book book) {
		AchievementStorage storage = parseSaveData(book, null);
		return storage.forPlayer(displayName);

	}

	private Map<String, String> achievementText(String text) {
		String firstPass = text.trim().replaceAll("[|]", "\n");
		String pattern = "(.*)\\s+\\[([^\\]]*)\\](\\s*)?";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(firstPass);
		Map<String, String> retval = new HashMap<String, String>();
		if(m.find()) {
			retval.put("achievement", m.group(1));
			retval.put("mod", m.group(2));
			return retval;
		}
		else {
			retval.put("achievement", firstPass);
			return retval;
		}
	}
}
