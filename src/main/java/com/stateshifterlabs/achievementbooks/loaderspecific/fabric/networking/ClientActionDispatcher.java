package com.stateshifterlabs.achievementbooks.loaderspecific.fabric.networking;

import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.loaderspecific.fabric.events.BookEvents;
import com.stateshifterlabs.achievementbooks.minecraftdependent.networking.AchievementToggledMessage;
import com.stateshifterlabs.achievementbooks.minecraftdependent.networking.PageTurnMessage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.stateshifterlabs.achievementbooks.AchievementBooks.ACHIEVEMENT_TOGGLE_PACKET_ID;
import static com.stateshifterlabs.achievementbooks.AchievementBooks.PAGE_TURN_PACKET_ID;

public class ClientActionDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(ClientActionDispatcher.class);

    public ClientActionDispatcher() {
        LOGGER.debug("Registering book listener events on the client");
        BookEvents.PAGE_TURN.register(this::onPageTurn);
        BookEvents.ACHIEVEMENT_TOGGLE.register(this::onAchievementToggled);
    }

    private void onAchievementToggled(int id, Book book) {
        LOGGER.debug("Achievement " + id + " toggled for " + book.itemName());
        PacketByteBuf message = AchievementToggledMessage.encode(id, book.itemName());
        LOGGER.debug("Sending " + message + " to the channel: " + ACHIEVEMENT_TOGGLE_PACKET_ID);
        ClientPlayNetworking.send(ACHIEVEMENT_TOGGLE_PACKET_ID, message);
    }

    private void onPageTurn(int newPage, Book book) {
        LOGGER.debug("Page turned to: " + newPage + " in book: " + book.itemName());
        PacketByteBuf message = PageTurnMessage.encode(newPage, book.itemName());
        LOGGER.debug("Sending " + message + " to the channel: " + PAGE_TURN_PACKET_ID);
        ClientPlayNetworking.send(PAGE_TURN_PACKET_ID, message);
    }
}
