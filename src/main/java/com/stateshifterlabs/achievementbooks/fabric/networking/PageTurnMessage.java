package com.stateshifterlabs.achievementbooks.fabric.networking;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.PacketByteBuf;

public class PageTurnMessage {

    private final String bookItemName;
    private final int pageNumber;

    PageTurnMessage(int pageNumber, String bookItemName) {

        this.pageNumber = pageNumber;
        this.bookItemName = bookItemName;
    }

    public static PageTurnMessage decode(PacketByteBuf buf) {
        String json = buf.readString();
        JsonObject data = JsonParser.parseString(json).getAsJsonObject();

        return new PageTurnMessage(data.get("pageNumber").getAsInt(), data.get("bookItemName").getAsString());

    }

    public static PacketByteBuf encode(int pageNumber, String bookItemName) {
        JsonObject data = new JsonObject();
        data.addProperty("pageNumber", pageNumber);
        data.addProperty("bookItemName", bookItemName);
        String json = data.toString();

        return BufferUtilities.toBuf(json);
    }

    public String bookItemName() {
        return bookItemName;
    }

    public int pageNumber() {
        return pageNumber;
    }
}
