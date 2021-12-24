package com.stateshifterlabs.achievementbooks.core.SA;

import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.stateshifterlabs.achievementbooks.helpers.Settings.DEFAULT_TEST_ITERATION_COUNT;
import static org.junit.Assert.assertEquals;

public class FormattingListTest {

	private Fairy fairy = Fairy.create();

	@Test(expected = NoSuchFormattingException.class)
	public void testNoFormatting() throws NoSuchFormattingException {
		FormattingList list = new FormattingList();
		list.formattingFor(fairy.baseProducer().randomBetween(0, 1000));
	}

	@Test
	public void testFormattingList() throws NoSuchFormattingException {
		int numberOfTestIterations = fairy.baseProducer().randomBetween(1, DEFAULT_TEST_ITERATION_COUNT);
		for(int i = 0; i< numberOfTestIterations; i++) {
			FormattingList list = new FormattingList();

			int numberOfFormattings = fairy.baseProducer().randomBetween(1, 100);

			for (int j=0; j<numberOfFormattings; j++) {
				boolean isHeader = fairy.baseProducer().trueOrFalse();
				Formatting f = new Formatting(!isHeader, isHeader);
				list.put(j, f);

				assertEquals("Formatting list doesn't return the right formatting", f, list.formattingFor(j));

			}

		}
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(FormattingList.class).verify();
	}
}
