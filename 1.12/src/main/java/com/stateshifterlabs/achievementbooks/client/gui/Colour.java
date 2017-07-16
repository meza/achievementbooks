package com.stateshifterlabs.achievementbooks.client.gui;

public class Colour {
	private final float red;
	private final float green;
	private final float blue;
	private final float alpha;

	public Colour(float red, float green, float blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public float red() {
		return red;
	}

	public float green() {
		return green;
	}

	public float blue() {
		return blue;
	}

	public float alpha() {
		return alpha;
	}

	public int rgb() {
		return toInt(this);
	}

	private int toInt(Colour col) {
		int r = Math.round(255 * col.red());
		int g = Math.round(255 * col.green());
		int b = Math.round(255 * col.blue());

		r = (r << 16) & 0x00FF0000;
		g = (g << 8) & 0x0000FF00;
		b = b & 0x000000FF;

		int result = 0xFF000000 | r | g | b;
		return result;
	}



}
