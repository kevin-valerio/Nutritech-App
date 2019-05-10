package com.nutritech;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.nutritech.models.DateTuple;
import com.nutritech.models.UserSingleton;

import java.util.Calendar;


public class MonthlyReportActivity extends AppCompatActivity{

        LineGraphSeries series1;
        LineGraphSeries series2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_monthlyreport);

            initFloatingButton();
            initToolBar();
        }


    private void shareReport() {
        Intent FBIntent = new Intent(Intent.ACTION_SEND);
        FBIntent.setType("text/plain");
        FBIntent.setPackage("com.facebook.orca");
        FBIntent.putExtra(Intent.EXTRA_TEXT, "Aujourd'hui j'ai mangé 2560 kcal ! J'ai atteint mon objectif de 2500 kcal.");
        try {
            startActivity(FBIntent);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

        //Initialise la ToolBar
        private void initToolBar() {
            Toolbar mToolbar = findViewById(R.id.retour);
            mToolbar.setTitle("Bilan Mensuel");
            mToolbar.setNavigationIcon(R.drawable.ic_add_alert_black_24dp);
            mToolbar.setNavigationOnClickListener(view -> {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            });
        }

    private void initFloatingButton() {
        FloatingActionButton shareReport = findViewById(R.id.sharingButton);
        shareReport.setOnClickListener(click -> {
            shareReport();
        });
    }
}
