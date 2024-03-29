package com.stateshifterlabs.achievementbooks.core.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stateshifterlabs.achievementbooks.AchievementBooks;
import com.stateshifterlabs.achievementbooks.core.serializers.AchievementStorageSerializer;

import java.io.*;

public class GameSave {

    private final Books books;
    private final Gson gson;
    private final File saveFile;
    private AchievementStorage storage;

    public GameSave(File saveFile, AchievementStorage storage, Books books) {
        this.saveFile = saveFile;
        this.storage = storage;
        this.books = books;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AchievementStorage.class, new AchievementStorageSerializer(storage));
        gson = builder.create();
    }

    public void load() {
        load(this.saveFile);
    }

    public void load(File saveFile) {
        if (!saveFile.exists()) {
            return;
        }

        try {
            storage.clear();
            AchievementStorage result = gson.fromJson(new FileReader(saveFile), AchievementStorage.class);
            if (result != null) {
                storage = result;
            }
        } catch (FileNotFoundException e) {

        }
    }

    public void save() {
        save(this.saveFile);
    }

    public void save(File saveFile) {

        String saveData = gson.toJson(storage);
        try {

            saveFile.createNewFile();

            FileWriter fw = new FileWriter(saveFile);
            fw.write(saveData);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            AchievementBooks.LOGGER.info("Failed to save achievements with file: " + saveFile.getAbsolutePath());
            //AchievementBooksMod.logger.severe("Could not save achievements file!");
        }
    }
}
