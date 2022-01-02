package com.stateshifterlabs.achievementbooks.helpers.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import io.codearte.jfairy.Fairy;

public class AchievementDataGenerator {

    public static RandomTestData<JsonElement, AchievementData> generate(String playerName) {
        Fairy fairy = Fairy.create();
        AchievementData expected = new AchievementData(playerName);

        JsonObject element = new JsonObject();
        element.addProperty("name", playerName);

        JsonArray books = new JsonArray();

        int numberOfBooks = fairy.baseProducer().randomBetween(1, 10);

        for (int i = 0; i < numberOfBooks; i++) {
            String bookName = fairy.textProducer().latinSentence();
            JsonArray checked = new JsonArray();

            int numberOfAchievements = fairy.baseProducer().randomBetween(1, 10);
            for (int j = 0; j < numberOfAchievements; j++) {
                checked.add(j);
                expected.toggle(bookName, j);
            }

            JsonObject book = new JsonObject();
            book.addProperty("name", bookName);
            book.add("checked", checked);
            books.add(book);
        }

        element.add("books", books);
        return new RandomTestData<>(element, expected);

    }

}
