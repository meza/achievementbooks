package com.stateshifterlabs.achievementbooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import com.stateshifterlabs.achievementbooks.core.serializers.AchievementDataSerializer;
import com.stateshifterlabs.achievementbooks.fabric.AchievementBookFabricItem;
import com.stateshifterlabs.achievementbooks.fabric.networking.BufferUtilities;
import com.stateshifterlabs.achievementbooks.fabric.networking.ClientActionDispatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.stateshifterlabs.achievementbooks.AchievementBooks.*;

@Environment(EnvType.CLIENT)
public class AchievementBooksClient {
    private static final Logger LOGGER = LogManager.getLogger(AchievementBooksClient.class);
    private final AchievementStorage achievementStorage;
    private final Books books;

    public AchievementBooksClient(AchievementStorage achievementStorage, Books books) {

        this.achievementStorage = achievementStorage;
        this.books = books;

        LOGGER.debug("Client initialising");

        new ClientActionDispatcher();

        LOGGER.debug("Registering JOIN event handler");
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            client.execute(() -> {
                LOGGER.debug("JOIN event handler invoked on the client");
                PacketByteBuf buf = PacketByteBufs.create();
                String playerUUID = client.player.getUuidAsString();
                buf.writeString(playerUUID);
                LOGGER.debug("Sending " + playerUUID + " to the channel: " + CLIENT_LOGIN_PACKET_ID);
                sender.sendPacket(CLIENT_LOGIN_PACKET_ID, buf);
            });
        });

        LOGGER.debug("Registering the " + ACHIEVEMENT_LOAD_PACKET_ID + " handler in the client");
        ClientPlayNetworking.registerGlobalReceiver(ACHIEVEMENT_LOAD_PACKET_ID, this::loadAchievements);

    }

    private void loadAchievements(
            MinecraftClient minecraftClient,
            ClientPlayNetworkHandler clientPlayNetworkHandler,
            PacketByteBuf packetByteBuf,
            PacketSender packetSender) {

        LOGGER.debug("The " + ACHIEVEMENT_LOAD_PACKET_ID + " handler has been invoked on the client");
        String json = BufferUtilities.toString(packetByteBuf);
        LOGGER.debug("Achievement Data received: " + json);
        minecraftClient.execute(() -> {
            LOGGER.debug("Executing the worker task on the received achievement data");

            String player = minecraftClient.player.getUuidAsString();
            LOGGER.debug("Got the UUID for player: " + minecraftClient.player.getName().getContent() + " to be: " + player);

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(player));
            Gson gson = builder.create();
            AchievementData data = gson.fromJson(json, AchievementData.class);
            achievementStorage.append(data);
            LOGGER.debug("Appended the new data to the achievement storage");

            for (String bookItemName : achievementStorage.forPlayer(player).books()) {
                AchievementBookFabricItem bookItem = (AchievementBookFabricItem) Registries.ITEM.get(new Identifier(MODID, bookItemName));
                bookItem.updateBook(data);
            }

            LOGGER.debug("Books for player: ");
            LOGGER.debug(achievementStorage.forPlayer(player).books());
        });

    }
}
