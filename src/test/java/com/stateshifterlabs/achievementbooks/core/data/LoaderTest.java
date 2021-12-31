package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.core.errors.CouldNotWriteConfigFile;
import com.stateshifterlabs.achievementbooks.core.errors.JsonParseError;
import org.apache.commons.io.FileUtils;
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

    private Path resourceDirectory = Paths.get("src","test","resources");
    File demoFixture = resourceDirectory.resolve("demo.json").toFile();

    @Test
    public void testInit() {
        File tempConfig = new File(tempFolder.getRoot().getAbsolutePath() + "/achievementbooks");
        Loader.init(tempConfig, demoFixture);

        assertEquals(1, tempConfig.listFiles().length);
        assertEquals("demo.json", tempConfig.listFiles()[0].getName());
    }

    @Test(expected = JsonParseError.class)
    public void testMalformedConfigs() throws IOException {
        File malformedFile = resourceDirectory.resolve("demo-malformed.json").toFile();

        File tempConfig = new File(tempFolder.getRoot().getAbsolutePath() + "/achievementbooks");
        tempConfig.mkdirs();
        FileUtils.copyFile(malformedFile, new File(tempConfig.getAbsolutePath() + "/malformed.json"));
        Loader.init(tempConfig, demoFixture);
    }


    @Test(expected = CouldNotWriteConfigFile.class)
    public void testConfigFolderNotWriteable() {
        // Temp folder permissions don't work on windows so ignoring the test on win...
        Assume.assumeFalse(System.getProperty("os.name").toLowerCase().startsWith("win"));

        File fakeFile = resourceDirectory.resolve("demo.json").toFile();

        File tempConfig = new File(tempFolder.getRoot().getAbsolutePath() + "/achievementbooks");
        tempConfig.mkdirs();
        tempConfig.setWritable(false);
        tempConfig.setReadOnly();
        Loader.init(tempConfig, fakeFile);

        assertEquals(0, tempConfig.listFiles().length);
    }
}
