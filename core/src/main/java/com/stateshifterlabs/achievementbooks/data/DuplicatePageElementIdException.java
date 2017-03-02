package com.stateshifterlabs.achievementbooks.data;

public class DuplicatePageElementIdException extends RuntimeException {

	public DuplicatePageElementIdException(PageElement element, String book) {
		super(String.format(
				"An entry with the id %d already exists in the config for book: %s.\nPlease use unique ids for each " +
				"element.",
				element.id(), book));
	}
}
