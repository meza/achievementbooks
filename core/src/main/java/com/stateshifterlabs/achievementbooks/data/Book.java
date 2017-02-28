package com.stateshifterlabs.achievementbooks.data;

import com.stateshifterlabs.achievementbooks.SA.NoSuchAchievementException;
import com.stateshifterlabs.achievementbooks.items.Colour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {

	public static final String US = "US";
	public static final String UK = "UK";
	private Map<Integer, Page> pages2 = new HashMap<Integer, Page>();
	private int addedPages = 0;
	private String name;
	private String craftingMaterial = "";
	private boolean craftable = false;
	private String itemName;
	private Colour colour = Colour.RED;
	private String language = "UK";

	public void addPage(Page page) {
		pages2.put(addedPages++, page);
	}

	public void withName(String name) {
		this.name = name;
	}

	public void withItemName(String name) {
		this.itemName = name;
	}

	public void withMaterial(String material) {
		this.craftingMaterial = material;
		this.craftable = true;
	}

	public Book withColour(String colour) {
		this.colour = Colour.fromString(colour);
		return this;
	}

	public boolean isCraftable() {
		return craftable;
	}

	public String name() {
		return this.name;
	}

	public String material() {
		return this.craftingMaterial;
	}

	public int pageCount() {
		return pages2.size();
	}

	public Page openPage(int pageNumber) {
		try {
			return pages2.get(pageNumber);
		} catch (IndexOutOfBoundsException e) {
			return new Page();
		}
	}

	public void loadDone(List<Integer> ids) {

		for (Page page : pages2.values()) {
			for (PageElement element : page.elements()) {
				if(ids.contains(element.id())) {
					element.toggleState(true);
				}
			}
		}

	}

	public String itemName() {
		return itemName;

	}

	public String colour() {
		return colour.getText();
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Book book = (Book) o;

		if (addedPages != book.addedPages) {
			return false;
		}
		if (craftable != book.craftable) {
			return false;
		}
		if (!pages2.equals(book.pages2)) {
			return false;
		}
		if (!name.equals(book.name)) {
			return false;
		}
		if (craftingMaterial != null ? !craftingMaterial.equals(book.craftingMaterial)
									 : book.craftingMaterial != null) {
			return false;
		}
		if (!itemName.equals(book.itemName)) {
			return false;
		}
		return colour == book.colour;
	}

	@Override
	public int hashCode() {
		int result = pages2.hashCode();
		result = 31 * result + addedPages;
		result = 31 * result + name.hashCode();
		result = 31 * result + (craftingMaterial != null ? craftingMaterial.hashCode() : 0);
		result = 31 * result + (craftable ? 1 : 0);
		result = 31 * result + itemName.hashCode();
		result = 31 * result + (colour != null ? colour.hashCode() : 0);
		return result;
	}

	public int findIdByAchievementText(String text) throws NoSuchAchievementException {
		for(Page page: pages2.values()) {
			for(PageElement element: page.elements()) {
				if(element.type() == PageElement.Type.ACHIEVEMENT) {
					if(element.achievement().equalsIgnoreCase(text)) {
						return element.id();
					}
				}
			}
		}
		throw new NoSuchAchievementException();
	}

	public void withLanguage(String language) {
		this.language = language;
	}

	public String language() {
		return this.language;
	}
}
