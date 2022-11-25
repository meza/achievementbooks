package com.stateshifterlabs.achievementbooks.fabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.server.command.CommandManager.literal;

public class GiveCommand {
    public static int give(CommandContext<ServerCommandSource> c, Books books) throws CommandSyntaxException {
        ServerCommandSource source = c.getSource();

        ServerPlayerEntity player = source.getPlayerOrThrow();
        for (Book book : books) {
            Identifier bookId = new Identifier(AchievementBooks.MODID, book.itemName());
            Item item = Registry.ITEM.get(bookId);
            player.giveItemStack(item.getDefaultStack());
        }
        source.sendFeedback(Text.of("Given all achievement books to " + player.getName().getString()), true);
        return 1;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, Books books) {
        dispatcher.register(
                literal("ab")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(
                                literal("give").executes((command) -> give(command, books))
                        )
        );
    }
}
