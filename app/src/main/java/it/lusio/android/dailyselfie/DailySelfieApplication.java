package it.lusio.android.dailyselfie;

import android.app.Application;

public class DailySelfieApplication extends Application {
    @Override
    public void onCreate() {
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }
}