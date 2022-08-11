package com.stateshifterlabs.achievementbooks.fabric.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.UTF8Utils;
import com.stateshifterlabs.achievementbooks.core.data.Book;
import com.stateshifterlabs.achievementbooks.core.data.Books;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class ListCommand {
    public static int list(CommandContext<ServerCommandSource> c, Books books) throws CommandSyntaxException {
        ServerCommandSource source = c.getSource();

        String player = source.getPlayerOrThrow().getName().getContent().toString();
        source.sendFeedback(Text.of("Currently loaded Achivement Books:\n"), false);
        for (Book book : books) {
            String bookIdRaw = AchievementBooks.MODID + ":" + book.itemName();
            Text bookId = Text.of("( " + bookIdRaw + " )");
            Style b = bookId.getStyle()
                    .withUnderline(true)
                    .withColor(Formatting.BLUE)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/give " + player + " " + bookIdRaw));

            String txt = UTF8Utils.utf8String(" - ", book.name(), " ");
            Text toSend = Text.of(txt + bookId.getWithStyle(b));
            source.sendFeedback(toSend, false);
        }
        return 1;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, Books books) {
        dispatcher.register(
                literal("ab")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(
                                literal("list").executes((command) -> list(command, books))
                        )
        );
    }
}
