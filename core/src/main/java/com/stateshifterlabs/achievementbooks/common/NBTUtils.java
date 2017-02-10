package com.stateshifterlabs.achievementbooks.common;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils
{
	public static NBTTagCompound getTag(ItemStack stack)
	{
		if (stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}

		return stack.getTagCompound();
	}
}
