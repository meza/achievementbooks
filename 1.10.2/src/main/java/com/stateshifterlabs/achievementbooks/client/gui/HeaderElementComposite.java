package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.data.PageElement;
import net.minecraft.client.gui.GuiButton;

import java.util.ArrayList;
import java.util.List;

public class HeaderElementComposite {

	private final int padding = 10;
	private int id;
	private final PageElement element;
	private final int top;
	private final int left;
	private final int maxWidth;
	private int height = 0;

	public HeaderElementComposite(int id, PageElement element, int top, int left, int maxWidth) {
		this.id = id;
		this.element = element;
		this.top = top + padding;
		this.left = left + 25;
		this.maxWidth = maxWidth;
	}

	public List<GuiButton> buttons() {

		List<GuiButton> buttons = new ArrayList<GuiButton>();

		HeaderLine header = new HeaderLine(id, left, top, maxWidth, element.formattedHeader());

		height = height + header.getHeight();
		buttons.add(header);

		if(element.hasDescription()) {
			DescriptionLine description = new DescriptionLine(id++, left, top + height, maxWidth, element.formattedDescription());
			height = height + description.getHeight();
			buttons.add(description);
		}

		return buttons;

	}


	public int height() {
		return height + padding;
	}
}
