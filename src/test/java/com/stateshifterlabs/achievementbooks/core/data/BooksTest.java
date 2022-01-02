package com.stateshifterlabs.achievementbooks.core.data;

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

    private final Fairy fairy = Fairy.create();
    private BookGenerator generator;

    public RandomTestData<List<Book>, Books> generateBooks(int amount) {
        List<Book> generatedBooks = new ArrayList<>();
        Books booksClassUnderTest = new Books();
        for (int i = 0; i < amount; i++) {
            Book book = generator.generate().objectFormat();
            generatedBooks.add(book);
            booksClassUnderTest.addBook(book);
        }

        return new RandomTestData<>(generatedBooks, booksClassUnderTest);
    }

    @Before
    public void setUp() throws Exception {
        generator = new BookGenerator();
    }

    @Test
    public void testBooks() {
        int numberOfBooksToTestWith = fairy.baseProducer().randomBetween(0, 50);
        RandomTestData<List<Book>, Books> generatedRandomBooks = generateBooks(numberOfBooksToTestWith);

        List<Book> expected = generatedRandomBooks.jsonFormat();
        Books actual = generatedRandomBooks.objectFormat();

        assertEquals(expected.size(), actual.size());

        for (Book actualBook : actual) {
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

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(Books.class).verify();
    }

}
