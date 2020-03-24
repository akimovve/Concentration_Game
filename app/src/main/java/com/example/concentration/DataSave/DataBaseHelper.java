package com.example.concentration.DataSave;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String TABLE_NAME = "GameRes";

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private String COLUMN_ID = "id";
    private String COLUMN_NAME = "Name";
    private String COLUMN_PERCENTS = "Percents";
    private String COLUMN_FLIPS = "Flips";
    private String COLUMN_POINTS = "Points";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;

        query = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT UNIQUE, "
                + COLUMN_PERCENTS + " REAL, "
                + COLUMN_FLIPS + " INTEGER, "
                + COLUMN_POINTS + " INTEGER);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
