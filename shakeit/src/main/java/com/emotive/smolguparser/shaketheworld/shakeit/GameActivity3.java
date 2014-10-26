package com.emotive.smolguparser.shaketheworld.shakeit;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


/////////////НА СКОРОСТЬ -> ВРЕМЯ ЗА КОЛИЧЕСТВО ШЕЙКОВ/////////////
public class GameActivity3 extends Activity {

    private static final String TAG = "ShakeActivity";
    private static  int SHAKE_SENSITIVITY ;
    public int i = 100, k = 0;

    private Timer myTimer;
    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;

    ///////////////SHARED PREFERENCE/////////////
    public static final String APP_PREFERENCES = "My Settings";
   // public static String SP_SHAKE_SENSIVITY = "";
    SharedPreferences mSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity3);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
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
                i -= 1;
                tv.setText("" + i);

                if (i == 0) {
                    onFinish();
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };


    protected void onFinish() {
        TextView tv = (TextView) findViewById(R.id.textResult);
        Intent intent = new Intent(getApplicationContext(), FInishActivity.class);
        intent.putExtra("count", tv.getText().toString());
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
    protected void onStop() {
        sensorManager.unregisterListener(sensorListener);
        super.onStop();
    }
}
