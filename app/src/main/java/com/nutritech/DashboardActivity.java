package com.nutritech;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nutritech.models.UserSingleton;
import com.nutritech.models.WeightAlertBuilder;
import com.wanderingcan.persistentsearch.PersistentSearchView;
import com.wanderingcan.persistentsearch.SearchMenuItem;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int VOICE_RECOGNITION_CODE = 9999;
    PersistentSearchView searchView;
    private boolean mMicEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        searchView = findViewById(R.id.search_bar);
        mMicEnabled = isIntentAvailable(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));

        initToolBar();
        initNavigationView();
        initFloatingButton();
        initSearchBar();

    }

    private boolean isIntentAvailable(Intent intent) {
        PackageManager mgr = getPackageManager();
        if (mgr != null) {
            List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
        return false;
    }

    private void initSearchBar() {
        searchView.setOnSearchListener(new PersistentSearchView.OnSearchListener() {
            @Override
            public void onSearchOpened() {
//                Called when the Searchbar is opened by the user or by something calling
                searchView.openSearch();
            }

            @Override
            public void onSearchClosed() {
                //Called when the searchbar is closed by the user or by something calling
                searchView.closeSearch();
            }

            @Override
            public void onSearchCleared() {
                //Called when the searchbar has been cleared by the user by removing all
                //the text or hitting the clear button. This also will be called if
                // is set with a null string or
                //an empty string

            }

            @Override
            public void onSearchTermChanged(CharSequence term) {
                //Called when the text in the searchbar has been changed by the user or
                //by searchView.populateSearchText() with text passed in.
                //Best spot to handle giving suggestions to the user in the menu
            }

            @Override
            public void onSearch(CharSequence text) {
                //Called when the user hits the IME Action Search on the keyboard to search
                //Here is the best spot to handle searches
            }

        });

        searchView.setOnIconClickListener(new PersistentSearchView.OnIconClickListener() {
            @Override
            public void OnNavigationIconClick() {
            }

            @Override
            public void OnEndIconClick() {
                startVoiceRecognition();
            }
        });

        searchView.setOnMenuItemClickListener(new PersistentSearchView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(SearchMenuItem item) {
                //Called when an Item in the SearchMenu is clicked, it passes in the
                //SearchMenuItem that was clicked
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchView.populateSearchText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startVoiceRecognition() {

        if (mMicEnabled) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Prononcez le nom de votre aliment");
            startActivityForResult(intent, VOICE_RECOGNITION_CODE);
        }
    }


    //Initialise le FloatinButton
    private void initFloatingButton() {
        FloatingActionButton addWeight = findViewById(R.id.addWeight);
        addWeight.setOnClickListener(click -> {
            askForWeight();
        });
    }

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

        String connectedWith = "Connecté avec " + UserSingleton.getUser().getMail();
        nomPrenomTxtView.setText(connectedWith);
        String mainGoal = "Votre objectif : " + UserSingleton.getUser().getGoal().toString().toLowerCase().replace("_", " ");
        objectifTxtView.setText(mainGoal);

    }

    //Définit les actions lorsqu'on clique sur les bouttons du menu de gauche

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
