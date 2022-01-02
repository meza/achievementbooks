package com.stateshifterlabs.achievementbooks.fabric.networking;

import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.events.BookEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.stateshifterlabs.achievementbooks.AchievementBooks.ACHIEVEMENT_TOGGLE_PACKET_ID;
import static com.stateshifterlabs.achievementbooks.AchievementBooks.PAGE_TURN_PACKET_ID;

@Environment(EnvType.CLIENT)
public class ClientActionDispatcher {
    private static final Logger LOGGER = LogManager.getLogger(ClientActionDispatcher.class);

    public ClientActionDispatcher() {
        LOGGER.info("Registering book listener events on the client");
        BookEvents.PAGE_TURN.register(this::onPageTurn);
        BookEvents.ACHIEVEMENT_TOGGLE.register(this::onAchievementToggled);
    }

    private void onAchievementToggled(int id, Book book) {
        LOGGER.info("Achievement "+id+" toggled for "+book.itemName());
        PacketByteBuf message = AchievementToggledMessage.encode(id, book.itemName());
        LOGGER.info("Sending "+message+" to the channel: "+ ACHIEVEMENT_TOGGLE_PACKET_ID);
        ClientPlayNetworking.send(ACHIEVEMENT_TOGGLE_PACKET_ID, message);
    }

    private void onPageTurn(int newPage, Book book) {
        LOGGER.info("Page turned to: " + newPage + " in book: " + book.itemName());
        PacketByteBuf message = PageTurnMessage.encode(newPage, book.itemName());
        LOGGER.info("Sending "+message+" to the channel: "+ PAGE_TURN_PACKET_ID);
        ClientPlayNetworking.send(PAGE_TURN_PACKET_ID, message);
    }
}
