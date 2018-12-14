package com.cabi.driver.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cabi.driver.BuildConfig;
import com.cabi.driver.CanceltripAct;
import com.cabi.driver.MainActivity;
import com.cabi.driver.MyStatus;
import com.cabi.driver.NotificationAct;
import com.cabi.driver.OngoingAct;
import com.cabi.driver.R;
import com.cabi.driver.SplashAct;
import com.cabi.driver.StreetPickUpAct;
import com.cabi.driver.UserLoginAct;
import com.cabi.driver.data.CommonData;
import com.cabi.driver.interfaces.StreetPickupInterface;
import com.cabi.driver.utils.CL;
import com.cabi.driver.utils.CToast;
import com.cabi.driver.utils.LocationDb;
import com.cabi.driver.utils.LocationUtils;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.socket.client.Socket;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * getting gps status without location manager
 * This class helps to get the driver current location using location client. It
 * Keep on updating driver location to server with certain time interval (Every
 * 5sec). In this class,Driver gets the new request notification and trip cancel
 * notifications.
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class LocationUpdate extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{
    public static final double Rad = 6372.8;
    public static final String BROADCAST_ACTION = "cabi Driver";
    //public static String distanceKM = "";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public static final String LOCATION_ACCURACY_LOW = "LOW_ACCURACY";

    private static boolean ROUTE_EXPIRED_TODAY = false;
    public static double currentLatitude = 0.0;
    public static double currentLongtitude = 0.0;
    public static double currentAccuracy = 0.0;
    public static String oLocation = "";
    public static double speed;
    public static double HTotdistanceKM = 0.0;
    public static LocationUpdate instance;
    public static StreetPickupInterface streetPickupInterface;
    private static int Notification_ID = 1;
    private NotificationManager notificationManager;
    public final int UPDATE_INTERVAL_IN_TRIP = 3000;
    public ArrayList<String> wayPoint = new ArrayList<String>();
    public Location previousBestLocation = null;
    int locationUpdate = 1000 * 5;
    String historyValues = "";
    int historyCount = 0;
    Notification notification;
    LocationDb LocDB;
    int tempKMVariable = 0;
    File file;
    Intent intent;
    private ScheduledExecutorService mTimer = Executors.newSingleThreadScheduledExecutor();
    private double lastlatitude = 0.0;
    private double lastlongitude = 0.0;
    private String sLocation = "";
    private String updateLocation = "";
    public Handler mhandler;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private String Reg_ID;
    private double haverdistance = 0.0;
    private int connectionCheck = 0;
    private int avoidLatLng = 0;
    private double tempLat = 0.0;
    private double tempLng = 0.0;
    private InputStream in;
    private boolean dont_encode;
    private String bearing = "0";
    public static boolean IS_SOCKET_CONNECTED;
    LocalBroadcastManager localBroadcastManager;
   /* Runnable socketConnect = new Runnable() {
        @Override
        public void run() {
            LocationUpdateSocket.connect();
        }
    };*/
    //    @Override
//    public void onStart(final Intent intent, final int startId) {
//        mGoogleApiClient.connect();
//        super.onStart(intent, startId);
//    }
    public static Location currentLocation = null;
    //    private long locationUpdatedAt = Long.MIN_VALUE;
    private double lastlatitude1, lastlongitude1;
    private Location mLastLocation;
    public static boolean isSocket = false;
 //   public static Socket LocationUpdateSocket;

    private double slabDistance = 250;
    private boolean canCalculateDistance;
    private Handler DistanceHandler;
    private Runnable distanceRunnable;
    private long locationUpdatedAt = 0L;
    private int startID;
    private String deviceID = "";
    private boolean CONNECTION_CALL_INPROGRESS = false;
    private int versionCode = BuildConfig.VERSION_CODE;
    private final float slabAccuracy = 100f;
    private final long FREE_UPDATE_INTERVAL = 5000;
    private final long INTRIP_UPDATE_INTERVAL = 10000;
    private long UPDATE_INTERVAL = 0;
    private long TIMER_INTERVAL = 5000;
    private int errorCount = 0;
    private boolean UPDATE_LOCATION_NO_TRAFFIC = true;
    private long DELAY_DUE_TO_TRAFFIC = 10000;
    private Runnable timerRunnable;

    public static void startLocationService(Context context) {
        System.out.println("Location  ConnectionResult !");
        if (!SessionSave.getSession("Id", context).equals("") && !SessionSave.getSession(CommonData.SHIFT_OUT, context, false)) {
            if (!CommonData.serviceIsRunningInForeground(context)) {
                System.out.println("nnn---serviceIsRunningInForeground");
                Intent pushIntent1 = new Intent(context, LocationUpdate.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(pushIntent1);
                } else {
                    context.startService(pushIntent1);
                }
            }
        }
    }

  /*  public static void registerInterface(StreetPickupInterface streetPickupInterfaces) {
        streetPickupInterface = streetPickupInterfaces;
    }*/


//    public void onCreate() {
//        super.onCreate();
//
//
//
//
//    }


//    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(MyIntentService.BROADCAST_ACTION_BAZ)) {
//                final String param = intent.getStringExtra(EXTRA_PARAM_B);
//                // do something
//            }
//        }
//    };

    public static boolean isNetworkEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Location  ConnectionResult @");
        Log.e("", "Location update service started Start");
        startID = startId;
        System.out.println("startID" + startId);





        // send local broadcast

        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("Wakelock")
    @Override
    public void onCreate() {
        System.out.println("Location  ConnectionResult #");
        super.onCreate();
        this.startForeground(10, getNotification());
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mhandler = new Handler(Looper.getMainLooper());
        deviceID = Settings.Secure.getString(LocationUpdate.this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        lastlatitude1 = SessionSave.getLastLng(LocationUpdate.this).latitude;
//        lastlongitude1 = SessionSave.getLastLng(LocationUpdate.this).longitude;
        lastlatitude = SessionSave.getLastLng(LocationUpdate.this).latitude;
        lastlongitude = SessionSave.getLastLng(LocationUpdate.this).longitude;
        DistanceHandler = new Handler();
//        distanceRunnable = new Runnable() {
//            @Override
//            public void run() {
//                canCalculateDistance = true;
//            }
//        };
//        DistanceHandler.post(distanceRunnable);

        intent = new Intent(BROADCAST_ACTION);
        instance = this;
        System.out.println("Location  ConnectionResult 1");
        Log.e("", "Location update service create");

        //Get Auth
        SessionSave.saveSession(CommonData.NODE_TOKEN, "0", LocationUpdate.this);
        new NodeAuth().getAuth(LocationUpdate.this);


        LocDB = new LocationDb(LocationUpdate.this);
//        mTimer.scheduleAtFixedRate(new LocationUpdateTask(), 0, UPDATE_INTERVAL_IN_TRIP);
        /*if (mTimer != null)
            mTimer.shutdown();*/


        timerRunnable = new Runnable() {
            @Override
            public void run() {

                System.out.println("Gcmupdate ----->   " + new java.util.Date());
                UPDATE_INTERVAL += TIMER_INTERVAL;

                try {
                    UtilizeLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("NetworkStatus " + NetworkStatus.isOnline(LocationUpdate.this) + " sLocation " + sLocation + " updateLocation " + updateLocation
                        + "__" + SessionSave.getSession("Id", LocationUpdate.this) + "__" + SessionSave.getSession("shift_status", LocationUpdate.this));


                if (SessionSave.getSession("Id", LocationUpdate.this).trim().equals("") || !SessionSave.getSession("shift_status", LocationUpdate.this).equals("IN")) {
                    if (mTimer != null)
                        mTimer.shutdown();
                    stopSelf();

                } else if (!GPSEnabled(LocationUpdate.this)) {
                    mhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("nnn---mhandler");
//
                            connectionCheck = 1;
                            String message = "";
                            if (!isNetworkEnabled(LocationUpdate.this))
                                message = LocationUpdate.this.getString(R.string.location_enable);
                            else
                                message = LocationUpdate.this.getString(R.string.change_network);
                            CToast.ShowToast(LocationUpdate.this, message);
                        }
                    });
                } else {

                    if (NetworkStatus.isOnline(LocationUpdate.this) && !sLocation.equals("")) {
                        if (localBroadcastManager != null) {
                            Intent localIntent = new Intent(LOCATION_ACCURACY_LOW);
                            localIntent.putExtra("show_alert", false);
                            localBroadcastManager.sendBroadcast(localIntent);
                        }
                        if (!SessionSave.getSession("Id", LocationUpdate.this).equals("")) {
                            /**
                             * if location history throws error for 3 times continously
                             * we will wait for DELAY_DUE_TO_TRAFFIC sec to next update
                             */
                            if (errorCount >= 3 && UPDATE_LOCATION_NO_TRAFFIC) {
                                UPDATE_LOCATION_NO_TRAFFIC = false;
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorCount=0;
                                        UPDATE_LOCATION_NO_TRAFFIC = true;
                                    }
                                }, DELAY_DUE_TO_TRAFFIC);
                            }


                            if (UPDATE_LOCATION_NO_TRAFFIC) {
                                if (SessionSave.getSession("travel_status", LocationUpdate.this).equals("2")) {
                                    if (UPDATE_INTERVAL >= INTRIP_UPDATE_INTERVAL) {
                                        DriverStatusUpdate(SessionSave.getSession("Id", LocationUpdate.this), SessionSave.getSession("status", LocationUpdate.this), "");
                                        UPDATE_INTERVAL = 0;
                                    }
                                } else if (UPDATE_INTERVAL >= FREE_UPDATE_INTERVAL) {
                                    DriverStatusUpdate(SessionSave.getSession("Id", LocationUpdate.this), SessionSave.getSession("status", LocationUpdate.this), "");
                                    UPDATE_INTERVAL = 0;
                                }

                            }
                        } else {
                            SessionSave.saveSession(CommonData.LOGOUT, true, LocationUpdate.this);
//                            SessionSave.saveSession(CommonData.SHIFT_OUT, true, LocationUpdate.this);
                            Intent intent = new Intent(LocationUpdate.this, UserLoginAct.class);
                            startActivity(intent);
                            if (mTimer != null)
                                mTimer.shutdown();
                            stopSelf();

                        }

                    } else if (sLocation.equals("")) {
                        if (localBroadcastManager != null) {
                            Intent localIntent = new Intent(LOCATION_ACCURACY_LOW);
                            localIntent.putExtra("show_alert", true);
// Send local broadcast
                            localBroadcastManager.sendBroadcast(localIntent);
                        }
                    }
                }

            }
        };


        mTimer.scheduleAtFixedRate(timerRunnable, 0, TIMER_INTERVAL, TimeUnit.MILLISECONDS);
        //  mTimer.
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

        /*//Added on 24-Feb-2017
        if (startService(new Intent(this, ForegroundEnablingService.class)) == null)
            throw new RuntimeException("Couldn't find " + ForegroundEnablingService.class.getSimpleName());*/
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }


    public void cancelNotify() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    private StringBuilder inputStreamToString(final InputStream is) {
        String rLine = "";
        final StringBuilder answer = new StringBuilder();
        final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = rd.readLine()) != null)
                answer.append(rLine);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return answer;
    }


    @Override
    public void onDestroy() {
        if (localBroadcastManager != null) {
            Intent localIntent = new Intent(LOCATION_ACCURACY_LOW);
            localIntent.putExtra("show_alert", false);
            localBroadcastManager.sendBroadcast(localIntent);
        }
        try {
            Log.e("timereererere", "Location update service Stopped");
          //  removeLocationUpdates();
            if (SessionSave.getSession("Id", LocationUpdate.this).equals("")) {

                Log.e("", "Location update service Stopped");
                stopLocationUpdates();
//                if (isSocket)

            } else {
                stopLocationUpdates();
            }
            if (mTimer != null) {
                mTimer.shutdown();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//

        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(final ConnectionResult arg0) {
    }

    /**
     * Calculates the Internal distance that travel by fleet during active status
     */
    public void DistanceCalculation(Location currentLocation, LatLng to) {
        Double lastlatitude = to.latitude;
        Double lastlongitude = to.longitude;
        if (currentLocation != null && currentLocation.getSpeed() > 2 && currentLocation.hasAccuracy() && currentLocation.getAccuracy() <= slabAccuracy) {
            boolean updateLocationandReport = false;


            System.out.println("haversine 2*" + updateLocationandReport + "__" + lastlatitude1 + "__" + lastlongitude1);


            if (lastlatitude != 0.0 && lastlongitude != 0.0) {
                LocationUpdate.oLocation += currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "," + mLastLocation.getAccuracy() + "," + "," + speed + "," + SessionSave.getDistance(LocationUpdate.this) + "|";
               /* haversine(lastlatitude,
                        lastlongitude, currentLocation.getLatitude(), currentLocation.getLongitude());*/

            }

        }
    }


    public void UtilizeLocation() {
        Location location = mLastLocation;
        if (currentLatitude != 0.0 && currentLongtitude != 0.0) {
            if (SessionSave.getSession("status", LocationUpdate.this).equalsIgnoreCase("A")) {

                if (speed > 5 && location.hasAccuracy() && location.getAccuracy() <= slabAccuracy) {
                    sLocation += currentLatitude + "," + currentLongtitude + "," + location.getAccuracy() + "," + speed + "," + SessionSave.getDistance(LocationUpdate.this) + "|";
                    if (location.hasBearing())
                        bearing = String.valueOf(location.getBearing());
                            /*if (lastlatitude != 0.0 && lastlongitude != 0.0) {
                                haversine(lastlatitude, lastlongitude, currentLatitude, currentLongtitude);
                            }*/


                    if (SessionSave.getSession("travel_status", LocationUpdate.this).equalsIgnoreCase("2")
                            && canCalculateDistance) {
//                        canCalculateDistance = false;


                        if (locationUpdatedAt == 0) {
                            locationUpdatedAt = System.currentTimeMillis();

                        } else {

                            System.out.println("secondElapsed" + startID + "*" + System.currentTimeMillis() + "__" + locationUpdatedAt + "__" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - locationUpdatedAt));
                            long secondsElapsed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - locationUpdatedAt);
                            if (secondsElapsed <= 0) {
                                locationUpdatedAt = System.currentTimeMillis();
                            }
                            if (secondsElapsed >= 3) {
                                // check location accuracy here
                                locationUpdatedAt = System.currentTimeMillis();
                                if (lastlatitude != 0.0 && lastlongitude != 0.0) {
                                    boolean isLocationRepeating = false;
                                    System.out.println("calculateDistancecalled");
                                    JSONArray jsonArray = SessionSave.ReadWaypoints(LocationUpdate.this);

                                    if (!isLocationRepeating) {
                                        DistanceCalculation(location, new LatLng(lastlatitude, lastlongitude));
                                        SessionSave.saveLastLng(new LatLng(location.getLatitude(), location.getLongitude()), LocationUpdate.this);
                                        lastlatitude = location.getLatitude();
                                        lastlongitude = location.getLongitude();
                                    //    DistanceHandler.postDelayed(distanceRunnable, 3000);
                                    }
                                } else {
                                    lastlatitude = currentLatitude;
                                    lastlongitude = currentLongtitude;
                                }
                            }

                        }


                        System.out.println("GCM update --- > ll  " + SessionSave.getDistance(LocationUpdate.this) + lastlatitude);

                    } else if (!SessionSave.getSession("travel_status", LocationUpdate.this).equalsIgnoreCase("2")) {
                        lastlatitude = currentLatitude;
                        lastlongitude = currentLongtitude;
                    }
                }


            } else {
                if (SessionSave.getSession("status", LocationUpdate.this).equalsIgnoreCase("F")) {
                    if (location != null)
                        if (location.getAccuracy() <= slabAccuracy) {
                            HTotdistanceKM = 0.0;
                            sLocation = currentLatitude + "," + currentLongtitude + "|";
                            lastlatitude = currentLatitude;
                            lastlongitude = currentLongtitude;
                        } else {
                            if (lastlatitude != 0.0 && lastlongitude != 0.0) {
                                sLocation = lastlatitude + "," + lastlongitude + "|";
                                if (location.hasBearing())
                                    bearing = String.valueOf(location.getBearing());
                            }
                        }


                } else if (SessionSave.getSession("status", LocationUpdate.this).equalsIgnoreCase("B")) {
                    if (location != null) {
                        if (location.getAccuracy() <= slabAccuracy) {
                            HTotdistanceKM = 0.0;
                            sLocation = currentLatitude + "," + currentLongtitude + "|";
                            lastlatitude = currentLatitude;
                            lastlongitude = currentLongtitude;
                            if (location.hasBearing())
                                bearing = String.valueOf(location.getBearing());
                        } else {
                            if (lastlatitude != 0.0 && lastlongitude != 0.0) {
                                sLocation = lastlatitude + "," + lastlongitude + "|";
                                if (location.hasBearing())
                                    bearing = String.valueOf(location.getBearing());
                            }
                        }
                    }


                }

            }
        }
    }

    @Override
    public void onConnected(final Bundle connectionHint) {
        System.out.println("nnn---onConnected");
        try {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                avoidLatLng = 0;
                tempKMVariable = 0;
                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (servicesConnected() && mLastLocation != null && mLastLocation.hasAccuracy() && mLastLocation.getAccuracy() <= slabAccuracy) {

                    currentLatitude = mLastLocation.getLatitude();
                    currentLongtitude = mLastLocation.getLongitude();
                    sLocation = currentLatitude + "," + currentLongtitude + "|";
                    if (mLastLocation.hasBearing())
                        bearing = String.valueOf(mLastLocation.getBearing());
                } else {
                    mhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            CToast.ShowToast(LocationUpdate.this, "Gps accuracy is not good");
                        }
                    });
                }
            } else {
                mGoogleApiClient.connect();
            }

        } catch (final SecurityException e) {
            Log.e("", "mlocation exception" + e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(final Location location) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (location != null && location.getLatitude() != 0.0) {
                if (location.hasAccuracy() && location.getAccuracy() < 250 && sLocation.isEmpty()) {
                    location.setAccuracy(45.0f);
                }
                String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                double _speed = location.getSpeed();
                speed = roundDecimal(convertSpeed(_speed), 2);
                currentLatitude = location.getLatitude();
                currentLongtitude = location.getLongitude();
                currentAccuracy = location.getAccuracy();
            //    SessionSave.saveSession(CommonData.SOS_LAST_LAT, "" + currentLatitude, LocationUpdate.this);
            //    SessionSave.saveSession(CommonData.SOS_LAST_LNG, "" + currentLongtitude, LocationUpdate.this);

             //   SessionSave.saveSession(CommonData.LAST_KNOWN_LAT, "" + currentLatitude, LocationUpdate.this);
               // SessionSave.saveSession(CommonData.LAST_KNOWN_LONG, "" + currentLongtitude, LocationUpdate.this);

                mLastLocation = location;
                System.out.println("onNewLocationAvailable " + startID + "__" + mLastUpdateTime + "_" + SessionSave.getSession("status", LocationUpdate.this) + "++" + currentLatitude + "__" + speed + "__" + location.getAccuracy());
            }
        } else
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!SessionSave.getSession("Id", LocationUpdate.this).equals(""))
                        CToast.ShowToast(LocationUpdate.this, "Gps accuracy is not good");
                }
            });
    }

    private void stopLocationUpdates() {
        System.out.println("disconnect " + "stopLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

//        stopService(new Intent(this, ForegroundEnablingService.class));
       /* if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }*/
    }

    private synchronized void getAndStoreStringValues(String result) {
        try {


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(result);
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
                    NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

                }
            }
            getValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void getValueDetail() {
        Field[] fieldss = R.string.class.getDeclaredFields();
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "string", getPackageName());
            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
                NC.fields.add(fieldss[i].getName());
                NC.fields_value.add(getResources().getString(id));
                NC.fields_id.put(fieldss[i].getName(), id);

            } else {
                ////System.out.println("Imissedthepunchrefree" + fieldss[i].getName());
            }
        }


        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            NC.nfields_byID.put(NC.fields_id.get(h), NC.nfields_byName.get(h));
            // do stuff
        }

    }


    synchronized void getColorValueDetail() {
        Field[] fieldss = R.color.class.getDeclaredFields();
        // fields =new int[fieldss.length];
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "color", getPackageName());
            if (CL.nfields_byName.containsKey(fieldss[i].getName())) {
                CL.fields.add(fieldss[i].getName());
                CL.fields_value.add(getResources().getString(id));
                CL.fields_id.put(fieldss[i].getName(), id);

            } else {
            }
        }

        for (Map.Entry<String, String> entry : CL.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            CL.nfields_byID.put(CL.fields_id.get(h), CL.nfields_byName.get(h));
            // do stuff
        }

    }

    private double convertSpeed(double speed) {
        return ((speed * 3600) * 0.001);
    }

    private double roundDecimal(double value, final int decimalPlace) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        value = bd.doubleValue();
        return value;
    }


    /**
     * This Function is used for calculate the distance travelled
     */
//    public synchronized void haversine(final double lat1, final double lon1, final double lat2, final double lon2) {
//        // TODO Auto-generated method stub
//        //Getting both the coordinates
//        LatLng from = new LatLng(lat1, lon1);
//        final LatLng to = new LatLng(lat2, lon2);
//
//        System.out.println("haversine 1");
//
//        double distance = (float) SphericalUtil.computeDistanceBetween(from, to) / 1000;
//        if (SessionSave.getSession("Metric", LocationUpdate.this).trim().equalsIgnoreCase("miles"))
//            distance = distance / 1.60934;
//        System.out.println("Haversine Distance*" + (distance) + "__" + from.latitude + "__" + from.longitude + "__" + to.latitude + "__" + to.longitude);
//        if ((distance * 1000) > slabDistance) {
//            new FindApproxDistance(this).getDistance(this, from.latitude, from.longitude, to.latitude, to.longitude);
//            lastlatitude1 = 0.0;
//            lastlongitude1 = 0.0;
//        } else {
//            distance += SessionSave.getDistance(LocationUpdate.this);
//
//            SessionSave.setDistance(distance, LocationUpdate.this);
//            System.out.println("Haversine Distance**" + (distance));
//
//            SessionSave.saveSession(CommonData.LAST_KNOWN_LAT, "" + to.latitude, LocationUpdate.this);
//            SessionSave.saveSession(CommonData.LAST_KNOWN_LONG, "" + to.longitude, LocationUpdate.this);
//
//
//            SessionSave.saveWaypoints(from, to, "haversine", distance, "" + "___" + startID, LocationUpdate.this);
//
//        }
//    }


    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
   /* public void removeLocationUpdates() {
        try {
            if (LocationUpdateSocket != null) {
                LocationUpdateSocket.disconnect();
            }
            this.stopSelf();
        } catch (SecurityException unlikely) {
            // Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }*/

    @SuppressWarnings("deprecation")
    @SuppressLint("Wakelock")

    public String DriverStatusUpdate(final String id, final String status, final String gcmid) {
        String r_message = "";

        try {

            System.out.println("DriverStatusUpdate  sLocation " + sLocation);
            if (NetworkStatus.isOnline(LocationUpdate.this)) {
                updateLocation = sLocation;
                SessionSave.saveSession("Driver_locations", updateLocation, LocationUpdate.this);
                if (!SessionSave.getSession("status", LocationUpdate.this).equalsIgnoreCase("F")) {
                    sLocation = "";
                }
                ServiceGenerator.API_BASE_URL = SessionSave.getSession("base_url", LocationUpdate.this);
                if (!SessionSave.getSession("wholekey", LocationUpdate.this).trim().equals("")) {
                    if (NC.getString(R.string.ok) == null) {
                        getAndStoreStringValues(SessionSave.getSession("wholekey", LocationUpdate.this));
                        getAndStoreColorValues(SessionSave.getSession("wholekeyColor", LocationUpdate.this));
                    }
                }

                JSONObject j = new JSONObject();
                j.put("driver_id", SessionSave.getSession("Id", LocationUpdate.this));
                j.put("trip_id", SessionSave.getSession("trip_id", LocationUpdate.this));
                j.put("locations", SessionSave.getSession("Driver_locations", LocationUpdate.this).replace("null", ""));// SessionSave.getSession("Driver_locations",
                j.put("status", SessionSave.getSession("status", LocationUpdate.this));
                j.put("travel_status", SessionSave.getSession("travel_status", LocationUpdate.this));
                j.put("device_token", Settings.Secure.getString(LocationUpdate.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                j.put("device_type", "1");
                j.put("above_min_km", "" + CommonData.km_calc);
                j.put("actual_distance", SessionSave.getDistance(LocationUpdate.this) + SessionSave.getGoogleDistance(LocationUpdate.this));
                System.out.println("request json" + j + "      Start id___________" + startID);
//                new UpdateLocation(j, "type=driver_location_history");


//                CoreClient client = new ServiceGenerator(LocationUpdate.this, dont_encode).createService(CoreClient.class);
//                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), j.toString());
//
//                coreResponse = client.updateUser(ServiceGenerator.COMPANY_KEY, body, "driver_location_history", ServiceGenerator.DYNAMIC_AUTH_KEY, SessionSave.getSession("Lang", LocationUpdate.this));
                JSONObject data = new JSONObject();
                data.put("data", j);
                data.put("platform", "ANDROID");
                data.put("app", "DRIVER");
                data.put("id", SessionSave.getSession("Id", LocationUpdate.this));
                CoreClient client = null;
                if (SessionSave.getSession("travel_status", LocationUpdate.this).equals("2"))
                    client = new NodeServiceGenerator(LocationUpdate.this, dont_encode, SessionSave.getSession(CommonData.NODE_URL, LocationUpdate.this), 12).createService(CoreClient.class);
                else
                    client = new NodeServiceGenerator(LocationUpdate.this, dont_encode, SessionSave.getSession(CommonData.NODE_URL, LocationUpdate.this), 8).createService(CoreClient.class);

                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data.toString());

                Call<ResponseBody> coreResponse = client.nodeUpdate(body);
                coreResponse.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        String data = null;
                        JSONObject json = null;
                        try {

                            data = response.body().string();
                            if (data != null)
                                driverResponseHandling(new JSONObject(data));
                            else {
                                errorCount += 1;
                                updateLocation = "";
                                SessionSave.saveSession("Driver_locations", "", LocationUpdate.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            errorCount += 1;
                            updateLocation = "";
                            SessionSave.saveSession("Driver_locations", "", LocationUpdate.this);
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        updateLocation = "";
                        errorCount = +1;
                        SessionSave.saveSession("Driver_locations", "", LocationUpdate.this);

                    }
                });

            }

            SessionSave.saveSession("status", SessionSave.getSession("status", LocationUpdate.this), LocationUpdate.this);
            if (SessionSave.getSession("base_url", LocationUpdate.this).trim().equals("")) {
                ServiceGenerator.API_BASE_URL = SessionSave.getSession("base_url", LocationUpdate.this);
                getAndStoreStringValues(SessionSave.getSession("wholekey", LocationUpdate.this));
                getAndStoreColorValues(SessionSave.getSession("wholekeyColor", LocationUpdate.this));
            }
        } catch (final Exception e) {
            e.printStackTrace();

        }

        return r_message;
    }



    private void driverResponseHandling(JSONObject json) {
        //   json = new JSONObject(data);

        try {
            System.out.println("GCMM____driverResponseHandling_____" + json.getInt("status"));

            if (json.getInt("status") == 1) {
                updateLocation = "";
                errorCount = 0;
                System.out.println("ssssssres____________" + json.toString() + "__" + SessionSave.getSession("status", LocationUpdate.this) + "__" + streetPickupInterface);
             //   SessionSave.saveSession(CommonData.DRIVER_LOCATION, "", LocationUpdate.this);
                if (SessionSave.getSession("status", LocationUpdate.this).equals("A")) {
                    if (streetPickupInterface != null) {

                        final JSONObject jsons = json;
                        if (mhandler != null) {
                            mhandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        streetPickupInterface.updateFare(jsons.getString("distance"), jsons.getString("trip_fare"), mLastLocation);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    }
                    CommonData.travel_km = json.getDouble("distance");

                    try {
                        CommonData.DISTANCE_FARE = json.getString("trip_fare");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonData.travel_km = 0;
                    SessionSave.setGoogleDistance(0f, LocationUpdate.this);
                    SessionSave.setDistance(0f, LocationUpdate.this);
                    SessionSave.saveWaypoints(null, null, "", 0.0, "" + "___" + startID, LocationUpdate.this);
                    SessionSave.saveGoogleWaypoints(null, null, "", 0.0, "", LocationUpdate.this);
                    //  LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }

            } else if (json.getInt("status") == 100) {
                isSocket = false;
                /*if (LocationUpdateSocket != null) {
                    if (LocationUpdateSocket.connected()) {
                        //Toast.makeText(LocationUpdate.this, "disconnecting on 100", Toast.LENGTH_LONG).show();
                        LocationUpdateSocket.disconnect();
                    }
                }*/


            } else if (json.getInt("status") == 5) {
                Intent intent = new Intent();
                intent.putExtra("message", json.toString());
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ComponentName cn = new ComponentName(LocationUpdate.this, NotificationAct.class);
                intent.setComponent(cn);
                startActivity(intent);
            } else if (json.getInt("status") == 7 || json.getInt("status") == 10) {
                System.out.println("VVVVVVVVVVv" + json.getInt("status"));
                final JSONObject jsons = json;
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String cancelmsg = "";
                            WaitingTimerRun.ClearSessionwithTrip(LocationUpdate.this);
                            stopService(new Intent(LocationUpdate.this, WaitingTimerRun.class));

                            generateNotifications(LocationUpdate.this, jsons.getString("message"), MyStatus.class, true);
                            cancelmsg = jsons.getString("message");
                            MainActivity.mMyStatus.setStatus("F");
                            SessionSave.saveSession("status", "F", LocationUpdate.this);
                            MainActivity.mMyStatus.settripId("");
                            SessionSave.saveSession("trip_id", "", LocationUpdate.this);
                            SessionSave.setWaitingTime(0L, LocationUpdate.this);
                            MainActivity.mMyStatus.setOnstatus("");
                            MainActivity.mMyStatus.setOnPassengerImage("");
                            MainActivity.mMyStatus.setOnpassengerName("");
                            MainActivity.mMyStatus.setOndropLocation("");
                            MainActivity.mMyStatus.setOnpickupLatitude("");
                            MainActivity.mMyStatus.setOnpickupLongitude("");
                            MainActivity.mMyStatus.setOndropLatitude("");
                            MainActivity.mMyStatus.setOndropLongitude("");
                            MainActivity.mMyStatus.setOndriverLatitude("");
                            MainActivity.mMyStatus.setOndriverLongitude("");
                            Intent cancelIntent = new Intent();
                            Bundle bun = new Bundle();
                            bun.putString("message", cancelmsg);
                            cancelIntent.putExtras(bun);
                            cancelIntent.setAction(Intent.ACTION_MAIN);
                            cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            cancelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            ComponentName cn = new ComponentName(getApplicationContext(), CanceltripAct.class);
                            cancelIntent.setComponent(cn);
                            startActivity(cancelIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 100);
            } else if (json.getInt("status") == 15 || json.getInt("status") == -15) {
                System.out.println("lTaximobilityut_____" + json);
                LocationUpdate.this.stopSelf();
                int length = CommonData.mActivitylist.size();
                if (length != 0) {
                    for (int i = 0; i < length; i++) {
                        CommonData.mActivitylist.get(i).finish();
                    }
                }

                try {
                    SessionSave.saveSession("status", "", LocationUpdate.this);
                    SessionSave.saveSession("Id", "", LocationUpdate.this);
                //    SessionSave.saveSession(CommonData.DRIVER_LOCATION, "", LocationUpdate.this);
                    SessionSave.saveSession("driver_id", "", LocationUpdate.this);
                    SessionSave.saveSession("Name", "", LocationUpdate.this);
                    SessionSave.saveSession("company_id", "", LocationUpdate.this);
                    SessionSave.saveSession("bookedby", "", LocationUpdate.this);
                    SessionSave.saveSession("p_image", "", LocationUpdate.this);
                    SessionSave.saveSession("Email", "", LocationUpdate.this);
                    SessionSave.saveSession("trip_id", "", LocationUpdate.this);
                    SessionSave.saveSession("phone_number", "", LocationUpdate.this);
                    SessionSave.saveSession("driver_password", "", LocationUpdate.this);
                    SessionSave.setWaitingTime(0L, LocationUpdate.this);

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                Bundle bun = new Bundle();
                bun.putString("alert_message", json.getString("message"));
                intent.putExtras(bun);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ComponentName cn = new ComponentName(LocationUpdate.this, UserLoginAct.class);
                intent.setComponent(cn);
                startActivity(intent);
            }

            // For getting SMS From Passenger
            else if (json.getInt("status") == 11) {
                cancelNotify();
                //  System.out.println("service" + json.getString("message"));
                generateNotifications(LocationUpdate.this, json.getString("message"), OngoingAct.class, false);
                Intent ongoing = new Intent();
                Bundle extras = new Bundle();
                String lTaximobilityutlmsg = "";
                lTaximobilityutlmsg = json.getString("message");
                extras.putString("alert_message", lTaximobilityutlmsg);
                extras.putString("status", json.getString("status"));
                ongoing.putExtras(extras);
                //                        ongoing.setAction(Intent.ACTION_MAIN);
                //                        ongoing.addCategory(Intent.CATEGORY_LAUNCHER);
                ongoing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ComponentName cn = new ComponentName(LocationUpdate.this, OngoingAct.class);
                ongoing.setComponent(cn);
                getApplication().startActivity(ongoing);
            } else if (json.getInt("status") == 16) {
                LocationUpdate.this.stopSelf();
                String lTaximobilityutlmsg = "";
                generateNotifications(LocationUpdate.this, json.getString("message"), CanceltripAct.class, false);
                lTaximobilityutlmsg = json.getString("message");
                Intent intent = new Intent();
                intent.putExtra("message", lTaximobilityutlmsg);
                intent.putExtra("status", json.getInt("status"));
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ComponentName cn = new ComponentName(LocationUpdate.this, CanceltripAct.class);
                intent.setComponent(cn);
                startActivity(intent);
            } else if (json.getInt("status") == -4 || json.getInt("status") == -3) {

                updateLocation = "";
           //     SessionSave.saveSession(CommonData.DRIVER_LOCATION, "", LocationUpdate.this);
            } else if (json.getInt("status") == -1) {

                updateLocation = "";
            //    SessionSave.saveSession(CommonData.DRIVER_LOCATION, "", LocationUpdate.this);
            } else if (json.getInt("status") == -101) {
                forceLogout();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            return true;
            // Google Play services was not available for some reason
        } else
            // Display an error dialog
            return false;
    }

    public void generateNotifications(Context context, String message, Class<?> class1, boolean cancelable) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // String title = context.(CommonData.getStringModel().app_name);
        String title = message;
        Intent notificationIntent = new Intent(this, class1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        //notification.setLatestEventInfo(context, title, message, pendingIntent);

        int notifyId = 1;
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        Notification myNotication;
        Notification.Builder builder = null;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
           /* notificationChannel.setVibrationPattern();
            notificationChannel.enableVibration(true);*/
            notificationManager.createNotificationChannel(notificationChannel);

            builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentText(message)
                    .setContentTitle(title)
                    .setOngoing(true)
                    .setSmallIcon(getNotificationIcon())
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.app_icon)).getBitmap())
                    .setWhen(System.currentTimeMillis());
        } else {
            builder = new Notification.Builder(this);

            builder.setAutoCancel(true);
            builder.setTicker(getResources().getString(R.string.common_name));
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(true);
            builder.setSmallIcon(getNotificationIcon());
            builder.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.app_icon)).getBitmap());

        }

        myNotication = builder.build();

        myNotication.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(Notification_ID, myNotication);
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification1);
            r.play();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.small_logo : R.drawable.app_icon;
    }

    private boolean GPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private synchronized void getAndStoreColorValues(String result) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

            System.out.println("lislength" + nList.getLength());
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element2 = (Element) node;
                    CL.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

                }
            }
            getColorValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to logout user if status -101 and redirect to login page
     */
    private void forceLogout() {
        ServiceGenerator.API_BASE_URL = "";
        SessionSave.saveSession("base_url", "", LocationUpdate.this);
        SessionSave.saveSession("Id", "", LocationUpdate.this);
        SessionSave.clearAllSession(LocationUpdate.this);
        stopSelf();
        stopService(new Intent(this, WaitingTimerRun.class));
        Intent intent = new Intent(LocationUpdate.this, UserLoginAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


//    @Override
//    public void onDistanceCalled(LatLng pick, LatLng drop, double distance, double time, String result, String status) {
//        System.out.println("haiiiiiii " + "LocationUpdate " + pick.latitude + "__" + pick.longitude + "_____" + drop.latitude + "__" + drop.longitude + "___" + distance + "____" + status);
//        if (status.equalsIgnoreCase("OK")) {
//            SessionSave.setGoogleDistance(SessionSave.getGoogleDistance(LocationUpdate.this) + distance, LocationUpdate.this);
//            System.out.println("googledistanceee " + "2_" + pick.latitude + "__" + pick.longitude + "_____" + drop.latitude + "__" + drop.longitude);
//
//
//            SessionSave.saveSession(CommonData.LAST_KNOWN_LAT, "" + drop.latitude, LocationUpdate.this);
//            SessionSave.saveSession(CommonData.LAST_KNOWN_LONG, "" + drop.longitude, LocationUpdate.this);
//            if (SessionSave.getSession(CommonData.isGoogleDistance, LocationUpdate.this, true)) {
//                SessionSave.saveGoogleWaypoints(pick, drop, "google", distance, "" + "___" + startID, LocationUpdate.this);
//                SessionSave.saveWaypoints(pick, drop, "google", distance, "server" + "___" + startID, LocationUpdate.this);
//            } else {
//                SessionSave.saveGoogleWaypoints(pick, drop, "mapbox", distance, "" + "___" + startID, LocationUpdate.this);
//                SessionSave.saveWaypoints(pick, drop, "mapbox", distance, "server" + "___" + startID, LocationUpdate.this);
//            }
//        } else {
//            SessionSave.setGoogleDistance(distance, LocationUpdate.this);
//            System.out.println("googledistanceee " + "1_" + pick.latitude + "__" + pick.longitude + "_____" + drop.latitude + "__" + drop.longitude);
//            SessionSave.saveGoogleWaypoints(pick, drop, "haversine", distance, "UNKNOWN" + result, LocationUpdate.this);
//            SessionSave.saveSession(CommonData.LAST_KNOWN_LAT, "" + drop.latitude, LocationUpdate.this);
//            SessionSave.saveSession(CommonData.LAST_KNOWN_LONG, "" + drop.longitude, LocationUpdate.this);
//            SessionSave.saveWaypoints(pick, drop, "haversine", distance, "" + "___" + startID, LocationUpdate.this);
//        }
//    }


    private Notification getNotification() {

        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, SplashAct.class), 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        int notifyId = 10;
        Notification notification;
        Notification.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Action action = new Notification.Action.Builder(0, getString(R.string.notiy_lanch_app), activityPendingIntent).build();
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
           /* notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);*/
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .addAction(action)
                    .setContentText(getString(R.string.app_running))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.small_logo)
                    .setWhen(System.currentTimeMillis());
        } else {
            builder = new Notification.Builder(this)
                    .addAction(0, getString(R.string.notiy_lanch_app) + ""/* + getTripStatus()*/,
                            activityPendingIntent)
                    .setContentText(getString(R.string.app_running))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setOngoing(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.small_logo)
                    .setWhen(System.currentTimeMillis());
        }

        notification = builder.build();
        notificationManager.notify(notifyId, notification);
        return notification;
    }

}

