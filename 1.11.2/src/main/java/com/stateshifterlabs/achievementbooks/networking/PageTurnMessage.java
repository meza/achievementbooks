package com.stateshifterlabs.achievementbooks.networking;


import com.stateshifterlabs.achievementbooks.facade.BufferUtilities;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PageTurnMessage implements IMessage {
	private final PageTurnMessageBase base = new PageTurnMessageBase(new BufferUtilities());

	public PageTurnMessage withData(String bookName, int pageOffset) {
		base.withData(bookName, pageOffset);
		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		base.fromBytes(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		base.toBytes(buf);
	}

	public int page() {
		return base.page();
	}

	public String bookName() {
		return base.bookName();
	}

}
