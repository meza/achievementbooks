package com.stateshifterlabs.achievementbooks.core.SA;

public class Formatting {
	private final boolean isAchievement;
	private final boolean isHeader;

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

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Formatting)) {
			return false;
		}

		Formatting that = (Formatting) o;

		if (isAchievement != that.isAchievement) {
			return false;
		}
		return isHeader == that.isHeader;
	}

	@Override
	public final int hashCode() {
		int result = (isAchievement ? 1 : 0);
		result = 31 * result + (isHeader ? 1 : 0);
		return result;
	}
}
