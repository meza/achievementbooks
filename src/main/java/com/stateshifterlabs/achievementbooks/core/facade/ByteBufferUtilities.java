package com.stateshifterlabs.achievementbooks.core.facade;

import io.netty.buffer.ByteBuf;

public interface ByteBufferUtilities {

	String readUTF8String(ByteBuf buf);

	void writeUTF8String(ByteBuf buf, String string);

}
