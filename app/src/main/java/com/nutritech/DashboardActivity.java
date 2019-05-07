package com.nutritech;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.nutritech.Receiver.AlarmReceiver;
import com.nutritech.models.FoodList;
import com.nutritech.models.UserSingleton;
import com.nutritech.models.WeightAlertBuilder;

import java.util.Calendar;


public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private PendingIntent pendingIntent;
    private String kcal;
    private String glu;
    private String prot;
    private String lip;
    private TextView calorie;
    private TextView glucide;
    private TextView proteine;
    private TextView lipide;
    private ProgressBar progressBar;
    private GraphView graphView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initToolBar();
        initNavigationView();
        initFloatingButton();
        initProgressBar();
        initChartWithWeight();

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, alarmIntent, 0);

        startNotificationAlarm();

        Calendar calendar = Calendar.getInstance();
        FoodList currentfoodList = UserSingleton.getUser().getCalendarFoodList().get(calendar.get(Calendar.DAY_OF_MONTH));
        kcal = currentfoodList.getCalorie() + " / " + UserSingleton.getUser().getKcal() + " kcal";
        lip = currentfoodList.getLipid() + " / " + UserSingleton.getUser().getObjLipides() + " g";
        glu = currentfoodList.getCarbs() + " / " + UserSingleton.getUser().getObjGlucides() + " g";
        prot = currentfoodList.getProteins() + " / " + UserSingleton.getUser().getObjProteines() + " g";
        calorie = findViewById(R.id.calorie);
        proteine = findViewById(R.id.proteine);
        lipide = findViewById(R.id.lipide);
        glucide = findViewById(R.id.glucide);
        glucide.setText(glu);
        proteine.setText(prot);
        calorie.setText(kcal);
        lipide.setText(lip);

    }

    private void initChartWithWeight() {
        graphView = findViewById(R.id.dashboardGraph);
        graphView.setTitle("Evolution de votre poids");
        Series series = new LineGraphSeries<>();
        ((LineGraphSeries) series).appendData(new DataPoint(1, 0), false, 999);
        ((LineGraphSeries) series).setTitle("Poids (en kg)");
        UserSingleton.getUser().getWeights().forEach(weight -> ((LineGraphSeries) series).appendData(new DataPoint(series.getHighestValueX() + 1, weight), false, 999));
        ((LineGraphSeries) series).setColor(Color.parseColor("#e5def4"));
        ((LineGraphSeries) series).setThickness(10);
        graphView.addSeries(series);
    }

    private void initProgressBar() {

        int todaysCalories = Math.toIntExact(UserSingleton.getUser().getCalendarFoodList().get(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).getCalorie());

        this.progressBar = findViewById(R.id.progressBar);
        int progressValue = (int) ((todaysCalories / UserSingleton.getUser().getKcal()) * 100);
        progressBar.setSecondaryProgress(progressValue);

        if (progressValue > 50 && progressValue < 75) {
            progressBar.setSecondaryProgressTintList(ContextCompat.getColorStateList(this, R.color.medium));
        } else if (progressValue > 75) {
            progressBar.setSecondaryProgressTintList(ContextCompat.getColorStateList(this, R.color.important));
        } else if (progressValue < 50) {
            progressBar.setSecondaryProgressTintList(ContextCompat.getColorStateList(this, R.color.primary_darker));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initProgressBar();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        initProgressBar();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initProgressBar();
    }

    //Initialise le FloatinButton
    private void initFloatingButton() {
        FloatingActionButton addWeight = findViewById(R.id.addWeight);
        addWeight.setOnClickListener(click -> {
            askForWeight();
        });
    }

    //Demande à l'utilisateur son nouveau poids
    public void askForWeight() {

        WeightAlertBuilder weightAlertBuilder = new WeightAlertBuilder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        weightAlertBuilder.setMainTextView();
        weightAlertBuilder.setSecondaryTextView();
        weightAlertBuilder.setWeightEditBox();
        weightAlertBuilder.setButtons();

        weightAlertBuilder.build();
    }

    //Initialise la toolbar
    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Initialise les composants de la NavigationView
    private void initNavigationView() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        TextView nomPrenomTxtView = header.findViewById(R.id.nomPrenomTxtView);
        TextView objectifTxtView = header.findViewById(R.id.objectifTxtView);
        TextView feedback = header.findViewById(R.id.feedbackTxtView);

        String connectedWith = "Connecté avec " + UserSingleton.getUser().getMail();
        String mainGoal = "Votre objectif : " + UserSingleton.getUser().getGoal().toString().toLowerCase().replace("_", " ");

        nomPrenomTxtView.setText(connectedWith);
        objectifTxtView.setText(mainGoal);
        feedback.setOnClickListener(click -> new AlertDialog.Builder(DashboardActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("Envoyez-nous un feedback !")
                .setMessage("Envoyez un mail à l'adresse feedback@nutritech.com, et dites nous ce que vous pensez de l'application ! \n\nEt n'oubliez pas de nous noter sur le Play Store")
                .setPositiveButton("D'accord", null)
                .setIcon(android.R.drawable.ic_dialog_email)
                .show()
        );

    }

    //Définit l'action lorsque l'onclique sur " < "
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Définit les actions lorsqu'on clique sur les bouttons du menu de gauche
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bilan_mensuel) {
            startActivity(new Intent(this, MonthlyReportActivity.class));
            finish();

        } else if (id == R.id.mapps) {
            startActivity(new Intent(this, MapsActivity.class));
            finish();
            overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
        } else if (id == R.id.bilan_hebdo) {
            startActivity(new Intent(this, WeeklyReportActivity.class));
            finish();
        } else if (id == R.id.deconnection) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(this, SearchFoodActivitty.class));
            overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            return true;
        });


        return true;
    }

    /**
     * Start the alarm manager daily
     */
    public void startNotificationAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 29);
        Log.d("d", "STAAAART");

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}
