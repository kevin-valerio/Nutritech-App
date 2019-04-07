package com.nutritech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nutritech.models.UserSingleton;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.input_email);

        String mail = UserSingleton.getUser().getMail();
        if (mail != null) {
            emailText.setText(mail);
        }

        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);


        loginButton.setOnClickListener(v -> login());

        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connection...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                () -> {
                    onLoginSuccess();
                    progressDialog.dismiss();
                }, 3000);


    }

    private boolean areCredentialsOk(String email, String password) {
        return email.equals(UserSingleton.getUser().getMail()) && password.equals(UserSingleton.getUser().getPassword());
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
        overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Votre adresse mail et votre mot de passe ne correspondent pas", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Veuillez entrer une adresse mail valide");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordText.setError("Le mot de passe doit avoir au moins 4 caratères");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (!areCredentialsOk(email, password)) {
            valid = false;
        }

        return valid;
    }
}