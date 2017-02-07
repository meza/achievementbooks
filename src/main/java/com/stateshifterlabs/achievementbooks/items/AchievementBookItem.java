package com.stateshifterlabs.achievementbooks.items;


import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.client.gui.GUI;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AchievementBookItem extends Item {

	private Book book;
	private AchievementStorage achievementStorage;
	private NetworkAgent networkAgent;

	public AchievementBookItem(Book book, AchievementStorage achievementStorage, NetworkAgent networkAgent) {
		super();
		this.book = book;
		this.achievementStorage = achievementStorage;
		this.networkAgent = networkAgent;
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName("achievementbooks.achievementBook." + book.name());
		setTextureName(AchievementBooksMod.MODID + ":book");
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			Minecraft.getMinecraft().displayGuiScreen(new GUI(player, book, achievementStorage.forPlayer(player), networkAgent));
		}
		return par1ItemStack;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack)
	{
		return book.name();
	}

}
