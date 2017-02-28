package com.stateshifterlabs.achievementbooks.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class BookTest {

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Book.class).suppress(Warning.NONFINAL_FIELDS).verify();
	}
}
