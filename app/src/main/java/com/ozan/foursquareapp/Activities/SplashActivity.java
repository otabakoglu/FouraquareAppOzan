package com.ozan.foursquareapp.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ozan.foursquareapp.R;

public class SplashActivity extends AppCompatActivity {

    private final static int SPLASH_SCREEN_DISPLAY_COUNTER_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        countSplashScreen();
    }

    private void countSplashScreen(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Show Main Page
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);

                SplashActivity.this.finish();

            }
        }, SPLASH_SCREEN_DISPLAY_COUNTER_MS);

    }
}
