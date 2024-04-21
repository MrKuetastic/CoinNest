//package com.example.coinnest;
//
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SettingActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting); // Ensure you have a corresponding layout file
//        ListView listView = findViewById(R.id.myListView2); // Use findViewById directly on the Activity
//        String[] names = {"Notification", "Credits", "Term of use", "Logout"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names); // Use 'this' instead of 'getContext()' if you're inside an Activity
//        listView.setAdapter(adapter);
//    }
//
//}
//


package com.example.coinnest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting); // Ensure you have the corresponding layout file

        // Finding the ListView
        ListView listView = findViewById(R.id.myListView2);

        // Array of names for the ListView
        String[] names = {"Notification", "Credits", "Term of use", "Logout"};

        // Creating an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);

        // Setting an item click listener on the ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter.getItem(position);

            // Check if the selected item is "Logout"
//            if ("Logout".equals(selectedItem)) {
//                // Log out the user
//                Database database = Database.getInstance(getApplicationContext());
//                database.logOut(new Database.MySQLAccessResponseListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        if ("1".equals(response.trim())) {
//                            Toast.makeText(SettingActivity.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
//                            // Start the LoginActivity
//                            Intent intent = new Intent(SettingActivity.this, LoginFragment.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Toast.makeText(SettingActivity.this, "Logout Failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onError(String message) {
//                        Toast.makeText(SettingActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
            // Check if the selected item is "Notification"
            if ("Logout".equals(selectedItem)) {
                // Start the Logout
                Intent intent = new Intent(SettingActivity.this, LoginFragment.class);
                Toast.makeText(SettingActivity.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
            // Check if the selected item is "Notification"
            if ("Notification".equals(selectedItem)) {
                // Start the NotificationsActivity
                Intent intent = new Intent(SettingActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }

            if ("Term of use".equals(selectedItem)) {
                // Start the NotificationsActivity
                Intent intent = new Intent(SettingActivity.this, TermsOfUseActivity.class);
                startActivity(intent);
            }

            if ("Credits".equals(selectedItem)) {
                // Start the NotificationsActivity
                Intent intent = new Intent(SettingActivity.this, CreditsActivity.class);
                startActivity(intent);
            }

        });
    }
}



