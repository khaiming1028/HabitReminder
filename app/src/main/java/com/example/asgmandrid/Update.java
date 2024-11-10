package com.example.asgmandrid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Update extends AppCompatActivity {

    EditText etHabitTitle_edit, etHabitDescription_edit, etStartingTime_edit, etEndingTime_edit;
    Button btnUpdateHabit, btnDeleteHabit, uhbtnback;
    String id, title, description, startingTime, endingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etHabitTitle_edit = findViewById(R.id.etHabitTitle_edit);
        etHabitDescription_edit = findViewById(R.id.etHabitDescription_edit);
        etStartingTime_edit = findViewById(R.id.etStartingTime_edit);
        etEndingTime_edit = findViewById(R.id.etEndingTime_edit);
        btnUpdateHabit = findViewById(R.id.btnUpdateHabit);
        btnDeleteHabit = findViewById(R.id.btnDeleteHabit);
        uhbtnback = findViewById(R.id.uhbtnback);


        // Set up the TimePicker for Starting Time
        etStartingTime_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(etStartingTime_edit);
            }
        });

        // Set up the TimePicker for Ending Time
        etEndingTime_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(etEndingTime_edit);
            }
        });

        // Call this first to populate data if available
        getandSetIntentData();



        // Update button action
        btnUpdateHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = etHabitTitle_edit.getText().toString();
                description = etHabitDescription_edit.getText().toString();
                startingTime = etStartingTime_edit.getText().toString();
                endingTime = etEndingTime_edit.getText().toString();

                dbConnect db = new dbConnect(Update.this);
                db.UpdateData(id, title, description, startingTime, endingTime);
            }
        });

        btnDeleteHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();


            }
        });

        uhbtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Update.this, main_daily_habit_tracker.class);
                startActivity(i);
            }
        });

    }

    // Method to open TimePickerDialog
    private void showTimePicker(final EditText timeEditText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Update.this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeEditText.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    // Method to get data from Intent and set it in EditText fields
    void getandSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("startingTime") &&
                getIntent().hasExtra("endingTime")) {

            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            startingTime = getIntent().getStringExtra("startingTime");
            endingTime = getIntent().getStringExtra("endingTime");

            etHabitTitle_edit.setText(title);
            etHabitDescription_edit.setText(description);
            etStartingTime_edit.setText(startingTime);
            etEndingTime_edit.setText(endingTime);

        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + " ?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbConnect db = new dbConnect(Update.this);
                db.deleteOneRow(id);

                // Set result code to indicate deletion and finish activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("isDeleted", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, just dismiss the dialog
            }
        });
        builder.create().show();
    }
}