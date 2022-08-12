package com.stateshifterlabs.achievementbooks.core.events;

import com.stateshifterlabs.achievementbooks.core.data.Book;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class BookEvents {
    public static final Event<AchievementToggled> ACHIEVEMENT_TOGGLE = EventFactory.createArrayBacked(
            AchievementToggled.class,
            callbacks -> (id, book) -> {
                for (AchievementToggled callback : callbacks) {
                    callback.onAchievementToggled(id, book);
                }
            });
    public static final Event<PageTurned> PAGE_TURN = EventFactory.createArrayBacked(
            PageTurned.class,
            callbacks -> (newPage, book) -> {
                for (PageTurned callback : callbacks) {
                    callback.onPageTurn(newPage, book);
                }
            });

    private BookEvents() {
    }

    @FunctionalInterface
    public interface PageTurned {
        void onPageTurn(int newPage, Book book);
    }

    @FunctionalInterface
    public interface AchievementToggled {
        void onAchievementToggled(int achievementId, Book book);
    }
}
