package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import com.stateshifterlabs.achievementbooks.core.data.Loader;
import com.stateshifterlabs.achievementbooks.fabric.AchievementBookFabricItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;

public class AchievementBooks implements ModInitializer {
    public static final String MODID = "achievementbooks";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private final File configDir = new File(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(MODID)));
    private final URL demoFile = getClass().getClassLoader().getResource("config/demo.json");

    public static Identifier OPEN_BOOK_SOUND_EVENT_ID = new Identifier(MODID, "open_book");
    public static SoundEvent OPEN_BOOK_SOUND_EVENT = new SoundEvent(OPEN_BOOK_SOUND_EVENT_ID);

    public static Identifier CLOSE_BOOK_SOUND_EVENT_ID = new Identifier(MODID, "close_book");
    public static SoundEvent CLOSE_BOOK_SOUND_EVENT = new SoundEvent(CLOSE_BOOK_SOUND_EVENT_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Achievement Books initialising");

        Registry.register(Registry.SOUND_EVENT, OPEN_BOOK_SOUND_EVENT_ID, OPEN_BOOK_SOUND_EVENT);
        Registry.register(Registry.SOUND_EVENT, CLOSE_BOOK_SOUND_EVENT_ID, CLOSE_BOOK_SOUND_EVENT);

        Books books = Loader.init(configDir, demoFile);
        for (Book book: books) {
            Identifier identifier = new Identifier(MODID, book.itemName());
            AchievementBookFabricItem item = new AchievementBookFabricItem(book);
            Registry.register(Registry.ITEM, identifier, item);
        }

        LOGGER.info("Achievement Books initialised");
    }
}
