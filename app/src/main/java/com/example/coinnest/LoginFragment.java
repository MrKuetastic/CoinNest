package com.example.coinnest;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    protected EditText usernameInput;
    protected EditText passwordInput;
    protected Button loginButton;
    protected Button signUpButton;

    private int userId = -1; // Initialize userId as an integer, -1 can indicate not set or error

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        signUpButton = view.findViewById(R.id.SignUpButton);

        loginButton.setOnClickListener(v -> {
            final String username = usernameInput.getText().toString().trim();
            final String password = passwordInput.getText().toString().trim();

            // Use the Database class to authorize login
            Database database = Database.getInstance(getActivity().getApplicationContext());
            database.logInAuthorization(username, password, new Database.MySQLAccessResponseListener() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("Login Failed")) {
                        userId = Integer.parseInt(response); // Parse the user ID to integer
                        ((MainActivity)getActivity()).onLoginSuccess();
                    } else {
                        Toast.makeText(getActivity(), "Login Failed. Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        signUpButton.setOnClickListener(v -> navigateToSignUpFragment());

        return view;
    }

    private void navigateToSignUpFragment() {
        Fragment signUpFragment = new SignUpFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, signUpFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public int getUserId() {
        return userId;
    }
}
