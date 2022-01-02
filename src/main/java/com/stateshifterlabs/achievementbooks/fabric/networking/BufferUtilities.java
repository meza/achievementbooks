package com.stateshifterlabs.achievementbooks.fabric.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class BufferUtilities {
    public static PacketByteBuf toBuf(String string) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(string);
        return buf;
    }

    public static PacketByteBuf toBuf(PacketByteBuf buf, String string) {
        buf.writeString(string);
        return buf;
    }

    public static String toString(PacketByteBuf buf) {
        return buf.readString();
    }
}
