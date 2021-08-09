package com.example.minesweeper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

interface SensorServiceListener {

    enum ALARM_STATE {
        ON,OFF
    }

    void alarmStateChanged(ALARM_STATE state);
}

public class SensorsService extends Service implements SensorEventListener {

    public Toast toast;
    private final static String TAG = "SENSOR_SERVICE";
    private final double MAX_GAP1 = 10;
    private final double MAX_GAP2 = 2.5;
    private final double MAX_GAP3 = 3;
    private double THRESHOLDx;
    private double THRESHOLDy;
    private double THRESHOLDz;
    private SensorServiceListener.ALARM_STATE currentAlarmState = SensorServiceListener.ALARM_STATE.OFF;
    private final IBinder mBinder = new SensorServiceBinder();     // Binder given to clients

    SensorServiceListener mListener;
    SensorManager mSensorManager;
    Sensor mMagField;
    SensorEvent mFirstEvent;

    public class SensorServiceBinder extends Binder {

        void registerListener(SensorServiceListener listener) {
            Log.d("Binder", "registering...");
            mListener = listener;
        }

        void startSensors() {
            mSensorManager.registerListener(SensorsService.this,mMagField,SensorManager.SENSOR_DELAY_NORMAL);
        }

        void stopSensors() {
            mSensorManager.unregisterListener(SensorsService.this);
        }

    }

    public SensorsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if(mMagField != null) {
            Log.d("Sensors ouput" , "Accelerometer avilable");
        } else {
            Log.d("Sensors ouput" , "Accelerometer NOT Availible");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            if (mFirstEvent == null){
                mFirstEvent = event;

                // First position
                THRESHOLDx = mFirstEvent.values[0];
                THRESHOLDy = mFirstEvent.values[1];
                THRESHOLDz = mFirstEvent.values[2];

            } else {

                double absDiffX = Math.abs(Math.abs(event.values[0])-Math.abs(THRESHOLDx));
                double absDiffY = Math.abs(Math.abs(event.values[1])-Math.abs(THRESHOLDy));
                double absDiffZ = Math.abs(Math.abs(event.values[2])-Math.abs(THRESHOLDz));

                SensorServiceListener.ALARM_STATE previousState = currentAlarmState;

                if ((absDiffX >= MAX_GAP3 && absDiffY >= MAX_GAP2) ||
                        (absDiffY >= MAX_GAP1 && absDiffZ >= MAX_GAP1) ||
                        (absDiffX >= MAX_GAP1 && absDiffZ >= MAX_GAP1) ||
                        (absDiffX != 0.0 && absDiffY==0.0 && absDiffZ==0.0) ||
                        (event.values[1] == THRESHOLDy*(-1))) {
                    this.currentAlarmState = SensorServiceListener.ALARM_STATE.ON;

                    if(previousState != currentAlarmState)
                        Log.d("GAME", "LOCK STATE: " + currentAlarmState); // ON

                    mListener.alarmStateChanged(currentAlarmState);
                }
                else if(THRESHOLDx==event.values[0] &&
                        THRESHOLDy==event.values[1] &&
                        THRESHOLDz==event.values[2]){
                    this.currentAlarmState = SensorServiceListener.ALARM_STATE.OFF;
                }

                if(previousState != currentAlarmState) {
                    if (currentAlarmState == SensorServiceListener.ALARM_STATE.ON){
                        toast = Toast.makeText(SensorsService.this, "LOCKED:\nReturn phone to the beggining postion!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else{
                        Log.d("GAME", "LOCK STATE: " + currentAlarmState); // OFF
                        toast = Toast.makeText(SensorsService.this, "UNLOCKED:\nSuccesful return! Continue Game.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        mListener.alarmStateChanged(currentAlarmState);
                    }
                }

            }
        }
    }
}
