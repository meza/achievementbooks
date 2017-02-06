package com.stateshifterlabs.achievementbooks.data;

import java.util.ArrayList;
import java.util.List;

public class Save {
	private List<Integer> done = new ArrayList<Integer>();

	public List<Integer> completedAchievements() {
		return done;
	}

	public void toggle(Integer id) {
		if(done.contains(id)) {
			done.remove(id);
		}

		done.add(id);
	}

}
