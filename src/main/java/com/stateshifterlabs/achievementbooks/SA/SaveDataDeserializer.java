package com.stateshifterlabs.achievementbooks.SA;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveDataDeserializer implements JsonDeserializer<AchievementStorage> {

	private Book book;

	public SaveDataDeserializer(Book book) {
		this.book = book;
	}

	@Override
	public AchievementStorage deserialize(
			JsonElement json, Type typeOfT, JsonDeserializationContext context
	) throws JsonParseException {

		AchievementStorage storage = new AchievementStorage();
		if(!json.isJsonObject()) {
			return storage;
		}
		JsonObject root = json.getAsJsonObject();

		for(Map.Entry<String, JsonElement> userEntry : root.entrySet()) {

			String username = userEntry.getKey();
			JsonArray achievements = userEntry.getValue().getAsJsonArray();

			AchievementData achievementData = storage.forPlayer(username);
			for(JsonElement achievement : achievements) {
				for(Map.Entry<String, JsonElement> achievementEntry: achievement.getAsJsonObject().entrySet()) {
					String achievementText = achievementEntry.getKey();
					boolean completed = achievementEntry.getValue().getAsBoolean();

					try {
						final String text = achievementText(achievementText);
						int achievementId = book.findIdByAchievementText(text);
						if(completed) {
							achievementData.toggle(book.itemName(), achievementId);
						}
					} catch (NoSuchAchievementException e) {
					}

				}
			}
		}

		return storage;
	}

	private String achievementText(String text) {
		String firstPass = text.trim().replaceAll("[|]", "\n");
		String pattern = "(.*)\\s+\\[([^\\]]*)\\](\\s*)?";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(firstPass);

		if(m.find()) {
			String result = m.group(1);
			return result;
		}
		else {
			return firstPass;
		}
	}

}
