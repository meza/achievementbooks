package com.stateshifterlabs.achievementbooks.client.gui;

import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import com.stateshifterlabs.achievementbooks.common.NBTUtils;
import com.stateshifterlabs.achievementbooks.data.AchievementData;
import com.stateshifterlabs.achievementbooks.data.Book;
import com.stateshifterlabs.achievementbooks.data.PageElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static com.stateshifterlabs.achievementbooks.client.items.AchievementBookItem.bookHeight;
import static com.stateshifterlabs.achievementbooks.client.items.AchievementBookItem.bookWidth;

public class GUI extends GuiScreen {

	public static final int GUI_ID = 20;
	private static ResourceLocation bgl =
			new ResourceLocation(AchievementBooksMod.MODID.toLowerCase() + ":" + "textures/gui/bookgui_left.png");
	private static ResourceLocation bgr =
			new ResourceLocation(AchievementBooksMod.MODID.toLowerCase() + ":" + "textures/gui/bookgui_right.png");
	private final int bookFrameHeight = 20;
	private EntityPlayer player;
	private Book book;
	private String nbttag;

	private int clickDelay = 5;
	private int pageOffset = 0;
	private AchievementData achievementData;
	private int oldLeft = 0;
	private int oldTop = 0;

	public GUI(EntityPlayer player, Book book, AchievementData achievementData) {
		this.player = player;
		this.book = book;
 		book.loadDone(achievementData.completed(book.name()));
		nbttag = AchievementBooksMod.MODID.toLowerCase() + ":" + book.name() + ":pageOffset";
		pageOffset = NBTUtils.getTag(player.getCurrentEquippedItem()).getInteger(nbttag);
		this.achievementData = achievementData;
	}

	@SuppressWarnings("unchecked")
	public void initGui(int bookTop, int bookLeft) {

		int maxWidth = (bookWidth / 2) - 35;
		int top = bookTop + bookFrameHeight;
		int left = bookLeft;

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

		for (PageElement element : book.openPage(pageOffset + 1).elements()) {

			if (element.type() == PageElement.Type.HEADER) {
				HeaderGui header = new HeaderGui(element.id(), element, top, left + (bookWidth / 2) - 15, maxWidth);
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


		if (pageOffset > 0) {
			buttonList.add(new PaginationButton(0, bookLeft, bookTop + bookHeight - 23, false));
		}
		if (pageOffset + 1 < book.pageCount() - 1) {
			buttonList.add(new PaginationButton(1, bookLeft + bookWidth - 22, bookTop + bookHeight - 23, true));
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
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			previousPage();
		} else if (button.id == 1) {
			nextPage();
		} else {
			((AchievementLine) button).toggle();
			achievementData.toggle(book, button.id);
		}
	}

	private void nextPage() {
		int bookLeft = (this.width - bookWidth) / 2;
		int bookTop = (int) ((this.height - bookHeight) / 2.5);

		pageOffset = pageOffset + 2;

		initGui(bookTop, bookLeft);
		setNBT();
	}

	private void previousPage() {
		int bookLeft = (this.width - bookWidth) / 2;
		int bookTop = (int) ((this.height - bookHeight) / 2.5);

		pageOffset = pageOffset - 2;

		initGui(bookTop, bookLeft);
		setNBT();
	}

	private void setNBT() {
		NBTUtils.getTag(player.getCurrentEquippedItem()).setInteger(nbttag, pageOffset);
	}

}
