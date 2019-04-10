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
import android.widget.EditText;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nutritech.models.Food;
import com.nutritech.models.FoodSuggestor;

import java.util.ArrayList;

public class SearchFoodActivitty extends AppCompatActivity {

    private MaterialSearchView searchView;
    TextView mainLabel;
    TextView protLbl;
    TextView gluLbl;
    TextView lipLbl;
    TextView kcalLabel;
    Food foo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSearchView();
        initComponents();

        refreshStats();

    }

    private void initComponents() {
        mainLabel = findViewById(R.id.foodNameTxt);
        protLbl = findViewById(R.id.PROT_TXTVIEW);
        gluLbl = findViewById(R.id.GLUCIDES_TXTVIEW);
        lipLbl = findViewById(R.id.LIPIDES_TXTVIEW);
        kcalLabel = findViewById(R.id.KCAL_TXTVIEW);
    }

    private void refreshStats() {
        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (foo == null) {
                    editText.setError("Veuillez chercher un aliment");
                    return;
                }

                if (Integer.valueOf(editable.toString()) > 2000) {
                    editText.setError("Avez-vous vraiment mangé tout cela ?");
                    return;
                }

                if (editable != null) {
                    Integer newGrammes = Integer.valueOf(editable.toString());
                    String text = String.valueOf(newGrammes * foo.getProtein()) + "";
                    protLbl.setText(text);
                    String text1 = String.valueOf(newGrammes * foo.getCarb()) + " ";
                    gluLbl.setText(text1);
                    String text2 = String.valueOf(newGrammes * foo.getLipid()) + " ";
                    lipLbl.setText(text2);
                    String text3 = String.valueOf(newGrammes * foo.getCalorie()) + " ";
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

        foo = FoodSuggestor.getFoodByName(food);

        if (foo != null) {
            mainLabel.setText(foo.getName());
            protLbl.setText(String.valueOf(Math.toIntExact(foo.getProtein())));
            gluLbl.setText(String.valueOf(Math.toIntExact(foo.getCarb())));
            lipLbl.setText(String.valueOf(Math.toIntExact(foo.getLipid())));
            kcalLabel.setText(String.valueOf(Math.toIntExact(foo.getCalorie())));
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

