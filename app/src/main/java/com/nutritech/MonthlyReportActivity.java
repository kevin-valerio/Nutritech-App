package com.nutritech;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MonthlyReportActivity extends AppCompatActivity{

        LineGraphSeries series1;
        LineGraphSeries series2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_monthlyreport);

            initGraph1();
            initGraph2();
            initToolBar();
        }

        //Initialise le graph 1
        private void initGraph1(){
            GraphView graph1 = findViewById(R.id.monthlyGraph1);
            series1 = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0,1),
                    new DataPoint(2,5),
                    new DataPoint(3,3)
            });
            graph1.addSeries(series1);
        }


    //Initialise le graph 2
        private void initGraph2(){
            GraphView graph2 = findViewById(R.id.monthlyGraph2);

            series2 = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0,1),
                    new DataPoint(0.5,5),
                    new DataPoint(1,3.5),
                    new DataPoint(1.5,2),
                    new DataPoint(2,4),
                    new DataPoint(3,1),
            });
            graph2.addSeries(series2);
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
