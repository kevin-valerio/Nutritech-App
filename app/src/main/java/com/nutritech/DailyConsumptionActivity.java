package com.nutritech;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.nutritech.Fragments.DateSelectorFragment;
import com.nutritech.models.DateSelectorViewModel;
import com.nutritech.models.Food;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class DailyConsumptionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private DateSelectorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_consumption);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),2);
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        // DatePickerDialog
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker = new DateSelectorFragment();
                datepicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        mViewModel = ViewModelProviders.of(this).get(DateSelectorViewModel.class);



        initToolBar();


    }

    //Initialise la toolbar
    private void initToolBar() {
        Toolbar mToolbar = findViewById(R.id.retour);
        mToolbar.setTitle("Ma consommation");
        mToolbar.setNavigationIcon(R.drawable.ic_add_alert_black_24dp);
        mToolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(this, DashboardActivity.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year,month,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView btn = findViewById(R.id.button);
        btn.setText(currentDateString);
    }
}
