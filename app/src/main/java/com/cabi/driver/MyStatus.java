package com.cabi.driver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cabi.driver.data.CommonData;
import com.cabi.driver.data.MapWrapperLayout;
import com.cabi.driver.earningchart.EarningsAct;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.LocationUpdate;
import com.cabi.driver.service.NonActivity;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.LatLngInterpolator;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;

import com.cabi.driver.utils.PaymentCompleted;
import com.cabi.driver.utils.SessionSave;
import com.cabi.driver.utils.drawable_program.Drawables_program;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author developer
 */

/**
 * This class is home page where you can select other menus from here
 */
public class MyStatus extends MainActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    public static boolean GEOCODE_EXPIRY = false;
    public static String CURRENT_COUNTRY_CODE = BuildConfig.CURRENT_COUNTRY_CODE;
    // Class members declarations.
    public static GoogleMap googleMap;
    private Marker c_marker;
    private TextView slider;
    public static MyStatus mystatus;
    private TextView currentLocation1, headerTxt;
    private TextView curlocation;
    private int mapstatus;
    private MapWrapperLayout mapWrapperLayout;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 5000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 200; // 10 meters
    private Location mLastLocation;
    private LatLng previousLatLong;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    LatLng coordinate;
    // Animate marker
//    ArrayList<LatLng> listPoint = new ArrayList<LatLng>();
//    ArrayList<LatLng> savedpoint = new ArrayList<LatLng>();
    ArrayList<String> savedlocation = new ArrayList<String>();
    ArrayList<LatLng> _trips = new ArrayList<LatLng>();
    Marker _marker;
    LatLngInterpolator _latLngInterpolator = new LatLngInterpolator.Spherical();
    TextView sendEmail;
    private boolean animStarted = false;
    private boolean animLocation = false;
    float bearing;
    LatLng savedLatLng = null;
    // private double speed = 0.0;
    private float zoom = 16f;
    private TextView Trip_history;
    private TextView third_tripamt, first_tripdate, first_tripmodel, first_tripamt, second_tripdate, second_tripmodel, second_tripamt, third_tripdate, third_tripmodel;
    private LinearLayout trip_detailslay;
    private LinearLayout first_lay, second_lay, third_lay;
    private LinearLayout home_lay, earnings_lay, profile_lay, streetpick_lay;
    private ImageView home_iv;
    private TextView lasttripheader;
    private TextView btn_shift;
    String checked = "OUT";
    NonActivity nonactiityobj = new NonActivity();
    private Bundle alert_bundle;
    private String alert_msg;

    Bitmap theBitmap = null;
    Bitmap bm = null;
    int height = 60;
    int width = 100;
    private Dialog errorDialog;
    private int HighAccuracyCount = 0;
    private CountDownTimer countDownTimer;
    private boolean doubleBackToExitPressedOnce;

    /**
     * Set the layout to activity.
     */
    @Override
    public int setLayout() {
        mapstatus = 0;
        setLocale();
        return R.layout.mystatus_lay;
    }

    private String getAccuracyText() {
        if (HighAccuracyCount < 3)
            return "High Accuracy " + mLastLocation.getAccuracy();
        else if (HighAccuracyCount < 7)
            return "Low Accuracy " + mLastLocation.getAccuracy();
        return " no accuracy";
    }

    /**
     * Initialize the views on layout
     */
    @Override
    public void Initialize() {
        showLoading(MyStatus.this);
        Point pointSize = new Point();
        //   System.out.println("__________hi" + pointSize.x);
        getWindowManager().getDefaultDisplay().getSize(pointSize);
        //  System.out.println("__________bye" + pointSize.x);
        height = pointSize.x / 6;
        width = pointSize.x / 9;
        CommonData.mActivitylist.add(this);
        CommonData.sContext = this;
        mapstatus = 0;
        CommonData.current_act = "MyStatus";

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) MyStatus.this
                .findViewById(android.R.id.content)).getChildAt(0)), MyStatus.this);


        try {
            alert_bundle = getIntent().getExtras();
            if (alert_bundle != null) {
                alert_msg = alert_bundle.getString("alert_message");
                //       System.err.println("oncreate called....." + alert_msg);
            }
            if (alert_msg != null && alert_msg.length() != 0)
                alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
        } catch (Exception e) {
            e.printStackTrace();
        }


        FontHelper.applyFont(this, findViewById(R.id.mystatus_layout));
        if (servicesConnected()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
            startLocationUpdates();
        }
        try {
            GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
            //  googleMap = mapFrag.getMap();

            // System.gc();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
          /*  Intent intent = getIntent();
            finish();
            startActivity(intent);*/
        }
        ImageView mov_cur_loc = (ImageView) findViewById(R.id.mov_cur_loc);
        mov_cur_loc.setVisibility(View.VISIBLE);
        Glide.with(this).load(SessionSave.getSession("image_path", this) + "currentLocation.png").placeholder(R.drawable.mapmove).error(R.drawable.mapmove).into((ImageView) findViewById(R.id.mov_cur_loc));
        mov_cur_loc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startLocationUpdates();
                    if (mLastLocation == null) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    }
                    if (mLastLocation != null) {
                        coordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));
                    }
                    //       System.out.println("__________________LLLLLLLppppm" + mLastLocation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        currentLocation1 = (TextView) findViewById(R.id.currentlocation);
        Trip_history = (TextView) findViewById(R.id.trip_history_header);

        Trip_history.setText(Html.fromHtml("<u>" + NC.getResources().getString(R.string.trip_history) + "</u>"));

        btn_shift = (TextView) findViewById(R.id.btn_shift);
        lasttripheader = (TextView) findViewById(R.id.lasttrip_header);
        first_tripdate = (TextView) findViewById(R.id.first_tripdate);
        first_tripmodel = (TextView) findViewById(R.id.first_tripmodel);
        first_tripamt = (TextView) findViewById(R.id.first_tripamt);
        second_tripdate = (TextView) findViewById(R.id.second_tripdate);
        second_tripmodel = (TextView) findViewById(R.id.second_tripmodel);
        second_tripamt = (TextView) findViewById(R.id.second_tripamt);
        third_tripdate = (TextView) findViewById(R.id.third_tripdate);
        third_tripmodel = (TextView) findViewById(R.id.third_tripmodel);
        third_tripamt = (TextView) findViewById(R.id.third_tripamt);
        trip_detailslay = (LinearLayout) findViewById(R.id.trip_detailslay);
        second_lay = (LinearLayout) findViewById(R.id.second_lay);
        first_lay = (LinearLayout) findViewById(R.id.firstlay);
        third_lay = (LinearLayout) findViewById(R.id.third_lay);
        ImageView headerlogo = (ImageView) findViewById(R.id.headicon);
        headerlogo.setVisibility(View.VISIBLE);

        // Glide.with(this).load(SessionSave.getSession("image_path", this) + "headerLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.headicon));

        // Log.e("_imagepath_", SessionSave.getSession("image_path", this));

        Trip_history.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyStatus.this, TripHistoryAct.class);
                startActivity(intent);
                //      finish();
            }
        });

        home_iv = (ImageView) findViewById(R.id.home_iv);
        home_iv.setImageResource(R.drawable.home_focus);
        Glide.with(this).load(SessionSave.getSession("image_path", this) + "homeFocus.png").error(R.drawable.home_focus).into((ImageView) findViewById(R.id.home_iv));
        home_lay = (LinearLayout) findViewById(R.id.home_lay);
        earnings_lay = (LinearLayout) findViewById(R.id.earnings_lay);
        profile_lay = (LinearLayout) findViewById(R.id.profile_lay);
        streetpick_lay = (LinearLayout) findViewById(R.id.streetpick_lay);


        earnings_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyStatus.this, EarningsAct.class);
                startActivity(intent);
                finish();

            }
        });

        home_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                                Intent intent = new Intent(MyStatus.this, MyStatus.class);
//                startActivity(intent);
//                finish();
            }
        });

        profile_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyStatus.this, MeAct.class);
                startActivity(intent);
                finish();
            }
        });

        streetpick_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyStatus.this, StreetPickUpAct.class);
                startActivity(intent);
                finish();
            }
        });

        btn_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_shift.setClickable(false);
                new RequestingCheckBox();
            }
        });


        FontHelper.applyFont(this, curlocation);
        slider = (TextView) findViewById(R.id.backup);
        headerTxt = (TextView) findViewById(R.id.headerTxt);
        headerTxt.setText("" + NC.getResources().getString(R.string.my_status));
        headerTxt.setVisibility(View.GONE);

        //  http://192.168.1.169:1009/mobileapi117/index/dGF4aV9hbGw/?lang=en&type=driver_recent_trip_list


        //Getting recent trip details
        try {
            JSONObject j = new JSONObject();
            j.put("driver_id", SessionSave.getSession("Id", MyStatus.this));
            // j.put("driver_id", "1531");
            String pro_url = "type=driver_recent_trip_list";
            new GetTripData(pro_url, j);
        } catch (Exception e) {

        }
        // GetTripData()

        // Close this activity.
        slider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // menu.toggle();
                if (c_marker != null) {
                    c_marker = null;
                }

                showLoading(MyStatus.this);
                finish();
            }
        });


        //Showing driver shift status
        if (SessionSave.getSession("shift_status", MyStatus.this).equals("IN")) {

            Drawables_program.shift_on(btn_shift);
            btn_shift.setText(NC.getString(R.string.online));
            SessionSave.saveSession(CommonData.SHIFT_OUT, false, MyStatus.this);
            nonactiityobj.startServicefromNonActivity(MyStatus.this);

        } else {

            Drawables_program.shift_bg_grey(btn_shift);
            btn_shift.setText(NC.getString(R.string.offline));
            SessionSave.saveSession(CommonData.SHIFT_OUT, true, MyStatus.this);
            stopService(new Intent(MyStatus.this, LocationUpdate.class));

        }

        //curlocation.setTypeface(setcontentTypeface(), Typeface.BOLD);
    }


    /**
     * Get the google map pixels from xml density independent pixel.
     */
    public static int getPixelsFromDp(final Context context, final float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(final ConnectionResult arg0) {
    }

    @Override
    protected void onPause() {
        //    System.out.println("pauseCalled");
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        stopLocationUpdates();
        startLocationUpdates();
        if (mLastLocation == null) {
            countDownTimer = new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //          System.out.println("seconds remaining: " + millisUntilFinished / 1000);
                    try {
                        if (mLastLocation != null && countDownTimer != null)
                            countDownTimer.cancel();


                        if (mLastLocation == null && MyStatus.this != null && (millisUntilFinished < 15000 && millisUntilFinished > 14000)) {

                            //latLongAlert("We are trying to get your location in low accuracy", null);
                            if (mLocationRequest.getPriority() != LocationRequest.PRIORITY_LOW_POWER) {
                                stopLocationUpdates();
                                mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                                startLocationUpdates();
                            } else {
                                //latLongAlert("Sorry we can't get your current location", null);
                                Toast.makeText(MyStatus.this, getString(R.string.address_cant_fetch), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(MyStatus.this, getString(R.string.getting_gps_low), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MyStatus.this, getString(R.string.address_cant_fetch), Toast.LENGTH_SHORT).show();
                    }


                }

                public void onFinish() {
                    if (mLastLocation == null && MyStatus.this != null) {

                        latLongAlert("Sorry we can't get your current location", null);

                    }
                }
            }.start();
        }
        //   Glide.with(this).load(SessionSave.getSession("image_path", this) + "headerLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.headicon));

        //Setting driver shift status
        if (SessionSave.getSession("shift_status", MyStatus.this).equals("IN")) {

            Drawables_program.shift_on(btn_shift);
            btn_shift.setText(NC.getString(R.string.online));

           nonactiityobj.startServicefromNonActivity(MyStatus.this);

        } else {

            Drawables_program.shift_bg_grey(btn_shift);
            btn_shift.setText(NC.getString(R.string.offline));

            stopService(new Intent(MyStatus.this, LocationUpdate.class));
        }

        try {
            JSONObject j = new JSONObject();
            j.put("driver_id", SessionSave.getSession("Id", MyStatus.this));
            // j.put("driver_id", "1531");
            String pro_url = "type=driver_recent_trip_list";
            new GetTripData(pro_url, j);
        } catch (Exception e) {

        }

        if (PaymentCompleted.tripcompleted) {
            alert_view(MyStatus.this, "" + "", "" + "payment has been processed successfully", "" + NC.getResources().getString(R.string.ok), "");
            PaymentCompleted.tripcompleted = false;
        }


    }


    int templength = 0;

    /**
     * This method will be called once map ready
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(false);

        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        MapsInitializer.initialize(MyStatus.this);
        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        mapWrapperLayout.init(googleMap, getPixelsFromDp(MyStatus.this, 39 + 20));
        setMap();
    }

    /**
     * @API call(get method) to get the driver trip data and parsing the response
     */
    private class GetTripData implements APIResult {
        public GetTripData(String url, JSONObject data) {

            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(MyStatus.this, this, data, false).execute(url);
                } else {
                    alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            try {

                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        JSONArray details = json.getJSONArray("trip_list");

                        templength = details.length();

                        if (templength > 0) {
                            if (templength == 1) {
                                first_lay.setVisibility(View.VISIBLE);
                                second_lay.setVisibility(View.GONE);
                                third_lay.setVisibility(View.GONE);

                                lasttripheader.setText(NC.getString(R.string.Lasttrip));

                            } else if (templength == 2) {
                                first_lay.setVisibility(View.VISIBLE);
                                second_lay.setVisibility(View.VISIBLE);
                                third_lay.setVisibility(View.GONE);

                                lasttripheader.setText(NC.getString(R.string.Last2trip));
                            } else if (templength == 3) {
                                first_lay.setVisibility(View.VISIBLE);
                                second_lay.setVisibility(View.VISIBLE);
                                third_lay.setVisibility(View.VISIBLE);

                                lasttripheader.setText(NC.getString(R.string.Last3trip));
                            } else {
                                first_lay.setVisibility(View.VISIBLE);
                                second_lay.setVisibility(View.VISIBLE);
                                third_lay.setVisibility(View.VISIBLE);

                                lasttripheader.setText(NC.getString(R.string.Last3trip));
                            }


                            for (int i = 0; i < templength; i++) {
                                if (i == 0) {
                                    first_tripdate.setText(details.getJSONObject(i).getString("drop_time").trim());
                                    first_tripmodel.setText(details.getJSONObject(i).getString("model_name").trim());
                                    first_tripamt.setText(SessionSave.getSession("site_currency", MyStatus.this) + " " + details.getJSONObject(i).getString("fare").trim());
                                }
                                if (i == 1) {
                                    second_tripdate.setText(details.getJSONObject(i).getString("drop_time").trim());
                                    second_tripmodel.setText(details.getJSONObject(i).getString("model_name").trim());
                                    second_tripamt.setText(SessionSave.getSession("site_currency", MyStatus.this) + " " + details.getJSONObject(i).getString("fare").trim());
                                }
                                if (i == 2) {
                                    third_tripdate.setText(details.getJSONObject(i).getString("drop_time").trim());
                                    third_tripmodel.setText(details.getJSONObject(i).getString("model_name").trim());
                                    third_tripamt.setText(SessionSave.getSession("site_currency", MyStatus.this) + " " + details.getJSONObject(i).getString("fare").trim());
                                }
                            }


                        } else {
                            trip_detailslay.setVisibility(View.GONE);
                        }

                    } else {
                        trip_detailslay.setVisibility(View.GONE);

                    }
                } else {
                    //alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(MyStatus.this, getString(R.string.server_error));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        try {
            if (mGoogleApiClient != null && mLocationRequest != null && mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // prakash@abinfosoft.com

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void getaddressfrom() {
        //   System.out.println("positionFirst--");
        Geocoder geocoder = new Geocoder(MyStatus.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
            Address obj = addresses.get(0);
            //    Log.v("IGA", "Address  " + obj.getCountryCode() + "  ISO CODE   ");
            SessionSave.saveSession("country_name", "" + obj.getCountryName(), MyStatus.this);
            SessionSave.saveSession("country_names", "" + obj.getCountryCode(), MyStatus.this);
            CURRENT_COUNTRY_CODE = obj.getCountryCode();
            //   System.out.println("-----" + CURRENT_COUNTRY_CODE);
            //  System.out.println("-------" + SessionSave.getSession("country_names", MyStatus.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // When connect with location client the following function get the current lat/lng and update the UI.
    @Override
    public void onConnected(Bundle connectionHint) {
        try {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLastLocation == null && MyStatus.this != null) {
                        try {
                            errorInSplash(getString(R.string.error_in_getting_gps));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 30000);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18));
                setMap();

            }
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.2006, 92.9376), zoom));
            startLocationUpdates();
            //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (servicesConnected()) {
                if (isOnline()) {
                    if (mLastLocation != null) {
                        coordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                        SessionSave.saveSession("PLAT", "" + mLastLocation.getLatitude(), MyStatus.this);
                        SessionSave.saveSession("PLNG", "" + mLastLocation.getLongitude(), MyStatus.this);
                        // getCurrentAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        new Address_s(this, new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).execute();
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));
                        getaddressfrom();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method for check the google service available or not.
     */
    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            return false;
        }
    }

    // Function get the current lat/lng and update the UI onlocationchange.
    @Override
    public void onLocationChanged(Location location) {
        //    System.out.println("__________________LLLLLLLpppp" + location);
        if (MyStatus.this != null && location != null) {
            //     if (HighAccuracyCount != 0)
            mLastLocation = location;

            //     System.out.println("__________________LLLLLLLpppps" + location.getAccuracy());
            try {
                if (previousLatLong != null) {
                    if (!(previousLatLong.latitude == location.getLatitude() && previousLatLong.longitude == location.getLongitude())) {
//                        if (HighAccuracyCount != 500)
//                            latLongAlert("We get location with " + getAccuracyText(), null);
                        //  HighAccuracyCount = 500;
                        setMap();
                    }
                } else {
//                    if (HighAccuracyCount != 500)
//                        latLongAlert("We get location with " + getAccuracyText(), null);
                    //  HighAccuracyCount = 500;
                    setMap();
                }
                previousLatLong = new LatLng(location.getLatitude(), location.getLongitude());

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(previousLatLong, 18));
                mapWrapperLayout.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                mapWrapperLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
    }


    /**
     * setting location movement
     */
    public void setMap() {
        try {


//        bearing = mLastLocation.getBearing();
//        if (bearing >= 0)
//            bearing = bearing + 90;
//        else
//            bearing = bearing - 90;

            //  speed = roundDecimal(convertSpeed(mLastLocation.getSpeed()), 2);
            // zoom = googleMap.getCameraPosition().zoom;
            if (googleMap != null && mLastLocation != null && MyStatus.this != null) {
                mapWrapperLayout.setVisibility(View.VISIBLE);
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                if (!animLocation) {
//                    listPoint.add(latLng);
//                } else {
//                    savedpoint.add(latLng);
//                }
//
//                System.out.println("listPoint size" + listPoint.size());
                if (true) {

                    if (c_marker != null)
                        c_marker.remove();
                    if (googleMap != null)
                        googleMap.clear();
//                if (!animStarted) {
//
//                    animStarted = true;
//                    animLocation = true;
//                    if (savedLatLng != null) {
//                        listPoint.set(0, savedLatLng);
//                        System.out.println("savedLatLng animation fst" + listPoint.get(0));
//                    }

            /*Bitmap theBitmap = Glide.
                    with(this).
                    load(SessionSave.getSession("image_path",this)+"setPickupPin.png").
                    asBitmap().
                    into(100, 100). // Width and height
                    get();*/


                    final LatLng templatlon = latLng;
                    new AsyncTask<String, Void, Void>() {


                        @Override
                        protected Void doInBackground(String... params) {
                            String TAG = "Error Message: ";
                            try {


                                //          System.out.println("sjdgfjsdfjsdfjdhfj" + SessionSave.getSession("image_path", MyStatus.this) + "setPickupPin.png");
                                theBitmap = Glide.
                                        with(MyStatus.this).
                                        load(SessionSave.getSession("image_path", MyStatus.this) + "setPickupPin.png").
                                        asBitmap().
                                        into(78, 129). // Width and height
                                        get();

                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void dummy) {
                            if (null != theBitmap) {
                                //Set image to imageview.
                                if (googleMap != null && MyStatus.this != null && templatlon != null)
                                    googleMap.clear();

                                //  BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.marker);
//                                Bitmap b=theBitmap;
//                                Bitmap theBitmap = Bitmap.createScaledBitmap(b, width, height, false);

                                try {
                                    c_marker = googleMap.addMarker(new MarkerOptions().position(templatlon).title("" + NC.getResources().getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(theBitmap))));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.execute();


                    // source = mMap.addMarker(new MarkerOptions()


                    // c_marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("" + NC.getResources().getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.fromBitmap(theBitmap)));
                    // System.out.println("________PPPPP" + theBitmap);
                    if (googleMap != null)
                        googleMap.clear();
                    if (null != theBitmap) {
                        //Set image to imageview.
                        if (googleMap != null)
                            googleMap.clear();
//                        Bitmap b=theBitmap;
//                        Bitmap theBitmap = Bitmap.createScaledBitmap(b, width, height, false);
                        c_marker = googleMap.addMarker(new MarkerOptions().position(templatlon).title("" + NC.getResources().getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(theBitmap))));
                    }
                    //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//                    savedLatLng = listPoint.get(listPoint.size() - 1);
//                    animateLine(listPoint, c_marker);

//                }

                }
                // getCurrentAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());


                new Address_s(this, new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap resizeMapIcons(Bitmap bitmap) {
        int newWidth = width;
        int newHeight = height;
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

        //   Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
//        return resizedBitmap;
    }

    /*
     * Convert the lat and lng into address value,set the marker with given lat/lng. Set the current location to text view.
     */
    public class Address_s extends AsyncTask<String, String, String> {
        public Context mContext;
        LatLng mPosition;
        String Address = "";
        Geocoder geocoder;
        private double latitude;
        private double longitude;
        List<android.location.Address> addresses = null;

        public Address_s(Context context, LatLng position) {

            mContext = context;
            mPosition = position;
            latitude = mPosition.latitude;
            longitude = mPosition.longitude;
            //   geocoder = new Geocoder(BookTaxiAct.mTag, Locale.getDefault());
            geocoder = new Geocoder(context, Locale.getDefault());
        }

//    public Address_s() {
//
//        try {
//            latitude = BookTaxiNewFrag.P_latitude;
//            longitude = BookTaxiNewFrag.P_longitude;
//            geocoder = new Geocoder(mContext, Locale.getDefault());
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //SessionSave.saveSession("notes", "", mContext);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                if (Geocoder.isPresent()) {
                    //    System.out.println("address size:" + latitude + "%$#" + longitude);
                    addresses = geocoder.getFromLocation(latitude, longitude, 3);
                    //    System.out.println("address size:" + addresses.size());
                    if (addresses.size() == 0) {
//                    geocoder = new Geocoder(mContext, Locale.getDefault());
//                    System.out.println("address size:N" + latitude + "%$#" + longitude);
//                    if(geocoder!=null)
//                        System.out.println("address Geo not null");
//                    addresses = geocoder.getFromLocation(latitude, longitude, 3);
                        //      System.out.println("address size:Nreq" + addresses.size());
                        new convertLatLngtoAddressApi(MyStatus.this, latitude, longitude);
                        //   requestingLocationConvert(latitude, longitude);
                    } else {
                        for (int i = 0; i < addresses.size(); i++) {
                            Address += addresses.get(0).getAddressLine(i) + ", ";
                        }
                        if (Address.length() > 0)
                            Address = Address.substring(0, Address.length() - 2);


                    }

                    //
                    //          if (addresses.size() > 0)
                    //          {
                    //             Address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1);
                    //          }
                } else {
                    if (NetworkStatus.isOnline(mContext))
                        new convertLatLngtoAddressApi(MyStatus.this, latitude, longitude);
                        // requestingLocationConvert(latitude, longitude);
                    else {
                        //  BookTaxiNewFrag.setfetch_address();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (NetworkStatus.isOnline(mContext))
                    new convertLatLngtoAddressApi(MyStatus.this, latitude, longitude);
                    //requestingLocationConvert(latitude, longitude);
                else {
                    // BookTaxiNewFrag.setfetch_address();
                }
            }
            return Address;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            cancelLoading();
            //   System.out.println("sjdgfjsdfjsdfjdhfj");
            if (!Address.equalsIgnoreCase(""))
                currentLocation1.setText(Address.replaceAll("null", "").replaceAll(", ,", "").replaceAll(", ,", ""));
//            TaxiUtil.pAddress = "" + Address;
//            if (Address.length() != 0) {
//                BookTaxiNewFrag.sf.setPickuplocTxt(Address);
//                BookTaxiNewFrag.sf.setPickuplat(latitude);
//                BookTaxiNewFrag.sf.setPickuplng(longitude);
//            } else {
//                BookTaxiNewFrag.setfetch_address();
//            }
            result = null;

        }


        /**
         * This class is used to convert latlan to address
         */
      /*  public class convertLatLngtoAddressApi implements APIResult {
            public convertLatLngtoAddressApi(Context c, double lati, double longi) {
                if (GEOCODE_EXPIRY) {
                    // googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
                    String googleMapUrl = SessionSave.getSession("base_url", MyStatus.this) + "?type=google_geocoding";
                    try {
                        JSONObject j = new JSONObject();
                        j.put("origin", lati + "," + longi);
                        j.put("type", "2");
                        new APIService_Retrofit_JSON(c, this, j, false, googleMapUrl, false).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
                    new APIService_Retrofit_JSON(c, this, null, true, googleMapUrl, true).execute();
                }
            }

            @Override
            public void getResult(boolean isSuccess, String result) {
                if (result != null && result.length() > 0)
                    setLocation(result.toString());
            }
        }*/
        public class convertLatLngtoAddressApi implements APIResult {
            public convertLatLngtoAddressApi(Context c, double lati, double longi) {
                String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
                new APIService_Retrofit_JSON(c, this, null, true, googleMapUrl, true).execute();
            }

            @Override
            public void getResult(boolean isSuccess, String result) {
                cancelLoading();
                if (result != null)
                    setLocation(result.toString());
            }
        }


//        private void requestingLocationConvert(double lati, double longi) {
//
//            try {
//                System.out.println("_____lati");
//                String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
//                RequestQueue queue = Volley.newRequestQueue(mContext);
//                @SuppressWarnings({"unchecked", "rawtypes"}) JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, googleMapUrl, null, new Response.Listener() {
//                    @Override
//                    public void onResponse(Object result) {
//
//                        setLocation(result.toString());
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//                queue.add(jsObjRequest);
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//                {
//                    //   BookTaxiNewFrag.setfetch_address();
//                }
//            }
//        }

        /**
         * Setting current location
         */
        public void setLocation(String inputJson) {

            try {
                JSONObject object = new JSONObject("" + inputJson);
                JSONArray array = object.getJSONArray("results");
                object = array.getJSONObject(0);
                if (!object.getString("formatted_address").equalsIgnoreCase(""))
                    currentLocation1.setText(object.getString("formatted_address").replaceAll("null", "").replaceAll(", ,", "").replaceAll(", ,", ""));
                //  BookTaxiAct.PicklocEdt.setText("" + object.getString("formatted_address"));
//                BookTaxiNewFrag.sf.setPickuplocTxt(object.getString("formatted_address"));
//                BookTaxiNewFrag.sf.setPickuplat(latitude);
//                BookTaxiNewFrag.sf.setPickuplng(longitude);
            } catch (Exception ex) {
                ex.printStackTrace();
                GEOCODE_EXPIRY = true;
                //  BookTaxiNewFrag.setfetch_address();
            }
        }
    }


    /**
     * This method used to get current address
     */
    private void getCurrentAddress(double lat, double lon) {
        Geocoder geocoder;
        List<Address> addresses = null;
        String address = "";
        String city = "";
        String country = "";
        geocoder = new Geocoder(this, Locale.UK);
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() != 0) {
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getAddressLine(1);
                country = addresses.get(0).getAddressLine(2);
                LatLng coordinate = new LatLng(lat, lon);

                if (mapstatus == 0) {
                    if (googleMap != null)
                        googleMap.clear();
                    if (c_marker != null) {
                        c_marker.remove();
                    }

                   /* Bitmap theBitmap = Glide.
                            with(this).
                            load(SessionSave.getSession("image_path",this)+"setPickupPin.png").
                            asBitmap().
                            into(100, 100). // Width and height
                            get();*/

                    final LatLng templatlon = coordinate;
                    new AsyncTask<String, Void, Void>() {
                        Bitmap theBitmap = null;
                        Bitmap bm = null;

                        @Override
                        protected Void doInBackground(String... params) {
                            String TAG = "Error Message: ";
                            try {
                                theBitmap = Glide.
                                        with(MyStatus.this).
                                        load(SessionSave.getSession("image_path", MyStatus.this) + "setPickupPin.png").
                                        asBitmap().
                                        into(100, 100). // Width and height
                                        get();

                            } catch (final Exception e) {
                                //
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void dummy) {
                            if (null != theBitmap) {
                                //Set image to imageview.
                                if (googleMap != null)
                                    googleMap.clear();
//                                Bitmap b=theBitmap;
//                                Bitmap theBitmap = Bitmap.createScaledBitmap(b, width, height, false);
                                c_marker = googleMap.addMarker(new MarkerOptions().position(templatlon).title("" + NC.getResources().getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.fromBitmap(theBitmap)));
                            }
                        }
                    }.execute();
                    //c_marker = googleMap.addMarker(new MarkerOptions().position(coordinate).title("" + NC.getResources().getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.fromBitmap(theBitmap)));
                    if (!address.equalsIgnoreCase(""))
                        currentLocation1.setText(("" + address + " " + city + " " + country).replaceAll("null", "").replaceAll(", ,", "").replaceAll(", ,", ""));
                    mapstatus = 1;
                }
                bearing = 0;
                if (!address.equalsIgnoreCase(""))
                    currentLocation1.setText(("" + address + "\n" + city + "\n" + country).replaceAll("null", "").replaceAll(", ,", "").replaceAll(", ,", ""));
            } else {
                Toast.makeText(MyStatus.this, "Address not found", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            stopLocationUpdates();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        if (mGoogleApiClient != null)
            stopLocationUpdates();
        googleMap = null;
        if (c_marker != null) {
            c_marker = null;
        }
        super.onDestroy();
    }

    /**
     * This method used to round the decimal values
     */
    private double roundDecimal(double value, final int decimalPlace) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        value = bd.doubleValue();
        return value;
    }

    /**
     * This method used to convert speed
     */
    private double convertSpeed(double speed) {
        return ((speed * 3600) * 0.001);
    }

    /**
     * Marker Animation with array of latlng
     */
//    public void animateLine(ArrayList<LatLng> Trips, Marker marker) {
//        _trips.clear();
//        _trips.addAll(Trips);
//        _marker = marker;
//        animateMarker();
//    }
//
//    /**
//     * This method is used to animate the location marker
//     */
//    public void animateMarker() {
//        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
//            @Override
//            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
//                return _latLngInterpolator.interpolate(fraction, startValue, endValue);
//            }
//        };
//        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
//
//        ObjectAnimator animator = null;
//
//        for (int i = 0; i < _trips.size(); i++) {
//            animator = ObjectAnimator.ofObject(_marker, property, typeEvaluator, _trips.get(i));
//
//        }
//
//        animator.addListener(new Animator.AnimatorListener() {
//                                 @Override
//                                 public void onAnimationCancel(Animator animation) {
//                                     System.out.println("Animation Cancel");
//                                 }
//
//                                 @Override
//                                 public void onAnimationRepeat(Animator animation) {
//                                     System.out.println("Animation Repeat");
//                                 }
//
//                                 @Override
//                                 public void onAnimationStart(Animator animation) {
//
//                                     System.out.println("Status--> started " + _trips.size() + " Animation started ");
//                                 }
//
//                                 @Override
//                                 public void onAnimationEnd(Animator animation) {
//                                     //listPoint.clear();
//                                     System.out.println("Status--> ended " + _trips.size() + " Animation ended ");
//
//
//                                     try {
//
//                                         /*Bitmap theBitmap = Glide.
//                                                 with(MyStatus.this).
//                                                 load(SessionSave.getSession("image_path",MyStatus.this)+"setPickupPin.png").
//                                                 asBitmap().
//                                                 into(100, 100). // Width and height
//                                                 get();*/
//
//                                         final LatLng templatlon = coordinate;
//                                         new AsyncTask<String, Void, Void>() {
//                                             Bitmap theBitmap = null;
//                                             Bitmap bm = null;
//
//                                             @Override
//                                             protected Void doInBackground(String... params) {
//                                                 String TAG = "Error Message: ";
//                                                 try {
//                                                     theBitmap = Glide.
//                                                             with(MyStatus.this).
//                                                             load(SessionSave.getSession("image_path", MyStatus.this) + "setPickupPin.png").
//                                                             asBitmap().
//                                                             into(100, 100). // Width and height
//                                                             get();
//
//                                                 } catch (final Exception e) {
//                                                     //
//                                                 }
//                                                 return null;
//                                             }
//
//                                             @Override
//                                             protected void onPostExecute(Void dummy) {
//                                                 if (null != theBitmap) {
//                                                     //Set image to imageview.
//
//                                                     if (c_marker != null) {
//                                                         c_marker.remove();
//                                                         c_marker = googleMap.addMarker(new MarkerOptions().position(savedLatLng).title("" + NC.getResources().getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.fromBitmap(theBitmap)));
//                                                     }
//                                                 }
//                                             }
//                                         }.execute();
//
//                                         /*if (c_marker != null) {
//                                             c_marker.remove();
//                                             c_marker = googleMap.addMarker(new MarkerOptions().position(savedLatLng).title("" + NC.getResources().getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.fromBitmap(theBitmap)));
//                                         }*/
//
//                                         if (savedpoint.size() > 1) {
//                                             for (int i = 0; i < savedpoint.size(); i++) {
//                                                 listPoint.add(savedpoint.get(i));
//                                                 listPoint.add(0, savedLatLng);
//                                                 System.out.println("savedLatLng animation end" + savedpoint.get(i));
//                                             }
//                                             savedpoint.clear();
//                                             animStarted = false;
//                                             animLocation = true;
//                                         } else {
//                                             animStarted = false;
//                                             animLocation = false;
//                                         }
//
//                                     } catch (Exception ex) {
//
//                                     }
//
//
//                                 }
//                             }
//
//        );
//        animator.setDuration(5000);
//        animator.start();
//    }


    /**
     * Driver Shift API response parsing.
     */
    private class RequestingCheckBox implements APIResult {
        public RequestingCheckBox() {
            try {
                if (btn_shift.getText().toString().equals(NC.getString(R.string.online)))
                    checked = "OUT";
                else
                    checked = "IN";
                JSONObject j = new JSONObject();
                j.put("driver_id", SessionSave.getSession("Id", MyStatus.this));
                j.put("shiftstatus", checked);
                j.put("reason", "");
                //  Log.e("shiftbefore ", j.toString());

                j.put("update_id", SessionSave.getSession("Shiftupdate_Id", MyStatus.this));
                String requestingCheckBox = "type=driver_shift_status";
                if (isOnline())
                    new APIService_Retrofit_JSON(MyStatus.this, this, j, false).execute(requestingCheckBox);
                else {
                    btn_shift.setClickable(true);
                    alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.server_error), "" + NC.getResources().getString(R.string.ok), "");
                    if (checked.equals("IN")) {
                        btn_shift.setText(NC.getString(R.string.online));
                        Drawables_program.shift_on(btn_shift);
                    } else {
                        btn_shift.setText(NC.getString(R.string.offline));
                        Drawables_program.shift_bg_grey(btn_shift);

                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            try {

                //  Log.e("driverstatus", result);

                if (isSuccess && MyStatus.this != null) {
                    btn_shift.setClickable(true);

                    JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {
                        if (checked.equals("IN")) {
                            alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                            btn_shift.setText(NC.getString(R.string.online));
                            Drawables_program.shift_on(btn_shift);
                            SessionSave.saveSession("shift_status", "IN", MyStatus.this);
                            SessionSave.saveSession("Shiftupdate_Id", object.getJSONObject("detail").getString("update_id"), MyStatus.this);
                            //   Log.e("sess", SessionSave.getSession("shift_status", MyStatus.this));
                            SessionSave.saveSession(CommonData.SHIFT_OUT, false, MyStatus.this);
                            nonactiityobj.startServicefromNonActivity(MyStatus.this);
                        } else {
                            alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                            btn_shift.setText(NC.getString(R.string.offline));
                            Drawables_program.shift_bg_grey(btn_shift);
                            SessionSave.saveSession("shift_status", "OUT", MyStatus.this);
                            SessionSave.saveSession(CommonData.SHIFT_OUT, true, MyStatus.this);
                            //  Log.e("sess", SessionSave.getSession("shift_status", MyStatus.this));
                          //  nonactiityobj.stopServicefromNonActivity(MyStatus.this);
                            stopService(new Intent(MyStatus.this, LocationUpdate.class));
                        }
                    } else if (object.getInt("status") == -4) {
                        alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                        btn_shift.setText(NC.getString(R.string.online));
//                        Drawables_program.shift_on(btn_shift);
                    } else {
                        alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        if (checked.equals("IN")) {
                            btn_shift.setText(NC.getString(R.string.online));
                            Drawables_program.shift_on(btn_shift);
                        } else {
                            btn_shift.setText(NC.getString(R.string.offline));
                            Drawables_program.shift_bg_grey(btn_shift);
                        }
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast toast = Toast.makeText(MyStatus.this, getString(R.string.please_check_internet), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                    btn_shift.setClickable(true);
                    // alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                    if (checked.equals("IN")) {
                        btn_shift.setText(NC.getString(R.string.online));
                        Drawables_program.shift_on(btn_shift);
                    } else {
                        btn_shift.setText(NC.getString(R.string.offline));
                        Drawables_program.shift_bg_grey(btn_shift);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                //    System.out.println("thambiError" + ex.getLocalizedMessage());
                btn_shift.setClickable(true);
                alert_view(MyStatus.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.server_error), "" + NC.getResources().getString(R.string.ok), "");
                if (checked.equals("IN")) {
                    btn_shift.setText(NC.getString(R.string.online));
                    Drawables_program.shift_on(btn_shift);
                } else {
                    btn_shift.setText(NC.getString(R.string.offline));
                    Drawables_program.shift_bg_grey(btn_shift);
                }
            }
        }
    }


    public void latLongAlert(String message, LatLng latLng) {
        try {
            if (true) {
                if (errorDialog != null && errorDialog.isShowing())
                    errorDialog.dismiss();
                //   System.out.println("setCanceledOnTouchOutside" + message);
                final View view = View.inflate(MyStatus.this, R.layout.netcon_lay, null);
                errorDialog = new Dialog(MyStatus.this, R.style.dialogwinddow);
                errorDialog.setContentView(view);
                errorDialog.setCancelable(false);
                errorDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(MyStatus.this, errorDialog.findViewById(R.id.alert_id));
                errorDialog.show();
                final TextView title_text = (TextView) errorDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) errorDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) errorDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) errorDialog.findViewById(R.id.button_failure);
                title_text.setText("" + getResources().getString(R.string.message));
                message_text.setText("" + message);
                button_success.setText("" + getResources().getString(R.string.ok));
                button_failure.setText("" + getResources().getString(R.string.cancel));
                button_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        errorDialog.dismiss();


                    }
                });
                button_failure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub


                        Activity activity = (Activity) MyStatus.this;

                        final Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        try {
                            if (MyStatus.this != null && errorDialog != null)
                                errorDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } else {
                try {
                    if (MyStatus.this != null && errorDialog != null)
                        errorDialog.dismiss();
//                    if (MyStatus.this != null) {
//                        Intent intent = new Intent(MyStatus.this, MyStatus.this.getClass());
//                        MyStatus.this.startActivity(intent);
//                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    public void errorInSplash(String message) {
        try {
            if (true) {
                if (errorDialog != null && errorDialog.isShowing())
                    errorDialog.dismiss();
                //     System.out.println("setCanceledOnTouchOutside" + message);
                final View view = View.inflate(MyStatus.this, R.layout.netcon_lay, null);
                errorDialog = new Dialog(MyStatus.this, R.style.dialogwinddow);
                errorDialog.setContentView(view);
                errorDialog.setCancelable(false);
                errorDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(MyStatus.this, errorDialog.findViewById(R.id.alert_id));
                errorDialog.show();
                final TextView title_text = (TextView) errorDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) errorDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) errorDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) errorDialog.findViewById(R.id.button_failure);
                title_text.setText("" + getResources().getString(R.string.message));
                message_text.setText("" + message);
                button_success.setText("" + getResources().getString(R.string.c_tryagain));
                button_failure.setText("" + getResources().getString(R.string.cancel));
                button_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        errorDialog.dismiss();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    }
                });
                button_failure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub


                        Activity activity = (Activity) MyStatus.this;

                        final Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        errorDialog.dismiss();

                    }
                });
            } else {
                try {
                    if (MyStatus.this != null && errorDialog != null)
                        errorDialog.dismiss();
//                    if (MyStatus.this != null) {
//                        Intent intent = new Intent(MyStatus.this, MyStatus.this.getClass());
//                        MyStatus.this.startActivity(intent);
//                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//            startActivity(intent);
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //  super.onBackPressed();
            return;
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "" + NC.getResources().getString(R.string.please_click_back_again_exit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}
