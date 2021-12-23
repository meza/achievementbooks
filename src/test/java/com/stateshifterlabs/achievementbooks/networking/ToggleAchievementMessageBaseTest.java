package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.facade.ByteBufferUtilities;
import io.codearte.jfairy.Fairy;
import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ToggleAchievementMessageBaseTest {
	private final Fairy fairy = Fairy.create();

	private ByteBufferUtilities byteBufferUtilities;

	@Before
	public void setUp() {
		byteBufferUtilities = mock(ByteBufferUtilities.class);
	}

	@Test
	public void testBaseData() {
		ToggleAchievementMessageBase base = new ToggleAchievementMessageBase(byteBufferUtilities);
		assumeTrue(base.bookName() == null);
		assumeTrue(base.achievementId() == 0);

		String randomBookName = fairy.textProducer().word();
		int randomAchievementId = fairy.baseProducer().randomInt(50);

		base.withData(randomBookName, randomAchievementId);

		assertEquals("The book name wasn't returned properly in the toggle message", randomBookName, base.bookName());
		assertEquals("The achievement wasn't returned properly in the toggle message", randomAchievementId, base.achievementId());

	}

	@Test
	public void testToBytes() {
		ToggleAchievementMessageBase base = new ToggleAchievementMessageBase(byteBufferUtilities);
		String randomBookName = fairy.textProducer().word();
		int randomAchievementID = fairy.baseProducer().randomInt(50);
		base.withData(randomBookName, randomAchievementID);

		ByteBuf buffer = Mockito.mock(ByteBuf.class);
		base.toBytes(buffer);

		verify(buffer, times(1)).writeInt(randomAchievementID);
		verify(byteBufferUtilities, times(1)).writeUTF8String(buffer, randomBookName);

	}

	@Test
	public void testFromBytes() {
		ToggleAchievementMessageBase base = new ToggleAchievementMessageBase(byteBufferUtilities);
		String randomBookName = fairy.textProducer().word();
		int randomAchievementID = fairy.baseProducer().randomInt(50);

		assumeTrue(base.bookName() == null);
		assumeTrue(base.achievementId() == 0);

		ByteBuf buffer = Mockito.mock(ByteBuf.class);

		when(buffer.readInt()).thenReturn(randomAchievementID);
		when(byteBufferUtilities.readUTF8String(buffer)).thenReturn(randomBookName);

		base.fromBytes(buffer);

		assertEquals("The book name wasn't parsed from the buffer in the toggle message", randomBookName, base.bookName());
		assertEquals("The achievement id wasn't parsed from the buffer in the toggle message", randomAchievementID, base.achievementId());
	}
}
