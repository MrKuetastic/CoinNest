

/*Jordan's Code*/

package com.example.coinnest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

//    Button btnDatePicker;
//    TextView txtDate;
//    private int sYear, sMonth, sDay;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidgets(view);
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets(View view)
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        Button previous = (Button) view.findViewById(R.id.month_navigation_previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthAction(v);
            }
        });

        Button forward = (Button) view.findViewById(R.id.month_navigation_forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthAction(v);
            }
        });


    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

//    private void setMonthView() { Calendar calendar = Calendar.getInstance(); selectedDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1); monthYearText.setText(monthYearFromDate(selectedDate)); ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
//
//        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this); RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7); calendarRecyclerView.setLayoutManager(layoutManager); calendarRecyclerView.setAdapter(calendarAdapter); }

//    private void setMonthView()
//    {
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
//        monthYearText.setText(monthYearFormat.format(calendar.getTime()));
//        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
//
//        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
//        calendarRecyclerView.setLayoutManager(layoutManager);
//        calendarRecyclerView.setAdapter(calendarAdapter);
//    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 43; i++)
        {
            if (i <= dayOfWeek || i> daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysInMonthArray;
    }


    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }


    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if (!dayText.equals(""))
        {
            int dayOfMonth = Integer.parseInt(dayText);
            selectedDate = selectedDate.withDayOfMonth(dayOfMonth);
//            Toast.makeText(getActivity(), Database.formatToLocalDate(selectedDate), Toast.LENGTH_LONG).show();
//            updateMyCalendar();

            openBillsPage(selectedDate);
        }
    }

    // Method to navigate to BillsPage Activity
    public void openBillsPage(LocalDate localDate) {
        if (getActivity() == null) {
            Toast.makeText(getContext(), "Activity is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert LocalDate to String

        Intent intent = new Intent(getActivity(), BillsPage.class);
        intent.putExtra("selectedDate", Database.formatToLocalDate(localDate));  // Use a key to retrieve this data in BillsPage
        startActivity(intent);
    }





    public void updateMyCalendar() {
        Database database = Database.getInstance(getActivity().getApplicationContext());
        database.fetchBillsByDate(Database.formatToLocalDate(selectedDate), true, new Database.MySQLAccessResponseListener() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onError(String message) {

            }
        });
    }


    private void updateCalendarWithBills(HashMap<String, Double> dailyTotals) {
        getActivity().runOnUiThread(() -> {
            // Assuming you have a way to reference days in your calendar view:
            for (Map.Entry<String, Double> entry : dailyTotals.entrySet()) {
                String date = entry.getKey();
                Double totalAmount = entry.getValue();

                // Update the specific day in the calendar with the total amount
                // This depends on how your calendar cells are defined; you might need a method to find the cell by date
                updateDayCellWithTotal(date, totalAmount);
            }
        });
    }



    private void updateDayCellWithTotal(String date, Double totalAmount) {

    }

}

