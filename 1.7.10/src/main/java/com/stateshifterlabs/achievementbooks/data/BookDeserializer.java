package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BookDeserializer implements JsonDeserializer<Book>  {
	@Override
	public Book deserialize(
			JsonElement json, Type typeOfT, JsonDeserializationContext context
	) throws JsonParseException {

		Book book = new Book();

		JsonObject bookObject = json.getAsJsonObject();

		book.withName(bookObject.get("bookName").getAsString());
		book.withItemName(bookObject.get("itemName").getAsString());
		if(bookObject.has("color")) {
			book.withColour(bookObject.get("color").getAsString());
		}
		if(bookObject.has("colour")) {
			book.withColour(bookObject.get("colour").getAsString());
		}
		if(bookObject.has("craftingMaterial")) {
			book.withMaterial(bookObject.get("craftingMaterial").getAsString());
		}
		final JsonArray bookJson = bookObject.getAsJsonArray("pages");

		for (int i = 0; i<bookJson.size(); i++) {
			Page page = new Page();
			JsonArray pageElements = bookJson.get(i).getAsJsonArray();

			for(int j = 0; j<pageElements.size(); j++) {
				JsonObject pageJson = pageElements.get(j).getAsJsonObject();

				PageElement element = new PageElement(pageJson.get("id").getAsInt());

				if(pageJson.has("achievement")) {
					element.withAchievement(pageJson.get("achievement").getAsString());
					if(pageJson.has("checked")) {
						element.toggleState(pageJson.get("checked").getAsBoolean());
					}
				}

				if(pageJson.has("description")) {
					element.withDescription(pageJson.get("description").getAsString());
				}

				if(pageJson.has("header")) {
					element.withHeader(pageJson.get("header").getAsString());
				}

				if(pageJson.has("mod")) {
					element.withMod(pageJson.get("mod").getAsString());
				}

				page.addElement(element);

			}

			book.addPage(page);
		}

		return book;

	}
}
