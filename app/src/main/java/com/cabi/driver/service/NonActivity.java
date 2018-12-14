package com.cabi.driver.service;

import android.content.Context;
import android.content.Intent;

import com.cabi.driver.utils.SessionSave;

import static com.cabi.driver.service.LocationUpdate.startLocationService;

//This class used to make the service stop/start functions easily.
public class NonActivity
{
	public Context context;

	// Constructor
	public NonActivity()
	{
	}

	public void startServicefromNonActivity(Context context)
	{

		/*if(SessionSave.getSession("shift_status", context).equals("IN")) {
			Intent intent = new Intent(context, LocationUpdate.class);
			context.startService(intent);
			}*/
		startLocationService(context);

	}

	public void stopServicefromNonActivity(Context context)
	{
		Intent intent = new Intent(context, LocationUpdate.class);
		context.stopService(intent);
	}
}
