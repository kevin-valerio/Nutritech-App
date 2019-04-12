package com.nutritech;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nutritech.models.Food;
import com.nutritech.models.UserSingleton;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initToolBar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    public ArrayList<Food> getLocationsFromAllFoods() {
        ArrayList<Food> locations = new ArrayList<>();
        for (Integer food : UserSingleton.getUser().getCalendarFoodList().keySet()) {
            locations.addAll(UserSingleton.getUser().getCalendarFoodList().get(food).getDailyFood());
        }
        return locations;
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
