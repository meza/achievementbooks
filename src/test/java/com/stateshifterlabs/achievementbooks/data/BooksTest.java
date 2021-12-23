package com.stateshifterlabs.achievementbooks.data;

import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import com.stateshifterlabs.achievementbooks.helpers.generators.BookGenerator;
import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BooksTest {

	private Fairy fairy = Fairy.create();
	private BookGenerator generator;

	@Before
	public void setUp() throws Exception {
		generator = new BookGenerator();
	}

	@Test
	public void testBooks() {
		int numberOfBooksToTestWith = fairy.baseProducer().randomBetween(0, 50);
		RandomTestData<List<Book>, Books> books = generateBooks(numberOfBooksToTestWith);

		List<Book> expected = books.jsonFormat();
		Books actual = books.objectFormat();

		assertEquals(expected.size(), actual.size());

		for(Book actualBook: actual) {
			assertTrue(expected.contains(actualBook));
		}
	}

	@Test
	public void testClear() {
		int numberOfBooksToTestWith = fairy.baseProducer().randomBetween(0, 50);
		Books books = generateBooks(numberOfBooksToTestWith).objectFormat();

		assertEquals(numberOfBooksToTestWith, books.size());

		books.empty();

		assertEquals("Books list could not be emptied", 0, books.size());

	}

	public RandomTestData<List<Book>, Books> generateBooks(int amount) {
		List<Book> books = new ArrayList<>();
		Books booksObject = new Books();
		for(int i=0; i<amount; i++) {
			Book book = generator.generate().objectFormat();
			books.add(book);
			booksObject.addBook(book);
		}


		return new RandomTestData<>(books, booksObject);
	}


	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Books.class).verify();
	}

}
