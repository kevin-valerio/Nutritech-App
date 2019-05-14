package com.nutritech.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {

    private static final String SERVICE_BROADCAST_KEY = "startNotification";
    final static int RQS_START_NOTIFICATION = 2;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            /* Setting the alarm here */
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.putExtra(SERVICE_BROADCAST_KEY,RQS_START_NOTIFICATION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            /* Set the alarm to start at 15:00  */
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 06);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }
    }

}
