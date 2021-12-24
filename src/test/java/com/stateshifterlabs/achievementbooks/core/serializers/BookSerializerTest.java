package com.stateshifterlabs.achievementbooks.core.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import com.stateshifterlabs.achievementbooks.helpers.generators.BookGenerator;
import io.codearte.jfairy.Fairy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Type;

import static com.stateshifterlabs.achievementbooks.helpers.Settings.DEFAULT_TEST_ITERATION_COUNT;
import static org.junit.Assert.assertEquals;

public class BookSerializerTest {

	private BookGenerator generator;
	private Type typeOfT;
	private JsonDeserializationContext deserializationContext;
	private Fairy fairy = Fairy.create();
	private JsonSerializationContext serializationContext;

	@Before
	public void setUp() throws Exception {
		generator = new BookGenerator();
		typeOfT = Mockito.mock(Type.class);
		deserializationContext = Mockito.mock(JsonDeserializationContext.class);
		serializationContext = Mockito.mock(JsonSerializationContext.class);
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

}
