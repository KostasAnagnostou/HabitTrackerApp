package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;
import com.example.android.habittrackerapp.data.HabitDbHelper;

/**
 * Created by Kostas on 18/7/2017.
 */

public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the habit name
     */
    private EditText mHabitEditText;

    /**
     * EditText field to enter the started date of the habit
     */
    private EditText mStartedEditText;

    /**
     * Spinner field to enter the Frequency of the habit
     */
    private Spinner mFrequencySpinner;

    /**
     * EditText field to enter the duration for the habit
     */
    private EditText mDurationEditText;

    /**
     * Spinner field to enter Duration Measurement for the habit
     */
    private Spinner mDurationMeasurementSpinner;

    /**
     * EditText field to enter notes for habit
     */
    private EditText mNotesEditText;

    /**
     * Frequency of the habit. The possible values are:
     * 0 for unknown, 1 for Daily, 2 for Weekly, 3 for Monthly.
     */
    private int mFrequency = 0;

    /**
     * Frequency of the habit. The possible values are:
     * 0 for unknown, 1 for Daily, 2 for Weekly, 3 for Monthly.
     */
    private String mDurationMeasurement = "min";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mHabitEditText = (EditText) findViewById(R.id.edit_habit_name);
        mStartedEditText = (EditText) findViewById(R.id.edit_started_date);
        mFrequencySpinner = (Spinner) findViewById(R.id.spinner_frequency);
        mDurationEditText = (EditText) findViewById(R.id.edit_habit_duration);
        mDurationMeasurementSpinner = (Spinner) findViewById(R.id.spinner_duration_measurement);
        mNotesEditText = (EditText) findViewById(R.id.edit_habit_notes);

        setupFrequencySpinner();
        setupDurationMeasurementSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the frequency of habit .
     */
    private void setupFrequencySpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter frequencySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_frequency_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        frequencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mFrequencySpinner.setAdapter(frequencySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.frequency_daily))) {
                        mFrequency = HabitEntry.FREQUENCY_DAILY; // Daily
                    } else if (selection.equals(getString(R.string.frequency_weekly))) {
                        mFrequency = HabitEntry.FREQUENCY_WEEKLY; // Weekly
                    } else if (selection.equals(getString(R.string.frequency_monthly))) {
                        mFrequency = HabitEntry.FREQUENCY_MONTHLY; // Monthly
                    } else {
                        mFrequency = HabitEntry.FREQUENCY_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFrequency = HabitEntry.FREQUENCY_UNKNOWN; // Unknown
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupDurationMeasurementSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter durationSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_duration_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        durationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mDurationMeasurementSpinner.setAdapter(durationSpinnerAdapter);

        // Set the String mDurationMeasurement to the constant values
        mDurationMeasurementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.duration_min))) {
                        mDurationMeasurement = HabitEntry.DURATION_MINS; // Minutes
                    } else {
                        mDurationMeasurement = HabitEntry.DURATION_HOURS; // Hours
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mDurationMeasurement = HabitEntry.DURATION_MINS; // Unknown
            }
        });
    }

    /**
     * Get user input from the editor and save new habit into the database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mHabitEditText.getText().toString().trim();
        String startedDate = mStartedEditText.getText().toString().trim();
        String notes = mNotesEditText.getText().toString().trim();
        String durationString = mDurationEditText.getText().toString().trim();

        // Check those string variables & if any of them isEmpty()
        // Open again the EditorActivity with a toast message
        // then stop the execute of the method
        if (nameString.isEmpty() || durationString.isEmpty() || startedDate.isEmpty()) {
            // If the name Habit is empty show this message
            Intent openEditorIntent = new Intent(this, EditorActivity.class);
            startActivity(openEditorIntent);
            Toast.makeText(this, "All fields are required except notes", Toast.LENGTH_LONG).show();
            return;
        }
        // else (if the String variables aren't empty)

        int duration = Integer.parseInt(durationString);

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and habit attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_STARTED, startedDate);
        values.put(HabitEntry.COLUMN_HABIT_NOTES, notes);
        values.put(HabitEntry.COLUMN_HABIT_FREQUENCY, mFrequency);
        values.put(HabitEntry.COLUMN_HABIT_DURATION, duration);
        values.put(HabitEntry.COLUMN_HABIT_DURATION_MEASUREMENT, mDurationMeasurement);

        // Insert a new row for habit in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}