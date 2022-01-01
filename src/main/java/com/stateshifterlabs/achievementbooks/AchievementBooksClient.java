package com.stateshifterlabs.achievementbooks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class AchievementBooksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AchievementBooks.LOGGER.info("Client initialising");
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(client.player.getName().asString());
            sender.sendPacket(AchievementBooks.CLIENT_LOGIN_PACKET_ID, buf);
            AchievementBooks.LOGGER.info("Client initialised");
        });
    }
}
