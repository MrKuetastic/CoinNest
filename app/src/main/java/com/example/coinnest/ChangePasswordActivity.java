package com.example.coinnest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        EditText oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        EditText newPasswordEditText = findViewById(R.id.newPasswordEditText);
        EditText confirmNewPasswordEditText = findViewById(R.id.confirmNewPasswordEditText);
        Button makeChangesButton = findViewById(R.id.makeChangesButton);

        makeChangesButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(this, "New password and confirm new password do not match", Toast.LENGTH_SHORT).show();
                return;
            }

        });
    };
}