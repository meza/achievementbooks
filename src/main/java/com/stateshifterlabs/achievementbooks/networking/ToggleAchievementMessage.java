package com.stateshifterlabs.achievementbooks.networking;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class ToggleAchievementMessage implements IMessage {
	private String bookName;
	private int achievmenetId;

	public ToggleAchievementMessage withData(String bookName, int achievmenetId) {

		this.bookName = bookName;
		this.achievmenetId = achievmenetId;

		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		achievmenetId = buf.readInt();
		bookName = buf.readBytes(buf.readableBytes()).toString(Charset.forName("UTF-8"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(achievmenetId);
		buf.writeBytes(bookName.getBytes(Charset.forName("UTF-8")));
	}

	public int achievmenetId() {
		return achievmenetId;
	}

	public String bookName() {
		return bookName;
	}
}
