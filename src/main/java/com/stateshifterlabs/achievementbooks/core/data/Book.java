package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.core.errors.DuplicatePageElementIdException;
import com.stateshifterlabs.achievementbooks.core.errors.NoSuchAchievementException;
import com.stateshifterlabs.achievementbooks.core.items.Colour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {

    private final List<Integer> elementIds = new ArrayList<Integer>();
    private final Map<Integer, Page> pages = new HashMap<Integer, Page>();
    private int addedPages = 0;
    private Colour colour = Colour.defaultColour();
    private boolean craftable = false;
    private String craftingMaterial = "";
    private String itemName = "";
    private String name = "";

    public void addPage(Page page) {
        pages.put(addedPages++, page);
        for (PageElement element : page.elements()) {
            if (elementIds.contains(element.id())) {
                throw new DuplicatePageElementIdException(element, name());
            }
            elementIds.add(element.id());
        }
    }

    public String colour() {
        return colour.getText();
    }

    public int findIdByAchievementText(String text) throws NoSuchAchievementException {
        for (Page page : pages.values()) {
            for (PageElement element : page.elements()) {
                if (element.type() == Type.ACHIEVEMENT) {
                    if (element.achievement().equalsIgnoreCase(text)) {
                        return element.id();
                    }
                }
            }
        }
        throw new NoSuchAchievementException();
    }

    @Override
    public final int hashCode() {
        int result = pages != null ? pages.hashCode() : 0;
        result = 31 * result + addedPages;
        if (elementIds != null) {
            result = 31 * result + elementIds.hashCode();
        }
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (craftingMaterial != null ? craftingMaterial.hashCode() : 0);
        result = 31 * result + (craftable ? 1 : 0);
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        return result;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }

        Book book = (Book) o;

        if (addedPages != book.addedPages) {
            return false;
        }
        if (craftable != book.craftable) {
            return false;
        }

        if (elementIds != null ? !elementIds.equals(book.elementIds) : book.elementIds != null) {
            return false;
        }

        if (pages != null ? !pages.equals(book.pages) : book.pages != null) {
            return false;
        }
        if (name != null ? !name.equals(book.name) : book.name != null) {
            return false;
        }
        if (craftingMaterial != null ? !craftingMaterial.equals(book.craftingMaterial)
                : book.craftingMaterial != null) {
            return false;
        }
        if (itemName != null ? !itemName.equals(book.itemName) : book.itemName != null) {
            return false;
        }
        return colour == book.colour;
    }

    public boolean idExists(int existingId) {
        return elementIds.contains(existingId);
    }

    public boolean isCraftable() {
        return craftable;
    }

    public String itemName() {
        return itemName;

    }

    public void loadDone(List<Integer> ids) {

        for (Page page : pages.values()) {
            for (PageElement element : page.elements()) {
                if (ids.contains(element.id())) {
                    element.toggleState(true);
                }
            }
        }

    }

    public String material() {
        return this.craftingMaterial;
    }

    public String name() {
        return this.name;
    }

    public int numberOfElements() {
        return elementIds.size();
    }

    public Page openPage(int pageNumber) {
        if (!pages.containsKey(pageNumber)) {
            return new Page();
        }
        return pages.get(pageNumber);

    }

    public int pageCount() {
        return pages.size();
    }

    public Book withColour(String colour) {
        this.colour = Colour.fromString(colour);
        return this;
    }

    public void withItemName(String name) {
        this.itemName = name;
    }

    public void withMaterial(String material) {
        this.craftingMaterial = material;
        this.craftable = true;
    }

    public void withName(String name) {
        this.name = name;
    }
}
