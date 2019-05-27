package com.crankysupertoon.achievementbooks.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Books implements Iterable<Book> {

	private final List<Book> books = new ArrayList<Book>();

	public void addBook(Book book) {
		books.add(book);
	}

	public int size() {
		return books.size();
	}

	@Override
	public Iterator<Book> iterator() {
		return books.iterator();
	}

	public void empty() {
		books.clear();
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Books)) {
			return false;
		}

		Books books1 = (Books) o;

		return books != null ? books.equals(books1.books) : books1.books == null;
	}

	@Override
	public final int hashCode() {
		return books != null ? books.hashCode() : 0;
	}

	public Book migration() {

		for(Book book : books) {
			if (book.isMigrationTarget()) {
				return book;
			}
		}

		return null;

	}
}
