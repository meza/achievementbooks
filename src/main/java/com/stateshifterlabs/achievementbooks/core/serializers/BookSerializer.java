package com.stateshifterlabs.achievementbooks.core.serializers;

import com.google.gson.*;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Page;
import com.stateshifterlabs.achievementbooks.core.data.PageElement;
import com.stateshifterlabs.achievementbooks.core.errors.JsonParseError;

import java.io.File;
import java.lang.reflect.Type;

public class BookSerializer implements JsonSerializer<Book>, JsonDeserializer<Book> {
    private final File conf;

    public BookSerializer(File conf) {
        this.conf = conf;
    }

    @Override
    public Book deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context
    ) throws RuntimeException {

        Book book = new Book();

        JsonObject bookObject = json.getAsJsonObject();

        if (!bookObject.has("bookName")) {
            throw new JsonParseError("bookName parameter is required, but not set", conf);
        }
        book.withName(bookObject.get("bookName").getAsString());

        if (!bookObject.has("itemName")) {
            throw new JsonParseError("itemName parameter is required, but not set", conf);
        }

        book.withItemName(bookObject.get("itemName").getAsString());
        if (bookObject.has("color")) {
            book.withColour(bookObject.get("color").getAsString());
        }
        if (bookObject.has("craftingMaterial")) {
            book.withMaterial(bookObject.get("craftingMaterial").getAsString());
        }

        if (!bookObject.has("pages")) {
            throw new JsonParseError("pages parameter is required, but not set", conf);
        }

        final JsonArray bookJson = bookObject.getAsJsonArray("pages");

        for (int i = 0; i < bookJson.size(); i++) {
            Page page = new Page();
            JsonArray pageElements = bookJson.get(i).getAsJsonArray();

            for (int j = 0; j < pageElements.size(); j++) {
                JsonObject pageJson = pageElements.get(j).getAsJsonObject();
                if (!pageJson.has("id")) {
                    continue;
                }
                PageElement element = new PageElement(pageJson.get("id").getAsInt());

                if (pageJson.has("achievement")) {
                    element.withAchievement(pageJson.get("achievement").getAsString());
                    if (pageJson.has("checked")) {
                        element.toggleState(pageJson.get("checked").getAsBoolean());
                    }
                }

                if (pageJson.has("description")) {
                    element.withDescription(pageJson.get("description").getAsString());
                }

                if (pageJson.has("header")) {
                    element.withHeader(pageJson.get("header").getAsString());
                }

                if (pageJson.has("mod")) {
                    element.withMod(pageJson.get("mod").getAsString());
                }

                page.addElement(element);

            }

            book.addPage(page);
        }

        return book;

    }

    @Override
    public JsonElement serialize(
            Book src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context
    ) {

        JsonObject book = new JsonObject();
        book.addProperty("itemName", src.itemName());
        book.addProperty("bookName", src.name());
        if (src.isCraftable()) {
            book.addProperty("craftingMaterial", src.material());
        }
        book.addProperty("color", src.colour());

        JsonArray pages = new JsonArray();

        for (int i = 0; i < src.pageCount(); i++) {

            JsonArray pageArray = new JsonArray();
            Page page = src.openPage(i);

            for (PageElement pageElement : page.elements()) {
                JsonObject element = new JsonObject();
                element.addProperty("id", pageElement.id());

                switch (pageElement.type()) {
                    case ACHIEVEMENT:
                        element.addProperty("achievement", pageElement.achievement());
                        if (pageElement.mod() != null) {
                            element.addProperty("mod", pageElement.mod());
                        }
                        if (pageElement.hasDescription()) {
                            element.addProperty("description", pageElement.description());
                        }
                        break;
                    case HEADER:
                        element.addProperty("header", pageElement.header());
                        if (pageElement.hasDescription()) {
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
