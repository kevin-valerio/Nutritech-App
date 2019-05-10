package com.nutritech;


import android.content.Intent;
import android.os.Bundle;
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


            initToolBar();
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
}
