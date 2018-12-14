package com.cabi.driver;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.data.MapWrapperLayout;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.NonActivity;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * This class is the base  for showing app notifications
 */
public class NotificationAct extends MainActivity implements OnMapReadyCallback {
    // Class members declarations.
    private TextView HeadTitle;
    private Button butt_decline;
    public String message;
    TextView txt;
    RelativeLayout layout;
    int myprogress = 0, time_out; // , timer;
    CountDownTimer countDownTimer;
    Ringtone r;
    private String trip_id = "";
    public String distance;
    private String pickup;
    public String taxi_id;
    private String drop;
    public String passenger_id;
    private String bookedby;
    private String passenger_phone;
    public String roundtrip;
    public String no_of_passengers;
    private String cityname;
    public String pickup_time;
    private String passenger_name;
    public String profile_image;
    public String distanceaway = "";
    private LinearLayout noteslayout;
    NonActivity nonactiityobj = new NonActivity();
    public static long timeSwap = 0L;
    Bundle bun;
    Button butt_accept;
    Activity nActivity;
    TextView remnTimeTxt, secTxt;
    TextView passNameTxt, pickupLocTxt, dropLocTxt, distanceawayTxt, idsec, idmin, minTxt, idname, idpick, iddrop, slideImg, text_notes;
    private String notes;
    private LinearLayout droplayout;
    private TextView backupTxt;
    private GoogleMap map;
    private MapWrapperLayout mapWrapperLayout;
    private RelativeLayout mapsupport_lay;
    private double pickup_lat;
    private double pickup_lng;
    private DonutProgress donutProgress;
    private View backbg;
    private AnimatorSet set;
    private TextView Rightlay;
    public static NotificationAct notificationObject;
    private String model_name;
    private TextView model_name_txt;

    // Set the layout to activity.
    @Override
    public int setLayout() {

        setLocale();
        return R.layout.notification_lay;
    }

    /**
     * This method is used to enable gps
     */
    private boolean GPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // Initialize the views on layout
    @Override
    public void Initialize() {

        try {
            bun = getIntent().getExtras();
            nActivity = this;
            CommonData.current_act = "NotificationAct";
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                getWindow().setStatusBarColor(getResources().getColor(R.color.colorprimary));
//            }
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);

//            Colorchange.ChangeColor((ViewGroup) (((ViewGroup) NotificationAct.this
//                    .findViewById(android.R.id.content)).getChildAt(0)), NotificationAct.this);

            //  map = mapFrag.getMap();
            if (bun != null) {
                unlockScreen();
                FontHelper.applyFont(this, findViewById(R.id.noti_font));
                // mapsupport_lay = (RelativeLayout) findViewById(R.id.mapsupport_lay);
                nonactiityobj.stopServicefromNonActivity(NotificationAct.this);
                HeadTitle = (TextView) findViewById(R.id.headerTxt);
                HeadTitle.setText(" " + NC.getResources().getString(R.string.Trip_Request));
                butt_accept = (Button) findViewById(R.id.butt_accept);
                butt_decline = (Button) findViewById(R.id.butt_decline);
                slideImg = (TextView) findViewById(R.id.slideImg);
                slideImg.setVisibility(View.GONE);
                remnTimeTxt = (TextView) findViewById(R.id.rmnTimeTxt);
                secTxt = (TextView) findViewById(R.id.secTxt);
                model_name_txt = (TextView) findViewById(R.id.model_name_txt);
                passNameTxt = (TextView) findViewById(R.id.pass_name);
                pickupLocTxt = (TextView) findViewById(R.id.pickup_loc);
                dropLocTxt = (TextView) findViewById(R.id.drop_loc);
                distanceawayTxt = (TextView) findViewById(R.id.distanceaway);
                idsec = (TextView) findViewById(R.id.idsec);
                idmin = (TextView) findViewById(R.id.idmin);
                minTxt = (TextView) findViewById(R.id.minTxt);
                idname = (TextView) findViewById(R.id.idname);
                idpick = (TextView) findViewById(R.id.idpick);
                iddrop = (TextView) findViewById(R.id.iddrop);
                text_notes = (TextView) findViewById(R.id.notes);
                noteslayout = (LinearLayout) findViewById(R.id.noteslayout);
                droplayout = (LinearLayout) findViewById(R.id.droplayout);
                backupTxt = (TextView) findViewById(R.id.backup);
                Rightlay = (TextView) findViewById(R.id.Rightlay);
                try {
                    ((TextView) findViewById(R.id.request_for)).setText(NC.getString(R.string.req_model));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Rightlay.setVisibility(View.VISIBLE);
                backbg = (View) findViewById(R.id.backbg);
                backupTxt.setVisibility(View.GONE);
                donutProgress = (DonutProgress) findViewById(R.id.donut_progress);

                FontHelper.applyFont(this, passNameTxt, "Roboto_Medium.ttf");

                FontHelper.applyFont(this, minTxt, "Roboto_Medium.ttf");


                message = bun.getString("message");
                final Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                r = RingtoneManager.getRingtone(NotificationAct.this, notification);
                try {
                    final JSONObject json = new JSONObject(message);
                    final JSONObject tripdetails = json.getJSONObject("trip_details");
                    time_out = tripdetails.getInt("notification_time");
                    notes = tripdetails.getString("notes");
                    trip_id = tripdetails.getString("passengers_log_id");
                    final JSONObject details = tripdetails.getJSONObject("booking_details");
                    model_name = details.getString("model_name");
                    if (model_name.equalsIgnoreCase("Delivery"))
                      //  SessionSave.saveSession("taxi_model_id", "3", NotificationAct.this);
                    model_name_txt.setText(model_name);
                    pickup = details.getString("pickupplace");
                    pickup_lat = details.getDouble("pickup_latitude");
                    pickup_lng = details.getDouble("pickup_longitude");
                    //  initalizemap();
                    pickup = pickup.trim();
                    if (pickup.length() != 0)
                        pickup = Character.toUpperCase(pickup.charAt(0)) + pickup.substring(1);
                    drop = details.getString("drop");
                    drop = drop.trim();
                    if (drop.length() != 0 || !drop.equals(""))
                        drop = Character.toUpperCase(drop.charAt(0)) + drop.substring(1);
                    else
                        droplayout.setVisibility(View.GONE);
                    pickup_time = details.getString("pickup_time");
                    passenger_phone = details.getString("passenger_phone");
                    passenger_id = details.getString("passenger_id");
                    distance = details.getString("distance_away");
                    passenger_name = details.getString("passenger_name");
                    passenger_phone = details.getString("passenger_phone");
                    distanceaway = details.getString("distance_away");
                    if (details.getString("bookedby").length() != 0) {
                        bookedby = details.getString("bookedby");
                    }
                    MainActivity.mMyStatus.setpassengerphone(passenger_phone);
                    passenger_name = passenger_name.trim();
                    if (passenger_name.length() != 0)
                        passenger_name = Character.toUpperCase(passenger_name.charAt(0)) + passenger_name.substring(1);
                    cityname = details.getString("cityname").trim();
                    if (cityname.length() != 0)
                        cityname = Character.toUpperCase(cityname.charAt(0)) + cityname.substring(1);
                    profile_image = details.getString("profile_image");

                    set = (AnimatorSet) AnimatorInflater.loadAnimator(NotificationAct.this, R.animator.progress_anim);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.setTarget(donutProgress);
                    set.setDuration((time_out) * 1000);
                    set.start();


                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                if (notes.length() != 0 && !notes.contains("null")) {
                    text_notes.setText("" + notes);
                    noteslayout.setVisibility(View.VISIBLE);
                }
                passNameTxt.setText(passenger_name);
                pickupLocTxt.setText(pickup);
                dropLocTxt.setText(drop);
                // Timer function runs based on server response and once it finished, Onfinish() method calls the reject _trip API to update the driver timeout status to server.
                countDownTimer = new CountDownTimer((time_out) * 1000, 1000) {
                    int time = 1;

                    @Override
                    public void onTick(final long millisUntilFinished_) {

                        r.play();
                        long sec = millisUntilFinished_ / 1000;
                        long minutes = 0;
                        if (sec >= 60) {
                            minutes = (Long) sec / 60;
                            sec = sec - (minutes * 60);
                        }
                        minTxt.setText("" + String.format(Locale.UK, String.valueOf(minutes)));
                        secTxt.setText("" + String.format(Locale.UK, "%1$02d", sec));
                        if (minutes > 0)
                            remnTimeTxt.setText(String.format("%1$02d", minutes) + " " + NC.getResources().getString(R.string.minutestxt).toUpperCase() + ":" + String.format("%1$02d", sec) + " " + NC.getResources().getString(R.string.secondstxt).toUpperCase() + " " + NC.getResources().getString(R.string.seconds_to_left));
                        else
                            remnTimeTxt.setText(String.format("%1$02d", sec) + " " + NC.getResources().getString(R.string.secondstxt).toUpperCase() + " " + NC.getResources().getString(R.string.seconds_to_left));
                        time++;
                    }

                    @Override
                    public void onFinish() {

                        try {
                            countDownTimer.cancel();
                            r.stop();
                            JSONObject j = new JSONObject();
                            j.put("trip_id", trip_id);
                            j.put("driver_id", SessionSave.getSession("Id", NotificationAct.this));
                            j.put("taxi_id", SessionSave.getSession("taxi_id", NotificationAct.this));
                            j.put("company_id", SessionSave.getSession("company_id", NotificationAct.this));
                            j.put("reason", "");
                            j.put("reject_type", "0");
                            final String Url = "type=reject_trip";
                            new TripReject(Url, j);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            // If driver accept the trip,following actions will perform.
            donutProgress.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {

                    try {

                        if (NetworkStatus.isOnline(NotificationAct.this)) {
                            if (GPSEnabled(NotificationAct.this)) {
                                countDownTimer.cancel();
                                set.cancel();
                                r.stop();
                                MainActivity.mMyStatus.settripId(trip_id);
                                SessionSave.saveSession("trip_id", "" + trip_id, NotificationAct.this);
                                MainActivity.mMyStatus.setpassengerId(trip_id);
                                JSONObject j = new JSONObject();
                                j.put("pass_logid", trip_id);
                                j.put("driver_id", SessionSave.getSession("Id", NotificationAct.this));
                                j.put("taxi_id", SessionSave.getSession("taxi_id", NotificationAct.this));
                                j.put("company_id", SessionSave.getSession("company_id", NotificationAct.this));
                                j.put("driver_reply", "A");
                                j.put("field", "rejection");
                                j.put("flag", "0");
                                final String Url = "type=driver_reply";
                                //       System.out.println("result" + "Sucess");
                                new TripAccept(Url, j);
                            } else {
                                Toast.makeText(NotificationAct.this, "GPS Connection Failed", Toast.LENGTH_SHORT).show();
                                countDownTimer.cancel();
                                set.cancel();
                                r.stop();
                                if(SessionSave.getSession("shift_status", NotificationAct.this).equals("IN"))
                                nonactiityobj.startServicefromNonActivity(NotificationAct.this);
                                finish();
                            }
                        } else {
                            countDownTimer.cancel();
                            set.cancel();
                            r.stop();
                            ShowToast(NotificationAct.this, "" + NC.getResources().getString(R.string.check_net_connection));
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // If driver decline the trip,following actions will perform.
            Rightlay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {

                    try {

                        if (NetworkStatus.isOnline(NotificationAct.this)) {
                            countDownTimer.cancel();
                            set.cancel();
                            r.stop();
                            JSONObject j = new JSONObject();
                            j.put("trip_id", trip_id);
                            j.put("driver_id", SessionSave.getSession("Id", NotificationAct.this));
                            j.put("taxi_id", SessionSave.getSession("taxi_id", NotificationAct.this));
                            j.put("company_id", SessionSave.getSession("company_id", NotificationAct.this));
                            j.put("reason", "");
                            j.put("reject_type", "1");
                            final String Url = "type=reject_trip";
                            new TripReject(Url, j);
                        } else {
                            countDownTimer.cancel();
                            set.cancel();
                            r.stop();
                            ShowToast(NotificationAct.this, "" + NC.getResources().getString(R.string.check_net_connection));
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopTimerAndNavigateToHome(final String msg) {
        System.out.println("nnn-----stopTimerAndNavigateToHome");
        // Toast.makeText(getApplicationContext(),""+ msg, Toast.LENGTH_LONG).show();
        countDownTimer.cancel();
        set.cancel();
        r.stop();
        //finish();
        final Intent intent = new Intent(NotificationAct.this, MyStatus.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle extras = new Bundle();
        extras.putString("alert_message", msg);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("nnn-----Runnable");
                ShowToast(getBaseContext(), msg);
            }
        });

    }


    @Override
    protected void onResume() {
        notificationObject = this;
        super.onResume();
    }

    /**
     * This method is initalize map settings
     */


    public void initalizemap() {
        try {
            final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(NotificationAct.this);
            if (resultCode == ConnectionResult.SUCCESS) {

                System.gc();
                MapsInitializer.initialize(NotificationAct.this);
                mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
                mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));
                mapWrapperLayout.setVisibility(View.VISIBLE);
                map.getUiSettings().setZoomControlsEnabled(false);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setMyLocationEnabled(false);
                map.setPadding(0, 0, 0, 120);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                TextView tv = new TextView(this);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100,100);
//                layoutParams.gravity = Gravity.CENTER;
//                tv.setLayoutParams(layoutParams);
//                tv.setText(getString(R.string.pickup1));
//                tv.setTextColor(Color.BLACK);
//                tv.setGravity(Gravity.CENTER);
//                tv.setBackgroundResource(R.drawable.pickup);
//
//                Bitmap testB;
//
//                testB = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//                Canvas c = new Canvas(testB);
//                tv.layout(0, 0, 100, 100);
//                tv.draw(c);
//                tv.setDrawingCacheEnabled(true);
//
//                tv.buildDrawingCache();
//
//                Bitmap bm = tv.getDrawingCache();
//                map.addMarker(new MarkerOptions().position(new LatLng(pickup_lat, pickup_lng)).flat(true).title("" + getResources().getString(R.string.droploc)).icon(BitmapDescriptorFactory.fromBitmap(bm)).draggable(true));

//
                // mapsupport_lay.setVisibility(View.VISIBLE);
                // nodataTxt.setVisibility(View.GONE);
                // System.err.println("animate camera:" + latitude1 + "lng" + longitude1);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickup_lat - 0.001, pickup_lng + 0.0001), 17f));
            } else {
                mapsupport_lay.setVisibility(View.GONE);
                //  nodataTxt.setVisibility(View.VISIBLE);
                //  nodataTxt.setText("" + "" + getResources().getString(R.string.device_not_support_map));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Get the google map pixels from xml density independent pixel.
     */
    public static int getPixelsFromDp(final Context context, final float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
        bun.clear();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        bun.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initalizemap();
    }

    /**
     * Used to call the trip accept API and parse the response
     */
    private class TripAccept implements APIResult {
        String msg;

        public TripAccept(final String url, JSONObject data) {

            //  System.out.println("result" + url);

            new APIService_Retrofit_JSON(NotificationAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {

            //   System.out.println("result" + result + isSuccess);

            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1 || bookedby.equals("2")) {
                        msg = json.getString("message");
//                        if (bookedby.equals("1")) {
                        SessionSave.saveSession("speedwaiting", "", NotificationAct.this);
                        MainActivity.mMyStatus.settripId(trip_id);
                        SessionSave.saveSession("trip_id", "" + trip_id, NotificationAct.this);
                        SessionSave.saveSession("status", "B",
                                NotificationAct.this);
                        SessionSave.saveSession("bookedby", "" + bookedby, NotificationAct.this);
                        showLoading(NotificationAct.this);
                        final Intent intent = new Intent(NotificationAct.this, OngoingAct.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", msg);
                        intent.putExtras(extras);
                        startActivity(intent);
                        finish();
//                        }
//                        else if (bookedby.equals("2")) {
//                            MainActivity.mMyStatus.settripId(trip_id);
//                            // SessionSave.saveSession("trip_id", "" + trip_id,
//                            // NotificationAct.this);
//                            SessionSave.saveSession("bookedby", "" + bookedby, NotificationAct.this);
//                            showLoading(NotificationAct.this);
//                            final Intent intent = new Intent(NotificationAct.this, JobsAct.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            Bundle extras = new Bundle();
//                            extras.putString("alert_message", msg);
//                            intent.putExtras(extras);
//                            startActivity(intent);
//                            finish();
//                        }
                    } else if (json.getInt("status") == 5) {
                        SessionSave.saveSession("trip_id", "", NotificationAct.this);
                        msg = json.getString("message");
                        Intent i = new Intent(getBaseContext(), MyStatus.class);
                        showLoading(NotificationAct.this);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", msg);
                        //i.putExtras(extras);
                        getApplication().startActivity(i);
                        ShowToast(NotificationAct.this, msg);
                        nActivity.finish();

                    } else if (json.getInt("status") == 7) {
                        SessionSave.saveSession("trip_id", "", NotificationAct.this);
                        msg = json.getString("message");
                        Intent i = new Intent(getBaseContext(), MyStatus.class);
                        showLoading(NotificationAct.this);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", msg);
                        // i.putExtras(extras);
                        ShowToast(NotificationAct.this, msg);
                        getApplication().startActivity(i);
                        nActivity.finish();
                    } else if (json.getInt("status") == 25) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ShowToast(NotificationAct.this, getString(R.string.server_error));
                            }
                        });
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(NotificationAct.this, getString(R.string.server_error));
                        }
                    });
                    finish();
                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to call the trip reject API and parse the response
     */
    private class TripReject implements APIResult {
        String msg;

        public TripReject(final String url, JSONObject data) {

            new APIService_Retrofit_JSON(NotificationAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            //  Log.d("result", "result" + result);
            try {
                if (isSuccess) {
                    if(SessionSave.getSession("shift_status", NotificationAct.this).equals("IN"))
                    nonactiityobj.startServicefromNonActivity(NotificationAct.this);
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 6) {
                        msg = json.getString("message");
                    } else if (json.getInt("status") == 7) {
                        msg = json.getString("message");
                    } else if (json.getInt("status") == 8) {
                        msg = json.getString("message");
                    } else if (json.getInt("status") != 6 || json.getInt("status") != 8 || json.getInt("status") != 3 || json.getInt("status") != 2 || json.getInt("status") != -1) {
                        msg = "Trip has been is rejected";
                    } else {
                        msg = "Trip has been already cancelled";
                    }
                    SessionSave.saveSession("trip_id", "", NotificationAct.this);
                    showLoading(NotificationAct.this);
                    final Intent intent = new Intent(NotificationAct.this, MyStatus.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    Bundle extras = new Bundle();
//                    extras.putString("alert_message", msg);
                    // intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    ShowToast(NotificationAct.this, msg);
                } else {
                    //

                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(NotificationAct.this, getString(R.string.server_error));
                        }
                    });
                    finish();
                }
                // alert_view(NotificationAct.this, "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_net_connection), "" + getResources().getString(R.string.ok), "");
            } catch (final JSONException e) {
                if (NotificationAct.this != null) {
                    final Intent intent = new Intent(NotificationAct.this, MyStatus.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    Bundle extras = new Bundle();
//                    extras.putString("alert_message", msg);
                    // intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    ShowToast(NotificationAct.this, NC.getString(R.string.server_error));
                }
                e.printStackTrace();
            }
        }
    }


    /**
     * This method is to check and open the notification view in front even the mobile screen off.
     */
    private void unlockScreen() {

        Window window = this.getWindow();
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
