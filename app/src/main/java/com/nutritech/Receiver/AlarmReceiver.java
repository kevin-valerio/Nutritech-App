package com.nutritech.Receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nutritech.R;
import com.nutritech.WeeklyReportActivity;

/**
 * Created by alexislefebvre on 27/04/2019.
 * When the receiver receive an order it send a notification
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final String DEBUG_TAG = "AlarmReceiver";
    private static final String SERVICE_BROADCAST_KEY = "starterService";
    final static int RQS_STOP_SERVICE = 1;
    final static int RQS_SEND_SERVICE = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        // start the notification
        NotificationManager notificationManager = null;
        int rqs = intent.getIntExtra(SERVICE_BROADCAST_KEY,0);
        if(rqs == RQS_STOP_SERVICE){
        }
        if (rqs == RQS_SEND_SERVICE){
            sendNotification("Aliments consommés aujourd'hui","Pensez à renseignez les aliments que vous avez consommé",context);
        }

    }

    /**
     * Send a notification with a title and a message
     * @param title
     * @param message
     * @param context
     */
    public void sendNotification(String title, String message, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context, WeeklyReportActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default")
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.mipmap.ic_launcher);

        notification.setContentIntent(resultPendingIntent);

        notificationManager.notify(1, notification.build());

        Log.d("d", "NOTIIIF");




    }


}

