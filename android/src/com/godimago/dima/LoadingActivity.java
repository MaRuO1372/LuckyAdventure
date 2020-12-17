package com.godimago.dima;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {

    private TimerTask timerTask;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, AndroidLauncher.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(timerTask, 4000);
    }
}