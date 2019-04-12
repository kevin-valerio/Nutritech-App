package com.nutritech;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nutritech.models.Food;
import com.nutritech.models.FoodList;
import com.nutritech.models.FoodSuggestor;
import com.nutritech.models.LocationService;
import com.nutritech.models.UserSingleton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class SearchFoodActivitty extends AppCompatActivity {

    private MaterialSearchView searchView;
    private TextView mainLabel;
    private TextView protLbl;
    private TextView gluLbl;
    private TextView lipLbl;
    private TextView kcalLabel;
    private EditText qtTxtView;
    private Food food;
    private Button addBtn;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_activity);

        initToolbar();
        initSearchView();
        initComponents();

        refreshStats();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_add_alert_black_24dp);
        toolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(this, DashboardActivity.class));
            overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
        });
    }

    private void initComponents() {
        mainLabel = findViewById(R.id.foodNameTxt);
        protLbl = findViewById(R.id.PROT_TXTVIEW);
        gluLbl = findViewById(R.id.GLUCIDES_TXTVIEW);
        lipLbl = findViewById(R.id.LIPIDES_TXTVIEW);
        kcalLabel = findViewById(R.id.KCAL_TXTVIEW);
        qtTxtView = findViewById(R.id.qtTextview);
        addBtn = findViewById(R.id.addFood);
        addBtn.setOnClickListener(view -> addFood());

    }

    private void refreshStats() {
        editText = findViewById(R.id.qtTextview);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (food == null) {
                    editText.setError("Veuillez chercher un aliment");
                    return;
                }

                if (Integer.valueOf(editable.toString()) > 2000) {
                    editText.setError("Avez-vous vraiment mangé tout cela ?");
                    return;
                }

                if (!editable.toString().equals("")) {
                    Integer newGrammes = Integer.valueOf(editable.toString());
                    String text = String.valueOf(newGrammes * food.getProtein()) + "";
                    protLbl.setText(text);
                    String text1 = String.valueOf(newGrammes * food.getCarb()) + " ";
                    gluLbl.setText(text1);
                    String text2 = String.valueOf(newGrammes * food.getLipid()) + " ";
                    lipLbl.setText(text2);
                    String text3 = String.valueOf(newGrammes * food.getCalorie()) + " ";
                    kcalLabel.setText(text3);
                } else {
                    String text = "0";
                    protLbl.setText(text);
                    gluLbl.setText(text);
                    lipLbl.setText(text);
                    String text1 = "0";
                    kcalLabel.setText(text1);
                }
            }
        });
    }

    private void initSearchView() {
        searchView = findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
        searchView.setSuggestions(FoodSuggestor.getFoodStringList().toArray(new String[0]));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.container), "Voici les informations disponibles pour : " + query, Snackbar.LENGTH_SHORT).show();
                installFood(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setSubmitOnClick(true);
    }


    //Initialise les textviews en fonction du food
    private void installFood(String food) {

        this.food = FoodSuggestor.getFoodByName(food);

        if (this.food != null) {
            mainLabel.setText(this.food.getName());
            protLbl.setText(String.valueOf(Math.toIntExact(this.food.getProtein())));
            gluLbl.setText(String.valueOf(Math.toIntExact(this.food.getCarb())));
            lipLbl.setText(String.valueOf(Math.toIntExact(this.food.getLipid())));
            kcalLabel.setText(String.valueOf(Math.toIntExact(this.food.getCalorie())));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void addFood() {

        if (canAddFood()) {
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            FoodList f;
            if (UserSingleton.getUser().getCalendarFoodList().containsKey(currentDay)) {
                f = UserSingleton.getUser().getCalendarFoodList().get(currentDay);
            } else {
                f = new FoodList();
                UserSingleton.getUser().getCalendarFoodList().put(currentDay, f);
            }
            try {
                this.food.setLatitude(getLatitude());
                this.food.setLongitude(getLongitude());

                f.addFood(this.food, Long.parseLong(qtTxtView.getText().toString()));
            } catch (NumberFormatException e) {
                return;
            }
            startActivity(new Intent(this, DashboardActivity.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } else {
            editText.setError("Vous ne pouvez rien ajouté sans avoir cherché un aliment");
        }
    }

    private double getLatitude() {

        LocationService locationService = new LocationService(this);
        return locationService.getLatitude();

    }

    private double getLongitude() {
        LocationService locationService = new LocationService(this);
        return locationService.getLongitude();
    }

    private boolean canAddFood() {
        return food != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

