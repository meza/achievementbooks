package com.stateshifterlabs.achievementbooks.items;

public enum Colour {

	PINK("pink"),
	DEEPBLUE("deepblue"),
	ORANGERED("orangered"),
	LIME("lime"),
	AQUA("aqua"),
	GRAY("gray"),
	BLACK("black"),
	BROWN("brown"),
	ORANGE("orange"),
	BLUE("blue"),
	RED("red"),
	YELLOW("yellow"),
	GREEN("green"),
	OLIVE("olive"),
	PEACH("peach");

	private String representation;

	Colour(String representation) {
		this.representation = representation;
	}

	public static Colour fromString(String colourString) {
		for (Colour colour : Colour.values()) {
			if (colour.representation.equalsIgnoreCase(colourString)) {
				return colour;
			}
		}
		return defaultColour();
	}

	public static Colour defaultColour() {
		return BLACK;
	}

	public String getText() {
		return representation;
	}
}
