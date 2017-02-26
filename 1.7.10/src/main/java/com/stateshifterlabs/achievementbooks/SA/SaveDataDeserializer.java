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
import com.stateshifterlabs.achievementbooks.data.Save;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;

import java.lang.reflect.Type;
import java.util.Map;

public class SaveDataDeserializer implements JsonDeserializer<AchievementStorage> {

	private AchievementStorage storage;
	private Book book;
	private NetworkAgent networkAgent;

	public SaveDataDeserializer(AchievementStorage storage, Book book, NetworkAgent networkAgent) {
		this.storage = storage;
		this.book = book;
		this.networkAgent = networkAgent;
	}

	@Override
	public AchievementStorage deserialize(
			JsonElement json, Type typeOfT, JsonDeserializationContext context
	) throws JsonParseException {

		JsonObject root = json.getAsJsonObject();

		for(Map.Entry<String, JsonElement> userEntry : root.entrySet()) {

			String username = userEntry.getKey();
			JsonArray achievements = userEntry.getValue().getAsJsonArray();

			AchievementData achievementData = storage.forPlayer(username);
			Save save = new Save();
			for(JsonElement achievement : achievements) {
				for(Map.Entry<String, JsonElement> achievementEntry: achievement.getAsJsonObject().entrySet()) {
					String achievementText = achievementEntry.getKey();
					boolean completed = achievementEntry.getValue().getAsBoolean();

					try {
						int achievementId = book.findIdByAchievementText(achievementText.trim().replaceAll("[|]", "\n"));
						if(completed) {
//							save.toggle(achievementId);
							networkAgent.toggle(book, achievementId);
						}
					} catch (NoSuchAchievementException e) {
					}

				}
			}
			achievementData.addSaveData(book.name(), save);
		}

		return storage;
	}
}
