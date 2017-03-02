package com.stateshifterlabs.achievementbooks.networking;

import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.facade.BufferUtilities;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


public class CompletionDetailsMessage implements IMessage {

	CompletionDetailsMessageBase base = new CompletionDetailsMessageBase(new BufferUtilities());

	public CompletionDetailsMessage withData(AchievementData achievementData) {
		base.withData(achievementData);
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

	public AchievementData achievementData() {
		return base.achievementData();
	}

}
