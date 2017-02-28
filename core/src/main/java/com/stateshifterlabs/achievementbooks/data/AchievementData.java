package com.stateshifterlabs.achievementbooks.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AchievementData {

	private final String player;
	private Map<String, Save> bookData = new HashMap<String, Save>();

	public AchievementData(String player) {
		this.player = player;
	}

	public void addSaveData(String bookName, Save save) {
		if (bookData.containsKey(bookName)) {
			bookData.remove(bookName);
		}
		bookData.put(bookName, save);
	}

	public String username() {
		return player;
	}


	public List<Integer> completed(String bookName) {
		if(!bookData.containsKey(bookName)) {
			addSaveData(bookName, new Save());
		}
		return bookData.get(bookName).completedAchievements();
	}

	public Set<String> books() {
		return bookData.keySet();
	}

	public void toggle(String bookName, int id) {
		if(!bookData.containsKey(bookName)) {
			addSaveData(bookName, new Save());
		}
		bookData.get(bookName).toggle(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AchievementData that = (AchievementData) o;

		if (!player.equals(that.player)) {
			return false;
		}

		if (bookData.keySet().size() != that.bookData.keySet().size()) {
			return false;
		}

		for(String key : bookData.keySet()) {
			if (!that.bookData.containsKey(key)) {
				return false;
			}

			if(!bookData.get(key).equals(that.bookData.get(key))) {
				return false;
			}
		}
		return true;

	}

	@Override
	public int hashCode() {
		int result = player.hashCode();
		result = 31 * result + bookData.hashCode();
		return result;
	}
}
