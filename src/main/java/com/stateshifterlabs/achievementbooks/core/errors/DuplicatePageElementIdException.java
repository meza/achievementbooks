package com.stateshifterlabs.achievementbooks.core.errors;

import com.stateshifterlabs.achievementbooks.core.UTF8Utils;
import com.stateshifterlabs.achievementbooks.core.data.PageElement;

public class DuplicatePageElementIdException extends RuntimeException {

    private final String book;
    private final PageElement element;

    public DuplicatePageElementIdException(PageElement element, String book) {
        super(String.format(
                "\n\n" +
                        "*****************************  THIS IS WHY YOUR GAME DOESN'T START  *******************************\n\n" +
                        "\tAn entry with the id %d already exists in the config for book: %s.\n" +
                        "\tPlease use unique ids for each element.\n" +
                        "\n**************************************************************************************************\n",
                element.id(), book));
        this.element = element;
        this.book = book;
    }

    public String simpleMessage() {
        return String.format(
                UTF8Utils.utf8String("§eAn entry with the id %d already exists in the config for book: %s.§r"),
                element.id(), book);
    }
}
