package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.helpers.Settings;
import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SaveTest {

	private final Fairy fairy = Fairy.create();

	@Test
	public void newSaveHasNothing() {
		Save save = new Save();
		assertEquals("New save data is not empty by default!", 0, save.completedAchievements().size());
	}

	@Test
	public void testSaveToggle() {

		Save save = new Save();
		int idToToggle = fairy.baseProducer().randomBetween(0, 1000);
		for (int i = 0; i < Settings.DEFAULT_TEST_ITERATION_COUNT; i++) {
			assertFalse("Save contains unticked achievement ID", save.completedAchievements().contains(idToToggle));
			save.toggle(idToToggle);
			assertTrue("Save does not contain ticked achievement ID",
					   save.completedAchievements().contains(idToToggle));
			save.toggle(idToToggle);
			assertFalse("Save couldn't untoggle the achievement", save.completedAchievements().contains(idToToggle));
			idToToggle++;
		}

	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Save.class).verify();
	}
}
