package com.stateshifterlabs.achievementbooks.core.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.core.serializers.AchievementDataSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AchievementData {

    private final Map<String, Save> bookData = new HashMap<String, Save>();
    private final String player;

    public AchievementData(String player) {
        this.player = player;
    }

    public void addSaveData(String bookName, Save save) {
        bookData.remove(bookName);
        bookData.put(bookName, save);
    }

    public Set<String> books() {
        return bookData.keySet();
    }

    public List<Integer> completed(String bookName) {
        if (!bookData.containsKey(bookName)) {
            addSaveData(bookName, new Save());
        }
        return bookData.get(bookName).completedAchievements();
    }

    @Override
    public final int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (bookData != null ? bookData.hashCode() : 0);
        return result;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AchievementData)) {
            return false;
        }

        AchievementData that = (AchievementData) o;

        if (player != null ? !player.equals(that.player) : that.player != null) {
            return false;
        }
        return bookData != null ? bookData.equals(that.bookData) : that.bookData == null;
    }

    public String toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer());
        Gson gson = builder.create();
        return gson.toJson(this);
    }

    public void toggle(String bookName, int id) {
        if (!bookData.containsKey(bookName)) {
            addSaveData(bookName, new Save());
        }
        bookData.get(bookName).toggle(id);
    }

    public String username() {
        return player;
    }
}
