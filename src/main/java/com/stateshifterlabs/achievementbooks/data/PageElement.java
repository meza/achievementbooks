package com.stateshifterlabs.achievementbooks.data;

public class PageElement {

	private String achievement;
	private String description;
	private String header;
	private String mod;

	public PageElement() {
	}

	public String achievement() {
		return String.format("%s", achievement);
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

	public String description() {
		return String.format("%s", description);
	}

	public String header() {
		return String.format("%s", header);
	}

	public String mod() {
		if (null == mod) {
			return "";
		}
		return String.format("§1§o[%s]§r", mod);
	}

	public boolean done() {
		return false;
	}


	public enum Type {
		HEADER, ACHIEVEMENT, TEXT
	}

}
