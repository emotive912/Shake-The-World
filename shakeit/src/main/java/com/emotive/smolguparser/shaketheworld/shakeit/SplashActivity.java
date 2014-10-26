package com.emotive.smolguparser.shaketheworld.shakeit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    private ImageView img;
    private TextView tv;
    public boolean firstLaunch =true;

    ///////////////SHARED PREFERENCE/////////////
    public static final String APP_PREFERENCES = "My Settings";
  //  public static String SP_FIRST_LAUNCH = "";
    SharedPreferences mSettings;
////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                if(firstLaunch){
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                finish();}
                else{Intent i = new Intent(getApplicationContext(), StartScreen.class);
                    startActivity(i);
                    finish();

                }
            }
        }, SPLASH_TIME_OUT);

        img = (ImageView) findViewById(R.id.Shaker_pic);
        tv = (TextView) findViewById(R.id.entry_title);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        img.startAnimation(shake);
        tv.startAnimation(shake);


    }

    protected void onResume() {
        super.onResume();
        try {
            if(mSettings.contains("SP_FIRST_LAUNCH")){
             firstLaunch = mSettings.getBoolean("SP_FIRST_LAUNCH",true);
            }
        } catch (Exception exception) {
        }
    }
}



