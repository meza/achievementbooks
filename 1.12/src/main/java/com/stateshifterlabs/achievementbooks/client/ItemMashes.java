package com.stateshifterlabs.achievementbooks.client;

import com.stateshifterlabs.achievementbooks.items.AchievementBookItem;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMashes implements ItemMeshDefinition {

	private AchievementBookItem item;

	public ItemMashes(AchievementBookItem item) {
		this.item = item;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModelLocation(
			ItemStack stack
	) {
		return item.getModelLocation();
	}
}
