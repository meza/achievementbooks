package com.stateshifterlabs.achievementbooks.core.data;

public enum Language {
	UK("UK"), US("US");

	private String representation;

	Language(String representation) {
		this.representation = representation;
	}

	public static Language fromString(String languageString) {
		for (Language language : Language.values()) {
			if (language.representation.equalsIgnoreCase(languageString)) {
				return language;
			}
		}
		return UK;
	}

	public String getText() {
		return representation;
	}
}
