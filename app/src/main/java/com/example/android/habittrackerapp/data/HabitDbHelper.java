package com.example.android.habittrackerapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;

/**
 * Created by Kostas on 18/7/2017.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HabitDbHelper.class.getSimpleName();

    // Name of the database
    private static final String DATABASE_NAME = "habits.db";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // The onCreate method Creates the Table for the habits in the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_HABITS_TABLE =
                "CREATE TABLE " + HabitEntry.TABLE_NAME + " (" +
                        HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        HabitEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL, " +
                        HabitEntry.COLUMN_HABIT_STARTED + " TEXT NOT NULL, " +
                        HabitEntry.COLUMN_HABIT_FREQUENCY + " INTEGER NOT NULL DEFAULT 0, " +
                        HabitEntry.COLUMN_HABIT_DURATION + " INTEGER NOT NULL," +
                        HabitEntry.COLUMN_HABIT_DURATION_MEASUREMENT + " TEXT NOT NULL, " +
                        HabitEntry.COLUMN_HABIT_NOTES + " TEXT);";

        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
    }
}