package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.common.NBTUtils;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import com.stateshifterlabs.achievementbooks.facade.Sound;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class GUI extends GuiScreen {

	public static final int GUI_ID = 20;
	public static int bookWidth = 417;
	public static int bookHeight = 245;
	private static ResourceLocation bgl;
	private static ResourceLocation bgr;
	private final int bookFrameHeight = 20;
	private EntityPlayer player;
	private Book book;
	private NetworkAgent networkAgent;
	private Sound sound;
	private String nbttag;
	private int pageOffset = 0;
	private AchievementData achievementData;
	private int oldLeft = 0;
	private int oldTop = 0;
	private int clickDelay = 5;

	public GUI(
			EntityPlayer player, Book book, AchievementData achievementData, NetworkAgent networkAgent, Sound sound
	) {
		this.player = player;
		this.book = book;
		this.networkAgent = networkAgent;
		this.sound = sound;
		book.loadDone(achievementData.completed(book.itemName()));
		nbttag = AchievementBooksMod.MODID.toLowerCase() + ":" + book.itemName() + ":pageOffset";
		pageOffset = NBTUtils.getTag(player.getCurrentEquippedItem()).getInteger(nbttag);
		this.achievementData = achievementData;

		bgl = new ResourceLocation(
				AchievementBooksMod.MODID.toLowerCase() + ":" + "textures/gui/bookgui_left-" + book.colour() + ".png");
		bgr = new ResourceLocation(
				AchievementBooksMod.MODID.toLowerCase() + ":" + "textures/gui/bookgui_right-" + book.colour() + "" +
				".png");

	}

	@SuppressWarnings("unchecked")
	public void initGui(int bookTop, int bookLeft) {

		int maxWidth = (bookWidth / 2) - 35;
		int top = bookTop + bookFrameHeight;
		int left = bookLeft;

		clickDelay = 5;
		this.buttonList.clear();

		for (PageElement element : book.openPage(pageOffset).elements()) {

			if (element.type() == PageElement.Type.HEADER) {
				HeaderGui header = new HeaderGui(element.id(), element, top, left, maxWidth);
				buttonList.addAll(header.buttons());
				top = top + header.height();
			}

			if (element.type() == PageElement.Type.TEXT) {
				DescriptionLine description =
						new DescriptionLine(element.id(), left + 25, top, maxWidth, element.formattedDescription());
				buttonList.add(description);
				top = top + description.getHeight();
			}

			if (element.type() == PageElement.Type.ACHIEVEMENT) {
				AchievementGui achievementGui = new AchievementGui(element.id(), element, top, left, maxWidth);
				buttonList.addAll(achievementGui.buttons());
				top = top + achievementGui.height();
			}

		}

		top = bookTop + bookFrameHeight;
		try {
			if (pageOffset + 1 < book.pageCount()) {
				for (PageElement element : book.openPage(pageOffset + 1).elements()) {

					if (element.type() == PageElement.Type.HEADER) {
						HeaderGui header =
								new HeaderGui(element.id(), element, top, left + (bookWidth / 2) - 15, maxWidth);

						buttonList.addAll(header.buttons());
						top = top + header.height();
					}

					if (element.type() == PageElement.Type.TEXT) {
						DescriptionLine description =
								new DescriptionLine(element.id(), left + 25 + (bookWidth / 2) - 15, top, maxWidth,
													element.formattedDescription());
						buttonList.add(description);
						top = top + description.getHeight();
					}

					if (element.type() == PageElement.Type.ACHIEVEMENT) {
						AchievementGui achievementGui =
								new AchievementGui(element.id(), element, top, left + (bookWidth / 2) - 15, maxWidth);
						buttonList.addAll(achievementGui.buttons());
						top = top + achievementGui.height();
					}

				}
			}
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}


		if (pageOffset > 0) {
			buttonList.add(new PaginationButton(0, bookLeft, bookTop + bookHeight - 23, false, clickDelay));
		}
		if (pageOffset + 1 < book.pageCount() - 1) {
			buttonList.add(new PaginationButton(1, bookLeft + bookWidth - 22, bookTop + bookHeight - 23, true,
												clickDelay));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		clickDelay = Math.max(0, clickDelay - 1);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int bookLeft = (this.width - bookWidth) / 2;
		int bookTop = (int) ((this.height - bookHeight) / 2.5);

		if (oldLeft != bookLeft || oldTop != bookTop) {
			oldLeft = bookLeft;
			oldTop = bookTop;
			initGui(bookTop, bookLeft);
		}

		this.mc.getTextureManager().bindTexture(bgl);
		this.drawTexturedModalRect(bookLeft, bookTop, 0, 0, bookWidth / 2, bookHeight);

		this.mc.getTextureManager().bindTexture(bgr);
		this.drawTexturedModalRect(bookLeft + bookWidth / 2, bookTop, 0, 0, bookWidth / 2, bookHeight);

		super.drawScreen(mouseX, mouseY, par3);
	}

	@Override
	public void setWorldAndResolution(Minecraft mcInstance, int width, int height) {
		this.mc = mcInstance;
		this.fontRendererObj = mcInstance.fontRenderer;
		this.width = width;
		this.height = height;
		int bookLeft = (this.width - bookWidth) / 2;
		int bookTop = (int) ((this.height - bookHeight) / 2.5);
		this.initGui(bookTop, bookLeft);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		sound.closeBook();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id > 1) {
			sound.toggle();
			((AchievementLine) button).toggle();
			networkAgent.toggle(book, button.id);
		} else if (clickDelay <= 0) {
			if (button.id == 0) {
				previousPage();
			} else if (button.id == 1) {
				nextPage();
			}
		}

	}

	private void nextPage() {
		int bookLeft = (this.width - bookWidth) / 2;
		int bookTop = (int) ((this.height - bookHeight) / 2.5);

		pageOffset = pageOffset + 2;

		sound.nextPage();
		initGui(bookTop, bookLeft);
		savePageNumber();
	}

	private void previousPage() {
		int bookLeft = (this.width - bookWidth) / 2;
		int bookTop = (int) ((this.height - bookHeight) / 2.5);

		pageOffset = pageOffset - 2;

		sound.previousPage();
		initGui(bookTop, bookLeft);
		savePageNumber();
	}

	private void savePageNumber() {
		NBTUtils.getTag(player.getCurrentEquippedItem()).setInteger(nbttag, pageOffset);
		networkAgent.sendPageNumber(book, pageOffset);
	}

}
