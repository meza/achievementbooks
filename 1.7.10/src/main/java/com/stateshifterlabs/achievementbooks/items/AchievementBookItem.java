package com.stateshifterlabs.achievementbooks.items;


import com.stateshifterlabs.achievementbooks.client.gui.GUI;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.facade.MCPlayer;
import com.stateshifterlabs.achievementbooks.facade.Player;
import com.stateshifterlabs.achievementbooks.facade.Sound;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class AchievementBookItem extends Item {

	private Book book;
	private AchievementStorage achievementStorage;
	private NetworkAgent networkAgent;
	private Sound sound;

	public AchievementBookItem(Book book, AchievementStorage achievementStorage, NetworkAgent networkAgent, Sound sound) {
		super();
		this.book = book;
		this.achievementStorage = achievementStorage;
		this.networkAgent = networkAgent;
		this.sound = sound;
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName("achievementbooks.achievementBook." + book.itemName());
		setTextureName(MODID + ":book" + "-" +book.colour());
		setMaxStackSize(1);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			sound.openBook();
			Player thePlayer = new MCPlayer(player);
			final GUI screen = new GUI(player, book, achievementStorage.forPlayer(thePlayer), networkAgent,
									   sound);
			Minecraft.getMinecraft().displayGuiScreen(screen);
		}
		return par1ItemStack;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack)
	{
		return book.name();
	}

	public void updateBook(Book book) {
		this.book = book;
	}

}
