package com.stateshifterlabs.achievementbooks.helpers.generators;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stateshifterlabs.achievementbooks.SA.Formatting;
import com.stateshifterlabs.achievementbooks.SA.FormattingList;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import io.codearte.jfairy.Fairy;

public class FormattingGenerator {

	private Fairy fairy = Fairy.create();

	public RandomTestData<JsonElement, FormattingList> generate() {

		JsonObject formattingsJson = new JsonObject();
		FormattingList formattingList = new FormattingList();

		int numberOfFormattings = fairy.baseProducer().randomBetween(0, 5);

		for(int i=0; i<numberOfFormattings; i++) {
			JsonObject formattingJson = new JsonObject();
			boolean isAchievement = fairy.baseProducer().trueOrFalse();
			boolean isHeader = fairy.baseProducer().trueOrFalse();

			formattingJson.addProperty("isAchievement", isAchievement);
			if(isHeader) {
				formattingJson.addProperty("align", "CENTER");
			} else {
				formattingJson.addProperty("align", "LEFT");
			}

			formattingsJson.add(String.valueOf(i), formattingJson);
			formattingList.put(i, new Formatting(isAchievement, isHeader));

		}

		return new RandomTestData<>(formattingsJson, formattingList);
	}

}
