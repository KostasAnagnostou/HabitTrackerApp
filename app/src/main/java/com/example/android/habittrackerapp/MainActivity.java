package com.example.android.habittrackerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.habittrackerapp.data.HabitContract;
import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;
import com.example.android.habittrackerapp.data.HabitDbHelper;

/**
 * Displays list of habits that were entered and stored in the app.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Database helper that will provide us access to the database
     */
    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * query() method to display information in the onscreen TextView about the state of
     * the habits database.
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM habits"
        // to get a Cursor that contains all rows from the habits table.
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_STARTED,
                HabitEntry.COLUMN_HABIT_FREQUENCY,
                HabitEntry.COLUMN_HABIT_DURATION,
                HabitEntry.COLUMN_HABIT_DURATION_MEASUREMENT,
                HabitEntry.COLUMN_HABIT_NOTES
        };

        Cursor cursor = db.query(                           // guery() method
                HabitEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        // Display the number of rows in the Cursor (which reflects the number of rows in the
        // habits table in the database).
        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - started - frequency - duration - duration_measurement - notes
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The Habits Table contains " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_NAME + " - " +
                    HabitEntry.COLUMN_HABIT_STARTED + " - " +
                    HabitEntry.COLUMN_HABIT_FREQUENCY + " - " +
                    HabitEntry.COLUMN_HABIT_DURATION + " - " +
                    HabitEntry.COLUMN_HABIT_DURATION_MEASUREMENT + " - " +
                    HabitEntry.COLUMN_HABIT_NOTES + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int startedColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_STARTED);
            int frequencyColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_FREQUENCY);
            int durationColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DURATION);
            int durationMeasurementColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DURATION_MEASUREMENT);
            int notesColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NOTES);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentStarted = cursor.getString(startedColumnIndex);
                int currentFrequency = cursor.getInt(frequencyColumnIndex);
                int currentDuration = cursor.getInt(durationColumnIndex);
                String currentDurationMeasurement = cursor.getString(durationMeasurementColumnIndex);
                String currentNotes = cursor.getString(notesColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentStarted + " - " +
                        currentFrequency + " - " +
                        currentDuration + " - " +
                        currentDurationMeasurement + " - " +
                        currentNotes));
            }
        } finally {
            // Close the cursor when you're done reading from it.
            cursor.close();
        }
    }
}
