package com.stateshifterlabs.achievementbooks.core.data;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private final List<PageElement> pageElements = new ArrayList<PageElement>();

    public void addElement(PageElement element) {
        pageElements.add(element);
    }

    public PageElement[] elements() {
        return pageElements.toArray(new PageElement[pageElements.size()]);
    }

    @Override
    public final int hashCode() {
        if (pageElements == null) {
            return 0;
        }
        return pageElements.hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Page)) {
            return false;
        }

        Page page = (Page) o;

        return pageElements != null ? pageElements.equals(page.pageElements) : page.pageElements == null;
    }
}
