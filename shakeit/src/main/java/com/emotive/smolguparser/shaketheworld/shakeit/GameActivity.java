package com.emotive.smolguparser.shaketheworld.shakeit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/////////////ВЫНОСЛИВОСТЬ -> КОЛИЧЕСТВО ШЕЙКОВ/////////////

public class GameActivity extends Activity {
    private static final String TAG = "ShakeActivity";

    public int i,t=0;

    int SHAKE_SENSITIVITY=20;
    public int savedStaminaShakes=0;

    Timer myTimer;

    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;
    StatisticActivity statAct; //экземпляр класса статистики

    ///////////////SHARED PREFERENCE/////////////
    public static final String APP_PREFERENCES = "My Settings";
    SharedPreferences mSettings;
////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        goStp();


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        i = 0;
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
         statAct = new StatisticActivity();
    }



        @Override
        protected void onStop () {
            sensorManager.unregisterListener(sensorListener);
            super.onStop();
        }


    protected void onShake() {Log.d(TAG, "SHAKE");}

    private final SensorEventListener sensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            accelPrevious = accel;
            accel = (float) Math.sqrt((double) (x * x + y * y + z * z));
            if (accel - accelPrevious > SHAKE_SENSITIVITY) {

                TextView tv = (TextView) findViewById(R.id.count);
                t=0;
                onShake();
                i += 1;
                tv.setText("" + i);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void onBackPressed() {}

    public void stop(){
        TextView tv = (TextView) findViewById(R.id.count);
        Intent finish = new Intent(getApplicationContext(), FInishActivity.class);
        finish.putExtra("count", tv.getText().toString());
        startActivity(finish);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (i<savedStaminaShakes){i=savedStaminaShakes;}
        statAct.all_shakes+=i;
        statAct.all_games+=1;
    }
    public void goStp() {
        Button btn_stop = (Button) findViewById(R.id.button_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stop();
            }
        });
    }
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        if (mSettings.contains("SP_SHAKE_SENSIVITY")) {
            SHAKE_SENSITIVITY = mSettings.getInt("SP_SHAKE_SENSIVITY", 20); }
        if (mSettings.contains("SP_all_games")){
            statAct.all_games = mSettings.getInt("SP_all_games", 0); }
        if (mSettings.contains("SP_all_shakes")){
            statAct.all_shakes = mSettings.getInt("SP_all_shakes",0); }
        if (mSettings.contains("SP_stamina")){
            savedStaminaShakes = mSettings.getInt("SP_stamina",0); }
    }
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("SP_all_shakes", statAct.all_shakes);
        editor.putInt("SP_all_games",statAct.all_games);
        editor.putInt("SP_stamina", i);
        editor.apply();
    }
    protected void onStart(){
        super.onStart();
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        },0,500);
    }
    private void TimerMethod(){
        t+=1;
        if(t==9){stop();}
        this.runOnUiThread(Timer_Tick);
    }
    private Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
        }
    };
}
