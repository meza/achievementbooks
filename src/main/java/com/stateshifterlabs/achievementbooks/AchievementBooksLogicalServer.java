package com.stateshifterlabs.achievementbooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import com.stateshifterlabs.achievementbooks.core.data.GameSave;
import com.stateshifterlabs.achievementbooks.core.serializers.AchievementDataSerializer;
import com.stateshifterlabs.achievementbooks.fabric.networking.BufferUtilities;
import com.stateshifterlabs.achievementbooks.fabric.networking.ServerActionHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.stateshifterlabs.achievementbooks.AchievementBooks.ACHIEVEMENT_LOAD_PACKET_ID;
import static com.stateshifterlabs.achievementbooks.AchievementBooks.CLIENT_LOGIN_PACKET_ID;

public class AchievementBooksLogicalServer {
    private static final Logger LOGGER = LogManager.getLogger(AchievementBooksLogicalServer.class);
    private final AchievementStorage achievementStorage;
    private final Books books;
    private final GameSave saveHandler;
    private final MinecraftServer server;
    private File saveFile;

    public AchievementBooksLogicalServer(MinecraftServer server, AchievementStorage achievementStorage, Books books) {
        this.server = server;

        this.achievementStorage = achievementStorage;
        this.books = books;
        this.saveFile = setUpSaveFile(server);
        this.saveHandler = new GameSave(this.saveFile, this.achievementStorage, this.books);
        this.saveHandler.load();
        new ServerActionHandler(this.achievementStorage, this.saveHandler);

        ServerPlayNetworking.registerGlobalReceiver(CLIENT_LOGIN_PACKET_ID, this::achievementLoadRequested);
    }

    private void achievementLoadRequested(
            MinecraftServer minecraftServer,
            ServerPlayerEntity serverPlayerEntity,
            ServerPlayNetworkHandler serverPlayNetworkHandler,
            PacketByteBuf packetByteBuf,
            PacketSender packetSender) {

        String playerUUID = BufferUtilities.toString(packetByteBuf);
        minecraftServer.execute(() -> {
            LOGGER.info("Achievements requested for " + playerUUID);
            if (!achievementStorage.hasPlayerData(playerUUID)) {
                LOGGER.info("No achievements found for " + playerUUID);
                return;
            }
            AchievementData data = achievementStorage.forPlayer(playerUUID);
            LOGGER.info("Achievements loaded for " + playerUUID);
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(playerUUID));
            Gson gson = builder.create();
            String json = gson.toJson(data);
            LOGGER.info("Sending " + json + " to " + ACHIEVEMENT_LOAD_PACKET_ID);
            packetSender.sendPacket(ACHIEVEMENT_LOAD_PACKET_ID, BufferUtilities.toBuf(json));
        });

    }

    private File setUpSaveFile(MinecraftServer server) {
        String levelName = server.getSaveProperties().getLevelName();
        Path gameDir = FabricLoader.getInstance().getGameDir();

        if (server.isSingleplayer()) {
            gameDir = gameDir.resolve("saves");
        }

        saveFile = gameDir.resolve(levelName).resolve(AchievementBooks.MODID).resolve("achievementbooks.save.json").toFile();
//        if (!saveFile.exists()) {
//            saveFile.mkdirs();
        try {
            this.saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }
        AchievementBooks.LOGGER.info("Save location: " + saveFile.getAbsolutePath());
        return saveFile;
    }
}
