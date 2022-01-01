package com.stateshifterlabs.achievementbooks.core.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.facade.ByteBufferUtilities;
import com.stateshifterlabs.achievementbooks.core.serializers.AchievementDataSerializer;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import com.stateshifterlabs.achievementbooks.helpers.generators.AchievementDataGenerator;
import io.codearte.jfairy.Fairy;
import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class CompletionDetailsMessageBaseTest {

    private final Fairy fairy = Fairy.create();

    private ByteBufferUtilities byteBufferUtilities;

    @Before
    public void setUp() {
        byteBufferUtilities = mock(ByteBufferUtilities.class);
    }

    @Test
    public void testAchievementDataStorage() {
        CompletionDetailsMessageBase completionDetailsMessageBase =
                new CompletionDetailsMessageBase(byteBufferUtilities);
        AchievementData data = mock(AchievementData.class);

        assumeTrue("The internal achievement data is not null to begin with",
                completionDetailsMessageBase.achievementData() == null);

        completionDetailsMessageBase.withData(data);

        assertEquals("Not the correct achievement data was returned", data,
                completionDetailsMessageBase.achievementData());

    }

    @Test
    public void testFromBuffer() {
        CompletionDetailsMessageBase completionDetailsMessageBase =
                new CompletionDetailsMessageBase(byteBufferUtilities);
        String randomPlayerName = fairy.textProducer().word();
        RandomTestData<JsonElement, AchievementData> data = AchievementDataGenerator.generate(randomPlayerName);

        ByteBuf buffer = mock(ByteBuf.class);
        final String value = data.jsonFormat().toString();
        when(byteBufferUtilities.readUTF8String(buffer)).thenReturn(value);

        completionDetailsMessageBase.fromBytes(buffer);

        assertEquals("Not the correct achievement data was returned", data.objectFormat(),
                completionDetailsMessageBase.achievementData());

    }

    @Test
    public void testToBuffer() {
        CompletionDetailsMessageBase completionDetailsMessageBase =
                new CompletionDetailsMessageBase(byteBufferUtilities);
        String randomPlayerName = fairy.textProducer().word();
        RandomTestData<JsonElement, AchievementData> data = AchievementDataGenerator.generate(randomPlayerName);
        completionDetailsMessageBase.withData(data.objectFormat());

        ByteBuf buffer = mock(ByteBuf.class);
        completionDetailsMessageBase.toBytes(buffer);

        verify(byteBufferUtilities).writeUTF8String(argThat(obj -> obj.equals(buffer)), argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(String argument) {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(randomPlayerName));
                Gson gson = builder.create();
                AchievementData internalData = gson.fromJson(argument, AchievementData.class);
                return data.objectFormat().equals(internalData);
            }
        }));

    }

}
