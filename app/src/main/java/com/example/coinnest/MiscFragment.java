package com.example.coinnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MiscFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_misc, container, false);
        ListView listView = rootView.findViewById(R.id.myListView);
        String[] names = {"Setting", "Contact Us", "Rate Us", "Account"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Setting
                    navigateToSetting();
                } else if (position == 1) { // Contact Us
                    navigateToContactUs();
                } else if (position == 2) { // Rate Us
                    navigateToRateUs();
                } else if (position == 3) { // Account
                    navigateToAccount();
                }
            }
        });

        return rootView;
    }

    private void navigateToSetting() {
        // Navigation code for Settings
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    private void navigateToContactUs() {
        // Navigation code for Contact Us
        // Assuming ContactUsActivity is the Activity to launch
        Intent intent = new Intent(getActivity(), ContactUsActivity.class);
        startActivity(intent);
    }

    private void navigateToRateUs() {
        // Navigation code for Rate Us
        // Assuming RateUsActivity is the Activity to launch
        Intent intent = new Intent(getActivity(), RateUsActivity.class);
        startActivity(intent);
    }

    private void navigateToAccount() {
        // Navigation code for Account
        // Assuming AccountActivity is the Activity to launch
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        startActivity(intent);
    }
}
