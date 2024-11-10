    package com.example.asgmandrid;

    import android.annotation.SuppressLint;
    import android.app.TimePickerDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TimePicker;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import java.util.Calendar;

    public class CreateHabitActivity extends AppCompatActivity {

        EditText etHabitTitle, etHabitDescription, etStartingTime, etEndingTime;
        Button btnSaveHabit, chbtnback;
        dbConnect db;

        @SuppressLint("MissingInflatedId")
        String username;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_habit);

            etHabitTitle = findViewById(R.id.etHabitTitle);
            etHabitDescription = findViewById(R.id.etHabitDescription);
            etStartingTime = findViewById(R.id.etStartingTime);
            etEndingTime = findViewById(R.id.etEndingTime);
            btnSaveHabit = findViewById(R.id.btnSaveHabit);
            chbtnback = findViewById(R.id.chbtnback);

            db = new dbConnect(this); // Initialize database helper
            username = getIntent().getStringExtra("username");

            etStartingTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePicker(etStartingTime);
                }
            });

            etEndingTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePicker(etEndingTime);
                }
            });

            btnSaveHabit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveHabit();
                }
            });

            chbtnback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CreateHabitActivity.this, main_daily_habit_tracker.class);
                    startActivity(i);
                }
            });
        }

        private void showTimePicker(final EditText timeField) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            timeField.setText(String.format("%02d:%02d", hourOfDay, minute));
                        }
                    }, hour, minute, true);

            timePickerDialog.show();
        }

        private void saveHabit() {
            String title = etHabitTitle.getText().toString();
            String description = etHabitDescription.getText().toString();
            String startingTime = etStartingTime.getText().toString();
            String endingTime = etEndingTime.getText().toString();

            if (title.isEmpty() || description.isEmpty() || startingTime.isEmpty() || endingTime.isEmpty()) {
                Toast.makeText(CreateHabitActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = db.addHabit(title, description, startingTime, endingTime);
            if (result != -1) {
                Toast.makeText(this, "Habit saved successfully", Toast.LENGTH_SHORT).show();
                etHabitTitle.setText("");
                etHabitDescription.setText("");
                etStartingTime.setText("");
                etEndingTime.setText("");

                // Send the username back to main_daily_habit_tracker
                Intent resultIntent = new Intent();
                resultIntent.putExtra("username", getIntent().getStringExtra("username"));
                setResult(RESULT_OK, resultIntent);
                finish(); // Close CreateHabitActivity and return to main_daily_habit_tracker
            } else {
                Toast.makeText(this, "Failed to save habit", Toast.LENGTH_SHORT).show();
            }
        }
    }
