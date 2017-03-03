package com.stateshifterlabs.achievementbooks.facade;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;


public class BufferUtilities implements ByteBufferUtilities {
	@Override
	public String readUTF8String(ByteBuf buf) {
		return ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void writeUTF8String(ByteBuf buf, String string) {
		ByteBufUtils.writeUTF8String(buf, string);
	}
}
