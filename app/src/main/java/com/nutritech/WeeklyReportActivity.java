package com.nutritech;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.nutritech.models.CoachReview;
import com.nutritech.models.UserSingleton;

public class WeeklyReportActivity extends AppCompatActivity{

    LineGraphSeries series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklyreport);


        initToolBar();
        initFloatingButton();
        initTextView();

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

    private void shareReport() {
        Intent FBIntent = new Intent(Intent.ACTION_SEND);
        FBIntent.setType("text/plain");
        FBIntent.setPackage("com.facebook.orca");
        FBIntent.putExtra(Intent.EXTRA_TEXT, "Aujourd'hui j'ai mangé 2560 kcal ! J'ai atteint mon objectif de 2500 kcal.");
        try {
            startActivity(FBIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "Impossible d'ouvrir Messenger. Veuillez réessayer plus tard.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initTextView(){
        CoachReview coachReview = new CoachReview(UserSingleton.getUser());
        TextView coachReviewTextView = (TextView) findViewById(R.id.coachReview);
        coachReviewTextView.setMovementMethod(new ScrollingMovementMethod());
        coachReviewTextView.setText(coachReview.getReview());
    }

    private void initFloatingButton() {
        FloatingActionButton shareReport = findViewById(R.id.sharingButton);
        shareReport.setOnClickListener(click -> {
            shareReport();
        });
    }
}
