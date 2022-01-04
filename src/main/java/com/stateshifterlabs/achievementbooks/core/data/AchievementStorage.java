package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.core.facade.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementStorage {
    private static final Logger LOGGER = LogManager.getLogger(AchievementStorage.class);

    private final Map<String, AchievementData> storage = new HashMap<String, AchievementData>();

    public void append(AchievementData data) {
        storage.remove(data.username());
        storage.put(data.username(), data);
    }

    public void clear() {
        storage.clear();
    }

    public AchievementData forPlayer(String name) {
        if (storage.containsKey(name)) {
            return storage.get(name);
        }

        final AchievementData achievementData = new AchievementData(name);
        this.append(achievementData);

        return achievementData;
    }

    public AchievementData forPlayer(Player player) {
        final String name = player.getDisplayName();
        return forPlayer(name);
    }

    public boolean hasPlayerData(String name) {
        return storage.containsKey(name);
    }

    @Override
    public final int hashCode() {
        return storage != null ? storage.hashCode() : 0;
    }

    @Override
    public final boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof AchievementStorage)) {
            return false;
        }

        AchievementStorage that = (AchievementStorage) o;

        return storage != null ? storage.equals(that.storage) : that.storage == null;
    }

    public List<String> players() {
        if (storage.size() == 0) {
            return new ArrayList<String>();
        }
        return new ArrayList<String>(storage.keySet());
    }

    public int size() {
        return storage.size();
    }

    public String jsonForPlayer(String playerUUID) {
        LOGGER.debug("Achievements requested for " + playerUUID);
        if (!this.hasPlayerData(playerUUID)) {
            this.append(new AchievementData(playerUUID));
        }
        AchievementData data = this.forPlayer(playerUUID);
        LOGGER.debug("Achievements loaded for " + playerUUID);
        return data.toJson();
    }
}
