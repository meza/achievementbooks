package com.stateshifterlabs.achievementbooks.networking;


import com.stateshifterlabs.achievementbooks.facade.BufferUtilities;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ToggleAchievementMessage implements IMessage {
	private final ToggleAchievementMessageBase base = new ToggleAchievementMessageBase(new BufferUtilities());

	public ToggleAchievementMessage withData(String bookName, int achievmenetId) {
		base.withData(bookName, achievmenetId);
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

	public int achievementId() {
		return base.achievementId();
	}

	public String bookName() {
		return base.bookName();
	}

}
