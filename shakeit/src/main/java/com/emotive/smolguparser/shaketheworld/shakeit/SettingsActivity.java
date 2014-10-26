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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


public class SettingsActivity extends Activity {

    public int SHAKE_SENSIVITY_SETTING = 25, SHAKE_SENSIVITY;
    public boolean check = true;
    Vibrator vibro;
    int shakes = 0;
    String strSensivity;
    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;
    ImageView pict;
    Animation shaking;

    StatisticActivity statAct = new StatisticActivity();
//////////////////////////////////SHARED PREFERENCE////////////////////////////////////
    public boolean firstLaunch;
    public static final String APP_PREFERENCES = "My Settings";
    SharedPreferences mSettings;
////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        pict = (ImageView) findViewById(R.id.pic);
        shaking = AnimationUtils.loadAnimation(this, R.anim.shake);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);//
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
                        strSensivity = "" + SHAKE_SENSIVITY;
                        firstLaunch = false;
                        //vibro.vibrate(500);

                        AlertDialog.Builder NEKIT_LOHUDRA = new AlertDialog.Builder(SettingsActivity.this);
                        NEKIT_LOHUDRA.setTitle(getString(R.string.done))
                                .setMessage(getString(R.string.cal_end))
                                .setCancelable(false)
                                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

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

            public void onFinish() {
                check = false;
                SHAKE_SENSIVITY = SHAKE_SENSIVITY_SETTING;
                strSensivity = "" + SHAKE_SENSIVITY;
                firstLaunch = false;
            }
        };
        cdt.start();
    }

    public void btn_start_set_Click(View v) {
        v.setClickable(false);
        Timer();
        Toast.makeText(getApplicationContext(), getString(R.string.start_shake), Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("SP_SHAKE_SENSIVITY", strSensivity);
        editor.apply();
        pict.startAnimation(shaking);
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            accelPrevious = accel;
            accel = (float) Math.sqrt((double) (x * x + y * y + z * z));
            if (accel - accelPrevious > SHAKE_SENSIVITY_SETTING) {
                shakes += 1;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("SP_SHAKE_SENSIVITY", SHAKE_SENSIVITY);
        editor.putBoolean("SP_FIRST_LAUNCH", firstLaunch);
        if(!mSettings.contains("SP_all_shakes") && !mSettings.contains("SP_all_games")){
        editor.putInt("SP_all_shakes", statAct.all_shakes);
        editor.putInt("SP_all_games", statAct.all_games);
        }
        editor.apply();
    }

    protected void onResume() {
        super.onResume();
        if (mSettings.contains("SP_SHAKE_SENSIVITY")) {
            SHAKE_SENSIVITY = mSettings.getInt("SP_SHAKE_SENSIVITY", 20);
        }
    }
}
