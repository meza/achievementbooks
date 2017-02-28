package com.stateshifterlabs.achievementbooks.items;

import io.codearte.jfairy.Fairy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColoursTest {

	private Fairy fairy = Fairy.create();


	@Test
	public void testDefaultColour() {
		String nonExistingColour = fairy.textProducer().latinWord();

		Colour actual = Colour.fromString(nonExistingColour);

		assertEquals("Default colour is not BLACK", Colour.BLACK, actual);

	}
}
