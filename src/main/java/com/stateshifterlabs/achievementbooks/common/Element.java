package com.stateshifterlabs.achievementbooks.common;

public class Element {

	public Element() {

	}

	public int getColorBasedOnState() {
		return 0x000000;
	}

	public String getText() {
		return "WIP";
	}

	public boolean getState() {
		return false;
	}

	public boolean isAchievement() {
		return false;
	}

	public Alignment align() {
		return Alignment.LEFT;
	}

	public boolean shadow() {
		return false;
	}

	public enum Alignment {
		LEFT, CENTER, RIGHT;
	}
}
