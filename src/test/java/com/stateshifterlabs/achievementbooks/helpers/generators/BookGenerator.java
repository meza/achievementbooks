package com.stateshifterlabs.achievementbooks.helpers.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Page;
import com.stateshifterlabs.achievementbooks.core.data.PageElement;
import com.stateshifterlabs.achievementbooks.core.data.Type;
import com.stateshifterlabs.achievementbooks.core.items.Colour;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import io.codearte.jfairy.Fairy;

import java.util.ArrayList;
import java.util.List;

public class BookGenerator {

    public static final boolean NO_STATUS = false;
    private Fairy fairy = Fairy.create();
    private int elementId = 0;

    public RandomTestData<JsonElement, Book> generate() {
        return generate(true);
    }

    public RandomTestData<JsonElement, Book> generate(List<String> excludeProperties) {
        return generate(true, excludeProperties);
    }

    public RandomTestData<JsonElement, Book> generate(boolean statusChecks) {
        List<String> excludeProperties = new ArrayList<>();
        return generate(statusChecks, excludeProperties);
    }

    public RandomTestData<JsonElement, Book> generate(boolean statusChecks, List<String> excludeProperties) {
        JsonObject bookJson = new JsonObject();
        Book book = new Book();

        if(!excludeProperties.contains("bookName")) {
            String bookName = fairy.textProducer().latinWord();
            bookJson.addProperty("bookName", bookName);
            book.withName(bookName);
        }

        if(!excludeProperties.contains("itemName")) {
            String itemName = fairy.textProducer().latinWord();
            bookJson.addProperty("itemName", itemName);
            book.withItemName(itemName);
        }

        addMigrationTarget(bookJson, book);
        addColour(bookJson, book);
        addCraftingMaterial(bookJson, book);


        if(!excludeProperties.contains("pages")) {
            int numberOfPages = fairy.baseProducer().randomBetween(1, 15);

            JsonArray pagesJson = new JsonArray();

            for (int i = 0; i < numberOfPages; i++) {
                RandomTestData<JsonElement, Page> page = randomPage(statusChecks);
                pagesJson.add(page.jsonFormat());
                book.addPage(page.objectFormat());
            }

            bookJson.add("pages", pagesJson);
        }

        return new RandomTestData<>(bookJson, book);
    }



    private void addMigrationTarget(JsonObject bookJson, Book book) {
        boolean shouldIncludeFalseMigrationTarget = fairy.baseProducer().trueOrFalse();
        if (shouldIncludeFalseMigrationTarget) {
            bookJson.addProperty("migrationTarget", false);
        }
    }

    public int getElementId() {
        return elementId++;
    }

    private void addCraftingMaterial(JsonObject bookJson, Book book) {
        if (fairy.baseProducer().trueOrFalse()) {
            String material = fairy.textProducer().latinWord();
            bookJson.addProperty("craftingMaterial", material);
            book.withMaterial(material);
        }
    }

    private void addColour(JsonObject bookJson, Book book) {
        if (fairy.baseProducer().trueOrFalse()) {
            Colour colour = fairy.baseProducer().randomElement(Colour.class);
            String colourText = colour.getText().toLowerCase();
            bookJson.addProperty("color", colourText);
            book.withColour(colourText);
        }
    }

    private RandomTestData<JsonElement, Page> randomPage(boolean statusChecks) {

        int numberOfElements = fairy.baseProducer().randomBetween(0, 5);

        JsonArray elementsJson = new JsonArray();
        Page page = new Page();
        for (int i = 0; i < numberOfElements; i++) {
            RandomTestData<JsonElement, PageElement> element = randomPageElement(statusChecks);
            elementsJson.add(element.jsonFormat());
            page.addElement(element.objectFormat());
        }

        return new RandomTestData<>(elementsJson, page);
    }

    private RandomTestData<JsonElement, PageElement> randomPageElement(boolean statusChecks) {

        Type randomType = fairy.baseProducer().randomElement(Type.class);

        int elementId = getElementId();

        JsonObject elementJson = new JsonObject();
        elementJson.addProperty("id", elementId);
        PageElement element = new PageElement(elementId);

        switch (randomType) {
            case ACHIEVEMENT:
                final String achievementText = fairy.textProducer().latinSentence();
                element.withAchievement(achievementText);
                elementJson.addProperty("achievement", achievementText);

                if (statusChecks) {
                    final boolean checked = fairy.baseProducer().trueOrFalse();
                    element.toggleState(checked);
                    elementJson.addProperty("checked", checked);
                }

                if (fairy.baseProducer().trueOrFalse()) {
                    final String modText = fairy.textProducer().latinSentence();
                    element.withMod(modText);
                    elementJson.addProperty("mod", modText);

                }

                if (fairy.baseProducer().trueOrFalse()) {
                    final String descriptionText = fairy.textProducer().latinSentence();
                    element.withDescription(descriptionText);
                    elementJson.addProperty("description", descriptionText);
                }
                break;
            case HEADER:
                final String headerText = fairy.textProducer().latinSentence();
                element.withHeader(headerText);
                elementJson.addProperty("header", headerText);


                if (fairy.baseProducer().trueOrFalse()) {
                    final String descriptionText = fairy.textProducer().latinSentence();
                    element.withDescription(descriptionText);
                    elementJson.addProperty("description", descriptionText);
                }
                break;
            case TEXT:
                final String descriptionText = fairy.textProducer().latinSentence();
                element.withDescription(descriptionText);
                elementJson.addProperty("description", descriptionText);
                break;
        }

        return new RandomTestData<>(elementJson, element);

    }

}
