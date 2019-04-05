package com.nutritech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nutritech.models.Goal;
import com.nutritech.models.UserSingleton;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] sexes = {"Homme", "Femme", "Autre"};
    private Button register;
    private EditText age;
    private EditText taille;
    private EditText mail;
    private EditText passwd;
    private EditText passwdRepeated;
    private Spinner objectives;
    private Spinner sex;
    private TextView goLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        register = findViewById(R.id.btn_signup);
        age = findViewById(R.id.input_age);
        goLogin = findViewById(R.id.link_login);
        taille = findViewById(R.id.input_taille);
        mail = findViewById(R.id.input_email);
        passwd = findViewById(R.id.input_password);
        passwdRepeated = findViewById(R.id.input_password_repeat);
        objectives = findViewById(R.id.objectiveSpinner);
        sex = findViewById(R.id.sexSpinner);
        objectives.setPrompt("Choisissez un objectif");
        sex.setPrompt("Selectionnez votre sexe");

        register.setOnClickListener(click -> register());
        goLogin.setOnClickListener(click -> startActivity(new Intent(this, LoginActivity.class)));


        prepareDropbox(R.id.objectiveSpinner, getGoodNames());
        prepareDropbox(R.id.sexSpinner, sexes);
    }


    private void register() {

        if (isSignupOk()) {
            UserSingleton.getUser().setAge(Integer.parseInt(age.getText().toString()));
            UserSingleton.getUser().setHeight(Integer.parseInt(taille.getText().toString()));
            UserSingleton.getUser().setMail(mail.getText().toString());
            UserSingleton.getUser().setPassword(passwd.getText().toString());

            UserSingleton.getUser().setGender(sex.getSelectedItem().toString());
            UserSingleton.getUser().setGoal(Goal.valueOf(sex.getSelectedItem().toString()));

            startActivity(new Intent(this, DashboardActivity.class));

        }
    }

    private boolean isSignupOk() {
        if (mail.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
            mail.setError("Veuillez entrer une adresse mail valide");
            return false;
        }
        if (!passwd.getText().toString().equals(passwdRepeated.getText().toString())) {
            passwd.setError("Vos mots de passe ne correspondent pas !");
            return false;
        }
        return true;
    }

    private String[] getGoodNames() {
        String[] goals = Goal.getNames(Goal.class);
        for (int i = 0; i < goals.length; i++) {
            String finalWord = goals[i].replace("_", " ").toLowerCase();
            finalWord = finalWord.substring(0, 1).toUpperCase() + finalWord.substring(1);
            goals[i] = finalWord;
        }
        return goals;
    }

    private void prepareDropbox(int spinnerId, String[] array) {
        Spinner spinner = findViewById(spinnerId);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(), sexes[position], Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(getApplicationContext(), "Veuillez selectionner votre sexe", Toast.LENGTH_LONG).show();
        passwd.setError("Vos mots de passe ne correspondent pas !");
    }
}