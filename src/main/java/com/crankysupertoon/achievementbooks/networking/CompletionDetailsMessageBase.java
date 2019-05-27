package com.crankysupertoon.achievementbooks.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.crankysupertoon.achievementbooks.data.AchievementData;
import com.crankysupertoon.achievementbooks.facade.ByteBufferUtilities;
import com.crankysupertoon.achievementbooks.facade.NetworkMessage;
import com.crankysupertoon.achievementbooks.serializers.AchievementDataSerializer;
import io.netty.buffer.ByteBuf;

public class CompletionDetailsMessageBase implements NetworkMessage {
	private Gson gson;
	private AchievementData achievementData;
	private ByteBufferUtilities bufferUtilities;

	public CompletionDetailsMessageBase(ByteBufferUtilities bufferUtilities) {
		this.bufferUtilities = bufferUtilities;
	}

	public CompletionDetailsMessageBase withData(AchievementData achievementData) {
		this.achievementData = achievementData;
		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(null));
		gson = builder.create();

		String json = bufferUtilities.readUTF8String(buf);
		achievementData = gson.fromJson(json, AchievementData.class);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(achievementData.username()));
		gson = builder.create();
		String json = gson.toJson(achievementData);
		bufferUtilities.writeUTF8String(buf, json);
	}

	public AchievementData achievementData() {
		return achievementData;
	}

}
