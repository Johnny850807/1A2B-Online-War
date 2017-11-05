package com.ood.clean.waterball.lifecounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


public class CountActivity extends AppCompatActivity implements Runnable{
    private int nowAge;
    private int targetAge;
    private SharedPreferences sp;
    private Handler handler = new Handler();
    private TextView countTxt;
    private long initTime;
    private boolean alive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        countTxt = (TextView) findViewById(R.id.countTxt);

        Intent intent = getIntent();
        nowAge = Integer.parseInt(intent.getStringExtra("nowAge"));
        targetAge = Integer.parseInt(intent.getStringExtra("targetAge"));

        sp = getSharedPreferences("LIFECOUNT", MODE_PRIVATE);
        sp.edit().putInt("nowAge", nowAge).putInt("targetAge", targetAge).apply();

        Log.d("count", "Now age : " + nowAge + ", target age : " + targetAge + ".");

        countTime();
        handler.postDelayed(this, 1000);
    }

    private void countTime(){
        initTime = (targetAge - nowAge) * 365 * 24 * 60 * 60 * 1000;

        countTxt.setText(String.valueOf(initTime));
    }

    @Override
    public void run() {
        while(alive)
        {
            try {
                Thread.sleep(1000);
                initTime -= 1000;
                countTxt.setText(String.valueOf(initTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alive = false;
    }
}
