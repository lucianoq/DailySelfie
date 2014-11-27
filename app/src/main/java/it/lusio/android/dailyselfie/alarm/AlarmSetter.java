package it.lusio.android.dailyselfie.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmSetter {
    private static final int PENDING_INTENT_ID = 0;
    private static final long REPEAT_TIME = 2 * 60 * 1000;
    private Context mContext;
    private PendingIntent mPendingIntent;

    public AlarmSetter(Context context) {
        this.mContext = context;
    }

    public void startAlarm() {
        mPendingIntent = PendingIntent.getBroadcast(mContext, PENDING_INTENT_ID, new Intent(mContext, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, REPEAT_TIME, REPEAT_TIME, mPendingIntent);
    }
}
