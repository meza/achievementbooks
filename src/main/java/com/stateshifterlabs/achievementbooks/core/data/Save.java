package com.stateshifterlabs.achievementbooks.core.data;

import java.util.ArrayList;
import java.util.List;

public class Save {
    private final List<Integer> done = new ArrayList<Integer>();

    public List<Integer> completedAchievements() {
        return done;
    }

    public void toggle(Integer id) {
        if (done.contains(id)) {
            done.remove(id);
            return;
        }

        done.add(id);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Save)) {
            return false;
        }

        Save save = (Save) o;

        return done != null ? done.equals(save.done) : save.done == null;

    }

    @Override
    public final int hashCode() {
        return done != null ? done.hashCode() : 0;
    }
}
