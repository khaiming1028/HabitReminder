package com.example.asgmandrid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText loginusername, loginpassword;
    Button btnlogin, btnregister, btnforgotpassword;
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginusername = findViewById(R.id.loginusername);
        loginpassword = findViewById(R.id.loginpassword);
        btnlogin = findViewById(R.id.btnlogin);
        btnregister = findViewById(R.id.btnregister);
        btnforgotpassword = findViewById(R.id.btnforgotpassword);
        db = new dbConnect(this);

        // Handle register button click
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });

        // Handle login button click
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUsername = loginusername.getText().toString().trim();
                String strPassword = loginpassword.getText().toString().trim();

                if (strUsername.isEmpty() || strPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the user exists in the database
                    if (checkUserCredentials(strUsername, strPassword)) {
                        // Login successful, navigate to main daily habit tracker
                        Intent i = new Intent(MainActivity.this, main_daily_habit_tracker.class);
                        i.putExtra("username", strUsername);
                        startActivity(i);
                        finish(); // Optional: finish the current activity
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Handle forgot password button click (if you implement it later)
        btnforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(i);
            }
        });

    }

    // Method to check user credentials in the database
    private boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        String query = "SELECT * FROM " + dbConnect.dbTable + " WHERE username = ? AND password = ?";
        Cursor cursor = dbRead.rawQuery(query, new String[]{username, password});

        boolean loginSuccessful = cursor.getCount() > 0; // Check if any results are returned

        cursor.close();
        dbRead.close();
        return loginSuccessful;
    }
}
