package com.stateshifterlabs.achievementbooks.core.facade;

import io.netty.buffer.ByteBuf;

public interface NetworkMessage {
    void fromBytes(ByteBuf buf);

    void toBytes(ByteBuf buf);
}
