package com.cabi.driver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.cabi.driver.MainActivity;
import com.cabi.driver.OngoingAct;
import com.cabi.driver.R;
import com.cabi.driver.utils.NC;

import java.util.Locale;

//This timer class used to calculate the driver waiting time based on user requirement.Driver can start/stop it.
public class TimerRun extends Service
{
	public static String sTimer = "00:00:00";
	private long startTime = 0L;
	private final Handler myHandler = new Handler();
	long timeInMillies = 0L;
	long timeSwap = 0L;
	long finalTime = 0L;
	private String Tag;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		startTime();
		return super.onStartCommand(intent, flags, startId);
	}

	private void startTime()
	{
		OngoingAct.WaitingTxt.setTag("stop");
		OngoingAct.WaitingTxt.setText("" + NC.getResources().getString(R.string.stop_waiting));
		startTime = SystemClock.uptimeMillis();
		timeSwap = MainActivity.mMyStatus.getsaveTime();
		myHandler.postDelayed(updateTimerMethod, 0);
	}

	private final Runnable updateTimerMethod = new Runnable()
	{
		@Override
		public void run()
		{
			timeInMillies = SystemClock.uptimeMillis() - startTime;
			finalTime = timeSwap + timeInMillies;
			int seconds = (int) (finalTime / 1000);
			int minutes = seconds / 60;
			seconds = seconds % 60;
			int hour = minutes / 60;
			sTimer = String.format(Locale.UK, "%02d", hour) + ":" + String.format(Locale.UK, "%02d", minutes) + ":" + String.format(Locale.UK, "%02d", seconds);
			myHandler.postDelayed(this, 0);
		}
	};

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		stoptime();
		MainActivity.mMyStatus.setsaveTime(timeSwap);
	}

	private void stoptime()
	{
		OngoingAct.WaitingTxt.setTag("start");
		OngoingAct.WaitingTxt.setText("" + NC.getResources().getString(R.string.start_waiting));
		timeSwap += timeInMillies;
		myHandler.removeCallbacks(updateTimerMethod);
	}
}