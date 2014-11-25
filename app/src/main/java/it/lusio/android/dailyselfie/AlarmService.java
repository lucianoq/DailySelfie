package it.lusio.android.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmService {
    private static final long REPEAT_TIME = 2 * 60 * 1000;
    private Context mContext;
    private PendingIntent mPendingIntent;

    public AlarmService(Context context) {
        this.mContext = context;
    }

    public void startAlarm() {
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, REPEAT_TIME, REPEAT_TIME, mPendingIntent);
    }
}
