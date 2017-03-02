package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.facade.ByteBufferUtilities;
import com.stateshifterlabs.achievementbooks.facade.NetworkMessage;
import io.netty.buffer.ByteBuf;

public class PageTurnMessageBase implements NetworkMessage {
	private String bookName;
	private int pageOffset;
	private ByteBufferUtilities bufferUtilities;

	public PageTurnMessageBase(ByteBufferUtilities bufferUtilities) {

		this.bufferUtilities = bufferUtilities;
	}

	public PageTurnMessageBase withData(String bookName, int pageOffset) {
		this.bookName = bookName;
		this.pageOffset = pageOffset;

		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pageOffset = buf.readInt();
		bookName = bufferUtilities.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pageOffset);
		bufferUtilities.writeUTF8String(buf, bookName);
	}

	public int page() {
		return pageOffset;
	}

	public String bookName() {
		return bookName;
	}
}
