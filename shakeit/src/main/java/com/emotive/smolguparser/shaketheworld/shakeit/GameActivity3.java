package com.emotive.smolguparser.shaketheworld.shakeit;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class GameActivity3 extends Activity {

    private static final String TAG = "ShakeActivity";
    private static final int SHAKE_SENSITIVITY = 15;
    public int j = 4;
    public int i = 100, k = 0;

    private Timer myTimer;
    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity3);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    protected void onStart() {
        super.onStart();
        TextView tv4 = (TextView) findViewById(R.id.count);
        tv4.setText("" + i);

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 1000);
    }

    private void TimerMethod() {
        k += 1;
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            TextView tvtv = (TextView) findViewById(R.id.textResult);
            tvtv.setText("" + k);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
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
            //if (accel >SHAKE_SENSITIVITY){accel =7;}
            if (accel - accelPrevious > SHAKE_SENSITIVITY) {
                TextView tv = (TextView) findViewById(R.id.count);

                onShake();
                i -= 1;
                tv.setText("" + i);

                if (i == 0) {
                    onFinish();
                }

            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    };


    public void onFinish() {
        TextView tv = (TextView) findViewById(R.id.textResult);
        Intent intent = new Intent(getApplicationContext(), FInishActivity.class);
        intent.putExtra("count", tv.getText().toString());
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onBackPressed() {
        //do nothing here
    }
}
