//package com.example.coinnest;
//
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class RateUsActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rate_us); // Set to your layout file
//    }
//}


package com.example.coinnest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RateUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us); // Set to your layout file

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Display "Thank you" message
                Button submitFeedbackButton = findViewById(R.id.submitFeedbackButton);
                submitFeedbackButton.setVisibility(View.VISIBLE);
            }
        });

        Button submitFeedbackButton = findViewById(R.id.submitFeedbackButton);
        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display "Thank you" message
                Toast.makeText(RateUsActivity.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}