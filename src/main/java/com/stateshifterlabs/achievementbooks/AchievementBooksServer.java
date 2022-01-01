package com.stateshifterlabs.achievementbooks;

import net.fabricmc.api.DedicatedServerModInitializer;

public class AchievementBooksServer implements DedicatedServerModInitializer {

    public static void onGameStarted() {
        AchievementBooks.LOGGER.info("Server Started");
    }

    @Override
    public void onInitializeServer() {
        AchievementBooks.LOGGER.info("Dedicated Server Started");
    }
}
