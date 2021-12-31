package com.stateshifterlabs.achievementbooks.helpers.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import io.codearte.jfairy.Fairy;

public class AchievementStorageGenerator {

	private Fairy fairy = Fairy.create();

	public RandomTestData<JsonElement, AchievementStorage> generate() {
		return generate(0);
	}

	public RandomTestData<JsonElement, AchievementStorage> generate(int numberOfPlayers) {

		JsonArray json = new JsonArray();
		AchievementStorage storage = new AchievementStorage();

		for(int i=0; i<numberOfPlayers; i++) {
			String playerName = fairy.textProducer().latinWord();
			RandomTestData<JsonElement, AchievementData> playerData = AchievementDataGenerator.generate(playerName);
			storage.append(playerData.objectFormat());
			json.add(playerData.jsonFormat());
		}

		return new RandomTestData<>(json, storage);

	}

}
