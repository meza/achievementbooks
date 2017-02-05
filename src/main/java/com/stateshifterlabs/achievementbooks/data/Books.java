package com.stateshifterlabs.achievementbooks.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Books implements Iterable<Book> {

	List<Book> books = new ArrayList<Book>();

	public void addBook(Book book) {
		books.add(book);
	}


	@Override
	public Iterator<Book> iterator() {
		return books.iterator();
	}

	public void empty() {
		books = new ArrayList<Book>();
	}
}
