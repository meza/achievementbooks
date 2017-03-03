package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.facade.ByteBufferUtilities;
import com.stateshifterlabs.achievementbooks.facade.NetworkMessage;
import io.netty.buffer.ByteBuf;

public class ToggleAchievementMessageBase implements NetworkMessage {
	private String bookName;
	private int achievementId;
	private ByteBufferUtilities bufferUtilities;

	public ToggleAchievementMessageBase(ByteBufferUtilities bufferUtilities) {
		this.bufferUtilities = bufferUtilities;
	}

	public ToggleAchievementMessageBase withData(String bookName, int achievmenetId) {
		this.bookName = bookName;
		this.achievementId = achievmenetId;

		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		achievementId = buf.readInt();
		bookName = bufferUtilities.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(achievementId);
		bufferUtilities.writeUTF8String(buf, bookName);
	}

	public int achievementId() {
		return achievementId;
	}

	public String bookName() {
		return bookName;
	}
}
