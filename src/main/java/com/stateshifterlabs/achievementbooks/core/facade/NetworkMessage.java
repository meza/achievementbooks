package com.stateshifterlabs.achievementbooks.core.facade;

import io.netty.buffer.ByteBuf;

public interface NetworkMessage {
	public void fromBytes(ByteBuf buf);
	public void toBytes(ByteBuf buf);
}
