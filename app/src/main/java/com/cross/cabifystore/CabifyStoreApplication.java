package com.cross.cabifystore;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cross.cabifystore.data.SQLiteHandler;

public class CabifyStoreApplication extends Application {
    private static CabifyStoreApplication instance;
    private SQLiteHandler sqLiteHandler;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        sqLiteHandler = new SQLiteHandler(this);
        sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static Context getAppContext() {
        return instance;
    }

    public SQLiteHandler getSQLiteHandler() {
        return sqLiteHandler;
    }
}
