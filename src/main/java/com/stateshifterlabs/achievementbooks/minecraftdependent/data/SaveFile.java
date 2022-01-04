package com.stateshifterlabs.achievementbooks.minecraftdependent.data;

import com.stateshifterlabs.achievementbooks.AchievementBooks;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SaveFile {
    public static File setUpSaveFile(MinecraftServer server) {
        File saveFile;
        String levelName = server.getSaveProperties().getLevelName();
        Path gameDir = FabricLoader.getInstance().getGameDir();

        if (server.isSingleplayer()) {
            gameDir = gameDir.resolve("saves");
        }

        saveFile = gameDir.resolve(levelName).resolve(AchievementBooks.MODID).resolve("achievementbooks.save.json").toFile();
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AchievementBooks.LOGGER.debug("Save location: " + saveFile.getAbsolutePath());
        return saveFile;
    }
}
