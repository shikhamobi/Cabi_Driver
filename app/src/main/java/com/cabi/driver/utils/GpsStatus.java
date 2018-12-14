package com.cabi.driver.utils;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.cabi.driver.MainActivity;
import com.cabi.driver.data.CommonData;

/**
 * This class is used to get the network status when it is enable/disable.
 */
public class GpsStatus extends BroadcastReceiver
{
	public Context mContext;
	Context appContext;
	private String message;
	public static Dialog mDialog;
	public static int count = 0;
	public static int ischecked =1;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		mContext = context;
		try
		{
			if (!isGpsEnabled(mContext))
			{
				if (count == 0)
				{
					message = " Gps connection is Disable!!";
					if (!CommonData.current_act.equals("SplashAct"))
					{
						if (CommonData.sContext != null)
							MainActivity.gpsalert(CommonData.sContext, false);
					}
				}
				count++;
			}
			else
			{
				count = 0;
				ischecked =0;
				message = " Gps connection is Enable!!";
				if(CommonData.current_act!=null)
				if (!CommonData.current_act.equals("SplashAct"))
				{
					if (CommonData.sContext != null)
						MainActivity.gpsalert(CommonData.sContext, true);
				}
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean isGpsEnabled(Context context)
	{
		LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return true;
		else
			return false;
	}
}
