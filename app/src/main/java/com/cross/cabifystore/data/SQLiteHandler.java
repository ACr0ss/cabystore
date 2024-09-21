package com.cross.cabifystore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cross.cabifystore.CabifyStoreApplication;
import com.cross.cabifystore.models.Item;

import java.util.ArrayList;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "cabstore";
    private static final int DB_VERSION = 1;
    private static final String COL_CODE = "code";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";

    public SQLiteHandler(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS items ("
                + COL_CODE + " text PRIMARY KEY NOT NULL, "
                + COL_NAME + " text, "
                + COL_PRICE + " double,"
                + "UNIQUE (" + COL_CODE + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {

    }

    public void addItems(ArrayList<Item> items) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            db.beginTransaction();

            for (int i = 0; i < items.size(); i++) {
                values.put(COL_CODE, items.get(i).getCode());
                values.put(COL_NAME, items.get(i).getName());
                values.put(COL_PRICE, items.get(i).getPrice());

                db.insert("items", null, values);
            }

            db.setTransactionSuccessful();
            new PreferencesManager(CabifyStoreApplication.getAppContext()).saveTimestamp();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM items", null);

        int colCode = c.getColumnIndex(COL_CODE);
        int colName = c.getColumnIndex(COL_NAME);
        int colPrice = c.getColumnIndex(COL_PRICE);

        if (c.moveToFirst()) {
            do {
                items.add(new Item(c.getString(colCode), c.getString(colName), c.getDouble(colPrice)));
            } while (c.moveToNext());
        }

        c.close();
        return items;
    }
}
