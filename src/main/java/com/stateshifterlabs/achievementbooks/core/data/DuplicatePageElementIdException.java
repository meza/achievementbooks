package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.core.UTF8Utils;

public class DuplicatePageElementIdException extends RuntimeException {

	private PageElement element;
	private String book;

	public DuplicatePageElementIdException(PageElement element, String book) {
		super(String.format(
				"\n\n" +
				"*****************************  THIS IS WHY YOUR GAME DOESN'T START  *******************************\n\n"+
				"\tAn entry with the id %d already exists in the config for book: %s.\n" +
				"\tPlease use unique ids for each element.\n" +
				"\n**************************************************************************************************\n",
				element.id(), book));
		this.element = element;
		this.book = book;
	}

	public String simpleMessage() {
		return String.format(
				UTF8Utils.utf8String("§eAn entry with the id %d already exists in the config for book: %s.§r"),
							 element.id(), book);
	}
}
