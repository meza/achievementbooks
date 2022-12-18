package com.stateshifterlabs.achievementbooks.fabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.stateshifterlabs.achievementbooks.core.data.Loader;
import com.stateshifterlabs.achievementbooks.core.errors.CouldNotWriteConfigFile;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.File;
import java.net.URL;

import static net.minecraft.server.command.CommandManager.literal;

public class InitCommand {
    public static int init(CommandContext<ServerCommandSource> c, File configDir, URL demoFile) throws CommandSyntaxException {
        ServerCommandSource source = c.getSource();
        try {
            Loader.initDemo(configDir, demoFile, true);
            source.sendFeedback(Text.of("Demo book recreated"), true);
            return 1;
        } catch (CouldNotWriteConfigFile e) {
            source.sendFeedback(Text.of("Could not recreate demo book"), true);
            return 0;
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, File configDir, URL demoFile) {
        dispatcher.register(
                literal("ab")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(
                                literal("init").executes((command) -> init(command, configDir, demoFile))
                        )
        );
    }

}
