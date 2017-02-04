package com.stateshifterlabs.achievementbooks.data;

import java.util.ArrayList;
import java.util.List;

public class Page {

	List<PageElement> pageElements = new ArrayList<PageElement>();

	public PageElement[] elements() {
		return pageElements.toArray(new PageElement[pageElements.size()]);
	}

	public void addElement(PageElement element) {
		pageElements.add(element);
	}

}
