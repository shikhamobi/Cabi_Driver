package com.cabi.driver.data;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;

import com.cabi.driver.service.LocationUpdate;
import com.cabi.driver.service.WaitingTimerRun;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

//Class for common variable which used in entire project.
public class CommonData {
    public static final String PASSENGER_LANGUAGE_TIME = "passenger_lang";
    public static final String PASSENGER_COLOR_TIME = "driver_lang";
    public static String DISTANCE_FARE = "";
    public static Vector<SearchListData> SEARCH_LIST_ITEM = new Vector<>();
    public static String companykey = "RH7PVsKE18qGe6Y6YOC8kdRntOEyDnD0uW";
    public static final String DOMAIN_URL = "http://live.taximobility.com/mobileapi117/index/";

    //  http://www.taximobility.com/mobileapi116/index/dGF4aV9hbGw/?type=getcoreconfig
    // live
    //  public static String BasePath = "http://www.taximobility.com/mobileapi116/index/dGF4aV9hbGw/?";
    //public static String BasePath = "http://192.168.1.77:1038/mobileapi116/index/dGF4aV9hbGw/?";
    // public static String BasePath = "http://newpack.taximobility.com/mobileapi116/index/dGF4aV9hbGw=/?type=getcoreconfig";
    //public static String BasePath = "http://192.168.1.169:1007/mobileapi115/index/dGF4aV9hbGw=/?";
    // public static String BasePath = "http://192.168.1.116:1027/mobileapi116/index/dGF4aV9hbGw=/?";
    //  public static String BasePath = "http://182.72.62.190:1020/mobileapi115/index/dGF4aV9hbGw/?";
    // public static String BasePath = "http://www.taximobility.com/mobileapi116/index/dGF4aV9hbGw/?";
    // QA url
    //http://192.168.1.47:1009/mobileapi117/index/dGF4aV9hbGw/?

    //  public static String BasePath = "http://testtaxi.know3.com/mobileapi117/index/dGF4aV9hbGw=/?";

    //http://testingtaxi.taximobility.com/mobileapi117/index/dGF4aV9hbGw=/?type=getcoreconfig
    // public static String BasePath = "http://192.168.1.169:1009/mobileapi117/index/dGF4aV9hbGw=/?";

    //public static String BasePath = "http://testingtaxi.taximobility.com/mobileapi117/index/dGF4aV9hbGw=/?";


    //   public static String BasePath = "http://192.168.1.115:1023/mobileapi117/index/dGF4aV9hbGw=/?";
    // Local url
    //public static String BasePath = "http://192.168.1.169:1000/mobileapi116/index/dGF4aV9hbGw=/?";
    public static String mDevice_id;
    public static double getlatitude = 0.0;
    public static double getlongitude = 0.0;
    public static final String DRIVER_LOCATION_STATIC = "driver_location";
    public static double last_getlatitude = 0.0;
    public static double last_getlongitude = 0.0;
    public static double travel_km = 0.0;
    public static String hstravel_km = "";
    public static String current_act;
    public static String currentspeed = "";
    public static boolean iswaitingrunning;
    public static boolean speed_waiting_stop = true;
    public static int km_calc = 0;
    public static final String SHIFT_OUT = "shift_out";
    public static final String LOGOUT = "log_out";
    public static Context sContext;
    public static ArrayList<Activity> mActivitylist = new ArrayList<Activity>();
    public static String METRIC = "";
    public static int LocationResult = 420;
    public static  String amount = " ";

    public static final String NODE_URL = "node_url";
    public static final String NODE_DOMAIN = "node_domain";
    public static final String NODE_TOKEN = "node_token";

    public static String encodeToBase64() {

        String live_key = "ntaxi_" + companykey;
        String encodedString = "";
        try {
            byte[] byteData = null;
            if (Build.VERSION.SDK_INT >= 8) {
                byteData = android.util.Base64.encode(live_key.getBytes(), android.util.Base64.DEFAULT);
            }
            encodedString = new String(byteData);
        } catch (Exception e) {
          //  Log.i("Exception ", "" + e);
        }
        try {
            encodedString = URLEncoder.encode(encodedString, "utf-8");
            encodedString = encodedString.replace("%0A", "");
        } catch (UnsupportedEncodingException e) {
          //  Log.i("Conversion ", "" + e.toString());
        }
        return encodedString;
    }


    public static void closeDialog(Dialog dialog) {
        if (dialog != null)
            if (dialog.isShowing())
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }


    /*
     * method to clear the cache. input-context
     */
    public static void trimCache(Context context) {

        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {

        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }
    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public static boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (LocationUpdate.class.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean serviceWaitingIsRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (WaitingTimerRun.class.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isCurrentTimeZone(long s) {
        long dateInMillis = System.currentTimeMillis() / 1000;
        //System.out.println("____________LLL" + s + "__" + dateInMillis + "__" + TimeUnit.MILLISECONDS.toSeconds(Math.abs(s - dateInMillis)));
        return TimeUnit.MILLISECONDS.toSeconds(Math.abs(s - dateInMillis)) < 24;
    }
}