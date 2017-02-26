package com.stateshifterlabs.achievementbooks.data;

import com.stateshifterlabs.achievementbooks.SA.NoSuchAchievementException;
import com.stateshifterlabs.achievementbooks.items.Colour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {

	private Map<Integer, Page> pages2 = new HashMap<Integer, Page>();
	private int addedPages = 0;
	private String name;
	private String craftingMaterial = "";
	private boolean craftable = false;
	private String itemName;
	private Colour colour = Colour.RED;

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
}
