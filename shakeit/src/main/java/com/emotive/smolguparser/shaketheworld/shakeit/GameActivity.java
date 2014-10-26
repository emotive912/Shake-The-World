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

/////////////ВЫНОСЛИВОСТЬ -> КОЛИЧЕСТВО ШЕЙКОВ/////////////

public class GameActivity extends Activity {
    private static final String TAG = "ShakeActivity";

    public int i;

    int SHAKE_SENSITIVITY;
    double gameF = 0;
    double maxForse = 0;
    double timeFG;

    private SensorManager sensorManager;
    private float accel = SensorManager.GRAVITY_EARTH;
    private float accelPrevious = SensorManager.GRAVITY_EARTH;

    ///////////////SHARED PREFERENCE/////////////
    public static final String APP_PREFERENCES = "My Settings";
    public static String SP_SHAKE_SENSIVITY = "";
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
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);//

    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        if (mSettings.contains(SP_SHAKE_SENSIVITY)) {
            SHAKE_SENSITIVITY = Integer.parseInt(mSettings.getString(SP_SHAKE_SENSIVITY, ""));
        }
    }
        @Override
        protected void onStop () {
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

    public void onBackPressed() {
        //do nothing here


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
}
