package com.emotive.smolguparser.shaketheworld.shakeit;

import android.app.Activity;
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

public class GameActivity extends Activity {
    private static final String TAG = "ShakeActivity";
    private static final int SHAKE_SENSITIVITY = 15;
    public int j = 4;
    public int i, k = 0; //k-our timer

    int gameForse = 0;
    double gameF = 0;
    double F = 0;
    int statForse = 0;
    double maxForse = 0;
    private Timer myTimer;
    double timeFG, T;
    double timeS = 0;
    double minTS = 1000000;
    double allPath = 0, OnePath;
    static String asd = "asd";
    int allShakes, gameShakes;
    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;

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
        //styles to text


    }

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
            if (accel - accelPrevious > SHAKE_SENSITIVITY) {

                TextView tv = (TextView) findViewById(R.id.count);

                onShake();
                i += 1;
                tv.setText("" + i);


            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void gameForse(SensorEvent sensorEvent) { //силы потрачено за игру, перменная gameForse
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        F = Math.sqrt(x * x + y * y + z * z) * 0.14;
        //	F=Math.round(F);
        gameF += F;
    }

    public void statForse() { //общая сила за все игры, в статистику
        statForse += gameF;
    }

    public void maxForse(SensorEvent sensorEvent) {// максимальная сила за шейк, переменная maxForse
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        F = Math.sqrt(x * x + y * y + z * z) * 0.14;
        if (F > maxForse) {
            maxForse = F;
        }
    }

    public void timeForGame() {
        timeFG = k;

        //вернуть это как общее время игры
    }

    public void fastestShake() {// нужно переделать из-за времени
        T = k - timeS;
        timeS = k;
        if (T < minTS) {
            minTS = T;
        }
    }

    public void fullPath(SensorEvent sensorEvent) {// нужно переделать из-за времени
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        T = k - timeS;
        timeS = k;
        OnePath = Math.sqrt(x * x + y * y + z * z) * T * T / 2;
        allPath += OnePath;
    }

    public void allShakes() {
        gameShakes = i;
        allShakes += gameShakes;

    }

    public void onBackPressed() {
        //do nothing here
        //NEVER RETURN TRUE!


    }

    public void goStp() {
        Button btn_stop = (Button) findViewById(R.id.button_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TextView tv = (TextView) findViewById(R.id.count);
                Intent finish = new Intent(getApplicationContext(), FInishActivity.class);
                finish.putExtra("count", tv.getText().toString());
                finish.putExtra("gameF", gameF);
                finish.putExtra("max_force", maxForse);
                finish.putExtra("time", timeFG);
                startActivity(finish);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
