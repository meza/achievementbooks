package com.stateshifterlabs.achievementbooks.data;

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

	public String achievement() {

		return String.format("%s", achievement);
	}

	public String formattedAchievement() {

		return String.format("§r%s %s", achievement, formattedMod());
	}

	public void withAchievement(String achievement) {
		this.achievement = achievement;
	}

	public void withDescription(String description) {
		this.description = description;
	}

	public void withHeader(String header) {
		this.header = header;
	}

	public void toggleState() {
		this.checked = !this.checked;
	}

	public boolean checked() {
		return this.checked;
	}

	public void withMod(String mod) {
		this.mod = mod;
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

	public String formattedDescription() {
		return String.format("%s", description);
	}

	public String description() {
		return description;
	}

	public String header() {
		return String.format("%s", header);
	}

	public String mod() {
		return mod;
	}

	public String formattedMod() {
		if (null == mod) {
			return "";
		}
		return String.format("§1§o[%s]§r", mod);
	}

	public boolean done() {
		return false;
	}

	public void toggleState(boolean checked) {
		this.checked = checked;
	}

	public int id() {
		return id;
	}

	public boolean hasDescription() {
		return description() != null;
	}


	public enum Type {
		HEADER, ACHIEVEMENT, TEXT
	}

}
