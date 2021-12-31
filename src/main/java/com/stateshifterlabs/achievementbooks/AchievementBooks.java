package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import com.stateshifterlabs.achievementbooks.core.data.Loader;
import com.stateshifterlabs.achievementbooks.fabric.AchievementBookFabricItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class AchievementBooks implements ModInitializer {
    public static final String MODID = "achievementbooks";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private final File configDir = new File(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(MODID)));
    private final File demoFile = new File(AchievementBooks.class.getResource("/config/demo.json").getFile());
    @Override
    public void onInitialize() {
        LOGGER.info("Achievement Books initialising");

        Books books = Loader.init(configDir, demoFile);
        for (Book book: books) {
            Identifier identifier = new Identifier(MODID, book.itemName());
            AchievementBookFabricItem item = new AchievementBookFabricItem(book);

            Registry.register(Registry.ITEM, identifier, item);
        }

        LOGGER.info("Achievement Books initialised");
    }
}
