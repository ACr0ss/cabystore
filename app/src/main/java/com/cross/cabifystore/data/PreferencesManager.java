package com.cross.cabifystore.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static final String PREF_NAME = "shared_preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesManager(Context ctx) {
        sharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveTimestamp() {
        editor.putLong("timestamp", System.currentTimeMillis());
        editor.apply();
    }

    public Long getLastTimestamp() {
        return sharedPreferences.getLong("timestamp", 0);
    }
}
