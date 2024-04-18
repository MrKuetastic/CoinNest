package com.example.coinnest;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    protected EditText emailInput;
    protected EditText usernameInput;
    protected EditText passwordInput;
    protected Button signUpButton;
    protected EditText dobInput;
    protected EditText addressInput;
    protected EditText phoneInput;


    private String email = "";
    private String DOB = "";
    private String Phone_Number = "";

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        emailInput = view.findViewById(R.id.emailInput);
        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        signUpButton = view.findViewById(R.id.SignUpButton);
        dobInput = view.findViewById(R.id.dobInput);
        addressInput = view.findViewById(R.id.addressInput);
        phoneInput = view.findViewById(R.id.phoneInput);

        email = emailInput.toString();
        DOB = dobInput.toString();
        Phone_Number = phoneInput.toString();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailInput.getText().toString().trim();
                final String username = usernameInput.getText().toString().trim();
                final String password = passwordInput.getText().toString().trim();
                final String dob = dobInput.getText().toString().trim();
                final String address = addressInput.getText().toString().trim();
                final String phone = phoneInput.getText().toString().trim();

                // Flag to indicate if all validations pass
                boolean isValid = true;

                if (!isValidEmail(email)) {
                    Toast.makeText(getActivity(), "Invalid email format.", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if (username.length() > 12) {
                    Toast.makeText(getActivity(), "Username cannot be more than 12 characters.", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(getActivity(), "Password must follow the requirement.", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if (!isValidDOB(dob)) {
                    Toast.makeText(getActivity(), "Invalid DOB format.", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if (!isValidPhoneNumber(phone)) {
                    Toast.makeText(getActivity(), "Invalid phone number format.", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if (isValid) {
                    // All validations passed, send data to the database
                    Database database = Database.getInstance(getActivity().getApplicationContext());
                    database.signUp(email, username, password, dob, address, phone, new Database.MySQLAccessResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            if ("1".equals(response.trim())) {
                                Toast.makeText(getActivity(), "Signup Successful.", Toast.LENGTH_SHORT).show();
                                // Go back to the LoginFragment
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                Toast.makeText(getActivity(), "Signup Failed: " + response, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|org|net|edu|gov|mil|biz|info|mobi|name|aero|asia|jobs|museum)$");
        return pattern.matcher(email).matches();
    }


    public boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false; // Checks for minimum length of 8 characters
        } else if (!password.matches(".*[a-z].*")) {
            return false; // Checks for at least one lowercase character
        } else if (!password.matches(".*[A-Z].*")) {
            return false; // Checks for at least one uppercase character
        } else if (!password.matches(".*\\d.*")) {
            return false; // Checks for at least one digit
        } else if (!password.matches(".*[!@#$%^&*()_+=-].*")) {
            return false; // Checks for at least one special character from the set provided
        }
        return true; // Passes all checks
    }

    public boolean isValidDOB(String dob) {
        return dob.matches("^(19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
    }

    public boolean isValidPhoneNumber(String phone) {
        return phone.matches("^[+]?[0-9]{10,13}$"); // Adjust regex as needed
    }

    public String getEmail() {
        return email;
    }

    public String DOB() {
        return DOB;
    }

    public String Phone_Number() {return Phone_Number;
    }



}
