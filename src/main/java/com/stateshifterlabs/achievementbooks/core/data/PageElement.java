package com.stateshifterlabs.achievementbooks.core.data;

import com.stateshifterlabs.achievementbooks.core.ChatFormatting;
import com.stateshifterlabs.achievementbooks.core.UTF8Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PageElement {
    private static final Logger LOGGER = LogManager.getLogger(PageElement.class);
    private final int id;
    private String achievement;
    private boolean checked = false;
    private String description;
    private String header;
    private String mod;

    public PageElement(int id) {
        this.id = id;
    }

    public String achievement() {
        return achievement;
    }

    public boolean checked() {
        return this.checked;
    }

    public String description() {
        return description;
    }

    public String formattedAchievement() {
        if (!hasAchievement()) {
            return "";
        }
        if (hasMod()) {
            return String.format("%s %s", UTF8Utils.utf8String(achievement), formattedMod());
        }
        return String.format("%s", UTF8Utils.utf8String(achievement));
    }

    public String formattedDescription() {
        if (!hasDescription()) {
            return "";
        }
        return UTF8Utils.utf8String(ChatFormatting.ITALIC.toString(), String.format("%s", description));
    }

    public String formattedHeader() {
        if (!hasHeader()) {
            return "";
        }
        return UTF8Utils.utf8String(ChatFormatting.BOLD.toString(), header);
    }

    public String formattedMod() {
        if (!hasMod()) {
            return "";
        }
        return UTF8Utils.utf8String(ChatFormatting.DARK_BLUE.toString(), ChatFormatting.ITALIC.toString(), String.format("[%s]", mod), ChatFormatting.RESET.toString());
    }

    public boolean hasAchievement() {
        return achievement() != null;
    }

    public boolean hasDescription() {
        return description() != null;
    }

    public boolean hasHeader() {
        return header() != null;
    }

    public boolean hasMod() {
        return mod() != null;
    }

    @Override
    public final int hashCode() {
        int result = achievement != null ? achievement.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (mod != null ? mod.hashCode() : 0);
        result = 31 * result + (checked ? 1 : 0);
        result = 31 * result + id;
        return result;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageElement)) {
            return false;
        }

        PageElement that = (PageElement) o;

        if (checked != that.checked) {
            return false;
        }
        if (id != that.id) {
            return false;
        }
        if (achievement != null ? !achievement.equals(that.achievement) : that.achievement != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (header != null ? !header.equals(that.header) : that.header != null) {
            return false;
        }
        return mod != null ? mod.equals(that.mod) : that.mod == null;
    }

    public String header() {
        return header;
    }

    public int id() {
        return id;
    }

    public String mod() {
        return mod;
    }

    public void toggleState() {
        this.checked = !this.checked;
    }

    public void toggleState(boolean checked) {
        this.checked = checked;
    }

    public Type type() {
        if (achievement != null) {
            return Type.ACHIEVEMENT;
        }

        if (header != null) {
            return Type.HEADER;
        }

        return Type.TEXT;
    }

    public void withAchievement(String achievement) {
        if (!hasAchievement()) {
            this.achievement = achievement;
        }
    }

    public void withDescription(String description) {
        if (!hasDescription()) {
            this.description = description;
        }
    }

    public void withHeader(String header) {
        if (!hasHeader()) {
            this.header = header;
        }
    }

    public void withMod(String mod) {
        if (!hasMod()) {
            this.mod = mod;
        }
    }

}
