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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Page page = (Page) o;

		return pageElements.equals(page.pageElements);
	}

	@Override
	public int hashCode() {
		return pageElements.hashCode();
	}
}
