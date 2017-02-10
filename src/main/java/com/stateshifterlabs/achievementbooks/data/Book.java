package com.stateshifterlabs.achievementbooks.data;

import com.stateshifterlabs.achievementbooks.items.Colour;

import java.util.ArrayList;
import java.util.List;

public class Book {

	private List<Page> pages = new ArrayList<Page>();
	private String name;
	private String craftingMaterial = "";
	private boolean craftable = false;
	private String itemName;
	private Colour colour = Colour.RED;

	public void addPage(Page page) {
		pages.add(page);
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
		return pages.size();
	}

	public Page openPage(int pageNumber) {
		try {
			return pages.get(pageNumber);
		} catch (IndexOutOfBoundsException e) {
			return new Page();
		}
	}

	public void loadDone(List<Integer> ids) {

		for (Page page : pages) {
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
}
