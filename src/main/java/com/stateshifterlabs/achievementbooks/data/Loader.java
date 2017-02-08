package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.AchievementBooksMod;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

public class Loader {
	private File configDir;

	public Loader(File configDir) {
		this.configDir = configDir;
	}

	public Books init() {
		Books books = new Books();
		FilenameFilter fileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					// get last index for '.' char
					int lastIndex = name.lastIndexOf('.');

					// get extension
					String str = name.substring(lastIndex);

					// match path name extension
					if (str.equals(".json")) {
						return true;
					}
				}
				return false;
			}
		};

		if (!configDir.exists()) {
			configDir.mkdirs();
		}

		final File[] files = configDir.listFiles(fileNameFilter);

		if(files.length == 0) {
			File file = new File(configDir.getAbsolutePath() + "/demo.json");
			URL url = AchievementBooksMod.class.getResource("demo.json");
			try {
				FileUtils.copyURLToFile(url, file);
				return init();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (File conf : files) {
			try {


				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(Book.class, new BookDeserializer());
				Gson gson = gsonBuilder.create();

				Book book = gson.fromJson(new FileReader(conf), Book.class);

				books.addBook(book);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return books;
	}
}
