package com.stateshifterlabs.achievementbooks.SA;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.helpers.RandomTestData;
import com.stateshifterlabs.achievementbooks.helpers.generators.BookGenerator;
import com.stateshifterlabs.achievementbooks.helpers.generators.SASaveGenerator;
import io.codearte.jfairy.Fairy;
import org.junit.Test;

import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class TestSaveImport {

	private final Fairy fairy = Fairy.create();
	private final SASaveGenerator generator = new SASaveGenerator();
	private final BookGenerator bookGenerator = new BookGenerator();

	private Type typeOfT;
	private JsonDeserializationContext deserializationContext;

	@Test
	public void testStuff() {

		Book book = bookGenerator.generate().objectFormat();

		RandomTestData<JsonElement, AchievementStorage> x = generator.generate(book);
		int randomPlayerIndex = fairy.baseProducer().randomBetween(0, x.objectFormat().players().size()-1);
		String player = x.objectFormat().players().get(randomPlayerIndex);

		AchievementData actual = thing(player, x.jsonFormat(), book);

		assertEquals("Save data wasn't parsed properly for a single user", x.objectFormat().forPlayer(player), actual);

	}

	private AchievementData thing(String player, JsonElement json, Book book) {
		SaveDataDeserializer deserializer = new SaveDataDeserializer(book);
		AchievementStorage achievementStorage = deserializer.deserialize(json, typeOfT, deserializationContext);

		return achievementStorage.forPlayer(player);
	}

}
