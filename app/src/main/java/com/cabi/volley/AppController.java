//package com.Taximobility.volley;
//
//import org.acra.annotation.ReportsCrashes;
//
//import android.app.Application;
//import android.text.TextUtils;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;
//
///**
// * This class is for get the crash report from the app to registered mail id by using ACRA library.
// *
// */
//@ReportsCrashes(formKey = "0B4GNhomjO7lfUkxpbkc0OUtZUlk", mailTo = "harishankar.k@ndot.in")
//public class AppController extends Application
//{
//	public static final String TAG = AppController.class.getSimpleName();
//	private RequestQueue mRequestQueue;
//	private static AppController mInstance;
//
//	@Override
//	public void onCreate()
//	{
//		super.onCreate();
////		ACRA.init(this);
//		mInstance = this;
//	}
//
//	public static synchronized AppController getInstance()
//	{
//		return mInstance;
//	}
//
//	public RequestQueue getRequestQueue()
//	{
//		if (mRequestQueue == null)
//		{
//			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//		}
//		return mRequestQueue;
//	}
//
//	public <T> void addToRequestQueue(Request<T> req, String tag)
//	{
//		// set the default tag if tag is empty
//		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//		getRequestQueue().add(req);
//	}
//
//	public <T> void addToRequestQueue(Request<T> req)
//	{
//		req.setTag(TAG);
//		getRequestQueue().add(req);
//	}
//
//	public void cancelPendingRequests(Object tag)
//	{
//		if (mRequestQueue != null)
//		{
//			mRequestQueue.cancelAll(tag);
//		}
//	}
//}
