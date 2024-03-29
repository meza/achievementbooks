package com.stateshifterlabs.achievementbooks.core.serializers;

import com.google.gson.*;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.Save;

import java.lang.reflect.Type;

public class AchievementDataSerializer implements JsonSerializer<AchievementData>, JsonDeserializer<AchievementData> {

    private final String player;

    public AchievementDataSerializer(String player) {

        this.player = player;
    }

    @Override
    public AchievementData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject userSaveAsJsonObject = json.getAsJsonObject();

        AchievementData data = new AchievementData(userSaveAsJsonObject.get("name").getAsString());

        JsonArray booksJson = userSaveAsJsonObject.get("books").getAsJsonArray();
        for (JsonElement bookJson : booksJson) {
            Save save = new Save();

            JsonObject bookJsonObject = bookJson.getAsJsonObject();

            String name = bookJsonObject.get("name").getAsString();
            JsonArray checked = bookJsonObject.getAsJsonArray("checked");

            for (JsonElement checkedId : checked) {
                save.toggle(checkedId.getAsInt());
            }

            data.addSaveData(name, save);

        }

        return data;
    }

    @Override
    public JsonElement serialize(
            AchievementData data, Type typeOfSrc, JsonSerializationContext context
    ) {

        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("name", player);

        JsonArray books = new JsonArray();

        for (String bookName : data.books()) {
            JsonObject book = new JsonObject();
            book.addProperty("name", bookName);

            JsonArray checked = new JsonArray();
            for (Integer id : data.completed(bookName)) {
                JsonElement checkedId = new JsonPrimitive(id);
                checked.add(checkedId);
            }
            book.add("checked", checked);
            books.add(book);
        }

        playerJson.add("books", books);

        return playerJson;
    }
}
