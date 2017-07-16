package com.stateshifterlabs.achievementbooks.items;


import com.stateshifterlabs.achievementbooks.client.gui.GUI;
import com.stateshifterlabs.achievementbooks.data.AchievementStorage;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.facade.MCPlayer;
import com.stateshifterlabs.achievementbooks.facade.Player;
import com.stateshifterlabs.achievementbooks.facade.Sound;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IRegistryDelegate;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class AchievementBookItem extends Item {

	private Book book;
	private AchievementStorage achievementStorage;
	private NetworkAgent networkAgent;
	private Sound sound;

	public AchievementBookItem(
			Book book, AchievementStorage achievementStorage, NetworkAgent networkAgent, Sound sound
	) {
		super();
		this.book = book;
		this.achievementStorage = achievementStorage;
		this.networkAgent = networkAgent;
		this.sound = sound;
		setRegistryName(MODID + ":" + book.itemName());
		setCreativeTab(CreativeTabs.MISC);
		setUnlocalizedName("achievementbooks:" + book.itemName());
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {

		if (worldIn.isRemote) {
			sound.openBook();
			Player thePlayer = new MCPlayer(playerIn);
			final GUI screen = new GUI(playerIn, book, achievementStorage.forPlayer(thePlayer), networkAgent, sound);
			Minecraft.getMinecraft().displayGuiScreen(screen);
		}

		return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(hand));
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return book.name();
	}

	public void updateBook(Book book) {
		this.book = book;
	}

	public ModelResourceLocation getModelLocation() {
		return new ModelResourceLocation(MODID + ":book-" + book.colour(), "inventory");
	}
}
