package com.nutritech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nutritech.models.Food;
import com.nutritech.models.FoodList;
import com.nutritech.models.FoodSuggestor;
import com.nutritech.models.LocationService;
import com.nutritech.models.UserSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SearchFoodActivitty extends AppCompatActivity {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    final static private String APP_METHOD = "GET";
    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private static String APP_SECRET = "6e038fe07ff848e4b23832ee1cf584cf";
    private static final String APP_KEY = "37f9d803908f49e9baca11695c05d287";
    ArrayList<Food> foodList = new ArrayList<>();
    ArrayList<String> foodListString = new ArrayList<>();

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
    private String reseachedString;

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
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.container), "Voici les informations disponibles pour : " + query, Snackbar.LENGTH_SHORT).show();
                installFood(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                reseachedString = newText;
                if (!newText.equals("")) {
                    refreshFoodList(newText);
                    searchView.setSuggestions(foodListString.toArray(new String[0]));
                }

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

    public String buildUrl(String searchFood, int page) {
        List<String> params = new ArrayList<>(Arrays.asList(generateOauthParams(page)));
        String[] template = new String[1];
        params.add("method=foods.search");
        params.add("search_expression=" + Uri.encode(searchFood));
        params.add("oauth_signature=" + sign(APP_METHOD, APP_URL, params.toArray(template)));
        String url = APP_URL + "?" + paramify(params.toArray(template));
        return url;
    }

    private static String[] generateOauthParams(int i) {
        return new String[]{
                "oauth_consumer_key=" + APP_KEY,
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" + Long.valueOf(System.currentTimeMillis() * 2).toString(),
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json",
                "page_number=" + i,
                "max_results=" + 20};
    }

    private static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        APP_SECRET += "&";
        SecretKey sk = new SecretKeySpec(APP_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
        APP_SECRET = APP_SECRET.substring(0, APP_SECRET.length() - 1);
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            m.init(sk);
            return Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        }
    }

    private static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    private static String join(String[] array, String separator) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    private static String nonce() {
        Random r = new Random();
        StringBuilder n = new StringBuilder();
        for (int i = 0; i < r.nextInt(8) + 2; i++)
            n.append(r.nextInt(26) + 'a');
        return n.toString();
    }

    Food buildFood(String name, String raw) {
        String[] a = raw.split("-");

        a[1] = a[1].replaceAll("Calories:", "");
        a[1] = a[1].replaceAll("Fat:", "");
        a[1] = a[1].replaceAll("Carbs:", "");
        a[1] = a[1].replaceAll("Protein:", "");
        a[1] = a[1].replaceAll("\\s+", "");
        a[1] = a[1].replaceAll("kcal", "");
        a[1] = a[1].replaceAll("g", "");
        String[] b = a[1].split("\\|");
        return new Food(name, Float.valueOf(b[3].trim()).longValue(), Float.valueOf(b[2].trim()).longValue(), Float.valueOf(b[1].trim()).longValue());


    }

    void refreshFoodList(String research) {

        String url = buildUrl(research, 2);

        RequestQueue exampleRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, this::queryAPI, error -> Log.e("error", error.toString())
        );

        exampleRequestQueue.add(jsonObjectRequest);

    }

    private void queryAPI(JSONObject response) {
         JSONArray arr;
        try {
            JSONObject foods = response.getJSONObject("foods");
            arr = foods.getJSONArray("food");
            if(arr.length() > 0){
                for (int i = 0; i < arr.length(); i++) {
                    Food currentFood = buildFood(arr.getJSONObject(i).getString("food_name"), arr.getJSONObject(i).getString("food_description"));
                    this.foodList.add(currentFood);
                    this.foodListString.add(currentFood.getName());
                }
            } else {
                this.foodList.clear();
                this.foodListString.clear();
            }

         } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

