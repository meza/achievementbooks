package com.stateshifterlabs.achievementbooks.core.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import com.stateshifterlabs.achievementbooks.helpers.generators.AchievementDataGenerator;
import com.stateshifterlabs.achievementbooks.helpers.generators.AchievementStorageGenerator;
import io.codearte.jfairy.Fairy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Type;

import static com.stateshifterlabs.achievementbooks.helpers.Settings.DEFAULT_TEST_ITERATION_COUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AchievementStorageSerializerTest {

	private AchievementStorageGenerator generator;
	private RandomTestData<JsonElement, AchievementStorage> testData;
	private Type typeOfT;
	private JsonDeserializationContext deserializationContext;
	private JsonSerializationContext serializationContext;
	private Fairy fairy = Fairy.create();


	@Before
	public void setUp() {
		generator = new AchievementStorageGenerator();
		typeOfT = Mockito.mock(Type.class);
		deserializationContext = Mockito.mock(JsonDeserializationContext.class);
		serializationContext = Mockito.mock(JsonSerializationContext.class);
	}

	@Test
	public void testDeserializeOnlyAdds() {

		int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);

		for(int i=0; i<numberOfTestIterations; i++) {
			RandomTestData<JsonElement, AchievementStorage> testData = generator.generate();
			AchievementStorageSerializer serializer = new AchievementStorageSerializer(seedStorage(true));
			AchievementStorage actual = serializer.deserialize(testData.jsonFormat(), typeOfT, deserializationContext);

			assertNotEquals("Achievement storage deserialization is not working properly", testData.objectFormat(),
							actual);
		}

	}

	@Test
	public void testDeserialize() {
		int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);

		for(int i=0; i<numberOfTestIterations; i++) {
			RandomTestData<JsonElement, AchievementStorage> testData = generator.generate();
			AchievementStorageSerializer serializer = new AchievementStorageSerializer(seedStorage(false));
			AchievementStorage actual = serializer.deserialize(testData.jsonFormat(), typeOfT, deserializationContext);

			assertEquals("Achievement storage deserialization is not working properly", testData.objectFormat(), actual);
		}

	}

	@Test
	public void testSerialize() {

		int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);

		for(int i=0; i<numberOfTestIterations; i++) {
			int numberOfElements = fairy.baseProducer().randomInt(20);
			RandomTestData<JsonElement, AchievementStorage> testData = generator.generate(numberOfElements);

			AchievementStorageSerializer serializer = new AchievementStorageSerializer(testData.objectFormat());

			JsonElement actualJson = serializer.serialize(testData.objectFormat(), typeOfT, serializationContext);

			//Converting it back to object format, as JSON is unpredictable
			AchievementStorage actual = serializer.deserialize(actualJson, typeOfT, deserializationContext);

			assertEquals("Achievement storage serializer is broken", testData.objectFormat(), actual);
		}

	}

	private AchievementStorage seedStorage(boolean populated) {
		AchievementStorage newStorage = new AchievementStorage();

		if (populated) {
			RandomTestData<JsonElement, AchievementData> xxx = AchievementDataGenerator.generate("dummy");
			newStorage.append(xxx.objectFormat());
		}

		return newStorage;
	}

}
