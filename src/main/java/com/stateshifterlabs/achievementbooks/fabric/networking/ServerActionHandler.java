package com.stateshifterlabs.achievementbooks.fabric.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.data.AchievementData;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.core.data.GameSave;
import com.stateshifterlabs.achievementbooks.core.serializers.AchievementDataSerializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.stateshifterlabs.achievementbooks.AchievementBooks.ACHIEVEMENT_LOAD_PACKET_ID;

public class ServerActionHandler {
    private final AchievementStorage achievementStorage;
    private final GameSave saveHandler;

    public ServerActionHandler(AchievementStorage achievementStorage, GameSave saveHandler) {
        this.achievementStorage = achievementStorage;
        this.saveHandler = saveHandler;

        ServerPlayNetworking.registerGlobalReceiver(AchievementBooks.PAGE_TURN_PACKET_ID, this::onPageTurn);
        ServerPlayNetworking.registerGlobalReceiver(AchievementBooks.ACHIEVEMENT_TOGGLE_PACKET_ID, this::onAchievementToggled);
    }

    private void onAchievementToggled(
            MinecraftServer minecraftServer,
            ServerPlayerEntity serverPlayerEntity,
            ServerPlayNetworkHandler serverPlayNetworkHandler,
            PacketByteBuf packetByteBuf,
            PacketSender packetSender) {
        AchievementToggledMessage message = AchievementToggledMessage.decode(packetByteBuf);
        minecraftServer.execute(() -> {
            AchievementData data = achievementStorage.forPlayer(serverPlayerEntity.getUuidAsString());
            data.toggle(message.bookItemName(), message.achievementId());
            saveHandler.save();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(serverPlayerEntity.getUuidAsString()));
            Gson gson = builder.create();
            AchievementBooks.LOGGER.debug("About to send the achievement data");
            packetSender.sendPacket(ACHIEVEMENT_LOAD_PACKET_ID, BufferUtilities.toBuf(gson.toJson(data)));
        });
    }

    private void onPageTurn(
            MinecraftServer minecraftServer,
            ServerPlayerEntity serverPlayerEntity,
            ServerPlayNetworkHandler serverPlayNetworkHandler,
            PacketByteBuf packetByteBuf,
            PacketSender packetSender
    ) {
        PageTurnMessage message = PageTurnMessage.decode(packetByteBuf);
        minecraftServer.execute(() -> {
            ItemStack itemStack = serverPlayerEntity.getStackInHand(serverPlayerEntity.getActiveHand());
            NbtCompound nbt = itemStack.getOrCreateNbt();
            nbt.putInt(AchievementBooks.MODID + ":" + message.bookItemName() + ":pageOffset", message.pageNumber());
        });
    }
}
