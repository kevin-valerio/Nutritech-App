package com.nutritech.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nutritech.Receiver.AlarmReceiver;
import com.nutritech.Receiver.AlarmReceiverForCalendar;

import java.util.Calendar;

import static android.provider.ContactsContract.Intents.Insert.ACTION;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class CalendarService extends Service {

    private AlarmReceiverForCalendar alarm;

    public void onCreate(){
        super.onCreate();
        if (this.alarm == null){
            this.alarm = new AlarmReceiverForCalendar();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Register the receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("AlarmReceiverForCalendar");
        registerReceiver(alarm, intentFilter);
        startAlarme();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(alarm);
    }

    private void startAlarme(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE,06);


        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE,1);
        }


        Intent alarmIntent = new Intent(this.getApplicationContext(), AlarmReceiverForCalendar.class);
        alarmIntent.putExtra("CalendarService",2);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }


}
