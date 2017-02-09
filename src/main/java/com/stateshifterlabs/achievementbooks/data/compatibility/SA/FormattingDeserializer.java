package com.stateshifterlabs.achievementbooks.data.compatibility.SA;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class FormattingDeserializer implements JsonDeserializer<FormattingList> {
	@Override
	public FormattingList deserialize(
			JsonElement json, Type typeOfT, JsonDeserializationContext context
	) throws JsonParseException {

		FormattingList formattings = new FormattingList();

		JsonObject root = json.getAsJsonObject();
		Set<Map.Entry<String, JsonElement>> entries = root.entrySet();
		for(Map.Entry<String, JsonElement> element : entries) {
			JsonObject config = element.getValue().getAsJsonObject();
			boolean isAchievement = false;
			if(config.has("isAchievement") && config.get("isAchievement").getAsBoolean()) {
				isAchievement = true;
			}

			boolean isHeader = false;
			if(config.has("align") && config.get("align").getAsString().equalsIgnoreCase("center")) {
				isHeader = true;
			}

			formattings.put(Integer.valueOf(element.getKey()), new Formatting(isAchievement, isHeader));
		}


		return formattings;
	}
}
