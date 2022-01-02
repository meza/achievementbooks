package com.stateshifterlabs.achievementbooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.serializers.AchievementDataSerializer;
import com.stateshifterlabs.achievementbooks.fabric.networking.BufferUtilities;
import com.stateshifterlabs.achievementbooks.fabric.networking.ClientActionDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class AchievementBooksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AchievementBooks.LOGGER.info("Client initialising");
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(client.player.getUuidAsString());
            sender.sendPacket(AchievementBooks.CLIENT_LOGIN_PACKET_ID, buf);
            AchievementBooks.LOGGER.info("Client initialised");

        });
        new ClientActionDispatcher();
        ClientPlayNetworking.registerGlobalReceiver(AchievementBooks.ACHIEVEMENT_LOAD_PACKET_ID, this::loadAchievements);
    }

    private void loadAchievements(
            MinecraftClient minecraftClient,
            ClientPlayNetworkHandler clientPlayNetworkHandler,
            PacketByteBuf packetByteBuf,
            PacketSender packetSender) {

        String json = BufferUtilities.toString(packetByteBuf);
        AchievementBooks.LOGGER.info("Achievement Data received: " + json);
        minecraftClient.execute(() -> {
            String player = minecraftClient.player.getUuidAsString();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(player));
            Gson gson = builder.create();
            AchievementData data = gson.fromJson(json, AchievementData.class);
            AchievementBooks.LOGGER.info(json);
            AchievementBooks.storage().append(data);
        });

    }
}
