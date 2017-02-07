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

	public void toggle(Book book, int id) {
		toggle(book.name(), id);
	}

	public void toggle(String bookName, int id) {
		if(!bookData.containsKey(bookName)) {
			addSaveData(bookName, new Save());
		}
		bookData.get(bookName).toggle(id);
	}
}
