package com.stateshifterlabs.achievementbooks.SA;

import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FormattingTest {

	private Fairy fairy = Fairy.create();

	@Test
	public void testFormattingPermutations() {

		for(int i = 0; i < 500; i++) {
			boolean isHeader = fairy.baseProducer().trueOrFalse();
			boolean isAchievement = fairy.baseProducer().trueOrFalse();
			Formatting formatting = new Formatting(isAchievement, isHeader);

			assertEquals("Header formatting is not returned properly", isHeader, formatting.isHeader());
			assertEquals("Achieveent formatting is not returned properly", isAchievement, formatting.isAchievement());

		}

	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Formatting.class).verify();
	}

}
