package com.stateshifterlabs.achievementbooks.core.data;

import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.when;

public class AchievementDataTest {

	private final Fairy fairy = Fairy.create();


	@Test
	public void testNames() {
		String randomName = fairy.textProducer().word();
		AchievementData achievementData = new AchievementData(randomName);

		assertEquals("Achievement Data didn't return the correct player name", randomName, achievementData.username());

	}

	@Test
	public void testEmptyBooks() {
		String randomName = fairy.textProducer().word();
		AchievementData achievementData = new AchievementData(randomName);

		assertTrue("Achievement Data doesn't start out empty", achievementData.books().isEmpty());
	}

	@Test
	public void testNonExistingBook() {
		String randomName = fairy.textProducer().word();
		String randomBookName = fairy.textProducer().word();
		AchievementData achievementData = new AchievementData(randomName);

		assumeTrue("Achievement data wasn't empty to begin with", achievementData.books().isEmpty());

		List<Integer> actual = achievementData.completed(randomBookName);
		assertNotNull("Requesting a non existing book data didn't return a list", actual);
		assertTrue("Requesting a non existing book data didn't return an empty list", actual.isEmpty());
		assertEquals("Requesting a non existing book didn't create the book data", 1, achievementData.books().size());
		assertTrue("Requesting a non existing book didn't add the new book to the list",
				   achievementData.books().contains(randomBookName));
	}

	@Test
	public void testReplaceSaveData() {
		String randomName = fairy.textProducer().word();
		String randomBookName = fairy.textProducer().word();
		AchievementData achievementData = new AchievementData(randomName);
		Save oldSave = Mockito.mock(Save.class);
		Save newSave = Mockito.mock(Save.class);

		List<Integer> oldSaveList = new ArrayList<>();
		oldSaveList.add(1);
		oldSaveList.add(2);
		oldSaveList.add(3);

		List<Integer> newSaveList = new ArrayList<>();
		newSaveList.add(4);
		newSaveList.add(5);
		newSaveList.add(6);

		when(oldSave.completedAchievements()).thenReturn(oldSaveList);
		when(newSave.completedAchievements()).thenReturn(newSaveList);

		assumeTrue("Achievement data wasn't empty to begin with", achievementData.books().isEmpty());

		achievementData.addSaveData(randomBookName, oldSave);

		assumeTrue("The correct completed list wasn't returned",
				   achievementData.completed(randomBookName) == oldSaveList);

		achievementData.addSaveData(randomBookName, newSave);

		assertEquals("The save data hasn't been replaced when adding for an existing book", newSaveList,
					 achievementData.completed(randomBookName));

	}

	@Test
	public void testToggle() {
		String randomName = fairy.textProducer().word();
		String randomBookName = fairy.textProducer().word();
		int randomAchievementId = fairy.baseProducer().randomInt(1000);
		AchievementData achievementData = new AchievementData(randomName);

		assumeTrue("Achievement data wasn't empty to begin with", achievementData.books().isEmpty());

		achievementData.toggle(randomBookName, randomAchievementId);
		List<Integer> completed = achievementData.completed(randomBookName);

		assertFalse("Toggling a non exsisting book's achievement didn't work", completed.isEmpty());
		assertEquals("Toggling a non existing book's achievement didn't toggle the right achievement",
					 randomAchievementId, completed.get(0).intValue());

		int randomAchievementId2 = fairy.baseProducer().randomInt(1000);
		achievementData.toggle(randomBookName, randomAchievementId2);

		assertEquals("Toggling an existing book's achievement didn't toggle the right achievement",
					 randomAchievementId2, completed.get(1).intValue());

	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(AchievementData.class).verify();
	}
}
