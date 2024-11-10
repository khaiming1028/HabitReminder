package com.example.asgmandrid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    EditText password_username, new_password;
    Button btnpassword, btnbackpassword;
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        password_username = findViewById(R.id.password_username);
        new_password = findViewById(R.id.new_password);
        btnpassword = findViewById(R.id.btnpassword);
        btnbackpassword = findViewById(R.id.btnbackpassword);
        db = new dbConnect(this);

        btnpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = password_username.getText().toString().trim();
                String newPassword = new_password.getText().toString().trim();

                if (username.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (validateUsername(username)) {
                        updatePassword(username, newPassword);
                        Toast.makeText(ForgotPassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to login screen
                    } else {
                        Toast.makeText(ForgotPassword.this, "Username not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnbackpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPassword.this, MainActivity.class);
                startActivity(i);
            }
        });
    }


    private boolean validateUsername(String username) {
        SQLiteDatabase dbRead = db.getReadableDatabase();
        String query = "SELECT * FROM " + dbConnect.dbTable + " WHERE username = ?";
        Cursor cursor = dbRead.rawQuery(query, new String[]{username});

        boolean userExists = cursor.getCount() > 0;

        cursor.close();
        dbRead.close();
        return userExists;
    }

    private void updatePassword(String username, String newPassword) {
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        String query = "UPDATE " + dbConnect.dbTable + " SET password = ? WHERE username = ?";
        dbWrite.execSQL(query, new String[]{newPassword, username});
        dbWrite.close();
    }
}
