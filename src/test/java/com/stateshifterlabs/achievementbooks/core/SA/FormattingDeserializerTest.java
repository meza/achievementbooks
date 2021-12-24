package com.stateshifterlabs.achievementbooks.core.SA;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import com.stateshifterlabs.achievementbooks.helpers.generators.FormattingGenerator;
import io.codearte.jfairy.Fairy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Type;

import static com.stateshifterlabs.achievementbooks.helpers.Settings.DEFAULT_TEST_ITERATION_COUNT;
import static org.junit.Assert.assertEquals;

public class FormattingDeserializerTest {

	private Fairy fairy = Fairy.create();
	private Type typeOfT;
	private JsonDeserializationContext deserializationContext;
	private RandomTestData<JsonElement, FormattingList> testData;
	private FormattingGenerator generator;

	@Before
	public void setUp() {
		typeOfT = Mockito.mock(Type.class);
		deserializationContext = Mockito.mock(JsonDeserializationContext.class);
		generator = new FormattingGenerator();
	}

	@Test
	public void testDeserializer() {

		int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);

		for (int i = 0; i < numberOfTestIterations; i++) {
			testData = generator.generate();
			FormattingDeserializer deserializer = new FormattingDeserializer();
			FormattingList actual = deserializer.deserialize(testData.jsonFormat(), typeOfT, deserializationContext);
			assertEquals("Formatting config deserializer is not working", testData.objectFormat(), actual);
		}

	}

}
