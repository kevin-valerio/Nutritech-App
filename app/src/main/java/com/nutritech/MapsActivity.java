package com.nutritech;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nutritech.models.Food;
import com.nutritech.models.User;
import com.nutritech.models.UserSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initToolBar();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (!UserSingleton.getUser().getEatenFood().isEmpty())
            addMostCaloricFoodToTable();
    }

    private void initToolBar() {
        Toolbar mToolbar = findViewById(R.id.toolbar_maps);
        mToolbar.setTitle("Carte de votre consommation");
        mToolbar.setNavigationIcon(R.drawable.ic_add_alert_black_24dp);
        mToolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(this, DashboardActivity.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        putMarkersOfFood();

    }

    private void putMarkersOfFood() {
        getLocationsFromAllFoods().forEach(food -> {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(food.getLatitude(), food.getLongitude()))
                    .title(food.getName())
                    .snippet(food.getQuantite() + " grammes")
                    .flat(true));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(food.getLatitude(), food.getLongitude()), 11.0f));

        });
    }

    private ArrayList<Food> getMostCaloricsFood() {

        User user = UserSingleton.getUser();
        user.getEatenFood().sort((food, t1) -> (int) (t1.getCalorie() - food.getCalorie()));
        return user.getEatenFood();
    }

    public void addMostCaloricFoodToTable() {

        TableLayout tl = findViewById(R.id.tableCalories);


        for (int i = 0; i < 7; ++i) {
            try {
                TableRow tableRow = getBasicTableRow();
                Food food = getMostCaloricsFood().get(i);
                tableRow.addView(getTextView(food.getName()));
                tableRow.addView(getTextView(getCityOfLocalisation(food.getLatitude(), food.getLongitude())));
                tableRow.addView(getTextView(String.valueOf(food.getCalorie()), i));
                tl.addView(tableRow);
            } catch (Exception e) {
                continue;
            }
        }
    }

    private String getCityOfLocalisation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String cityName = null;
        String countryName = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            cityName = addresses.get(0).getAddressLine(0);
            countryName = addresses.get(0).getAddressLine(2);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName.concat(", ").concat(countryName);
    }

    private TextView getTextView(String content, int i) {
        TextView tv = new TextView(this);

        if (content.length() > 20) {
            content = content.substring(0, 20).concat("...");
        }

        tv.setText(content);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        switch (i) {
            case 0:
                tv.setTextColor(Color.parseColor("#D00000"));
                break;
            case 1:
                tv.setTextColor(Color.parseColor("#FF7F11"));
                break;
            case 2:
                tv.setTextColor(Color.parseColor("#8e34e3"));
                break;
            default:
                tv.setTextColor(Color.parseColor("#FFFFFF"));
        }

        return tv;
    }

    private TableRow getBasicTableRow() {
        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(getResources().getColor(R.color.realTableRowColor));
        tableRow.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        tableRow.setPadding(0, 5, 0, 5);
        tableRow.setMinimumWidth(398);
        return tableRow;
    }

    public ArrayList<Food> getLocationsFromAllFoods() {
        ArrayList<Food> locations = new ArrayList<>();
        for (Integer food : UserSingleton.getUser().getCalendarFoodList().keySet()) {
            locations.addAll(UserSingleton.getUser().getCalendarFoodList().get(food).getDailyFood());
        }
        return locations;
    }


    private TextView getTextView(String content) {

        TextView tv = new TextView(this);
        if (content.length() > 20) {
            content = content.substring(0, 20).concat("...");
        }
        tv.setText(content);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(getResources().getColor(R.color.primary_dark));
        return tv;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11.0f));
    }
}
