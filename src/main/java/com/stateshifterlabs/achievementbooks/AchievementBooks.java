package com.stateshifterlabs.achievementbooks;

import com.stateshifterlabs.achievementbooks.fabric.AchievementBookFabricItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AchievementBooks implements ModInitializer {
    public static final String MODID = "achievementbooks";
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        Registry.register(
                Registry.ITEM,
                new Identifier(MODID, "book"),
                new AchievementBookFabricItem()
        );

        LOGGER.info("Hello Fabric world!");
    }
}
