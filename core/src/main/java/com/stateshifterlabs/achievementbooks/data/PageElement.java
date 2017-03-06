package com.stateshifterlabs.achievementbooks.data;

import com.stateshifterlabs.achievementbooks.UTF8Utils;

public class PageElement {
	private String achievement;
	private String description;
	private String header;
	private String mod;
	private boolean checked = false;
	private int id;

	public PageElement(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}

	public String achievement() {
		return achievement;
	}

	public String formattedAchievement() {
		if(!hasAchievement()) {
			return "";
		}
		if (hasMod()) {
			return String.format("%s %s", UTF8Utils.utf8String(achievement), formattedMod());
		}
		return String.format("%s", UTF8Utils.utf8String(achievement));
	}

	public void withAchievement(String achievement) {
		if (!hasAchievement()) {
			this.achievement = achievement;
		}
	}

	public boolean hasAchievement() {
		return achievement() != null;
	}


	public String formattedDescription() {
		if (!hasDescription()) {
			return "";
		}
		return UTF8Utils.utf8String("§o", String.format("%s", description));
	}

	public String description() {
		return description;
	}

	public void withDescription(String description) {
		if (!hasDescription()) {
			this.description = description;
		}
	}

	public boolean hasDescription() {
		return description() != null;
	}


	public String header() {
		return header;
	}

	public String formattedHeader() {
		if (!hasHeader()) {
			return "";
		}
		return UTF8Utils.utf8String("§l", header);
	}

	public void withHeader(String header) {
		if (!hasHeader()) {
			this.header = header;
		}
	}

	public boolean hasHeader() {
		return header() != null;
	}

	public String mod() {
		return mod;
	}

	public String formattedMod() {
		if (!hasMod()) {
			return "";
		}
		return UTF8Utils.utf8String(String.format("§1§o[%s]§r", mod));
	}

	public void withMod(String mod) {
		if (!hasMod()) {
			this.mod = mod;
		}
	}

	public boolean hasMod() {
		return mod() != null;
	}

	public void toggleState() {
		this.checked = !this.checked;
	}

	public void toggleState(boolean checked) {
		this.checked = checked;
	}

	public boolean checked() {
		return this.checked;
	}

	public Type type() {
		if (achievement != null) {
			return Type.ACHIEVEMENT;
		}

		if (header != null) {
			return Type.HEADER;
		}

		return Type.TEXT;
	}


	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PageElement)) {
			return false;
		}

		PageElement that = (PageElement) o;

		if (checked != that.checked) {
			return false;
		}
		if (id != that.id) {
			return false;
		}
		if (achievement != null ? !achievement.equals(that.achievement) : that.achievement != null) {
			return false;
		}
		if (description != null ? !description.equals(that.description) : that.description != null) {
			return false;
		}
		if (header != null ? !header.equals(that.header) : that.header != null) {
			return false;
		}
		return mod != null ? mod.equals(that.mod) : that.mod == null;
	}

	@Override
	public final int hashCode() {
		int result = achievement != null ? achievement.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (header != null ? header.hashCode() : 0);
		result = 31 * result + (mod != null ? mod.hashCode() : 0);
		result = 31 * result + (checked ? 1 : 0);
		result = 31 * result + id;
		return result;
	}

}
