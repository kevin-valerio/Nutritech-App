package com.nutritech.Receiver;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.nutritech.models.UserSingleton;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by thomascolomban
 * When the receiver receive an order it set a report on the google calendar
 */

public class AlarmReceiverForCalendar extends BroadcastReceiver {

    private static final String DEBUG_TAG = "AlarmReceiver";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        int rqs = intent.getIntExtra("CalendarService", 0);
        if (rqs == 1) {
        }
        if (rqs == 2) {
            setReportOnCalendar();
        }
    }


    private void setReportOnCalendar(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, "Bilan de la journée");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, UserSingleton.getUser().getKcalCurrent()+"kcal / "+UserSingleton.getUser().getKcalObj()+" kcal");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        GregorianCalendar calDate = new GregorianCalendar(2019, 04, 13);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());


        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);


        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");


        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(context, intent, null);
    }
}

