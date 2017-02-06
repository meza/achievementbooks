package com.stateshifterlabs.achievementbooks.client.items;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.client.gui.GUI;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AchievementBookItem extends Item {

	public static int bookWidth = 417;
	public static int bookHeight = 245;
	private Book book;
	private AchievementStorage achievementStorage;

	public AchievementBookItem(Book book, AchievementStorage achievementStorage) {
		super();
		this.book = book;
		this.achievementStorage = achievementStorage;
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName("achievementbooks.achievementBook." + book.name());
		setTextureName(AchievementBooksMod.MODID + ":book");
		setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			Minecraft.getMinecraft().displayGuiScreen(new GUI(player, book, achievementStorage.forPlayer(player)));
		}
		return par1ItemStack;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack)
	{
		return book.name();
	}

}
