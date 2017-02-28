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
			return;
		}

		done.add(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Save save = (Save) o;

		return done != null ? done.equals(save.done) : save.done == null;

	}

	@Override
	public int hashCode() {
		return done != null ? done.hashCode() : 0;
	}
}
