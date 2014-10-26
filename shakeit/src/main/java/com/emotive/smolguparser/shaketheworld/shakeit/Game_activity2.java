package com.emotive.smolguparser.shaketheworld.shakeit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/////////////НА ВРЕМЯ -> КОЛИЧЕСТВО ШЕЙКОВ ЗА ВРЕМЯ/////////////
public class Game_activity2 extends Activity {

    private static final String TAG = "ShakeActivity";
    private static int SHAKE_SENSITIVITY ;
    public int i;
    public int j = 5;

    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;

    ///////////////SHARED PREFERENCE/////////////
    public static final String APP_PREFERENCES = "My Settings";
    SharedPreferences mSettings;
////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity2);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        i = 0;
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);//
    }

    protected void onStop() {
        sensorManager.unregisterListener(sensorListener);
        super.onStop();
    }

    protected void onShake() {
        Log.d(TAG, "SHAKE");
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            accelPrevious = accel;
            accel = (float) Math.sqrt((double) (x * x + y * y + z * z));
            if (accel - accelPrevious > SHAKE_SENSITIVITY) {

                TextView tv = (TextView) findViewById(R.id.count);

                onShake();
                i += 1;
                tv.setText("" + i);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    protected void onStart() {
        super.onStart();

        new CountDownTimer(55000, 1000) {
            TextView tv_to_timer = (TextView) findViewById(R.id.textResult);
            TextView counte = (TextView) findViewById(R.id.count);

            public void onTick(long count) {

                if (j == 5) {
                    counte.setText(R.string.ready);
                }
                if (j == 4) {
                    counte.setText(R.string.set);
                }
                if (j == 3) {
                    counte.setText(R.string.shake);
                }
                if (j == 2) {
                    counte.setText("0");
                }
                if (j < 2) {
                    tv_to_timer.setText("" + count / 1000);
                }
                j--;
            }

            public void onFinish() {
                TextView tv = (TextView) findViewById(R.id.count);
                Intent finish = new Intent(getApplicationContext(), FInishActivity.class);
                finish.putExtra("count", tv.getText().toString());
                startActivity(finish);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }.start();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        if (mSettings.contains("SP_SHAKE_SENSIVITY")) {
            SHAKE_SENSITIVITY = mSettings.getInt("SP_SHAKE_SENSIVITY", 20);
        }
    }
}