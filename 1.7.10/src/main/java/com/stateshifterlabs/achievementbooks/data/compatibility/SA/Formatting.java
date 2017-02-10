package com.stateshifterlabs.achievementbooks.data.compatibility.SA;

public class Formatting {
	private boolean isAchievement;
	private boolean isHeader;

	public Formatting(boolean isAchievement, boolean isHeader) {
		this.isAchievement = isAchievement;
		this.isHeader = isHeader;
	}

	public boolean isAchievement() {
		return isAchievement;
	}

	public boolean isHeader() {
		return isHeader;
	}
}
