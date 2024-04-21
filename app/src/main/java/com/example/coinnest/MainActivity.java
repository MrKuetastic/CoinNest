package com.example.coinnest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import com.example.coinnest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean isLoggedIn = false; // You would typically check this against SharedPreferences or a similar persistent state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initially hide the navigation bar until the user logs in
        binding.bottomAppBar.setVisibility(View.GONE);
        binding.floatingActionButton.setVisibility(View.GONE); // The ID here must match the XML


        if (!isLoggedIn) {
            // Show the LoginFragment
            replaceFragment(new LoginFragment());
        } else {
            // User is already logged in, so show the HomeFragment and the navigation bar
            replaceFragment(new HomeFragment());
            binding.bottomAppBar.setVisibility(View.VISIBLE);
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (!isLoggedIn) return false; // Do nothing if not logged in

            // Use if-else statements as you requested
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.reports) {
                replaceFragment(new BillPageFragment());
            } else if (itemId == R.id.calendar) {
                replaceFragment(new CalendarFragment());
            } else if (itemId == R.id.misc) {
                replaceFragment(new MiscFragment());
            } else if (itemId == R.id.fab) {
                replaceFragment(new BillPageFragment());
            }
            return true;
        });
    }

    public void onLoginSuccess() {
        // This method will be called when login is successful
        isLoggedIn = true;
        replaceFragment(new HomeFragment());
        binding.bottomAppBar.setVisibility(View.VISIBLE);
        binding.floatingActionButton.setVisibility(View.VISIBLE); // Make the FAB visible after login
    }


    protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
