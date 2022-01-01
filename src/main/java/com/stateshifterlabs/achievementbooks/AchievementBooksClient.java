package com.stateshifterlabs.achievementbooks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AchievementBooksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AchievementBooks.LOGGER.info("Client initialising");
    }
}
