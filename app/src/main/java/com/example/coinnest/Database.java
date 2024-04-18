package com.example.coinnest;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.IOException;

public class Database {

    private static final String CACHE_FILE_NAME = "bills_cache.json";

    private Context context;
    private static Database instance;
    protected RequestQueue queue;

    private Map<String, List<Bill>> dailyBillsCache = new HashMap<>();
    private Map<String, List<Bill>> monthlyBillsCache = new HashMap<>();

    private static final String LOGIN_URL = "http://18.189.7.172/login.php";
    private static final String SignUp_URL = "http://18.189.7.172/signup.php";

    //fenzhang: fetch bill by month
    private static final String bills_url = "http://18.189.7.172/fetchBill.php";

    //fenzhang:fetch bill by date
    private static final String bills_url_by_date = "http://18.189.7.172/fetchBillV2.php";
    // Define URLs for other PHP scripts as needed

    // Maarij's Code
    private static final String LOGOUT_URL = "http://18.189.7.172/logout.php";
    private static final String CHANGE_EMAIL_URL = "http://18.189.7.172/change_email.php";
    private static final String CHANGE_PASSWORD_URL = "http://18.189.7.172/change_password";

    // Private constructor to prevent instantiation from outside the class
    private Database(Context context) {
        this.queue = Volley.newRequestQueue(context.getApplicationContext()); // Use application context to prevent leaks
        this.context = context.getApplicationContext(); // Use getApplicationContext to avoid memory leaks

    }

    // Public method to get the singleton instance
    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
        return instance;
    }


    public void changeEmail(String oldEmail, String newEmail, MySQLAccessResponseListener mySQLAccessResponseListener) {
    }

    public interface MySQLAccessResponseListener {
        void onResponse(String response);
        void onError(String message);
    }

        //fenzhang: tool api for formatting the month to pull bills
    public static String formatToLocalDateYearMonth(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("The date to format cannot be null.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return date.format(formatter);
    }

    //fenzhang: tool api for formatting the date to pull bills
    public static String formatToLocalDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("The date to format cannot be null.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public static String formatToLocalDatebyYearMonth(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("The date to format cannot be null.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return date.format(formatter);
    }

    // Method for login authorization
    public void logInAuthorization(final String username, final String password, final MySQLAccessResponseListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                response -> {
                    if (!response.trim().equals("0")) {
                        // Login successful, response contains the user ID
                        listener.onResponse(response.trim());
                    } else {
                        // Login failed
                        listener.onResponse("Login Failed");
                    }
                },
                error -> listener.onError(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void signUp(final String email, final String username, final String password, final String dob, final String address, final String phoneNumber, final MySQLAccessResponseListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("username", username);
        params.put("password", password);
        params.put("dob", dob);
        params.put("address", address);
        params.put("phone_number", phoneNumber);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SignUp_URL,
                response -> listener.onResponse(response),
                error -> listener.onError(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // Method for logout
    public void logOut(final MySQLAccessResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGOUT_URL,
                response -> listener.onResponse(response),
                error -> listener.onError(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return null; // No parameters needed for logout
            }
        };

        queue.add(stringRequest);
    }



    // fenzhang: fetch bills from cache or server
    public void fetchBillsByDate(final String date, final boolean forceUpdate, final MySQLAccessResponseListener listener) {
        if (forceUpdate) {
            fetchFromServer(date, listener);
        } else {
            fetchFromCache(date, listener);
        }
    }

    public void fetchBillsByMonth(final String month, final MySQLAccessResponseListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("month", month);  // Expecting month in format "YYYY-MM"

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bills_url,
                response -> listener.onResponse(response),
                error -> listener.onError(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void fetchFromServer(final String date, final MySQLAccessResponseListener listener) {
        // Append the month parameter to the URL
        String urlWithParams = bills_url_by_date + "?date=" + date;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlWithParams,
                response -> {
                    try {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Bill>>(){}.getType();
                        List<Bill> bills = gson.fromJson(response, listType);
                        // Serialize and cache the bills
                        cacheBills(bills);
                        // Convert list of bills to JSON and pass to the onResponse callback
                        String billsJson = gson.toJson(bills);
                        listener.onResponse(billsJson);
                    } catch (Exception e) {
                        listener.onError("Parsing error: " + e.getMessage());
                    }
                },
                error -> listener.onError("Request error: " + error.toString()));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    private void fetchFromCache(final String date, final MySQLAccessResponseListener listener) {
        List<Bill> cachedBills = readBillsFromCache();
        if (cachedBills == null || cachedBills.isEmpty()) {
            listener.onError("No cached data available.");
        } else {
            Gson gson = new Gson();
            String billsJson = gson.toJson(cachedBills);
            listener.onResponse(billsJson);
        }
    }


    // Cache bills
    public void cacheBills(List<Bill> bills) {
        for (Bill bill : bills) {
            cacheBillByDate(bill);
            cacheBillByMonth(bill);
        }
    }

    private void cacheBillByDate(Bill bill) {
        String dateKey = bill.getDueDate();  // "YYYY-MM-DD"
        dailyBillsCache.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(bill);
    }

    private void cacheBillByMonth(Bill bill) {
        String monthKey = bill.getDueDate().substring(0, 7);  // "YYYY-MM"
        monthlyBillsCache.computeIfAbsent(monthKey, k -> new ArrayList<>()).add(bill);
    }

    // Read bills by date
    public List<Bill> readBillsFromCacheByDate(String date) {
        return dailyBillsCache.getOrDefault(date, new ArrayList<>());
    }

    // Read bills by month
    public List<Bill> readBillsFromCacheByMonth(String month) {
        return monthlyBillsCache.getOrDefault(month, new ArrayList<>());
    }

    // Fetch bills by date from the cache
    private void fetchFromCacheByDate(final String date, final MySQLAccessResponseListener listener) {
        List<Bill> cachedBills = readBillsFromCacheByDate(date);
        if (cachedBills == null || cachedBills.isEmpty()) {
            listener.onError("No cached data available for date: " + date);
        } else {
            Gson gson = new Gson();
            String billsJson = gson.toJson(cachedBills);
            listener.onResponse(billsJson);
        }
    }


    private List<Bill> readBillsFromCache() {
        // Assume the cached bills are stored in a JSON file
        try (FileInputStream fis = context.openFileInput(CACHE_FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Bill>>(){}.getType();
            return gson.fromJson(sb.toString(), listType);
        } catch (FileNotFoundException e) {
            Log.e("Database", "Cache file not found", e);
            return null;
        } catch (IOException e) {
            Log.e("Database", "Error reading from cache", e);
            return null;
        }
    }


    //sum month bill by day into an array
    // Method to calculate totals per day and return them as strings in a list
    public static List<String> calculateTotalPerDayAsString(List<Bill> bills) {
        List<String> dayTotalsAsString = new ArrayList<>();

        if (bills == null || bills.isEmpty()) {
            return dayTotalsAsString;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust according to the actual format

        // Group bills by day
        Map<LocalDate, Double> dailyTotalMap = new HashMap<>();
        for (Bill bill : bills) {
            LocalDate dueDate;
            try {
                dueDate = LocalDate.parse(bill.getDueDate(), formatter);
            } catch (DateTimeParseException e) {
                // Log or handle the error appropriately
                continue; // Skip this bill if the date is not parseable
            }
            double amount = bill.getAmount();
            dailyTotalMap.merge(dueDate, amount, Double::sum);
        }

        if (dailyTotalMap.isEmpty()) {
            // If no bills could be parsed, fill the month with zeros
            LocalDate today = LocalDate.now();
            LocalDate firstDayOfMonth = today.withDayOfMonth(1);
            LocalDate lastDayOfMonth = today.withDayOfMonth(today.getMonth().length(today.isLeapYear()));
            for (LocalDate currentDate = firstDayOfMonth; !currentDate.isAfter(lastDayOfMonth); currentDate = currentDate.plusDays(1)) {
                dayTotalsAsString.add(String.format("%s: $0.00", currentDate));
            }
            return dayTotalsAsString;
        }

        // Find the first and last day of the month based on the earliest and latest bill dates
        LocalDate firstDayOfMonth = dailyTotalMap.keySet().stream().min(LocalDate::compareTo).get().withDayOfMonth(1);
        LocalDate lastDayOfMonth = dailyTotalMap.keySet().stream().max(LocalDate::compareTo).get().withDayOfMonth(firstDayOfMonth.getMonth().length(firstDayOfMonth.isLeapYear()));

        // Fill day totals for each day in the month, including days with no bills
        for (LocalDate currentDate = firstDayOfMonth; !currentDate.isAfter(lastDayOfMonth); currentDate = currentDate.plusDays(1)) {
            double total = dailyTotalMap.getOrDefault(currentDate, 0.0);
            dayTotalsAsString.add(String.format("%s: $%.2f", currentDate, total));
        }

        return dayTotalsAsString;
    }

    public static List<Bill> parseBillsFromResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type billListType = new TypeToken<List<Bill>>(){}.getType();
        return gson.fromJson(jsonResponse, billListType);
    }

    public void fetchBillsByMonth(final String month, final boolean forceUpdate, final MySQLAccessResponseListener listener) {
        if (forceUpdate) {
            fetchFromServerByMonth(month, listener);
        } else {
            fetchFromCacheByMonth(month, listener);
        }
    }

    private void fetchFromServerByMonth(final String month, final MySQLAccessResponseListener listener) {
        String urlWithParams = bills_url + "?month=" + month;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlWithParams,
                response -> {
                    try {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Bill>>(){}.getType();
                        List<Bill> bills = gson.fromJson(response, listType);
//                        cacheBills(bills);
                        String billsJson = gson.toJson(bills);
                        listener.onResponse(billsJson);
                    } catch (Exception e) {
                        listener.onError("Parsing error: " + e.getMessage());
                    }
                },
                error -> listener.onError("Request error: " + error.toString()));

        queue.add(stringRequest);
    }

    private void fetchFromCacheByMonth(final String month, final MySQLAccessResponseListener listener) {
        List<Bill> cachedBills = readBillsFromCacheByMonth(month);
        if (cachedBills == null || cachedBills.isEmpty()) {
            listener.onError("No cached data available for month: " + month);
        } else {
            Gson gson = new Gson();
            String billsJson = gson.toJson(cachedBills);
            listener.onResponse(billsJson);
        }
    }




    // Placeholder for the method to fetch summary
    public void fetchSummary(final String someParameter, final MySQLAccessResponseListener listener) {
        // Implementation goes here
    }

    // Placeholder for the method to get user information
    public void getUserInfo(final String username, final MySQLAccessResponseListener listener) {
        // Implementation goes here
    }
}
