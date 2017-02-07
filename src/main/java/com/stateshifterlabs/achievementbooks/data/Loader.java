package com.stateshifterlabs.achievementbooks.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;

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
