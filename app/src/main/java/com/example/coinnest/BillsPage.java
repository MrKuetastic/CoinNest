package com.example.coinnest;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//fenzhang added
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class BillsPage extends AppCompatActivity implements BillsAdapter.OnBillListener {
    private RecyclerView recyclerViewBills;
    private BillsAdapter adapter;
    private List<Bill> billsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bill);

        String dateString = getIntent().getStringExtra("selectedDate");
        if (dateString == null) {
            Toast.makeText(this, "No date provided!", Toast.LENGTH_LONG).show();
            finish(); // Exit activity if no date is provided
            return;
        }


        recyclerViewBills = findViewById(R.id.upcomingBillsRecyclerView);
        adapter = new BillsAdapter(billsList, this, this);
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBills.setAdapter(adapter);

        // Optionally, fetch bills for the selected date
        Database database = Database.getInstance(getApplicationContext());
        database.fetchBillsByDate(dateString, true, new Database.MySQLAccessResponseListener() {
            @Override
            public void onResponse(String response) {
                updateBillList(response);  // Parse and update UI
            }

            @Override
            public void onError(String message) {
                Toast.makeText(BillsPage.this, "Error fetching bills: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }



    //fenzhang: update the list
    private void updateBillList(String jsonData) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Bill>>(){}.getType();
        try {
            List<Bill> bills = gson.fromJson(jsonData, listType);
            billsList.clear();
            if (bills != null) {
                billsList.addAll(bills);
            }
            adapter.notifyDataSetChanged();  // Notify the adapter to refresh the data
        } catch (Exception e) {
            Toast.makeText(this, "Failed to parse bills: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onEditBill(Bill bill) {
        // Create an AlertDialog to edit the bill
    }

}
