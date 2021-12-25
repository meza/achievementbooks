package com.stateshifterlabs.achievementbooks.core.data;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.serializers.BookSerializer;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class Loader {
    public static Books init(File configDir) {
        Books books = new Books();
        books.empty();
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

        if (files.length == 0) {
            File file = new File(configDir.getAbsolutePath() + "/demo.json");

            if (file.exists()) {
                throw new DemoAlreadyExistsException();
            }

            URL url = AchievementBooks.class.getResource("/config/demo.json");
            try {
                FileUtils.copyURLToFile(url, file);
                return init(configDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (File conf : files) {
            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Book.class, new BookSerializer(conf));
                Gson gson = gsonBuilder.create();
                try {
                    Book book = gson.fromJson(new BufferedReader(Files.newReader(conf, Charset.defaultCharset())), Book.class);
                    books.addBook(book);
                } catch (JsonSyntaxException e) {
                    throw new JsonParseError("There is an error in the book config. Use http://jsonlint.com/ to find it", conf);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return books;
    }
}
