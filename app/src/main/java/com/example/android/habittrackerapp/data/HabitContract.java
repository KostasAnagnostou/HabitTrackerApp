package com.example.android.habittrackerapp.data;

import android.provider.BaseColumns;

/**
 * Created by Kostas on 18/7/2017.
 * HabitContract is the Habit Database Schema
 */

public final class HabitContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private HabitContract() {
    }

    /* Inner class that defines the table constants for each habit */
    public static final class HabitEntry implements BaseColumns {

        /*  Constant for the table name in the database */
        public final static String TABLE_NAME = "habits";

        /*  Constants for Columns Names (or Attributes of the Table)  */
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HABIT_NAME = "habit";
        public final static String COLUMN_HABIT_STARTED = "started";
        public final static String COLUMN_HABIT_FREQUENCY = "frequency";
        public final static String COLUMN_HABIT_DURATION = "duration";
        public final static String COLUMN_HABIT_DURATION_MEASUREMENT = "min_hr";
        public final static String COLUMN_HABIT_NOTES = "notes";

        /* Constants for HABIT_FREQUENCY COLUMN (Attribute of the table) */
        public static final int FREQUENCY_UNKNOWN = 0;
        public static final int FREQUENCY_DAILY = 1;
        public static final int FREQUENCY_WEEKLY = 2;
        public static final int FREQUENCY_MONTHLY = 3;

        /* Constants for HABIT_DURATION_MEASUREMENT (Attribute of the table) */
        public static final String DURATION_MINS = "min";
        public static final String DURATION_HOURS = "hr";
    }
}


