package it.lusio.android.dailyselfie;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import it.lusio.android.dailyselfie.alarm.AlarmSetter;

public class ApplicationDailySelfie extends Application {

    private static int started = 0;
    private static int stopped = 0;

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    @Override
    public void onCreate() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ++started;
                Log.d(Constants.TAG, "One Activity Started.");
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ++stopped;
                Log.d(Constants.TAG, "One Activity Stopped.");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });

        new AlarmSetter(getApplicationContext()).startAlarm();
    }
}
