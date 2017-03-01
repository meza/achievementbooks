package com.stateshifterlabs.achievementbooks.data;

import com.stateshifterlabs.achievementbooks.helpers.generators.BookGenerator;
import com.stateshifterlabs.achievementbooks.items.Colour;
import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public class BookTest {

	private final Fairy fairy = Fairy.create();

	@Test
	public void testName() {
		final Book book = new Book();
		final String bookName = fairy.textProducer().latinWord();

		assertEquals("Empty book name isn't empty string", "", book.name());
		book.withName(bookName);
		assertEquals("Book name cannot be set", bookName, book.name());

	}

	@Test
	public void testItemName() {
		final Book book = new Book();
		final String bookItemName = fairy.textProducer().latinWord();

		assertEquals("Empty book item name isn't empty string", "", book.itemName());
		book.withItemName(bookItemName);
		assertEquals("Book item name cannot be set", bookItemName, book.itemName());

	}

	@Test
	public void testColour() {
		final Book book = new Book();
		final Colour colour = fairy.baseProducer().randomElement(Colour.class);

		assertEquals("Empty book colour is not the default RED", Colour.RED.getText(), book.colour());
		book.withColour(colour.getText());
		assertEquals("Book colour cannot be set", colour.getText(), book.colour());

	}

	@Test
	public void testCraftingMaterial() {
		final Book book = new Book();
		final String materialName = fairy.textProducer().latinWord();

		assertFalse("Book is not marked uncraftable with no crafting material", book.isCraftable());
		assertEquals("Empty book name isn't empty string", "", book.material());
		book.withMaterial(materialName);
		assertEquals("Book crafting material cannot be set", materialName, book.material());
		assertTrue("Book is not marked craftable with crafting material", book.isCraftable());

	}

	@Test
	public void testBasicPageManagement() {
		final Book book = new Book();
		final int numberOfTestPages = fairy.baseProducer().randomBetween(1, 50);
		final Map<Integer, Page> pages = new HashMap<>();

		for (int i = 0; i < numberOfTestPages; i++) {
			Page page = new Page();
			pages.put(i, page);
			book.addPage(page);
		}

		assertEquals("The book didn't register the right amount of pages.", pages.size(), book.pageCount());

		for (int i = 0; i < pages.size(); i++) {
			assertEquals("The book returns the wrong page when opening it", pages.get(i), book.openPage(i));
		}
	}

	@Test
	public void testOpenUnexistingPage() {
		final Book book = new Book();
		assumeTrue(book.pageCount() == 0);
		final int randomPageNumber = fairy.baseProducer().randomBetween(1, 100);
		try {
			Page emptyPage = book.openPage(randomPageNumber);
			assertTrue(emptyPage instanceof Page);
		} catch (Exception e) {
			fail("Book didn't return an empty page when trying to open a not existing page");
		}



	}

	@Test
	public void testLoadingComplete() {
		Book book;
		List<Integer> achievementIds = new ArrayList<>();
		do {
			achievementIds = new ArrayList<>();
			book = new BookGenerator().generate(false).objectFormat();

			for (int i = 0; i < book.pageCount(); i++) {
				Page page = book.openPage(i);
				for (PageElement element : page.elements()) {
					if (element.type() == Type.ACHIEVEMENT) {
						achievementIds.add(element.id());
						assertFalse("Element is not checked false by default, would break the test.", element.checked());
					}
				}
			}
		} while (achievementIds.size() == 0);

		final int count = fairy.baseProducer().randomBetween(1, achievementIds.size());
		final List<Integer> markChecked = fairy.baseProducer().randomElements(achievementIds, count);

		book.loadDone(markChecked);

		for(int i=0; i<book.pageCount(); i++) {
			Page page = book.openPage(i);
			for (PageElement element : page.elements()) {
				if(markChecked.contains(element.id())) {
					assertTrue("Element is not marked done after batch loading.", element.checked());
				}
			}
		}


	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Book.class).suppress(Warning.NONFINAL_FIELDS).verify();
	}
}
