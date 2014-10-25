package com.emotive.smolguparser.shaketheworld.shakeit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends Activity {

    public int SHAKE_SENSIVITY_SETTING = 25,SHAKE_SENSIVITY;
    public boolean check = true;
    Vibrator vibro;
    int shakes = 0;
    String strSensivity;
    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;

///////////////SHARED PREFERENCE/////////////
    public static final String APP_PREFERENCES = "My Settings";
    public static String SP_SHAKE_SENSIVITY = "SS";
    SharedPreferences mSettings;
////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void Timer() {
        CountDownTimer cdt = new CountDownTimer(25000, 1000) {
            @Override
            public void onTick(long l) {
                if (check) {
                    if (shakes < 10) {
                        SHAKE_SENSIVITY_SETTING -= 1;

                    } else {
                        check = false;
                        SHAKE_SENSIVITY = SHAKE_SENSIVITY_SETTING;
                        strSensivity = ""+SHAKE_SENSIVITY;
                        //vibro.vibrate(500);
                        AlertDialog.Builder NEKIT_LOHUDRA = new AlertDialog.Builder(SettingsActivity.this);
                        NEKIT_LOHUDRA.setTitle("Done")
                                .setMessage("Colibration has ended.")
                                .setCancelable(false)
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.cancel();
                                        Intent intent = new Intent(getApplicationContext(), StartScreen.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        AlertDialog alert = NEKIT_LOHUDRA.create();
                        alert.show();
                    }
                }
        }
        @Override
        public void onFinish () {
        }
    };
    cdt.start();
}//method of CountDownTimer
 protected void onPause(){
     super.onPause();
     SharedPreferences.Editor editor = mSettings.edit();
     editor.putString(SP_SHAKE_SENSIVITY, strSensivity);
     editor.apply();
 }

    public void btn_start_set_Click(View v){
        Timer();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(SP_SHAKE_SENSIVITY, strSensivity);
        editor.apply();
        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            accelPrevious = accel;
            accel  =(float) Math.sqrt((double)( x*x + y*y + z*z));
            if (accel - accelPrevious > SHAKE_SENSIVITY_SETTING){
                shakes +=1;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    protected void onResume(){
        super.onResume();
        if(mSettings.contains(SP_SHAKE_SENSIVITY)){
            //TextView tv = (TextView)findViewById(R.id.tv_title_settings);
           SHAKE_SENSIVITY= Integer.parseInt(mSettings.getString(SP_SHAKE_SENSIVITY,""));
        }
    }
}
