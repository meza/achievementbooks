package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AchievementStorageSerializer implements JsonSerializer<AchievementStorage>,
		JsonDeserializer<AchievementStorage> {

	private AchievementStorage storage;

	public AchievementStorageSerializer(AchievementStorage storage) {

		this.storage = storage;
	}

	@Override
	public JsonElement serialize(
			AchievementStorage src, Type typeOfSrc, JsonSerializationContext context
	) {

		JsonArray storage = new JsonArray();

		for (String player : src.players()) {

			JsonObject playerJson = new JsonObject();
			playerJson.addProperty("name", player);

			JsonArray books = new JsonArray();

			AchievementData data = src.forPlayer(player);
			for(String bookName: data.books()) {
				JsonObject book = new JsonObject();
				book.addProperty("name", bookName);

				JsonArray checked = new JsonArray();
				for(Integer id: data.completed(bookName)) {
					JsonElement checkedId = new JsonPrimitive(id);
					checked.add(checkedId);
				}
				book.add("checked", checked);
				books.add(book);
			}

			playerJson.add("books", books);
			storage.add(playerJson);

		}

		return storage;
	}

	@Override
	public AchievementStorage deserialize(
			JsonElement json, Type typeOfT, JsonDeserializationContext context
	) throws JsonParseException {

		JsonArray storageJson = json.getAsJsonArray();

		for(JsonElement userSave: storageJson) {
			final JsonObject userSaveAsJsonObject = userSave.getAsJsonObject();
			AchievementData data = new AchievementData(userSaveAsJsonObject.get("name").getAsString());

			JsonArray booksJson = userSaveAsJsonObject.get("books").getAsJsonArray();
			for(JsonElement bookJson: booksJson) {


				Save save = new Save();

				JsonObject bookJsonObject = bookJson.getAsJsonObject();

				String name = bookJsonObject.get("name").getAsString();
				JsonArray checked = bookJsonObject.getAsJsonArray("checked");


				for(JsonElement checkedId: checked) {
					save.toggle(checkedId.getAsInt());
				}

				data.addSaveData(name, save);

			}

			storage.append(data);
		}


		return storage;

	}
}
