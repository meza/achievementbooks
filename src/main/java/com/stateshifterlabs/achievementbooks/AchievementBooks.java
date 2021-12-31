package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import com.stateshifterlabs.achievementbooks.core.data.Loader;
import com.stateshifterlabs.achievementbooks.fabric.AchievementBookFabricItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class AchievementBooks implements ModInitializer {
    public static final String MODID = "achievementbooks";
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private final AchievementStorage storage = new AchievementStorage();
    private File configDir = new File(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(MODID)));
    File demoFile = new File(AchievementBooks.class.getResource("/config/demo.json").getFile());

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        Books books = Loader.init(configDir, demoFile);
        for (Book book: books) {
            Registry.register(
                    Registry.ITEM,
                    new Identifier(MODID, "book"),
                    new AchievementBookFabricItem(book)
            );
        }

        LOGGER.info("Hello Fabric world!");
    }
}
