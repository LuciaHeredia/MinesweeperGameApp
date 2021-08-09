package com.example.minesweeper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.minesweeper.Logic.Game;

import static java.lang.String.valueOf;

public class GameScreen extends Activity implements  SensorServiceListener {

    public Handler handler=new Handler();
    Runnable runnable;
    public ALARM_STATE stateSensor = ALARM_STATE.OFF;
    String fineTimeTurn = "Bomb";
    SensorsService.SensorServiceBinder mBinder;
    boolean isBound = false;
    public int size_board;
    public int number_bombs;
    private static final int EASY_SIZE = 4;
    private static final int EASY_NUMBER_BOMBS = 2;
    private static final int MEDIUM_SIZE = 6;
    private static final int MEDIUM_NUMBER_BOMBS = 4;
    private static final int HARD_SIZE = 8;
    private static final int HARD_NUMBER_BOMBS = 6;
    com.example.minesweeper.Logic.Board minesweeper_board;
    TextView bombs_flags;
    TextView timer_value;
    SensorsService mService;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Bundle extras = getIntent().getExtras();
        // extras is never null because as default "EASY" level is selected
        assert extras != null;
        String level = extras.getString("level_selected"); // level selected from main

        assert level != null;
        switch (level) {
            case "MEDIUM":
                size_board = MEDIUM_SIZE;
                number_bombs = MEDIUM_NUMBER_BOMBS;
                break;
            case "HARD":
                size_board = HARD_SIZE;
                number_bombs = HARD_NUMBER_BOMBS;
                break;
            default:
                size_board = EASY_SIZE;
                number_bombs = EASY_NUMBER_BOMBS;
                break;
        }

        minesweeper_board = findViewById(R.id.minesweeperGridView);
        minesweeper_board.setNumColumns(size_board); // setting size of board

        bombs_flags = findViewById(R.id.count);
        bombs_flags.setText(valueOf(number_bombs)); // setting number of bombs on board

        timer_value = findViewById(R.id.timer); // setting timer

        Game.getInstance().settingBoard(this, size_board, number_bombs, bombs_flags, timer_value);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(isBound) {
            mBinder.startSensors();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isBound) {
            mBinder.stopSensors();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SensorsService.class);
        Log.d("On start", "binding to service...");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Service Connection", "bound to service");
            mBinder = (SensorsService.SensorServiceBinder) service;
            mBinder.registerListener(GameScreen.this);
            Log.d("Service Connection", "registered as listener");
            isBound = true;
            mBinder.startSensors();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };


    /**
     * STATE: ON
     * Every 4sec - a box gets covered.
     * Every 8sec - a bomb gets added to the board.
     * STATE: OFF
     * return to game.
     */
    @Override
    public void alarmStateChanged(ALARM_STATE state) {
        if(state == ALARM_STATE.ON){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); //lock touch screen

            if(fineTimeTurn.equals("Bomb"))
                fineTimeTurn = "Box";
            else
                fineTimeTurn = "Bomb";

            sensorFine(fineTimeTurn); // the FINE
        }
        else{ // OFF
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); //unlock touch screen
            fineTimeTurn = "Bomb"; // default
        }
    }


    public void sensorFine(String fineTimeTurn) { // execute FINEs
        onPause();

        if(fineTimeTurn.equals("Box")) {
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    Game.getInstance().sensorAddBox();
                    onResume();
                }
            }, 4000);
        }
        else{ //"Bomb"
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    Game.getInstance().sensorAddBomb();
                    onResume();
                }
            }, 4000);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}