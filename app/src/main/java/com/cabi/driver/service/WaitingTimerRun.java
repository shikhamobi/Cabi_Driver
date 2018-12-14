package com.cabi.driver.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.utils.SessionSave;

import java.util.Locale;

//This timer class used to calculate the driver waiting time based on driver below KM
// speed it will automatically start/stop.
public class WaitingTimerRun extends Service {
    public static String sTimer = "00:00:00";
    private static long startTime = 0L;
    private final Handler myHandler = new Handler();
    public static long timeInMillies = 0L;
    long timeSwap;
    public static long finalTime = 0L;
    public static long saveTime;
    private String Tag;


    public static void ClearSession(Context context) {
        timeInMillies = 0L;
        finalTime = 0L;
        startTime = 0L;
        sTimer = "00:00:00";
        SessionSave.setWaitingTime(0L, context);
        SessionSave.setDistance(0.0, context);
        SessionSave.setGoogleDistance(0f, context);
        SessionSave.saveSession("lastknowlats", "", context);
        SessionSave.saveGoogleWaypoints(null, null, "", 0.0, "", context);
        SessionSave.saveWaypoints(null, null, "", 0.0, "", context);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CommonData.speed_waiting_stop = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTime();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTime() {

        System.out.println("timer started" + SessionSave.getWaitingTime(WaitingTimerRun.this));
        startTime = SystemClock.uptimeMillis();
        timeSwap = SessionSave.getWaitingTime(WaitingTimerRun.this);
        myHandler.postDelayed(updateTimerMethod, 0);
    }
    public static void ClearSessionwithTrip(Context context) {
        System.out.println("nn--ClearSessionwithTrip");
        timeInMillies = 0L;
        finalTime = 0L;
        startTime = 0L;
        sTimer = "00:00:00";
        SessionSave.setWaitingTime(0L, context);
        SessionSave.setDistance(0.0, context);
        SessionSave.setGoogleDistance(0f, context);
        SessionSave.saveSession("status", "F", context);
        SessionSave.saveSession("travel_status", "", context);
        SessionSave.saveSession("trip_id", "", context);
        SessionSave.saveGoogleWaypoints(null, null, "", 0.0, "", context);
        SessionSave.saveWaypoints(null, null, "", 0.0, "", context);
    }

    private final Runnable updateTimerMethod = new Runnable() {
        @Override
        public void run() {

            CommonData.iswaitingrunning = true;
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;
            int seconds = (int) (finalTime / 1000);

            int minutes = seconds / 60;

            seconds = seconds % 60;
            int hour = minutes / 60;
            if (minutes >= 60) {
                minutes = minutes - (hour * 60);
            }
            sTimer = String.format(Locale.UK, "%02d", hour) + ":" + String.format(Locale.UK, "%02d", minutes)
                    + ":" + String.format(Locale.UK, "%02d", seconds);
            System.out.println("timer runing" + SessionSave.getWaitingTime(WaitingTimerRun.this) + "   " + SystemClock.uptimeMillis());
            if (finalTime != 0) {
                SessionSave.setWaitingTime(finalTime, WaitingTimerRun.this);
            }
            myHandler.postDelayed(this, 0);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonData.speed_waiting_stop = true;
        System.out.println("timer destory " + timeSwap + " finalTime " + finalTime + " timeInMillies" + timeInMillies);
        stoptime();
        System.out.println("wait destroy");
    }

    private void stoptime() {

        timeSwap += SessionSave.getWaitingTime(WaitingTimerRun.this);
        System.out.println("timer stop " + SessionSave.getWaitingTime(WaitingTimerRun.this));
        myHandler.removeCallbacks(updateTimerMethod);
    }
}