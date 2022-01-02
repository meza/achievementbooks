package com.stateshifterlabs.achievementbooks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stateshifterlabs.achievementbooks.core.data.*;
import com.stateshifterlabs.achievementbooks.fabric.AchievementBookFabricItem;
import com.stateshifterlabs.achievementbooks.fabric.networking.ServerActionHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AchievementBooks implements ModInitializer {

    public static final String MODID = "achievementbooks";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final Identifier CLIENT_LOGIN_PACKET_ID = new Identifier(MODID, "client_login");
    public static final Identifier ACHIEVEMENT_TOGGLE_PACKET_ID = new Identifier(MODID, "achievement_toggle");
    public static final Identifier ACHIEVEMENT_LOAD_PACKET_ID = new Identifier(MODID, "achievement_load");
    public static final Identifier PAGE_TURN_PACKET_ID = new Identifier(MODID, "page_turn");

    private final File configDir = new File(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(MODID)));
    private final URL demoFile = getClass().getClassLoader().getResource("config/demo.json");

    public static Identifier OPEN_BOOK_SOUND_EVENT_ID = new Identifier(MODID, "open_book");
    public static SoundEvent OPEN_BOOK_SOUND_EVENT = new SoundEvent(OPEN_BOOK_SOUND_EVENT_ID);

    public static Identifier CLOSE_BOOK_SOUND_EVENT_ID = new Identifier(MODID, "close_book");
    public static SoundEvent CLOSE_BOOK_SOUND_EVENT = new SoundEvent(CLOSE_BOOK_SOUND_EVENT_ID);

    public static Identifier TICK_SOUND_EVENT_ID = new Identifier(MODID, "tick");
    public static SoundEvent TICK_SOUND_EVENT = new SoundEvent(TICK_SOUND_EVENT_ID);

    public static Identifier RUB_SOUND_EVENT_ID = new Identifier(MODID, "rub");
    public static SoundEvent RUB_SOUND_EVENT = new SoundEvent(RUB_SOUND_EVENT_ID);

    private static final Map<Identifier, JsonObject> Recipes = new HashMap<>();
    private final AchievementStorage achievementStorage = new AchievementStorage();
    private GameSave saveHandler;
    private Books books;
    private static File saveFile;

    @Override
    public void onInitialize() {
        LOGGER.info("Achievement Books initialising");

        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);

        Registry.register(Registry.SOUND_EVENT, OPEN_BOOK_SOUND_EVENT_ID, OPEN_BOOK_SOUND_EVENT);
        Registry.register(Registry.SOUND_EVENT, CLOSE_BOOK_SOUND_EVENT_ID, CLOSE_BOOK_SOUND_EVENT);

        this.books = Loader.init(configDir, demoFile);

        saveHandler = new GameSave(achievementStorage, books);

        for (Book book : books) {
            Identifier identifier = new Identifier(MODID, book.itemName());
            AchievementBookFabricItem item = new AchievementBookFabricItem(book, achievementStorage);
            Registry.register(Registry.ITEM, identifier, item);

            if (book.isCraftable()) {
                AchievementBooks.Recipes.put(identifier, getRecipeFor(book));
            }

        }
        new ServerActionHandler(achievementStorage, saveHandler);
        LOGGER.info("Achievement Books initialised");
    }

    private void onServerStarting(MinecraftServer minecraftServer) {
        LOGGER.info("[SERVER] Server Starting");
    }

    private void onServerStarted(MinecraftServer minecraftServer) {
        String levelName = minecraftServer.getSaveProperties().getLevelName();
        Path gameDir = FabricLoader.getInstance().getGameDir();

        if (minecraftServer.isSingleplayer()) {
            gameDir = gameDir.resolve("saves");
        }

        saveFile = gameDir.resolve(levelName).resolve("achievementbooks.json").toFile();
        saveHandler.load(saveFile);
    }

    public static File saveFile() {
        return saveFile;
    }

    public static Map<Identifier, JsonObject> bookRecipes() {
        return Recipes;
    }

    private JsonObject getRecipeFor(Book book) {
        String template = String.format("{\n" +
                        "  \"type\": \"minecraft:crafting_shapeless\",\n" +
                        "  \"ingredients\": [\n" +
                        "    {\n" +
                        "      \"item\": \"minecraft:book\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"item\": \"%s\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"result\": {\n" +
                        "    \"item\": \"%s:%s\"\n" +
                        "  }\n" +
                        "}",
                book.material(),
                MODID,
                book.itemName()
        );

        JsonElement result = JsonParser.parseString(template);

        return result.getAsJsonObject();
    }
}
