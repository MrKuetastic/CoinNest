package com.example.coinnest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BillPageFragment extends Fragment implements BillsAdapter.OnBillListener {
    private RecyclerView upcomingBillsRecyclerView;
    private BillsAdapter adapter;
    private List<Bill> billsList = new ArrayList<>();
    private Button buttonAddBill;
    private LoginFragment loginfragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        buttonAddBill = view.findViewById(R.id.buttonAddBill);
        buttonAddBill.setOnClickListener(v -> showAddOrEditBillDialog(null));

        setUpRecyclerView(view);
        fetchAndDisplayBills();
        return view;
    }

    private void setUpRecyclerView(View view) {
        upcomingBillsRecyclerView = view.findViewById(R.id.upcomingBillsRecyclerView);
        adapter = new BillsAdapter(billsList, getContext(), this);
        upcomingBillsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingBillsRecyclerView.setAdapter(adapter);
    }
    private void updateRecyclerViewData() {
        sortBillsByDate(); // Sort the list first
        adapter.notifyDataSetChanged(); // Notify the adapter about the data change
    }

    @Override
    public void onEditBill(Bill bill) {
        showAddOrEditBillDialog(bill);  // This will open the dialog with the existing bill's details
    }

    private void showAddOrEditBillDialog(@Nullable Bill existingBill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(existingBill == null ? "Add Bill" : "Edit Bill");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText billNameInput = createEditText("Bill Name", InputType.TYPE_CLASS_TEXT, existingBill);
        final EditText billAmountInput = createEditText("Bill Amount", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, existingBill);
        final EditText billDueDateInput = createDatePicker("Select a Date", existingBill);
        final EditText billCategoryInput = createEditText("Bill Category", InputType.TYPE_CLASS_TEXT, existingBill);

        layout.addView(billNameInput);
        layout.addView(billAmountInput);
        layout.addView(billDueDateInput);
        layout.addView(billCategoryInput);

        builder.setView(layout);
        builder.setPositiveButton("Save", (dialog, which) -> saveBill(billNameInput, billAmountInput, billDueDateInput, billCategoryInput, existingBill));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        if (existingBill != null) {
            builder.setNeutralButton("Delete", (dialog, which) -> deleteBill(existingBill));
        }
        sortBillsByDate();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private EditText createEditText(String hint, int inputType, Bill existingBill) {
        EditText editText = new EditText(getContext());
        editText.setInputType(inputType);
        editText.setHint(hint);
        if (existingBill != null) {
            String value = "";
            switch (hint) {
                case "Bill Name":
                    value = existingBill.getBillName();
                    break;
                case "Bill Amount":
                    value = String.valueOf(existingBill.getAmount());
                    break;
                case "Bill Category":
                    value = existingBill.getCategory();
                    break;
            }
            editText.setText(value);
        }
        return editText;
    }

    private EditText createDatePicker(String hint, Bill existingBill) {
        EditText editText = new EditText(getContext());
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setHint(hint);
        editText.setOnClickListener(v -> showDatePicker(editText));
        if (existingBill != null && existingBill.getDueDate() != null) {
            editText.setText(existingBill.getDueDate());
        }
        return editText;
    }

    private void saveBill(EditText nameInput, EditText amountInput, EditText dateInput, EditText categoryInput, @Nullable Bill existingBill) {
        String name = nameInput.getText().toString();
        double amount = Double.parseDouble(amountInput.getText().toString());
        String date = dateInput.getText().toString();
        String category = categoryInput.getText().toString();

        if (existingBill == null) {
            Bill newBill = new Bill(name, amount, date, category);
            billsList.add(newBill);
        } else {
            existingBill.setBillName(name);
            existingBill.setAmount(amount);
            existingBill.setDueDate(date);
            existingBill.setCategory(category);
        }
        //saveBillToDatabase(loginfragment.getUserId(), name, amount, date, category, existingBill);
        sortBillsByDate();
        adapter.notifyDataSetChanged();
    }

    private void deleteBill(Bill bill) {
        billsList.remove(bill);
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Bill deleted", Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.US, "%d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void sortBillsByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Collections.sort(billsList, (bill1, bill2) -> {
            try {
                Date date1 = sdf.parse(bill1.getDueDate());
                Date date2 = sdf.parse(bill2.getDueDate());
                return date1.compareTo(date2);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    private void fetchAndDisplayBills() {
        if (getContext() == null) return;

        String url = "http://18.189.7.172/pullBill.php";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray billsArray = new JSONArray(response);
                        for (int i = 0; i < billsArray.length(); i++) {
                            JSONObject bill = billsArray.getJSONObject(i);
                            billsList.add(new Bill(
                                    bill.getString("bill_name"),
                                    bill.getDouble("amount"),
                                    bill.getString("due_date"),
                                    bill.getString("category")
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error parsing bills", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error fetching bills: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
        ) {

        };
        queue.add(stringRequest);
    }
    private void saveBillToDatabase(int id, String billName, double amount, String dueDate, String category, @Nullable Bill existingBill) {
        // First, update the RecyclerView with the new data
        if (existingBill == null) {
            // Adding a new bill
            Bill newBill = new Bill(billName, amount, dueDate, category);
            billsList.add(newBill);
            updateRecyclerViewData();
        } else {
            // Editing an existing bill
            existingBill.setBillName(billName);
            existingBill.setAmount(amount);
            existingBill.setDueDate(dueDate);
            existingBill.setCategory(category);
            updateRecyclerViewData();
        }

        // Define the URL for adding or updating the bill
        String url = existingBill == null ? "http://18.189.7.172/addBill.php" :
                existingBill.isMarkedForDeletion() ? "http://18.189.7.172/deleteBill.php" :
                        "http://18.189.7.172/updateBill.php";

        // Create the request to add the bill to the database
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(getContext(), "Bill operation successful!", Toast.LENGTH_SHORT).show();
                    // Optionally, fetch all bills again to sync with the database
                    updateRecyclerViewData();
                    fetchAndDisplayBills();
                },
                error -> {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(id)); // Using the retrieved ID
                params.put("billName", billName);
                params.put("amount", String.format(Locale.getDefault(), "%.2f", amount));
                params.put("dueDate", dueDate);
                params.put("category", category);
                // Add other bill details if necessary
                return params;
            }
        };

        // Add the request to the queue
        queue.add(request);
    }

}
