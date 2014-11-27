package it.lusio.android.dailyselfie.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import it.lusio.android.dailyselfie.ApplicationDailySelfie;
import it.lusio.android.dailyselfie.Constants;
import it.lusio.android.dailyselfie.R;
import it.lusio.android.dailyselfie.ui.ActivityMain;


public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ApplicationDailySelfie.isApplicationVisible()) {
            Log.d(Constants.TAG, "Alarm received and ignored.");
            return;
        }

        Log.d(Constants.TAG, "Alarm received and handled.");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, ActivityMain.class), 0);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setTicker(context.getString(R.string.notification_ticker))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setAutoCancel(true)
                .build();

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
        nm.notify(NOTIFICATION_ID, notification);
    }
}
