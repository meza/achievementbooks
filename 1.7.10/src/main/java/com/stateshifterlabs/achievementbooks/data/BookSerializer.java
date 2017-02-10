package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class BookSerializer implements JsonSerializer<Book> {
	@Override
	public JsonElement serialize(
			Book src, Type typeOfSrc, JsonSerializationContext context
	) {
		int id = 0;
		JsonObject book = new JsonObject();
		book.addProperty("itemName", src.itemName());
		book.addProperty("bookName", src.name());
		if(src.isCraftable()) {
			book.addProperty("craftingMaterial", src.material());
		}

		JsonArray pages = new JsonArray();

		for(int i = 0; i<src.pageCount(); i++) {

			JsonArray pageArray = new JsonArray();
			Page page = src.openPage(i);

			for(PageElement pageElement : page.elements()) {
				JsonObject element = new JsonObject();
				element.addProperty("id", id++);

				switch (pageElement.type()) {
					case ACHIEVEMENT:
						element.addProperty("achievement", pageElement.achievement());
						if(pageElement.mod() != null) {
							element.addProperty("mod", pageElement.mod());
						}
						break;
					case HEADER:
						element.addProperty("header", pageElement.header());
						if(pageElement.hasDescription()) {
							element.addProperty("description", pageElement.description());
						}
						break;
					default:
						element.addProperty("description", pageElement.description());
				}
				pageArray.add(element);

			}

			pages.add(pageArray);

		}

		book.add("pages", pages);

		return book;
	}
}
