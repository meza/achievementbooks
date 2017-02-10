package com.stateshifterlabs.achievementbooks.data;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AchievementStorage {

	private Map<String, AchievementData> storage = new HashMap<String, AchievementData>();

	public void append(AchievementData data) {
		if (storage.containsKey(data.username())) {
			storage.remove(data.username());
		}
		storage.put(data.username(), data);
	}

	public AchievementData forPlayer(EntityPlayer player) {
		final String name = player.getDisplayName();
		return forPlayer(name);
	}

	public Set<String> players() {
		return storage.keySet();
	}

	public AchievementData forPlayer(String name) {
		if (storage.containsKey(name)) {
			return storage.get(name);
		}

		final AchievementData achievementData = new AchievementData(name);
		this.append(achievementData);

		return achievementData;
	}

	public void clear() {
		storage.clear();
	}
}
