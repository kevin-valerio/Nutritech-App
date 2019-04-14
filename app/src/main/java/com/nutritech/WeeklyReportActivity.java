package com.nutritech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class WeeklyReportActivity extends AppCompatActivity{

    LineGraphSeries series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklyreport);


        initGraph();
        initToolBar();

    }

    //Initialise le graph
    private void initGraph(){
        GraphView graph = (GraphView) findViewById(R.id.weeklyGraph);

        series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,1),
                new DataPoint(2,5),
                new DataPoint(3,3)
        });

        graph.addSeries(series);
    }


    //Initialise la toolbar
    private void initToolBar() {
        Toolbar mToolbar = findViewById(R.id.retour);
        mToolbar.setTitle("Bilan Hedbomadaire");
        mToolbar.setNavigationIcon(R.drawable.ic_add_alert_black_24dp);
        mToolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(this, DashboardActivity.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }
}
