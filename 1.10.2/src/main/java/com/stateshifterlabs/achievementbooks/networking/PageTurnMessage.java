package com.stateshifterlabs.achievementbooks.networking;


import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.Charset;

public class PageTurnMessage implements IMessage {
	private String bookName;
	private int pageOffset;

	public PageTurnMessage withData(String bookName, int pageOffset) {
		this.bookName = bookName;
		this.pageOffset = pageOffset;

		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pageOffset = buf.readInt();
		bookName = buf.readBytes(buf.readableBytes()).toString(Charset.forName("UTF-8"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pageOffset);
		buf.writeBytes(bookName.getBytes(Charset.forName("UTF-8")));
	}

	public int page() {
		return pageOffset;
	}

	public String bookName() {
		return bookName;
	}
}
