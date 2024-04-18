///*Jordan's Code*/
//package com.example.coinnest;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//
//public class HomeFragment extends Fragment {
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        ListView listView = view.findViewById(R.id.listBalances);
//
//        TextView currentDateTextView = view.findViewById(R.id.currentDateTextView);
//        String currentDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(new Date());
//        currentDateTextView.setText(currentDate);
//
//        // Apply the adapter to the ListView
//        String[] items = new String[] {"Credit Card 1 = $234.22", "Credit Card 2: $567.23", "Mortgage: $80,500.00",
//                "Car Loan: $35,000.00", "Student Loans: $20,000.00"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
//        listView.setAdapter(adapter);
//
//        ListView billsListView = view.findViewById(R.id.upcomingBillsListView);
//        String[] bills = new String[] {"Credit Card: $30.00", "Credit Card 2: $50.00",
//                "Mortgage: $1,500.00" , "Car Loan: $550.00", "Student Loans: $1,000"};
//        ArrayAdapter<String> billsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, bills);
//        billsListView.setAdapter(billsAdapter);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//}



package com.example.coinnest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RequestQueue queue;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queue = Volley.newRequestQueue(requireContext());

        ListView listView = view.findViewById(R.id.listBalances);
        ListView billsListView = view.findViewById(R.id.upcomingBillsListView);

        TextView currentDateTextView = view.findViewById(R.id.currentDateTextView);
        String currentDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(new Date());
        currentDateTextView.setText(currentDate);


        fetchAndDisplayBalances(listView);

        fetchAndDisplayBills(billsListView);
    }

    private void fetchAndDisplayBalances(ListView listView) {
        String url = "http://18.189.7.172/fetchBill.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray data = new JSONArray(response);
                        Map<String, Double> categoryTotals = new HashMap<>();


                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);

                            if (item.has("category") && item.has("amount")) {
                                String category = item.getString("category");
                                double amount = item.getDouble("amount");

                                if (!categoryTotals.containsKey(category)) {
                                    categoryTotals.put(category, amount);
                                } else {
                                    double currentTotal = categoryTotals.get(category);
                                    categoryTotals.put(category, currentTotal + amount);
                                }
                            }
                        }

                        List<String> balanceSummaries = new ArrayList<>();
                        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                            balanceSummaries.add(entry.getKey() + ": $" + String.format("%.2f", entry.getValue()));
                        }

                        ArrayAdapter<String> balanceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, balanceSummaries);
                        listView.setAdapter(balanceAdapter);

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Error parsing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(getActivity(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        queue.add(stringRequest);
    }



    private void fetchAndDisplayBills(ListView billsListView) {
        String url = "http://18.189.7.172/fetchBill.php";  // URL to your PHP script

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray billsArray = new JSONArray(response);
                        String[] bills = new String[billsArray.length()];
                        for (int i = 0; i < billsArray.length(); i++) {
                            JSONObject bill = billsArray.getJSONObject(i);
                            String billName = bill.getString("bill_name");
                            String amount = bill.getString("amount");
                            bills[i] = billName + ": $" + amount;
                        }
                        ArrayAdapter<String> billsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, bills);
                        billsListView.setAdapter(billsAdapter);
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Error parsing bills", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(getActivity(), "Error fetching bills: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        queue.add(stringRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}