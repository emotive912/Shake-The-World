package com.emotive.smolguparser.shaketheworld.shakeit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FInishActivity extends Activity {

    //String res = getString(R.string.my_result);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Intent i = getIntent();
        TextView tvresult = (TextView) findViewById(R.id.textResult);
        String count = i.getStringExtra("count");
        tvresult.setText(count);
        AllButtons();
    }

    public void AllButtons() {
        Button btnstat = (Button) findViewById(R.id.buttonToStat);
        Button btntoShare = (Button) findViewById(R.id.sharing);
        Button onemorebtn = (Button) findViewById(R.id.button_onemore);


        //

        btnstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(getApplicationContext(), StatisticActivity.class);
                startActivity(b);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btntoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.my_result));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        onemorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(getApplicationContext(), GameModes.class);
                startActivity(c);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finish, menu);
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

    public void onBackPressed() {
        //never return the true
        Intent a = new Intent(getApplicationContext(), StartScreen.class);
        startActivity(a);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
