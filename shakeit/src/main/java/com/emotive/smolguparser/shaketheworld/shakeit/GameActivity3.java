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
import android.view.View;
import android.widget.TextView;


/////////////НА СКОРОСТЬ -> ВРЕМЯ ЗА КОЛИЧЕСТВО ШЕЙКОВ/////////////
public class GameActivity3 extends Activity {

    private static final String TAG = "ShakeActivity";
    private static int SHAKE_SENSITIVITY;
    public int i = 100, k = 0,t=0,savedSpeed=999;

    private Timer myTimer;
    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;

    ///////////////SHARED PREFERENCE/////////////
    public static final String APP_PREFERENCES = "My Settings";
    SharedPreferences mSettings;
    StatisticActivity statAct = new StatisticActivity();

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
                i -= 1;t+=1;
                tv.setText("" + i);

                if (i == 0) {
                    onFinish();
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    protected void onFinish() {
        TextView tv = (TextView) findViewById(R.id.textResult);
        Intent intent = new Intent(getApplicationContext(), FInishActivity.class);
        intent.putExtra("count", tv.getText().toString());
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if(i<1){if(k>savedSpeed){k=savedSpeed;}}
        statAct.all_shakes += t;
        statAct.all_games += 1;
        finish();
    }

    public void onBackPressed() {}

    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        if (mSettings.contains("SP_SHAKE_SENSIVITY")) {
            SHAKE_SENSITIVITY = mSettings.getInt("SP_SHAKE_SENSIVITY", 20); }
        if (mSettings.contains("SP_all_games")) {
            statAct.all_games = mSettings.getInt("SP_all_games", 0); }
        if (mSettings.contains("SP_all_shakes")) {
            statAct.all_shakes = mSettings.getInt("SP_all_shakes", 0); }
        if (mSettings.contains("SP_speed")){
            savedSpeed = mSettings.getInt("SP_speed",999); }
    }

    protected void onStop() {
        sensorManager.unregisterListener(sensorListener);
        super.onStop();
    }

    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("SP_all_shakes", statAct.all_shakes);
        editor.putInt("SP_all_games", statAct.all_games);
        if (i<1)editor.putInt("SP_speed",k);
        editor.apply();
    }

    public void Stop_Click(View v){
        Intent intent = new Intent(getApplicationContext(), StartScreen.class);
        startActivity(intent);finish();
    }
}
