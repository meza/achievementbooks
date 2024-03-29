package com.stateshifterlabs.achievementbooks.core.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import com.stateshifterlabs.achievementbooks.helpers.generators.AchievementDataGenerator;
import io.codearte.jfairy.Fairy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Type;

import static com.stateshifterlabs.achievementbooks.helpers.Settings.DEFAULT_TEST_ITERATION_COUNT;
import static org.junit.Assert.assertEquals;

public class AchievementDataSerializerTest {

    private final Fairy fairy = Fairy.create();
    private JsonDeserializationContext deserializationContext;
    private String player;
    private JsonSerializationContext serializationContext;
    private AchievementDataSerializer serializer;
    private Type typeOfT;

    @Before
    public void setUp() {
        player = randomPlayerName();
        typeOfT = Mockito.mock(Type.class);
        deserializationContext = Mockito.mock(JsonDeserializationContext.class);
        serializationContext = Mockito.mock(JsonSerializationContext.class);
        serializer = new AchievementDataSerializer(player);
    }

    @Test
    public void testDeserialize() {

        int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);

        for (int i = 0; i < numberOfTestIterations; i++) {
            RandomTestData<JsonElement, AchievementData> testData = AchievementDataGenerator.generate(player);
            AchievementData actual = serializer.deserialize(testData.jsonFormat(), typeOfT, deserializationContext);

            assertEquals("Achievement Data couldn't be deserialized properly", testData.objectFormat(), actual);
        }
    }

    @Test

    public void testSerialize() {

        int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);

        for (int i = 0; i < numberOfTestIterations; i++) {
            RandomTestData<JsonElement, AchievementData> testData = AchievementDataGenerator.generate(player);
            JsonElement actualJson = serializer.serialize(testData.objectFormat(), typeOfT, serializationContext);
            //need to convert back to a comparable object. The JSON Format is unpredictable
            AchievementData actualResult = serializer.deserialize(actualJson, typeOfT, deserializationContext);

            assertEquals("Achievement Data couldn't be serialized properly", testData.objectFormat(), actualResult);
        }

    }

    private String randomPlayerName() {
        return fairy.textProducer().latinWord();
    }

}
