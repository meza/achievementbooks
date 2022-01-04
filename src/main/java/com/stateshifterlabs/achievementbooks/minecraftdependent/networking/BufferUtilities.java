package com.stateshifterlabs.achievementbooks.minecraftdependent.networking;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;

public class BufferUtilities {
    public static PacketByteBuf toBuf(String string) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
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
