package com.stateshifterlabs.achievementbooks.helpers;

public class RandomTestData<J, O> {

    private final J json;
    private final O object;

    public RandomTestData(J json, O object) {
        this.json = json;
        this.object = object;
    }

    public J jsonFormat() {
        return json;
    }

    public O objectFormat() {
        return object;
    }

}
