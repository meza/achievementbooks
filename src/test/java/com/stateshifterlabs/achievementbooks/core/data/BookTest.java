package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.core.errors.DuplicatePageElementIdException;
import com.stateshifterlabs.achievementbooks.core.errors.NoSuchAchievementException;
import com.stateshifterlabs.achievementbooks.core.items.Colour;
import com.stateshifterlabs.achievementbooks.helpers.generators.BookGenerator;
import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
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

		assertEquals("Empty book colour is not the default", Colour.defaultColour().getText(), book.colour());
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
	public void testCollectingIds() {
		final Book book = new Book();
		final int numberOfTestPages = fairy.baseProducer().randomBetween(1, 50);
		int id = 0;
		List<Integer> expectedIds = new ArrayList<>();
		addPages(book, numberOfTestPages, id, expectedIds);

		assertEquals("The ids of the elements have not been registered", expectedIds.size(), book.numberOfElements());

	}

	@Test(expected = DuplicatePageElementIdException.class)
	public void testDuplicateIdDetection() {
		final Book book = new Book();
		int id = fairy.baseProducer().randomBetween(1, 100);

		Page page = new Page();

		page.addElement(new PageElement(id));
		page.addElement(new PageElement(id));

		book.addPage(page);
	}

	@Test
	public void testIdExists() {
		final Book book = new Book();
		final int numberOfTestPages = fairy.baseProducer().randomBetween(1, 50);
		int id = 0;
		List<Integer> expectedIds = new ArrayList<>();
		addPages(book, numberOfTestPages, id, expectedIds);

		for(int i=-100; i<0; i++) {
			assertFalse("Non existing ID is reported to exist in book", book.idExists(i));
		}

		for(Integer existingId: expectedIds) {
			assumeTrue("Existing ID is reported to not exist in book", book.idExists(existingId));
		}
	}

	private void addPages(Book book, int numberOfTestPages, int id, List<Integer> expectedIds) {
		for (int i = 0; i < numberOfTestPages; i++) {
			Page page = new Page();

			int numberOfElements = fairy.baseProducer().randomBetween(0, 10);

			for (int j = 0; j < numberOfElements; j++) {
				PageElement element = new PageElement(id);
				page.addElement(element);
				expectedIds.add(id);
				id++;
			}

			book.addPage(page);
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
		Book book = new Book();
		List<Integer> achievementIds = new ArrayList<>();

		do {
			achievementIds = new ArrayList<>();
			book = new BookGenerator().generate(false).objectFormat();

			for (int i1 = 0; i1 < book.pageCount(); i1++) {
				Page page1 = book.openPage(i1);
				for (PageElement element1 : page1.elements()) {
					if (element1.type() == Type.ACHIEVEMENT) {
						achievementIds.add(element1.id());
						assertFalse("Element is not checked false by default, would break the test.",
									element1.checked());
					}
				}
			}
		} while (achievementIds.size() == 0);

		final int count = fairy.baseProducer().randomBetween(1, achievementIds.size());
		final List<Integer> markChecked = fairy.baseProducer().randomElements(achievementIds, count);

		book.loadDone(markChecked);

		for (int i = 0; i < book.pageCount(); i++) {
			Page page = book.openPage(i);
			for (PageElement element : page.elements()) {
				if (markChecked.contains(element.id())) {
					assertTrue("Element is not marked done after batch loading.", element.checked());
				}
			}
		}
	}

	@Test
	public void testAchievementLocator() {
		Book book = new Book();
		Map<String, Integer> achievements = new HashMap<>();
		int id = 0;
		final int numberOfPages = fairy.baseProducer().randomBetween(2, 10);

		for (int i = 0; i < numberOfPages; i++) {
			int numberOfElements = fairy.baseProducer().randomBetween(1, 5);
			Page page = new Page();

			for (int j = 0; j < numberOfElements; j++) {
				boolean isAchievement = fairy.baseProducer().trueOrFalse();

				PageElement element = new PageElement(id);
				String text = fairy.textProducer().latinSentence();
				if (isAchievement) {
					element.withAchievement(text);
					achievements.put(text, id);
				} else {
					element.withDescription(text);
				}

				page.addElement(element);

				id++;
			}

			book.addPage(page);

		}

		for (Map.Entry<String, Integer> achievement : achievements.entrySet()) {
			try {
				int locatedId = book.findIdByAchievementText(achievement.getKey());
				assertEquals(achievement.getValue().intValue(), locatedId);
			} catch (NoSuchAchievementException e) {
				fail("Achievement could not be located");
			}
		}
	}

	@Test(expected = NoSuchAchievementException.class)
	public void testNoAchievementText() throws NoSuchAchievementException {
		Book book = new Book();
		String text = fairy.textProducer().latinSentence();

		book.findIdByAchievementText(text);
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Book.class).suppress(Warning.NONFINAL_FIELDS).verify();
	}
}
