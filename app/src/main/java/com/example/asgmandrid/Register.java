package com.example.asgmandrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText regusername, regemail, regpassword;
    Button regbtn, regbtnback;
    TextView txtDisplayInfoReg;
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regusername = findViewById(R.id.regusername);
        regemail = findViewById(R.id.regemail);
        regpassword = findViewById(R.id.regpassword);
        regbtn = findViewById(R.id.regbtn);
        regbtnback = findViewById(R.id.regbtnback);
        txtDisplayInfoReg = findViewById(R.id.txtDisplayInfoReg);
        db = new dbConnect(this);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = regemail.getText().toString().trim();
                String strUsername = regusername.getText().toString().trim();
                String strPassword = regpassword.getText().toString().trim();

                if (strEmail.isEmpty() || strUsername.isEmpty() || strPassword.isEmpty()) {
                    txtDisplayInfoReg.setText("All fields are required");
                } else {
                    // Create a new user object
                    Users user = new Users(strEmail, strUsername, strPassword);

                    // Add user to the database
                    try {
                        db.addUser(user);
                        txtDisplayInfoReg.setText("Registration successful");

                        // Optionally, clear the input fields
                        regemail.setText("");
                        regusername.setText("");
                        regpassword.setText("");

                        // Redirect to MainActivity or another activity
                        Intent i = new Intent(Register.this, MainActivity.class);
                        startActivity(i);
                        finish(); // Optional: close this activity
                    } catch (Exception e) {
                        txtDisplayInfoReg.setText("Error: " + e.getMessage());
                    }
                }
            }
        });

        regbtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
