package com.example.asgmandrid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class main_daily_habit_tracker extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton btnaddactivity, btnlogout;
    dbConnect db;
    ArrayList<String> habit_id, title, description, starting_time, ending_time;
    HabitsAdapter HabitsAdapter;
    String username; // Declare username here to be accessible in the entire class
    TextView textUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_daily_habit_tracker);

        // Retrieve the username passed from MainActivity
        username = getIntent().getStringExtra("username");

        recyclerView = findViewById(R.id.recycleView);
        btnaddactivity = findViewById(R.id.btnaddactivity);
        btnlogout = findViewById(R.id.btnlogout);
        textUser = findViewById(R.id.textuser);
        textUser.setText(username); // Display username initially

        btnaddactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(main_daily_habit_tracker.this, CreateHabitActivity.class);
                i.putExtra("username", username); // Pass the username to CreateHabitActivity
                startActivityForResult(i, 1); // Use request code 1
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(main_daily_habit_tracker.this, MainActivity.class);
                startActivity(i);
            }
        });

        // Initialize database and other setup
        db = new dbConnect(main_daily_habit_tracker.this);
        habit_id = new ArrayList<>();
        title = new ArrayList<>();
        description = new ArrayList<>();
        starting_time = new ArrayList<>();
        ending_time = new ArrayList<>();

        displayData();
        HabitsAdapter = new HabitsAdapter(main_daily_habit_tracker.this, habit_id, title, description, starting_time, ending_time);
        recyclerView.setAdapter(HabitsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(main_daily_habit_tracker.this));
    }

    // Method to receive data from CreateHabitActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                username = data.getStringExtra("username");
                textUser.setText(username); // Update the displayed username

                // Clear existing data
                habit_id.clear();
                title.clear();
                description.clear();
                starting_time.clear();
                ending_time.clear();

                // Refresh data from database
                displayData();
                HabitsAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
            }
        }
    }

    void displayData() {
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data Found.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String habitId = cursor.getString(0);
                String habitTitle = cursor.getString(1);
                String startingTime = cursor.getString(3);

                habit_id.add(habitId);
                title.add(habitTitle);
                description.add(cursor.getString(2));
                starting_time.add(startingTime);
                ending_time.add(cursor.getString(4));

                // Schedule a notification for each habit
                setHabitNotification(habitId, habitTitle, startingTime);
            }
        }
    }

    // Call this function when display the data or add a new habit
    private void setHabitNotification(String habit_id, String title, String starting_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Set the current date first
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        try {
            // Parse the time only
            calendar.set(year, month, day, Integer.parseInt(starting_time.split(":")[0]), Integer.parseInt(starting_time.split(":")[1]), 0);


            // Set the alarm 10 minutes before the set time
            calendar.add(Calendar.MINUTE, -10);

            // Log the full calendar date and time
            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {  // Ensure it's a future time
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(this, NotificationReceiver.class);
                intent.putExtra("habit_title", title);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(habit_id), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}