package com.nutritech;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nutritech.models.UserSingleton;
import com.nutritech.models.WeightAlertBuilder;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    SearchBarFragment searchBarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initToolBar();
        initNavigationView();
        initFloatingButton();

        searchBarFragment = (SearchBarFragment) getFragmentManager().findFragmentById(R.id.search_bar);
    }


    /* Quand on revient sur le Dashboard, on active onActivityResult
       Aussi utilisé pour récuperer le texte de la voix
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBarFragment.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchBarFragment.getmSearchView().populateSearchText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Initialise le FloatinButton
    private void initFloatingButton() {
        FloatingActionButton addWeight = findViewById(R.id.addWeight);
        addWeight.setOnClickListener(click -> {
            askForWeight();
        });
    }

    //Demande à l'utilisateur son nouveau poids
    public void askForWeight() {


/*        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
      builder.setTitle("Title");

      LinearLayout layout = new LinearLayout(getApplicationContext());
      layout.setOrientation(LinearLayout.VERTICAL);

      TextView title = new TextView(this);
      title.setText(R.string.new_poids);
      title.setPadding(10, 10, 10, 10);
      title.setGravity(Gravity.CENTER);
      title.setTextColor(Color.BLACK);
      title.setTextSize(20);
      builder.setCustomTitle(title);

      TextView msg = new TextView(this);
      msg.setText("\nVeuillez entrer votre nouveau poids. \n Cela nous permettra de suivre l'évolution de votre poids ! \n\n");
      msg.setGravity(Gravity.CENTER_HORIZONTAL);
      msg.setTextColor(Color.BLACK);
      layout.addView(msg);

      EditText poids = new EditText(getApplicationContext());
      poids.setInputType(InputType.TYPE_CLASS_NUMBER);
      poids.setHint("Poids (en kg)");
      layout.addView(poids);

      builder.setPositiveButton("Ajouter", (dialog, which) -> {
          UserSingleton.getUser().addWeight(Integer.valueOf(poids.getText().toString()));
                  Toast.makeText(this, "Votre nouveau poids a été ajouté (" + poids.getText().toString() + " kg)", Toast.LENGTH_LONG).show();
              }
      );

      builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
      builder.setView(layout);
      builder.show();
*/
        WeightAlertBuilder weightAlertBuilder = new WeightAlertBuilder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        weightAlertBuilder.setMainTextView();
        weightAlertBuilder.setSecondaryTextView();
        weightAlertBuilder.setWeightEditBox();
        weightAlertBuilder.setButtons();

        weightAlertBuilder.build();
    }

    //Initialise la toolbar
    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Initialise les composants de la NavigationView
    private void initNavigationView() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        TextView nomPrenomTxtView = header.findViewById(R.id.nomPrenomTxtView);
        TextView objectifTxtView = header.findViewById(R.id.objectifTxtView);
        TextView feedback = header.findViewById(R.id.feedbackTxtView);

        String connectedWith = "Connecté avec " + UserSingleton.getUser().getMail();
        String mainGoal = "Votre objectif : " + UserSingleton.getUser().getGoal().toString().toLowerCase().replace("_", " ");

        nomPrenomTxtView.setText(connectedWith);
        objectifTxtView.setText(mainGoal);
        feedback.setOnClickListener(click -> new AlertDialog.Builder(getApplicationContext())
                .setTitle("Envoyez-nous un feedback !")
                .setMessage("Envoyez un mail à l'adresse feedback@nutritech.com, et dites nous ce que vous pensez de l'application ! \nEt n'oubliez pas de nous noter sur le Play Store")
                .setPositiveButton("D'accord", null)
                .setIcon(android.R.drawable.ic_dialog_email)
                .show()
        );

    }

    //Définit l'action lorsque l'onclique sur " < "
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Définit les actions lorsqu'on clique sur les bouttons du menu de gauche
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bilan_mensuel) {

        } else if (id == R.id.dashboard) {

        } else if (id == R.id.bilance_hebdo) {

        } else if (id == R.id.deconnection) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
