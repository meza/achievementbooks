package com.stateshifterlabs.achievementbooks.fabric.networking;

import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.events.BookEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class ClientActionDispatcher {
    public ClientActionDispatcher() {
        BookEvents.PAGE_TURN.register(this::onPageTurn);
        BookEvents.ACHIEVEMENT_TOGGLE.register(this::onAchievementToggled);
    }

    private void onAchievementToggled(int id, Book book) {
        PacketByteBuf message = AchievementToggledMessage.encode(id, book.itemName());
        ClientPlayNetworking.send(AchievementBooks.ACHIEVEMENT_TOGGLE_PACKET_ID, message);
    }

    private void onPageTurn(int newPage, Book book) {
        AchievementBooks.LOGGER.info("Page turned to: " + newPage + " in book: " + book.name());
        PacketByteBuf message = PageTurnMessage.encode(newPage, book.itemName());
        ClientPlayNetworking.send(AchievementBooks.PAGE_TURN_PACKET_ID, message);
    }
}
