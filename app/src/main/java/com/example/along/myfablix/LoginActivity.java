package com.example.along.myfablix;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    // UI references
    private EditText emailEditText;
    private EditText passwordEditText;
    //private Button loginButton;

    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void login(View view) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.email_empty_error));
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEditText.setError(getString(R.string.email_error));
            focusView = emailEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            StringBuilder sb = new StringBuilder();
            JSONObject json = null;

            try {
                URL url = new URL("http://ec2-54-215-239-206.us-west-1.compute.amazonaws.com:8080/Fabflix/androidLogin?email="
                        +mEmail+"&pass="+mPassword);
                // Simulate network access.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                int status = urlConnection.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        json = new JSONObject(sb.toString());
                        if(json.getBoolean("auth"))
                            return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                Intent intent = new Intent(getApplicationContext(), MovieSearchActivity.class);
                startActivity(intent);
            } else {
                passwordEditText.setError(getString(R.string.password_error));
                passwordEditText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private boolean isEmailValid(String email)
    {
        return email.contains("@");
    }

}
