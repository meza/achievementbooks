package com.stateshifterlabs.achievementbooks.core.networking;

import com.stateshifterlabs.achievementbooks.core.facade.ByteBufferUtilities;
import io.codearte.jfairy.Fairy;
import io.netty.buffer.ByteBuf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

public class PageTurnMessageBaseTest {

    private final Fairy fairy = Fairy.create();

    private ByteBufferUtilities byteBufferUtilities;

    @Before
    public void setUp() {
        byteBufferUtilities = mock(ByteBufferUtilities.class);
    }

    @Test
    public void testBaseData() {
        PageTurnMessageBase base = new PageTurnMessageBase(byteBufferUtilities);
        assumeTrue(base.bookName() == null);
        assumeTrue(base.page() == 0);

        String randomBookName = fairy.textProducer().word();
        int randomPage = fairy.baseProducer().randomInt(50);

        base.withData(randomBookName, randomPage);

        assertEquals("The book name wasn't returned properly in the toggle message", randomBookName, base.bookName());
        assertEquals("The page offset wasn't returned properly in the toggle message", randomPage, base.page());

    }

    @Test
    public void testToBytes() {
        PageTurnMessageBase base = new PageTurnMessageBase(byteBufferUtilities);
        String randomBookName = fairy.textProducer().word();
        int randomPage = fairy.baseProducer().randomInt(50);
        base.withData(randomBookName, randomPage);

        ByteBuf buffer = Mockito.mock(ByteBuf.class);
        base.toBytes(buffer);

        verify(buffer, times(1)).writeInt(randomPage);
        verify(byteBufferUtilities, times(1)).writeUTF8String(buffer, randomBookName);

    }

    @Test
    public void testFromBytes() {
        PageTurnMessageBase base = new PageTurnMessageBase(byteBufferUtilities);
        String randomBookName = fairy.textProducer().word();
        int randomPage = fairy.baseProducer().randomInt(50);

        assumeTrue(base.bookName() == null);
        assumeTrue(base.page() == 0);

        ByteBuf buffer = Mockito.mock(ByteBuf.class);

        when(buffer.readInt()).thenReturn(randomPage);
        when(byteBufferUtilities.readUTF8String(buffer)).thenReturn(randomBookName);

        base.fromBytes(buffer);

        assertEquals("The book name wasn't parsed from the buffer in the toggle message", randomBookName, base.bookName());
        assertEquals("The page offset wasn't parsed from the buffer in the toggle message", randomPage, base.page());
    }
}
