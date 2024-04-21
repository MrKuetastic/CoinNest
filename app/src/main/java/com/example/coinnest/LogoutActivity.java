package com.example.coinnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coinnest.R;


public class LogoutActivity extends AppCompatActivity {

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_logout);
//
//        TextView logoutActivity = findViewById(R.id.logoutActivity);
//        Button logoutButton = findViewById(R.id.logoutButton);
//
//        logoutActivity.setText("Logout Successful");
//        logoutButton.setVisibility(View.GONE);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        TextView logoutActivity = findViewById(R.id.logoutActivity);
        Button submitFeedbackButton = findViewById(R.id.logoutButton);
        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display "Thank you" message
                Toast.makeText(LogoutActivity.this, "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


