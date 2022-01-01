package com.stateshifterlabs.achievementbooks.core.errors;

import com.stateshifterlabs.achievementbooks.core.UTF8Utils;

import java.io.File;

public class FreakFileReadError extends RuntimeException {

    private final File file;

    public FreakFileReadError(File file) {
        super(UTF8Utils.utf8String(
                "\n\n" +
                        "*****************************  THIS IS WHY YOUR GAME DOESN'T START  *******************************\n\n" +
                        "\tCannot find the file: \"%s\"\n" +
                        "\tThis error should never happen.\n" +
                        "\tPlease check your configuration folder and make sure the file exists.\n" +
                        "\tIt's also very likely that if you re-run the server it will just work.\n" +
                        "\tIf not, please submit a ticket over at: https://github.com/meza/achievementbooks/issues/new\n" +
                        "\n**************************************************************************************************\n",
                file.getAbsolutePath()));
        this.file = file;

    }

    public String simpleMessage() {
        return String.format(UTF8Utils.utf8String("Cannot find file: %s§r"), file.getAbsolutePath());
    }
}
