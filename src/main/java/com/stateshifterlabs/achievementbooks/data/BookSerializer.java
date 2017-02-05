package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class BookSerializer implements JsonSerializer<Book> {
	@Override
	public JsonElement serialize(
			Book src, Type typeOfSrc, JsonSerializationContext context
	) {
		JsonObject book = new JsonObject();

		JsonArray checked = new JsonArray();

		for(int i=0; i<src.pageCount(); i++) {
			Page page = src.openPage(i);
			for(PageElement element : page.elements()) {

				if(element.type() == PageElement.Type.ACHIEVEMENT) {
					if (element.checked()) {
						JsonElement ch = new JsonPrimitive(element.id());
						checked.add(ch);
					}
				}
			}
		}

		book.add("done", checked);

		return book;
	}
}
