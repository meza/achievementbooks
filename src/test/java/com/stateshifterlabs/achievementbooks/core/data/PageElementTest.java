package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.core.UTF8Utils;
import com.stateshifterlabs.achievementbooks.helpers.Settings;
import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static com.stateshifterlabs.achievementbooks.core.data.Type.ACHIEVEMENT;
import static com.stateshifterlabs.achievementbooks.core.data.Type.HEADER;
import static com.stateshifterlabs.achievementbooks.core.data.Type.TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PageElementTest {

	private Fairy fairy = Fairy.create();

	@Test
	public void testImmutability() {

		int id = fairy.baseProducer().randomBetween(0, 1000);

		String achievement = fairy.textProducer().latinSentence();
		String header = fairy.textProducer().latinSentence();
		String description = fairy.textProducer().latinSentence();
		String mod = fairy.textProducer().latinSentence();

		PageElement element = new PageElement(id);

		assertNull(element.achievement());
		assertFalse(element.hasAchievement());
		assertNull(element.header());
		assertFalse(element.hasHeader());
		assertNull(element.description());
		assertFalse(element.hasDescription());
		assertNull(element.mod());
		assertFalse(element.hasMod());

		element.withAchievement(achievement);
		element.withHeader(header);
		element.withDescription(description);
		element.withMod(mod);

		assertTrue(element.hasAchievement());
		assertEquals("Page element doesn't return the original achievement", achievement, element.achievement());
		assertTrue(element.hasHeader());
		assertEquals("Page element doesn't return the original header", header, element.header());
		assertTrue(element.hasDescription());
		assertEquals("Page element doesn't return the original description", description, element.description());
		assertTrue(element.hasMod());
		assertEquals("Page element doesn't return the original mod", mod, element.mod());

		element.withAchievement(fairy.textProducer().latinSentence());
		element.withHeader(fairy.textProducer().latinSentence());
		element.withDescription(fairy.textProducer().latinSentence());
		element.withMod(fairy.textProducer().latinSentence());

		assertEquals("Page element isn't immutable for achievement", achievement, element.achievement());
		assertEquals("Page element isn't immutable for header", header, element.header());
		assertEquals("Page element isn't immutable for description", description, element.description());
		assertEquals("Page element isn't immutable for mod", mod, element.mod());

	}

	@Test
	public void testFormattedStrings() {

		int id = fairy.baseProducer().randomBetween(0, 1000);
		String achievement = fairy.textProducer().latinSentence();
		String header = fairy.textProducer().latinSentence();
		String description = fairy.textProducer().latinSentence();
		String mod = fairy.textProducer().latinSentence();

		PageElement element = new PageElement(id);

		element.withAchievement(achievement);
		element.withHeader(header);
		element.withDescription(description);
		element.withMod(mod);

		assertEquals(UTF8Utils.utf8String(achievement, " §1§o[", mod, "]§r"), element.formattedAchievement());
		assertEquals(UTF8Utils.utf8String("§l", header), element.formattedHeader());
		assertEquals(UTF8Utils.utf8String("§o", description), element.formattedDescription());
		assertEquals(UTF8Utils.utf8String("§1§o[", mod, "]§r"), element.formattedMod());

	}

	@Test
	public void defaultStates() {
		int id = fairy.baseProducer().randomBetween(0, 1000);
		PageElement element = new PageElement(id);

		assertEquals("", element.formattedAchievement());
		assertEquals("", element.formattedDescription());
		assertEquals("", element.formattedHeader());
		assertEquals("", element.formattedMod());

	}

	@Test
	public void testToggle() {
		int id = fairy.baseProducer().randomBetween(0, 1000);
		PageElement element = new PageElement(id);

		assertFalse("Element is not unchecked by default", element.checked());
		element.toggleState();
		assertTrue("Element toggle does not change checked state", element.checked());
		element.toggleState();
		assertFalse("Element toggle doesn't change flag to false", element.checked());

	}

	@Test
	public void testExplicitToggle() {
		int id = fairy.baseProducer().randomBetween(0, 1000);
		PageElement element = new PageElement(id);

		assertFalse("Element is not unchecked by default", element.checked());
		element.toggleState(true);
		assertTrue("Explicit element toggle does not change checked state", element.checked());
		element.toggleState(false);
		assertFalse("Explicit element toggle doesn't change flag to false", element.checked());
	}

	@Test
	public void testEmptyType() {

		int id = fairy.baseProducer().randomBetween(0, 1000);
		PageElement element = new PageElement(id);

		assertEquals("Default element type is NOT TEXT", TEXT, element.type());

	}

	@Test
	public void testAchievementType() {

		for (int i = 0; i < Settings.DEFAULT_TEST_ITERATION_COUNT; i++) {

			int id = fairy.baseProducer().randomBetween(0, 1000);
			PageElement element = new PageElement(id);

			element.withAchievement(fairy.textProducer().latinSentence());
			assertEquals("Element type is not marked achievement when there's an achievement text", ACHIEVEMENT,
						 element.type());

			if (fairy.baseProducer().trueOrFalse()) {
				element.withHeader(fairy.textProducer().latinSentence());
				assertEquals("Element type is not marked achievement when there's a header in the same config as an " +
							 "achievement",

							 ACHIEVEMENT, element.type());
			}

			if (fairy.baseProducer().trueOrFalse()) {
				element.withDescription(fairy.textProducer().latinSentence());
				assertEquals("Element type is not marked achievement when there's a description text in the config",
							 ACHIEVEMENT, element.type());
			}

			if (fairy.baseProducer().trueOrFalse()) {
				element.withMod(fairy.textProducer().latinSentence());
				assertEquals("Element type is not marked achievement when there's mod info with the achievement",
							 ACHIEVEMENT, element.type());
			}
		}

	}

	@Test
	public void testHeaderType() {

		for (int i = 0; i < Settings.DEFAULT_TEST_ITERATION_COUNT; i++) {

			int id = fairy.baseProducer().randomBetween(0, 1000);
			PageElement element = new PageElement(id);

			element.withHeader(fairy.textProducer().latinSentence());
			assertEquals("Element type is not marked achievement when there's a header in the same config as an " +
						 "achievement",

						 HEADER, element.type());

			if (fairy.baseProducer().trueOrFalse()) {
				element.withDescription(fairy.textProducer().latinSentence());
				assertEquals("Element type is not marked achievement when there's a description text in the config",
							 HEADER, element.type());
			}

			if (fairy.baseProducer().trueOrFalse()) {
				element.withMod(fairy.textProducer().latinSentence());
				assertEquals("Element type is not marked achievement when there's mod info with the achievement",
							 HEADER, element.type());
			}
		}

	}

	@Test
	public void testTextType() {

		for (int i = 0; i < Settings.DEFAULT_TEST_ITERATION_COUNT; i++) {

			int id = fairy.baseProducer().randomBetween(0, 1000);
			PageElement element = new PageElement(id);

			element.withDescription(fairy.textProducer().latinSentence());
			assertEquals("Element type is not marked achievement when there's a description text in the config", TEXT,
						 element.type());

			if (fairy.baseProducer().trueOrFalse()) {
				element.withMod(fairy.textProducer().latinSentence());
				assertEquals("Element type is not marked achievement when there's mod info with the achievement", TEXT,
							 element.type());
			}
		}

	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(PageElement.class).suppress(Warning.NONFINAL_FIELDS).verify();
	}
}
