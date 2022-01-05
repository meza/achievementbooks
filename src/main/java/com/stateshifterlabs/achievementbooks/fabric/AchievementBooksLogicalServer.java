package com.stateshifterlabs.achievementbooks.fabric;

import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import com.stateshifterlabs.achievementbooks.core.data.GameSave;
import com.stateshifterlabs.achievementbooks.fabric.networking.ServerActionHandler;
import com.stateshifterlabs.achievementbooks.minecraftdependent.networking.BufferUtilities;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.stateshifterlabs.achievementbooks.AchievementBooks.ACHIEVEMENT_LOAD_PACKET_ID;
import static com.stateshifterlabs.achievementbooks.AchievementBooks.CLIENT_LOGIN_PACKET_ID;
import static com.stateshifterlabs.achievementbooks.minecraftdependent.data.SaveFile.setUpSaveFile;

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
            String json = achievementStorage.jsonForPlayer(playerUUID);
            packetSender.sendPacket(ACHIEVEMENT_LOAD_PACKET_ID, BufferUtilities.toBuf(json));
            unlockRecipesFor(serverPlayerEntity);
        });

    }

    private void unlockRecipesFor(ServerPlayerEntity serverPlayerEntity) {
        serverPlayerEntity.unlockRecipes(AchievementBooks.BookIdentifiers());
    }
}
