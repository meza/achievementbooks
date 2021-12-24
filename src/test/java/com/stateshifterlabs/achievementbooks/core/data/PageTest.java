package com.stateshifterlabs.achievementbooks.core.data;

import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class PageTest {

	private Fairy fairy = Fairy.create();

	@Test
	public void elementArrayTest() {

		int arraySize = fairy.baseProducer().randomBetween(0, 10);
		PageElement[] expected = new PageElement[arraySize];

		Page page = new Page();

		for(int i = 0; i< arraySize; i++) {
			PageElement element = new PageElement(i+100);

			expected[i] = element;
			page.addElement(element);

		}

		Assert.assertArrayEquals(expected, page.elements());

	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Page.class).verify();
	}
}
