package com.stateshifterlabs.achievementbooks.core.errors;

import com.stateshifterlabs.achievementbooks.core.UTF8Utils;

import java.io.File;

public class CouldNotWriteConfigFile extends java.lang.RuntimeException {

    private File file;

    public CouldNotWriteConfigFile(File file) {
        super(String.format(
                "\n\n" +
                        "*****************************  THIS IS WHY YOUR GAME DOESN'T START  *******************************\n\n"+
                        "\tCannot write file: \"%s\"\n" +
                        "\tPlease make sure the directory is writeable\n"+
                        "\n**************************************************************************************************\n",
                file.getAbsolutePath()));
        this.file = file;

    }

    public String simpleMessage() {
        return String.format(UTF8Utils.utf8String("Cannot write file: %s§r"), file.getAbsolutePath());
    }
}
