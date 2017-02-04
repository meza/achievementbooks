package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.data.PageElement;
import net.minecraft.client.gui.GuiButton;

import java.util.ArrayList;
import java.util.List;

public class AchievementGui {

	private int id;
	private final PageElement element;
	private final int top;
	private final int left;
	private final int maxWidth;
	private int height = 0;
	private int padding = 10;

	public AchievementGui(int id, PageElement element, int top, int left, int maxWidth) {
		this.id = id;
		this.element = element;
		this.top = top;
		this.left = left + 25;

		this.maxWidth = maxWidth;
	}

	public List<GuiButton> buttons() {

		List<GuiButton> buttons = new ArrayList<GuiButton>();

		String text = element.achievement() + " " + element.mod();

		AchievementLine achievement = new AchievementLine(id, left, top, maxWidth, text);

		height = height + achievement.getHeight();
		buttons.add(achievement);

//		DescriptionLine description = new DescriptionLine(id++, left + 25, top + height, maxWidth - 25, element.description());
//		height = height + description.getHeight();
//		buttons.add(description);

		return buttons;

	}


	public int height() {
		return height + padding;
	}
}
