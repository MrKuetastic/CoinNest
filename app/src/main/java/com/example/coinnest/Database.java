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
        DateTimeFormatter formatter = null;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return date.format(formatter);
    }

    //fenzhang: tool api for formatting the date to pull bills
    public static String formatToLocalDate(LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException("The date to format cannot be null.");
        }
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

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


    private void cacheBills(List<Bill> bills) {
        Gson gson = new Gson();
        String jsonData = gson.toJson(bills);
        try (FileOutputStream fos = context.openFileOutput(CACHE_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(jsonData.getBytes());
            Log.d("cacheBills", "Data successfully cached.");
        } catch (IOException e) {
            Log.e("cacheBills", "Error writing to cache", e);
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

    // Placeholder for the method to fetch summary
    public void fetchSummary(final String someParameter, final MySQLAccessResponseListener listener) {
        // Implementation goes here
    }

    // Placeholder for the method to get user information
    public void getUserInfo(final String username, final MySQLAccessResponseListener listener) {
        // Implementation goes here
    }
}
