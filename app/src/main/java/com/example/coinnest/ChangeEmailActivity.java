package com.example.coinnest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        EditText oldEmailEditText = findViewById(R.id.oldEmailEditText);
        EditText newEmailEditText = findViewById(R.id.newEmailEditText);
        EditText confirmNewEmailEditText = findViewById(R.id.confirmNewEmailEditText);
        Button makeChangesButton = findViewById(R.id.makeChangesButton);

        makeChangesButton.setOnClickListener(v -> {
            String oldEmail = oldEmailEditText.getText().toString();
            String newEmail = newEmailEditText.getText().toString();
            String confirmNewEmail = confirmNewEmailEditText.getText().toString();

            if (!newEmail.equals(confirmNewEmail)) {
                Toast.makeText(this, "New email and confirm new email do not match", Toast.LENGTH_SHORT).show();
                return;
            }


        });
    }
}