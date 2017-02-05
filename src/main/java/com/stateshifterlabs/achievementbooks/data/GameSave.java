package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.stateshifterlabs.achievementbooks.AchievementBooksMod.MODID;

public class GameSave {


	private final Gson gson;
	private File saveDir;
	private Books books;

	public GameSave(Books books) {
		this.books = books;
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Book.class, new BookSerializer());
		gson = builder.create();
	}

	public void load() {
		saveDir =
				new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/" + MODID.toLowerCase());

		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}

		for (Book book : books) {
			File saveFile = new File(saveDir.getAbsolutePath() + "/" + book.name() + ".save.json");
			if (!saveFile.exists()) {
				save(book);
			}


			try {
				Save bookSave = gson.fromJson(new FileReader(saveFile), Save.class);
				book.loadDone(bookSave.completedAchievements());

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

	}

	public void save() {
		saveDir =
				new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "/" + MODID.toLowerCase());
		saveDir.mkdirs();

		for (Book book : books) {
			save(book);

		}

	}

	public void save(Book book) {

		File saveFile = new File(saveDir.getAbsolutePath() + "/" + book.name() + ".save.json");
		String saveData = gson.toJson(book);

		try {
			FileWriter fw = new FileWriter(saveFile);

			fw.write(saveData);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			//AchievementBooksMod.logger.severe("Could not save achievements file!");
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load load) {
		if (!load.world.isRemote) {
			load();
		}

	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save save) {
		if (!save.world.isRemote) {
			save();
		}

	}

}
