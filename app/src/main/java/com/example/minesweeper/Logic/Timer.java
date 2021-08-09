package com.example.minesweeper.Logic;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

public class Timer {

    private TextView timer_value;
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long updatedTime = 0L;
    private Handler timerHandler = new Handler();

    Timer(TextView timer_value) {
        this.timer_value = timer_value;
        Log.i("my_tag", "NEW CLOCK");
    }

    public void onStart() {
        this.startTime = SystemClock.uptimeMillis();
        this.timerHandler.postDelayed(updateTimerThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int secs = (int) (timeInMilliseconds / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timer_value.setText(String.format("%02d:%02d", mins, secs));
            timerHandler.postDelayed(updateTimerThread, 100);
        }
    };
}
