package com.stateshifterlabs.achievementbooks;

public enum ChatFormatting {
	BLACK('0'),
	DARK_BLUE('1'),
	DARK_GREEN('2'),
	DARK_AQUA('3'),
	DARK_RED('4'),
	DARK_PURPLE('5'),
	GOLD('6'),
	GRAY('7'),
	DARK_GRAY('8'),
	BLUE('9'),
	GREEN('a'),
	AQUA('b'),
	RED('c'),
	LIGHT_PURPLE('d'),
	YELLOW('e'),
	WHITE('f'),
	OBFUSCATED('k', true),
	BOLD('l', true),
	STRIKETHROUGH('m', true),
	UNDERLINE('n', true),
	ITALIC('o', true),
	RESET('r');

	public static final char PREFIX_CODE = '§';
	private final char code;
	private final boolean isFormat;
	private final String toString;

	private ChatFormatting(char code) {
		this(code, false);
	}

	private ChatFormatting(char code, boolean isFormat) {
		this.code = code;
		this.isFormat = isFormat;
		this.toString = "§" + code;
	}

	public String toString() {
		return this.toString;
	}
}
