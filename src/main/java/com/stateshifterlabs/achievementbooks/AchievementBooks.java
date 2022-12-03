package com.stateshifterlabs.achievementbooks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stateshifterlabs.achievementbooks.core.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import com.stateshifterlabs.achievementbooks.core.data.Loader;
import com.stateshifterlabs.achievementbooks.fabric.AchievementBookFabricItem;
import com.stateshifterlabs.achievementbooks.fabric.commands.GiveCommand;
import com.stateshifterlabs.achievementbooks.fabric.commands.ListCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AchievementBooks implements ModInitializer, ClientModInitializer {

    public static final String MODID = "achievementbooks";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final Identifier CLIENT_LOGIN_PACKET_ID = new Identifier(MODID, "client_login");
    public static final Identifier ACHIEVEMENT_TOGGLE_PACKET_ID = new Identifier(MODID, "achievement_toggle");
    public static final Identifier ACHIEVEMENT_LOAD_PACKET_ID = new Identifier(MODID, "achievement_load");
    public static final Identifier CLOSE_BOOK_SOUND_EVENT_ID = new Identifier(MODID, "close_book");
    public static final SoundEvent CLOSE_BOOK_SOUND_EVENT = new SoundEvent(CLOSE_BOOK_SOUND_EVENT_ID);
    public static final Identifier OPEN_BOOK_SOUND_EVENT_ID = new Identifier(MODID, "open_book");
    public static final SoundEvent OPEN_BOOK_SOUND_EVENT = new SoundEvent(OPEN_BOOK_SOUND_EVENT_ID);
    public static final Identifier PAGE_TURN_PACKET_ID = new Identifier(MODID, "page_turn");
    public static final Identifier RUB_SOUND_EVENT_ID = new Identifier(MODID, "rub");
    public static final SoundEvent RUB_SOUND_EVENT = new SoundEvent(RUB_SOUND_EVENT_ID);
    public static final Identifier TICK_SOUND_EVENT_ID = new Identifier(MODID, "tick");
    public static final SoundEvent TICK_SOUND_EVENT = new SoundEvent(TICK_SOUND_EVENT_ID);
    private static final Map<Identifier, JsonObject> Recipes = new HashMap<>();

    private final AchievementStorage achievementStorage = new AchievementStorage();
    private final File configDir = new File(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve(MODID)));
    private final URL demoFile = getClass().getClassLoader().getResource("config/demo.json");
    private Books books;

    public static Map<Identifier, JsonObject> bookRecipes() {
        return Recipes;
    }

    @Override
    public void onInitialize() {
        LOGGER.debug("Achievement Books main initialising");
        ServerLifecycleEvents.SERVER_STARTED.register(this::onInitializeLogicalServer);

        this.books = Loader.init(configDir, demoFile);

        this.registerAssets();
        this.registerCommands();
        LOGGER.debug("Achievement Books initialised");
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {
        new AchievementBooksClient(achievementStorage, books);
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

    private void onInitializeLogicalServer(MinecraftServer minecraftServer) {
        LOGGER.debug("SERVER STARTING");
        new AchievementBooksLogicalServer(minecraftServer, achievementStorage, books);

    }

    private void registerAssets() {

        Registry.register(Registries.SOUND_EVENT, OPEN_BOOK_SOUND_EVENT_ID, OPEN_BOOK_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, CLOSE_BOOK_SOUND_EVENT_ID, CLOSE_BOOK_SOUND_EVENT);



        for (Book book : books) {
            Identifier identifier = new Identifier(MODID, book.itemName());
            AchievementBookFabricItem item = new AchievementBookFabricItem(book);

            Registry.register(Registries.ITEM, identifier, item);

            if (book.isCraftable()) {
                AchievementBooks.Recipes.put(identifier, getRecipeFor(book));
            }

        }
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ListCommand.register(dispatcher, books);
            GiveCommand.register(dispatcher, books);
        });
    }
}
