package com.stateshifterlabs.achievementbooks.core.serializers;

import com.google.gson.*;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.errors.JsonParseError;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import com.stateshifterlabs.achievementbooks.helpers.generators.BookGenerator;
import io.codearte.jfairy.Fairy;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.stateshifterlabs.achievementbooks.helpers.Settings.DEFAULT_TEST_ITERATION_COUNT;
import static org.junit.Assert.assertEquals;

public class BookSerializerTest {

    private final Fairy fairy = Fairy.create();
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private JsonDeserializationContext deserializationContext;
    private BookGenerator generator;
    private JsonSerializationContext serializationContext;
    private Type typeOfT;

    @Before
    public void setUp() throws Exception {
        generator = new BookGenerator();
        typeOfT = Mockito.mock(Type.class);
        deserializationContext = Mockito.mock(JsonDeserializationContext.class);
        serializationContext = Mockito.mock(JsonSerializationContext.class);
    }

    @Test
    public void testDeserialize() {

        int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);

        for (int i = 0; i < numberOfTestIterations; i++) {
            RandomTestData<JsonElement, Book> testData = generator.generate();

            BookSerializer serializer = new BookSerializer(null);
            Book actual = serializer.deserialize(testData.jsonFormat(), typeOfT, deserializationContext);

            assertEquals("Book deserializer is broken", testData.objectFormat(), actual);
        }

    }

    @Test(expected = JsonParseError.class)
    public void testDeserializePagesWithNoIds() throws IOException {
        Path fixtureDirectory = Paths.get("src", "test", "resources", "serializer");

        // Has 4 pages, 1 is missing its ID
        File malformedFile = new File(fixtureDirectory.toAbsolutePath() + "/noIdForAPage.json");

        JsonElement jsonElement = JsonParser.parseReader(new FileReader(malformedFile));
        JsonArray pageElements = jsonElement.getAsJsonObject().getAsJsonArray("pages").get(0).getAsJsonArray();
        Assume.assumeTrue(
                "The fixture did not have the expected 4 page elements",
                pageElements.size() == 4
        );

        Assume.assumeFalse(
                "The 2nd page element wasn't missing its ID",
                pageElements.get(1).getAsJsonObject().has("id")
        );

        File conf = tempFolder.getRoot();
        BookSerializer serializer = new BookSerializer(conf);

        serializer.deserialize(jsonElement, typeOfT, deserializationContext);

    }

    @Test(expected = JsonParseError.class)
    public void testDeserializeWithNoBookName() {
        File conf = tempFolder.getRoot();
        BookSerializer serializer = new BookSerializer(conf);

        RandomTestData<JsonElement, Book> testData = generator.generate(Arrays.asList("bookName"));

        serializer.deserialize(testData.jsonFormat(), typeOfT, deserializationContext);

    }

    @Test(expected = JsonParseError.class)
    public void testDeserializeWithNoItemName() {
        File conf = tempFolder.getRoot();
        BookSerializer serializer = new BookSerializer(conf);

        RandomTestData<JsonElement, Book> testData = generator.generate(Arrays.asList("itemName"));

        serializer.deserialize(testData.jsonFormat(), typeOfT, deserializationContext);

    }

    @Test(expected = JsonParseError.class)
    public void testDeserializeWithNoPages() {
        File conf = tempFolder.getRoot();
        BookSerializer serializer = new BookSerializer(conf);

        RandomTestData<JsonElement, Book> testData = generator.generate(Arrays.asList("pages"));

        serializer.deserialize(testData.jsonFormat(), typeOfT, deserializationContext);

    }

    @Test
    public void testSerialize() {
        int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);
        for (int i = 0; i < numberOfTestIterations; i++) {

            RandomTestData<JsonElement, Book> testData = generator.generate(BookGenerator.NO_STATUS);
            BookSerializer serializer = new BookSerializer(null);

            JsonElement actualJson = serializer.serialize(testData.objectFormat(), typeOfT, serializationContext);
            Book actual = serializer.deserialize(actualJson, typeOfT, deserializationContext);

            assertEquals(String.format("Book serializer is broken for book: %s", testData.jsonFormat()),
                    testData.objectFormat(), actual);
        }
    }
}
