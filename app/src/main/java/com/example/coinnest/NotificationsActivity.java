package com.example.coinnest;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coinnest.R;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Switch switch2 = findViewById(R.id.switch2);
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(NotificationsActivity.this, "You have successfully turned on notifications", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotificationsActivity.this, "You have successfully turned off notifications", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
