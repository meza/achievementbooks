package com.stateshifterlabs.achievementbooks.fabric.networking;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.PacketByteBuf;

public class AchievementToggledMessage {
    private final int achievementId;
    private final String bookItemName;

    AchievementToggledMessage(int achievementId, String bookItemName) {

        this.achievementId = achievementId;
        this.bookItemName = bookItemName;
    }

    public static AchievementToggledMessage decode(PacketByteBuf buf) {
        String json = buf.readString();
        JsonObject data = JsonParser.parseString(json).getAsJsonObject();

        return new AchievementToggledMessage(data.get("achievementId").getAsInt(), data.get("bookItemName").getAsString());

    }

    public static PacketByteBuf encode(int achievementId, String bookItemName) {
        JsonObject data = new JsonObject();
        data.addProperty("achievementId", achievementId);
        data.addProperty("bookItemName", bookItemName);
        String json = data.toString();

        return BufferUtilities.toBuf(json);
    }

    public int achievementId() {
        return achievementId;
    }

    public String bookItemName() {
        return bookItemName;
    }
}
