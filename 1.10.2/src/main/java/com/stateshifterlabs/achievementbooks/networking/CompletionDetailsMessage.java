package com.stateshifterlabs.achievementbooks.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.serializers.AchievementDataSerializer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


public class CompletionDetailsMessage implements IMessage {
	private Gson gson;
	private AchievementData achievementData;

	public CompletionDetailsMessage withData(AchievementData achievementData) {

		this.achievementData = achievementData;

		return this;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(null));
		gson = builder.create();

		String json = ByteBufUtils.readUTF8String(buf);
		achievementData = gson.fromJson(json, AchievementData.class);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(AchievementData.class, new AchievementDataSerializer(achievementData.username()));
		gson = builder.create();
		String json = gson.toJson(achievementData);
		ByteBufUtils.writeUTF8String(buf, json);

	}

	public AchievementData achievementData() {
		return achievementData;
	}

}
