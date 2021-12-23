package com.stateshifterlabs.achievementbooks.helpers.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.Page;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import io.codearte.jfairy.Fairy;

public class SASaveGenerator {

	private final Fairy fairy = Fairy.create();


	public RandomTestData<JsonElement, AchievementStorage> generate(Book book) {


		JsonObject saveJson = new JsonObject();
		AchievementStorage storage = new AchievementStorage();

		int numberOfPlayers = fairy.baseProducer().randomBetween(5,10);

		for(int j = 0; j < numberOfPlayers; j++) {
			String player = fairy.textProducer().word();

			JsonArray user = new JsonArray();
			AchievementData expectedAchievementData = new AchievementData(player);

			for (int i = 0; i < book.pageCount(); i++) {
				Page page = book.openPage(i);
				for (PageElement element : page.elements()) {
					String text = "";
					switch (element.type()) {
						case HEADER:
							text = String.format("%s", element.header());
							break;
						case ACHIEVEMENT:
							text = String.format("%s", element.achievement());
							if (element.hasMod()) {
								text = String.format("%s [%s]", element.achievement(), element.mod());
							}
							if (element.checked()) {
								expectedAchievementData.toggle(book.itemName(), element.id());
							}
							break;
						case TEXT:
							text = String.format("%s", element.description());
							break;
					}
					JsonObject x = new JsonObject();
					x.addProperty(text, element.checked());
					user.add(x);
				}
			}

			saveJson.add(player, user);
			storage.append(expectedAchievementData);
		}

		return new RandomTestData<>(saveJson, storage);

	}

}
