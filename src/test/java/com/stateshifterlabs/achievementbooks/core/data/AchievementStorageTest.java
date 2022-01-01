package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.core.facade.Player;
import com.stateshifterlabs.achievementbooks.helpers.generators.AchievementDataGenerator;
import com.stateshifterlabs.achievementbooks.helpers.generators.AchievementStorageGenerator;
import io.codearte.jfairy.Fairy;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AchievementStorageTest {

    final Fairy fairy = Fairy.create();
    final AchievementStorageGenerator storageGenerator = new AchievementStorageGenerator();

    @Test
    public void testAddingPlayers() {
        AchievementStorage storage = new AchievementStorage();
        assumeTrue("Achievement Storage is not empty by default", storage.players().size() == 0);

        String playerName = fairy.textProducer().word();
        AchievementData data = new AchievementData(playerName);

        storage.append(data);

        assertEquals("Achievement Storage did not grow by adding a player", 1, storage.players().size());

        AchievementData actual = storage.forPlayer(playerName);
        assertEquals("Achievement Storage could not return the right data for the player", data, actual);

    }

    @Test
    public void testNewPlayer() {
        AchievementStorage storage = new AchievementStorage();
        assumeTrue("Achievement Storage is not empty by default", storage.players().size() == 0);

        String playerName = fairy.textProducer().word();

        AchievementData actual = storage.forPlayer(playerName);

        assertNotNull(actual);
        assertTrue(actual instanceof AchievementData);
    }

    @Test
    public void testClear() {
        AchievementStorage storage = storageGenerator.generate(1).objectFormat();
        assumeTrue("Achievement Storage is not primed properly", storage.players().size() > 0);

        storage.clear();
        assertEquals("Achievement Storage wasn't cleared", 0, storage.players().size());
    }

    @Test
    public void testSize() {
        int numberOfPlayers = fairy.baseProducer().randomInt(100);
        AchievementStorage storage = storageGenerator.generate(numberOfPlayers).objectFormat();

        Assert.assertEquals(numberOfPlayers, storage.size());
    }

    @Test
    public void testGetFromMinecraftPlayer() {
        AchievementStorage storage = new AchievementStorage();
        String playerName = fairy.textProducer().word();
        AchievementData data = new AchievementData(playerName);

        storage.append(data);
        Player player = Mockito.mock(Player.class);
        when(player.getDisplayName()).thenReturn(playerName);

        AchievementData actual = storage.forPlayer(player);
        assertEquals("Achievement Storage could not return the right data for the player", data, actual);

        verify(player).getDisplayName();
    }

    @Test
    public void testHasPlayer() {
        AchievementStorage storage = new AchievementStorage();
        String playerName = fairy.textProducer().word();
        AchievementData data = new AchievementData(playerName);

        Assert.assertFalse(storage.hasPlayerData(playerName));
        storage.append(data);
        Assert.assertTrue(storage.hasPlayerData(playerName));

    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(AchievementStorage.class).verify();
    }

    @Test
    public void testAppendReplacesData() {
        AchievementStorage storage = new AchievementStorage();
        String playerName = fairy.textProducer().word();
        AchievementData originalData = AchievementDataGenerator.generate(playerName).objectFormat();

        storage.append(originalData);
        assertSame(originalData, storage.forPlayer(playerName));

        AchievementData newData = AchievementDataGenerator.generate(playerName).objectFormat();
        storage.append(newData);
        assertSame(newData, storage.forPlayer(playerName));
    }
}
