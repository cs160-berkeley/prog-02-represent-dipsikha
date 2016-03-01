package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class ShakeSplash extends Activity {

    private static final long DELAY = 2000;
    private static final String PREF_FIRST_TIME_KEY = "firstTime";
    private boolean mScheduled = false;
    private Timer mSplashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_splash);
        TextView logo = (TextView) findViewById(R.id.text);

        mSplashTimer = new Timer();
        mSplashTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Activity activity = ShakeSplash.this;
                        activity.finish();
                        startActivity(new Intent(activity, RandomLocationActivity.class));

                    }
                },
                DELAY);
        mScheduled = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mScheduled) {
            mSplashTimer.cancel();
        }
        mSplashTimer.purge();
    }
}
