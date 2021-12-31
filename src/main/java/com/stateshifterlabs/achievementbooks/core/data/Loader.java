package com.stateshifterlabs.achievementbooks.core.data;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.stateshifterlabs.achievementbooks.core.errors.CouldNotWriteConfigFile;
import com.stateshifterlabs.achievementbooks.core.errors.FreakFileReadError;
import com.stateshifterlabs.achievementbooks.core.errors.JsonParseError;
import com.stateshifterlabs.achievementbooks.core.serializers.BookSerializer;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.nio.charset.Charset;

public class Loader {
    public static Books init(File configDir, File demoFile) {
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

        ensureConfigfolder(configDir);

        final File[] files = configDir.listFiles(fileNameFilter);

        // Create the demo config if no config is present -> recursive
        if (files.length == 0) {
            File file = new File(configDir.getAbsolutePath() + "/demo.json");

            try {
                FileUtils.copyFile(demoFile, file);
                return init(configDir, demoFile);
            } catch (Exception e) {
                throw new CouldNotWriteConfigFile(file);
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
                    throw new JsonParseError("There is an error in the book config. Paste the following file to http://jsonlint.com/ to find what the error is.", conf);
                }
            } catch (FileNotFoundException e) {
                throw new FreakFileReadError(conf);
            }
        }
        return books;
    }

    private static void ensureConfigfolder(File configDir) {
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }
}
