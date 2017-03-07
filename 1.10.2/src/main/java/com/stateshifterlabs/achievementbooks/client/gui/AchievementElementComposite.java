package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.client.AchievmentElementCompositeSettings;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import net.minecraft.client.gui.GuiButton;

import java.util.ArrayList;
import java.util.List;

public class AchievementElementComposite {

	private int id;
	private final PageElement element;
	private final int top;
	private final int left;
	private final int maxWidth;
	private int height = 0;

	public AchievementElementComposite(int id, PageElement element, int top, int left, int maxWidth) {
		this.id = id;
		this.element = element;
		this.top = top;
		this.maxWidth = maxWidth;
		this.left = left + AchievmentElementCompositeSettings.leftPadding;
	}

	public List<GuiButton> buttons() {

		List<GuiButton> buttons = new ArrayList<GuiButton>();

		AchievementLine achievement = new AchievementLine(id, left, top, maxWidth, element);

		height = height + achievement.getHeight();
		buttons.add(achievement);

//		DescriptionLine formattedDescription = new DescriptionLine(id++, left + 25, top + height, maxWidth - 25, element.formattedDescription());
//		height = height + formattedDescription.getHeight();
//		buttons.add(formattedDescription);

		return buttons;

	}


	public int height() {
		return height + AchievmentElementCompositeSettings.bottomPadding;
	}
}
