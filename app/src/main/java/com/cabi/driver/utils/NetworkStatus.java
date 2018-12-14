package com.cabi.driver.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.R;
import com.cabi.driver.data.CommonData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This BroadcastReceiver to update network connections is Available/Not.
 */
public class NetworkStatus extends BroadcastReceiver
{
	public Context mContext;
	Context appContext;
	private String message;
	public static Dialog sDialog;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		mContext = context;
		//System.out.println("_____________netChangesssss");
		try
		{
			if (!getConnectivityStatus(mContext))
			{
				//System.out.println("_____________netChange");
				if (CommonData.sContext != null)
					isConnect(CommonData.sContext, false);

			}else{
				isConnect(CommonData.sContext, true);
			}
//			else if (CommonData.sContext != null )
//				new URLReachable(mContext).execute();
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private  class URLReachable extends AsyncTask<URL, Boolean, Boolean> {
		Context mContext;

		URLReachable(Context mContext) {
			this.mContext = mContext;
		}

		protected Boolean doInBackground(URL... urls) {
			try {
				URL url = new URL("http://google.com");   // Change to "http://google.com" for www  test.
				HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
				urlc.setConnectTimeout(10 * 1000);
				urlc.setReadTimeout(10 * 1000);
				// 10 s.

				urlc.connect();
				if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
					Log.wtf("Connection", "Success !");
					return true;
				} else {
					return false;
				}
			} catch (MalformedURLException e1) {
				return false;
			} catch (IOException e) {
				return false;
			}
			// return isURLReachable(mContext);
		}


		protected void onPostExecute(Boolean result) {
		//	System.out.println("connection_reachable " + result);
			if(mContext!=null && mContext instanceof Activity && ((Activity)mContext).getCurrentFocus()!=null)
				isConnect(mContext, result);
		}
	}


	public  void isConnect(final Context mContext, final boolean isconnect) {

		try {

			if (!isconnect) {
				if(sDialog!=null)
					sDialog.dismiss();
				sDialog=null;
				final View view = View.inflate(mContext, R.layout.netcon_lay, null);
				sDialog = new Dialog(mContext, R.style.dialogwinddow);

				try {
					Colorchange.ChangeColor((ViewGroup) view, mContext);
				} catch (Exception e) {
					e.printStackTrace();
				}
				sDialog.setContentView(view);
				sDialog.setCancelable(false);
				FontHelper.applyFont(mContext, sDialog.findViewById(R.id.alert_id));
				if(!((Activity)mContext).isFinishing())
				{
					//show dialog
					sDialog.show();
				}


				final TextView title_text = (TextView) sDialog.findViewById(R.id.title_text);
				final TextView message_text = (TextView) sDialog.findViewById(R.id.message_text);
				final Button button_success = (Button) sDialog.findViewById(R.id.button_success);
				final Button button_failure = (Button) sDialog.findViewById(R.id.button_failure);
				title_text.setText("" + NC.getResources().getString(R.string.message));
				message_text.setText("" + NC.getResources().getString(R.string.check_net_connection));
//				message_text.setText("");
				button_success.setText("" + NC.getResources().getString(R.string.c_tryagain));
				button_failure.setText("" + NC.getResources().getString(R.string.cancell));
				button_success.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View v) {
						// TODO Auto-generated method stub
						if (NetworkStatus.isOnline(mContext)) {
							sDialog.dismiss();
							if (!SessionSave.getSession("Email", mContext).equals("")) {

								Intent intent = new Intent(mContext, mContext.getClass());
								Activity activity = (Activity) mContext;
								activity.finish();
								mContext.startActivity(intent);
							}
						} else {
							Toast.makeText(mContext, NC.getResources().getString(R.string.check_net_connection), Toast.LENGTH_LONG).show();
						}


					}
				});
				button_failure.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View v) {
						// TODO Auto-generated method stub
						sDialog.dismiss();
						Activity activity = (Activity) mContext;
						activity.finish();
						final Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						activity.startActivity(intent);
					}
				});
			} else {
				try {
					if(sDialog!=null)
					sDialog.dismiss();
					sDialog=null;
					if (mContext != null) {
//						Intent intent = new Intent(mContext, mContext.getClass());
//						mContext.startActivity(intent);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}




	public static boolean isOnline(Context mContext2)
	{
		//System.out.println("_____________netChangeooo"+sDialog);
		ConnectivityManager connectivity = (ConnectivityManager) mContext2.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
		}
		return false;
	}

	public  boolean getConnectivityStatus(Context context)
	{
		boolean conn = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork)
		{
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
			{
				if (activeNetwork.isConnected())
				{
					if(context!=null)
						new URLReachable(context).execute();
					conn = true;
				}
				else
				{
					conn = false;
				}
			}
		}
		else
		{
			conn = false;
		}
		return conn;
	}
}