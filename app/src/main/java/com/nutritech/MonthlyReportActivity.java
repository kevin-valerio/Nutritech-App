package com.nutritech;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.nutritech.models.CoachReview;
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
            initTextView();
        }


    private void shareReport() {
        Intent FBIntent = new Intent(Intent.ACTION_SEND);
        int currentday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        FBIntent.setType("text/plain");
        FBIntent.setPackage("com.facebook.orca");
        String messagekcal = "";
        messagekcal = String.valueOf(UserSingleton.getUser().getCalendarFoodList().get(currentday).getCalorie());
        String messageObj = "";
        messageObj = String.valueOf(UserSingleton.getUser().getKcalObj());
        String messageNutrients = "";
        messageNutrients = "Macronutriments :\n" + "Glucides  "
                + UserSingleton.getUser().getCalendarFoodList().get(currentday).getCarbs()
                + "/" + UserSingleton.getUser().getObjGlucides() + "\nLipides  "
                + UserSingleton.getUser().getCalendarFoodList().get(currentday).getLipid()
                + "/" + UserSingleton.getUser().getObjLipides() + "\nProtéïnes  "
                + UserSingleton.getUser().getCalendarFoodList().get(currentday).getProteins()
                + "/" + UserSingleton.getUser().getObjProteines();
        FBIntent.putExtra(Intent.EXTRA_TEXT, "Aujourd'hui j'ai mangé " + messagekcal + " kcal !\n" +
                "Mon objectif est de " + messageObj + " kcal.\n" + messageNutrients
                );
        try {
            startActivity(FBIntent);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

        //Initialise la ToolBar
        private void initToolBar() {
            Toolbar mToolbar = findViewById(R.id.retour);
            mToolbar.setTitle("Bilan mensuel");
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

    private void initTextView(){
        CoachReview coachReview = new CoachReview(UserSingleton.getUser());
        TextView coachReviewTextView = (TextView) findViewById(R.id.coachReview);
        coachReviewTextView.setMovementMethod(new ScrollingMovementMethod());
        coachReviewTextView.setText(coachReview.getReview());
    }
}
