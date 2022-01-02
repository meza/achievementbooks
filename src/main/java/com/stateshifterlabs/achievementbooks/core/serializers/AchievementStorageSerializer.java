package com.stateshifterlabs.achievementbooks.core.serializers;

import com.google.gson.*;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;

import java.lang.reflect.Type;

public class AchievementStorageSerializer implements JsonSerializer<AchievementStorage>,
        JsonDeserializer<AchievementStorage> {

    private final AchievementStorage storage;

    public AchievementStorageSerializer(AchievementStorage storage) {

        this.storage = storage;
    }

    @Override
    public AchievementStorage deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context
    ) throws JsonParseException {

        JsonArray storageJson = json.getAsJsonArray();

        for (JsonElement userSave : storageJson) {

            String thePlayer = userSave.getAsJsonObject().get("name").getAsString();
            AchievementDataSerializer ser = new AchievementDataSerializer(thePlayer);
            AchievementData data = ser.deserialize(userSave, AchievementData.class, context);

            storage.append(data);
        }

        return storage;

    }

    @Override
    public JsonElement serialize(
            AchievementStorage src, Type typeOfSrc, JsonSerializationContext context
    ) {

        JsonArray storage = new JsonArray();

        for (String player : src.players()) {
            AchievementData data = src.forPlayer(player);

            AchievementDataSerializer dataSer = new AchievementDataSerializer(player);
            JsonElement playerJson = dataSer.serialize(data, AchievementData.class, context);

            storage.add(playerJson);
        }

        return storage;
    }
}
