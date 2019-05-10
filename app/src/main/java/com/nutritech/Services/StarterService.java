package com.nutritech.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.nutritech.Receiver.AlarmReceiver;

import java.util.Calendar;

public class StarterService extends Service {
    private static final String TAG = "MyService";
    private  AlarmReceiver alarmReceiver;

    final static String ACTION = "alarmReceiver";


    /**
     * starts the AlarmManager.
     */

    @Override
    public void onCreate() {
        super.onCreate();
        if (this.alarmReceiver == null)
            this.alarmReceiver= new AlarmReceiver();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.unregisterReceiver(alarmReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Register the receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(alarmReceiver, intentFilter);
        //startNotificationAlarm();
        return START_NOT_STICKY;
    }


}

