package com.nutritech;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import com.nutritech.Services.StarterService;
import com.nutritech.models.UserSingleton;
import com.nutritech.models.WeightAlertBuilder;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private StarterService mBoundService;
    private static final String TAG = "NotifyActivity";
    final static String SERVICE_RECEIVER = "alarmReceiver";
    final static String SERVICE_BROADCAST_KEY = "starterService";
    final static int RQS_SEND_SERVICE = 2;

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
       // Intent alarmIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
       // pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, alarmIntent, 0);

        startService(new Intent(this, StarterService.class));


        Calendar calendar = Calendar.getInstance();
        FoodList currentfoodList = UserSingleton.getUser().getCalendarFoodList().get(calendar.get(Calendar.DAY_OF_MONTH));
        kcal = currentfoodList.getCalorie() + " / " + UserSingleton.getUser().getKcalObj() + " kcal";
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

    }

    private void initProgressBar() {

        int todaysCalories = Math.toIntExact(UserSingleton.getUser().getCalendarFoodList().get(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).getCalorie());

        this.progressBar = findViewById(R.id.progressBar);
        int progressValue = (int) ((todaysCalories / UserSingleton.getUser().getKcalObj()) * 100);
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
        Intent intent = new Intent();
        intent.setAction(SERVICE_RECEIVER);
        intent.putExtra(SERVICE_BROADCAST_KEY,RQS_SEND_SERVICE);
        sendBroadcast(intent);
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

/*        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
      builder.setTitle("Title");

      LinearLayout layout = new LinearLayout(getApplicationContext());
      layout.setOrientation(LinearLayout.VERTICAL);

      TextView title = new TextView(this);
      title.setText(R.string.new_poids);
      title.setPadding(10, 10, 10, 10);
      title.setGravity(Gravity.CENTER);
      title.setTextColor(Color.BLACK);
      title.setTextSize(20);
      builder.setCustomTitle(title);

      TextView msg = new TextView(this);
      msg.setText("\nVeuillez entrer votre nouveau poids. \n Cela nous permettra de suivre l'évolution de votre poids ! \n\n");
      msg.setGravity(Gravity.CENTER_HORIZONTAL);
      msg.setTextColor(Color.BLACK);
      layout.addView(msg);

      EditText poids = new EditText(getApplicationContext());
      poids.setInputType(InputType.TYPE_CLASS_NUMBER);
      poids.setHint("Poids (en kg)");
      layout.addView(poids);

      builder.setPositiveButton("Ajouter", (dialog, which) -> {
          UserSingleton.getUser().addWeight(Integer.valueOf(poids.getText().toString()));
                  Toast.makeText(this, "Votre nouveau poids a été ajouté (" + poids.getText().toString() + " kg)", Toast.LENGTH_LONG).show();
              }
      );

      builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
      builder.setView(layout);
      builder.show();
*/
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
        }else if (id == R.id.foodlist) {
            //startActivity(new Intent(this, FoodRecyclerActivity.class));
            startActivity(new Intent(this, DailyConsumptionActivity.class));
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

}
