package com.stateshifterlabs.achievementbooks.core.serializers;

import com.google.gson.*;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Books;

import java.lang.reflect.Type;

public class BooksSerializer implements JsonSerializer<Books>, JsonDeserializer<Books> {

    private final BookSerializer bookSerializer = new BookSerializer(null);

    @Override
    public Books deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Books books = new Books();

        for (JsonElement bookJson : json.getAsJsonArray()) {
            books.addBook(bookSerializer.deserialize(bookJson, typeOfT, context));
        }

        return books;
    }

    @Override
    public JsonElement serialize(Books src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray books = new JsonArray();

        for (Book book : src) {
            books.add(bookSerializer.serialize(book, typeOfSrc, context));
        }

        return books;
    }
}
