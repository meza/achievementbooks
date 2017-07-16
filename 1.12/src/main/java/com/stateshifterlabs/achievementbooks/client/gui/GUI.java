package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.client.BookSettings;
import com.stateshifterlabs.achievementbooks.common.NBTUtils;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import com.stateshifterlabs.achievementbooks.data.Type;
import com.stateshifterlabs.achievementbooks.facade.Sound;
import com.stateshifterlabs.achievementbooks.networking.NetworkAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;


public class GUI extends GuiScreen {


	private static ResourceLocation bgl;
	private static ResourceLocation bgr;

	private int nextButtonId;
	private int prevButtonId;
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
		this.prevButtonId = getFreeId(new Integer[]{});
		this.nextButtonId = getFreeId(new Integer[]{prevButtonId});
		book.loadDone(achievementData.completed(book.itemName()));
		nbttag = AchievementBooksMod.MODID.toLowerCase() + ":" + book.itemName() + ":pageOffset";
		pageOffset = NBTUtils.getTag(player.getHeldItemMainhand()).getInteger(nbttag);
		this.achievementData = achievementData;

		bgl = new ResourceLocation(
				AchievementBooksMod.MODID.toLowerCase() + ":" + "textures/gui/bookgui_left-" + book.colour() + ".png");
		bgr = new ResourceLocation(
				AchievementBooksMod.MODID.toLowerCase() + ":" + "textures/gui/bookgui_right-" + book.colour() + "" +
				".png");

	}

	@SuppressWarnings("unchecked")
	public void initGui(int bookTop, int bookLeft) {

		int maxWidth = (BookSettings.bookWidth / 2) - 35;
		int top = bookTop + BookSettings.bookFrameHeight;
		int left = bookLeft;

		clickDelay = 5;
		this.buttonList.clear();


		for (PageElement element : book.openPage(pageOffset).elements()) {

			if (element.type() == Type.HEADER) {
				HeaderElementComposite header = new HeaderElementComposite(element.id(), element, top, left, maxWidth);
				buttonList.addAll(header.buttons());
				top = top + header.height();
			}

			if (element.type() == Type.TEXT) {
				DescriptionLine description =
						new DescriptionLine(element.id(), left + 25, top, maxWidth, element.formattedDescription());
				buttonList.add(description);
				top = top + description.getHeight();
			}

			if (element.type() == Type.ACHIEVEMENT) {
				AchievementElementComposite
						achievementElementComposite = new AchievementElementComposite(element.id(), element, top, left, maxWidth);
				buttonList.addAll(achievementElementComposite.buttons());
				top = top + achievementElementComposite.height();
			}

		}

		top = bookTop + BookSettings.bookFrameHeight;
		try {
			if (pageOffset + 1 < book.pageCount()) {
				for (PageElement element : book.openPage(pageOffset + 1).elements()) {

					if (element.type() == Type.HEADER) {
						HeaderElementComposite header =
								new HeaderElementComposite(element.id(), element, top, left + (BookSettings.bookWidth / 2) - 15, maxWidth);

						buttonList.addAll(header.buttons());
						top = top + header.height();
					}

					if (element.type() == Type.TEXT) {
						DescriptionLine description =
								new DescriptionLine(element.id(), left + 25 + (BookSettings.bookWidth / 2) - 15, top, maxWidth,
													element.formattedDescription());
						buttonList.add(description);
						top = top + description.getHeight();
					}

					if (element.type() == Type.ACHIEVEMENT) {
						AchievementElementComposite achievementElementComposite =
								new AchievementElementComposite(element.id(), element, top, left + (BookSettings.bookWidth / 2) - 15, maxWidth);
						buttonList.addAll(achievementElementComposite.buttons());
						top = top + achievementElementComposite.height();
					}

				}
			}
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}

		if (pageOffset > 0) {
			buttonList.add(new PaginationButton(prevButtonId, bookLeft, bookTop + BookSettings.bookHeight - 23, false));
		}
		if (pageOffset + 1 < book.pageCount() - 1) {
			buttonList
					.add(new PaginationButton(nextButtonId, bookLeft + BookSettings.bookWidth - 22, bookTop + BookSettings.bookHeight - 23, true));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {
		clickDelay = Math.max(0, clickDelay - 1);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int bookLeft = (this.width - BookSettings.bookWidth) / 2;
		int bookTop = (int) ((this.height - BookSettings.bookHeight) / 2.5);

		if (oldLeft != bookLeft || oldTop != bookTop) {
			oldLeft = bookLeft;
			oldTop = bookTop;
			initGui(bookTop, bookLeft);
		}

		this.mc.getTextureManager().bindTexture(bgl);
		this.drawTexturedModalRect(bookLeft, bookTop, 0, 0, BookSettings.bookWidth / 2, BookSettings.bookHeight);

		this.mc.getTextureManager().bindTexture(bgr);
		this.drawTexturedModalRect(bookLeft + BookSettings.bookWidth / 2, bookTop, 0, 0, BookSettings.bookWidth / 2, BookSettings.bookHeight);

		super.drawScreen(mouseX, mouseY, par3);
	}

	@Override
	public void setWorldAndResolution(Minecraft mcInstance, int width, int height) {
		this.mc = mcInstance;
		this.fontRenderer = mcInstance.fontRenderer;
		this.width = width;
		this.height = height;
		int bookLeft = (this.width - BookSettings.bookWidth) / 2;
		int bookTop = (int) ((this.height - BookSettings.bookHeight) / 2.5);
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
		if (button.id != prevButtonId && button.id != nextButtonId) {
			sound.toggle();
			((AchievementLine) button).toggle();
			networkAgent.toggle(book, button.id);
		} else if (clickDelay <= 0) {
			if (button.id == prevButtonId) {
				previousPage();
			} else if (button.id == nextButtonId) {
				nextPage();
			}
		}
	}

	private void nextPage() {
		int bookLeft = (this.width - BookSettings.bookWidth) / 2;
		int bookTop = (int) ((this.height - BookSettings.bookHeight) / 2.5);

		pageOffset = pageOffset + 2;

		sound.nextPage();
		initGui(bookTop, bookLeft);
		savePageNumber();
	}

	private void previousPage() {
		int bookLeft = (this.width - BookSettings.bookWidth) / 2;
		int bookTop = (int) ((this.height - BookSettings.bookHeight) / 2.5);

		pageOffset = pageOffset - 2;

		sound.previousPage();
		initGui(bookTop, bookLeft);
		savePageNumber();
	}

	private void savePageNumber() {
		NBTUtils.getTag(player.getHeldItemMainhand()).setInteger(nbttag, pageOffset);
		networkAgent.sendPageNumber(book, pageOffset);
	}

	private int getFreeId(Integer[] ints) {
		List blacklist = Arrays.asList(ints);
		for (int i = -65535; i < 65535; i++) {
			if (!book.idExists(i) && !blacklist.contains(i)) {
				return i;
			}
		}
		throw new RuntimeException("Could not find an empty ID for buttons");
	}

}
