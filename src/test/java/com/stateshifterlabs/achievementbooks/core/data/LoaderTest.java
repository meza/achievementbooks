package com.stateshifterlabs.achievementbooks.core.data;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class LoaderTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testInit() {
        Path resourceDirectory = Paths.get("src","test","resources");
        File fakeFile = resourceDirectory.resolve("demo.json").toFile();

        File tempConfig = new File(tempFolder.getRoot().getAbsolutePath() + "/achievementbooks");
        Loader.init(tempConfig, fakeFile);

        assertEquals(1, tempConfig.listFiles().length);
        assertEquals("demo.json", tempConfig.listFiles()[0].getName());
    }

    @Test
    public void testConfigFolderNotWriteable() {
        // Temp folder permissions don't work on windows so ignoring the test on win...
        Assume.assumeFalse(System.getProperty("os.name").toLowerCase().startsWith("win"));
        Path resourceDirectory = Paths.get("src","test","resources");
        File fakeFile = resourceDirectory.resolve("demo.json").toFile();

        File tempConfig = new File(tempFolder.getRoot().getAbsolutePath() + "/achievementbooks");
        tempConfig.setWritable(false);
        Loader.init(tempConfig, fakeFile);

        assertEquals(0, tempConfig.listFiles().length);
    }
}
