package com.emotive.smolguparser.shaketheworld.shakeit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class StatisticActivity extends Activity {

///////////////Статистические переменные(глобальные)//////////////
    public int all_shakes=0; //всего шейков за все игры
    public int all_games=0; //всего игр
//////////////////////////////////////////////////////////////////
TextView tvPlayedGames,tvAllSHakes,tvStamina,tvTime,tvSpeed;
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "My Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        initializeForm();
    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    tvPlayedGames = (TextView)findViewById(R.id.tv_PlayedGames_Number);
    tvAllSHakes = (TextView)findViewById(R.id.tv_AllShakes_Number);
        tvStamina = (TextView)findViewById(R.id.tv_BestStamina_Number);
        tvTime = (TextView)findViewById(R.id.tv_BestTime_Number);
        tvSpeed = (TextView)findViewById(R.id.tv_Speed_Number);
    }

    private void initializeForm() {

        Button btnMenu = (Button) findViewById(R.id.menu);


        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tomenu = new Intent(getApplicationContext(), StartScreen.class);
                startActivity(tomenu);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statistic, menu);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    protected void onResume(){
        super.onResume();
        if(mSettings.contains("SP_all_games")){
            tvPlayedGames.setText(""+mSettings.getInt("SP_all_games",0));}
        if(mSettings.contains("SP_all_shakes")){
            tvAllSHakes.setText(""+mSettings.getInt("SP_all_shakes",0));}
        if(mSettings.contains("SP_stamina")){
            tvStamina.setText(""+mSettings.getInt("SP_stamina",0));}
        if(mSettings.contains("SP_time")){
            tvTime.setText(""+mSettings.getInt("SP_time",0));}
        if(mSettings.contains("SP_speed")){
            tvSpeed.setText(""+mSettings.getInt("SP_speed",999));}
    }

}
