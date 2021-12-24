package com.stateshifterlabs.achievementbooks.core.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AchievementData {

	private final String player;
	private final Map<String, Save> bookData = new HashMap<String, Save>();

	public AchievementData(String player) {
		this.player = player;
	}

	public String username() {
		return player;
	}

	public Set<String> books() {
		return bookData.keySet();
	}

	public void addSaveData(String bookName, Save save) {
		if (bookData.containsKey(bookName)) {
			bookData.remove(bookName);
		}
		bookData.put(bookName, save);
	}

	public List<Integer> completed(String bookName) {
		if(!bookData.containsKey(bookName)) {
			addSaveData(bookName, new Save());
		}
		return bookData.get(bookName).completedAchievements();
	}

	public void toggle(String bookName, int id) {
		if(!bookData.containsKey(bookName)) {
			addSaveData(bookName, new Save());
		}
		bookData.get(bookName).toggle(id);
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

	@Override
	public final int hashCode() {
		int result = player != null ? player.hashCode() : 0;
		result = 31 * result + (bookData != null ? bookData.hashCode() : 0);
		return result;
	}
}
