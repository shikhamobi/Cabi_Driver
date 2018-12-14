package com.cabi.driver;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Property;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.data.MapWrapperLayout;
import com.cabi.driver.data.MenuSlider;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.route.Route;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.LocationUpdate;
import com.cabi.driver.service.NonActivity;
import com.cabi.driver.service.TimerRun;
import com.cabi.driver.service.WaitingTimerRun;
import com.cabi.driver.slidemenu.SlidingMenu;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.GpsStatus;
import com.cabi.driver.utils.LatLngInterpolator;
import com.cabi.driver.utils.LocationDb;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.RoundedImageView;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
/*
import com.cabi.driver.utils.Utils;
*/
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.cabi.driver.MyStatus.GEOCODE_EXPIRY;


/**
 * This class will be called once the trip is accepted.Here we can start,end trip etc.
 */
@SuppressLint("DefaultLocale")
public class OngoingAct extends MainActivity implements
        LocationListener, ConnectionCallbacks, OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 112;
    private static final int MY_PERMISSIONS_REQUEST_GPS = 113;
    private static boolean ROUTE_DRAW_ON_START;
    private static boolean LOCATION_UPDATE_STOPPED;
    // Class members declarations.
    private LocationRequest mLocationRequest;
    public GoogleMap map;
    private Marker c_marker, p_marker, d_marker;
    private double latitude1 = 0.0;
    private double longitude1 = 0.0;
    private Double p_latitude, p_longtitude;
    private Double d_latitude, d_longtitude;
    private Double driver_latitude, driver_longtitude;
    private String waitingTime = "";
    private TextView CurrentlocationTxt;
    private Double actual_p_latitude, actual_p_longtitude;

    private TextView droplocationTxt, tv_notes;
    private TextView TripcancelTxt;
    private TextView nodataTxt;
    private TextView passengerphoneTxt;
    private RoundedImageView proimg;
    private String Address = "";
    private TextView backup;
    public static TextView HeadTitle;
    public static TextView CancelTxt;
    public static TextView WaitingTxt;
    private TextView passnameTxt;
    private double previous_latitude, previous_longitude, lat1, log1;
    private RelativeLayout mapsupport_lay;
    public static Activity mFlagger;
    private final Handler myHandler = new Handler();
    private Float waitingHr;
    public static Button butt_onboard;
    Route route = null;
    long timeclear = 0L;
    private LatLng pickupLatLng;
    private LatLng dropLatLng;
    private LatLng currentLatLng;
    public static CharSequence sTimer;
    Context context;
    String str;
    public static OngoingAct activity;
    private MenuSlider menu1;
    private SlidingMenu menu;
    private ImageLoader imageLoader;
    private LinearLayout tripInfo;
    //private TextView minMaxBtn;
    float zoom = 16f;
    NonActivity nonactiityobj = new NonActivity();
    public static TextView speedtext, speed_txt, total_km, total_km_val;
    private String totalkm = "";
    private String LowSpeed = "";
    public static String speedlimit = "0";
    LinearLayout speed_lay, km_lay;
    private Bundle alert_bundle = new Bundle();
    private String alert_msg;
    RelativeLayout navigator_layout;
    LocationDb objLocationDb;
    float bearing;
    int layoutheight;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 5000; // 10 sec
    private static int FATEST_INTERVAL = 1000; // 5 sec
    private static int DISPLACEMENT = 1; // 10 meters
    private MapWrapperLayout mapWrapperLayout;
    public Dialog cDialog;
    // Animate marker
    ArrayList<LatLng> listPoint = new ArrayList<LatLng>();
    ArrayList<LatLng> savedpoint = new ArrayList<LatLng>();
    ArrayList<String> savedlocation = new ArrayList<String>();
    ArrayList<LatLng> _trips = new ArrayList<LatLng>();
    Marker _marker;
    float animteBearing;
    String p_travelstatus = "";
    LatLngInterpolator _latLngInterpolator = new LatLngInterpolator.Spherical();
    TextView sendEmail;
    private boolean animStarted = false;
    private boolean animLocation = false;
    LatLng savedLatLng = null;
    private double speed = 0.0;
    private Marker a_marker;
    ObjectAnimator animator = null;
    int count;
    private String status;
    private RelativeLayout dropp;
    private LinearLayout dropppp, pickupp;
    FrameLayout pickup_pinlay;
    ImageView pickup_pin;
    private Dialog mcancelDialog;
    private Dialog mlogoutDialog;
    private ImageView butt_navigator;
    private float bearings;
    private LinearLayout nodataLay;
    private Button back_main;
    private LocationManager mLocationManager;
    private boolean isGPSEnabled;
    public static Dialog gpsdialog;
    public static int retrycount = 1;
    private AlertDialog alert;

    // Set the layout to activity.
    @Override
    public int setLayout() {

        setLocale();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return R.layout.accept_lay;

    }

    @Override
    protected void onStop() {
        CommonData.closeDialog(mcancelDialog);
        CommonData.closeDialog(mProgressdialog);
        CommonData.closeDialog(cDialog);
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    final Intent callIntent = new Intent(Intent.ACTION_CALL);
//                    callIntent.setData(Uri.parse("tel:" + MainActivity.mMyStatus.getpassengerphone()));
//                    startActivity(callIntent);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ensureCall();
                        }
                    });

                }
                break;
            case MY_PERMISSIONS_REQUEST_GPS:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getGPS();
                }

                return;
        }
    }

    /**
     * Call passenger
     */
    private void ensureCall() {
        try {
            final View view = View.inflate(this, R.layout.netcon_lay, null);
            mlogoutDialog = new Dialog(this, R.style.dialogwinddow);
            FontHelper.applyFont(this, view);
            mlogoutDialog.setContentView(view);
            mlogoutDialog.setCancelable(false);
            mlogoutDialog.show();
            FontHelper.applyFont(this, mlogoutDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) mlogoutDialog.findViewById(R.id.alert_id), OngoingAct.this);
            final TextView title_text = (TextView) mlogoutDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mlogoutDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mlogoutDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mlogoutDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.VISIBLE);
            title_text.setText("" + NC.getResources().getString(R.string.message));
            message_text.setText("" + NC.getResources().getString(R.string.confirm_call));
            button_success.setText("" + NC.getResources().getString(R.string.call));
            button_failure.setText("" + NC.getResources().getString(R.string.cancel));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    try {
                        mlogoutDialog.dismiss();
                        final Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + MainActivity.mMyStatus.getpassengerphone()));
                        startActivity(callIntent);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    mlogoutDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Initialize the views on layout
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void Initialize() {

        CommonData.mActivitylist.add(this);
        CommonData.current_act = "OngoingAct";
        CommonData.sContext = this;
        if (servicesConnected()) {
            // Building the GoogleApi client
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
            buildGoogleApiClient();

        }
        final SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        try {
            objLocationDb = new LocationDb(OngoingAct.this);
            alert_bundle = getIntent().getExtras();
            if (alert_bundle != null) {
                alert_msg = alert_bundle.getString("alert_message");
                try {
                    status = alert_bundle.getString("status");
                    getIntent().replaceExtras(new Bundle());
                    getIntent().setAction("");
                    getIntent().setData(null);
                    getIntent().setFlags(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (status != null)
                if (status.equals("11")) {
                    startActivity(new Intent(OngoingAct.this, OngoingAct.class));
                }
            if (alert_msg != null && alert_msg.length() != 0)
                alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
            if (!SessionSave.getSession("trip_id", OngoingAct.this).equals("")) {
                JSONObject j = new JSONObject();
                j.put("trip_id", SessionSave.getSession("trip_id", OngoingAct.this));
                final String Url = "type=get_trip_detail";
                new Tripdetails(Url, j);
                if(SessionSave.getSession("shift_status", OngoingAct.this).equals("IN"))
                nonactiityobj.startServicefromNonActivity(OngoingAct.this);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("pass---" + e);
            e.printStackTrace();
        }
        FontHelper.applyFont(this, findViewById(R.id.ongoing_lay));
        //siva 27/02/2018
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.colorprimary));
//        }
        activity = this;
        mFlagger = this;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        menu1 = new MenuSlider(this, menu);
        menu = menu1.slidemenu(this);

        dropppp = (LinearLayout) findViewById(R.id.dropppp);
        pickup_pinlay = (FrameLayout) findViewById(R.id.pickup_pinlay);
        pickupp = (LinearLayout) findViewById(R.id.pickupp);
        pickup_pin = (ImageView) findViewById(R.id.pickup_pin);
        HeadTitle = (TextView) findViewById(R.id.headerTxt);
        CancelTxt = (TextView) findViewById(R.id.cancelTxtnew);
        TripcancelTxt = (TextView) findViewById(R.id.TripcancelTxt);
        WaitingTxt = (TextView) findViewById(R.id.waitingTxtnew);
        nodataTxt = (TextView) findViewById(R.id.nodataTxt);
        butt_onboard = (Button) findViewById(R.id.butt_onboard);
        HeadTitle.setText(" " + NC.getResources().getString(R.string.app_name));
        speed_lay = (LinearLayout) findViewById(R.id.timerlayout);
        speedtext = (TextView) findViewById(R.id.cancelTxtnew);
        speed_txt = (TextView) findViewById(R.id.km_txt);
        km_lay = (LinearLayout) findViewById(R.id.km_lay);
        total_km = (TextView) findViewById(R.id.total_km);
        total_km_val = (TextView) findViewById(R.id.total_km_val);
        backup = (TextView) findViewById(R.id.backup);
        dropp = (RelativeLayout) findViewById(R.id.dropp);
        CurrentlocationTxt = (TextView) findViewById(R.id.currentlocTxt);
        droplocationTxt = (TextView) findViewById(R.id.droplocTxt);
        CurrentlocationTxt.setSelected(true);
        droplocationTxt.setSelected(true);
        passnameTxt = (TextView) findViewById(R.id.passnameTxt);
        proimg = (RoundedImageView) findViewById(R.id.proimg);
        passengerphoneTxt = (TextView) findViewById(R.id.phoneTxt);
        back_main = (Button) findViewById(R.id.back_main);
        nodataLay = (LinearLayout) findViewById(R.id.nodataLay);
        back_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(OngoingAct.this);
                startActivity(new Intent(OngoingAct.this, MyStatus.class));
                finish();
            }
        });
        // Glide.with(this).load(SessionSave.getSession("image_path",this)+"callDriver.png").placeholder(R.drawable.phone_bg).error(R.drawable.phone_bg).into((TextView) ((TextView) findViewById(R.id.phoneTxt)).setCompoundDrawables(););

        backup.setVisibility(View.GONE);

        Glide.with(OngoingAct.this)
                .load(SessionSave.getSession("image_path", OngoingAct.this) + "callDriver.png")
                .asBitmap().signature(new StringSignature("35"))
                .into(new SimpleTarget<Bitmap>((int) pxtoDp(100), (int) pxtoDp(100)) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        passengerphoneTxt.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(passengerphoneTxt.getResources(), resource), null, null, null);
                    }
                });


        Glide.with(OngoingAct.this)
                .load(SessionSave.getSession("image_path", OngoingAct.this) + "tripCancel.png")
                .asBitmap().signature(new StringSignature("34"))
                .into(new SimpleTarget<Bitmap>((int) pxtoDp(100), (int) pxtoDp(100)) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        TripcancelTxt.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(TripcancelTxt.getResources(), resource), null, null, null);

                    }
                });
        System.out.println("imagepatchhh" + SessionSave.getSession("image_path", OngoingAct.this) + "tripCancel.png");
        // Log.e("imagepath_", SessionSave.getSession("image_path", OngoingAct.this) + "cancelDriver.png");

        tripInfo = (LinearLayout) findViewById(R.id.tripdetail_layout);
        butt_navigator = (ImageView) findViewById(R.id.butt_navigator);

        //butt_navigator.
        Glide.with(this).load(SessionSave.getSession("image_path", this) + "mapDirection.png").placeholder(R.drawable.gps_navigator).error(R.drawable.gps_navigator).into((ImageView) findViewById(R.id.butt_navigator));
        // minMaxBtn = (TextView) findViewById(R.id.close_icon);
        tv_notes = (TextView) findViewById(R.id.notes);
        butt_onboard.setVisibility(View.VISIBLE);
        mapsupport_lay = (RelativeLayout) findViewById(R.id.mapsupport_lay);
        navigator_layout = (RelativeLayout) findViewById(R.id.botton_layout);
//        if (SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
//            total_km.setText("Total KM");
//        } else {
//            total_km.setText("Total Miles");
//        }
        final LinearLayout infoLayout = (LinearLayout) findViewById(R.id.info_layout);
        final TextView mapInfoTxt = (TextView) findViewById(R.id.mapinfo_txt);
        // This onclick method used to hide the passenger info view.
        mapInfoTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {

                tripInfo.setVisibility(View.VISIBLE);
                infoLayout.setVisibility(View.GONE);
            }
        });
        // This onclick method used to show the passenger info view.
        // Following set of code to initialize and google map.

        // This onclick method used to make a call to passenger.
        passengerphoneTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    if (MainActivity.mMyStatus.getpassengerphone().length() == 0)
                        alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.invalid_mobile_number), "" + NC.getResources().getString(R.string.ok), "");
                    else {
                        final Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + MainActivity.mMyStatus.getpassengerphone()));
                        if (ActivityCompat.checkSelfPermission(OngoingAct.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(OngoingAct.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling

                            ActivityCompat.requestPermissions(OngoingAct.this,
                                    new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_CALL);
                            return;
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //Call passenger
                                    ensureCall();
                                }
                            });

                        }

                        //startActivity(callIntent);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        // This onclick method used to cancel the current ongoing trip.
        TripcancelTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {

                try {
                    final View view = View.inflate(OngoingAct.this, R.layout.netcon_lay, null);
                    mcancelDialog = new Dialog(OngoingAct.this, R.style.dialogwinddow);
                    mcancelDialog.setContentView(view);
                    mcancelDialog.setCancelable(true);
                    mcancelDialog.show();
                    FontHelper.applyFont(OngoingAct.this, mcancelDialog.findViewById(R.id.alert_id));
                    Colorchange.ChangeColor((ViewGroup) mcancelDialog.findViewById(R.id.alert_id), OngoingAct.this);
                    final TextView title_text = (TextView) mcancelDialog.findViewById(R.id.title_text);
                    final TextView message_text = (TextView) mcancelDialog.findViewById(R.id.message_text);
                    final Button button_success = (Button) mcancelDialog.findViewById(R.id.button_success);
                    final Button button_failure = (Button) mcancelDialog.findViewById(R.id.button_failure);
                    button_failure.setVisibility(View.VISIBLE);
                    title_text.setText("" + NC.getResources().getString(R.string.message));
                    message_text.setText("" + NC.getResources().getString(R.string.cancel_in_going_trip));
                    button_success.setText("" + NC.getResources().getString(R.string.yes));
                    button_failure.setText("" + NC.getResources().getString(R.string.no));
                    button_success.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            // TODO
                            // Auto-generated
                            // method stub
                            try {
                                mcancelDialog.dismiss();
                                // TODO Auto-generated method stub
                                if (SessionSave.getSession("status", OngoingAct.this).equalsIgnoreCase("A"))
                                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.you_are_in_trip), "" + NC.getResources().getString(R.string.ok), "");
                                else if (SessionSave.getSession("trip_id", OngoingAct.this).length() == 0)
                                    finish();
                                else {
                                    nonactiityobj.stopServicefromNonActivity(OngoingAct.this);
                                    JSONObject j = new JSONObject();
                                    j.put("pass_logid", SessionSave.getSession("trip_id", OngoingAct.this));
                                    j.put("driver_id", SessionSave.getSession("Id", OngoingAct.this));
                                    j.put("taxi_id", SessionSave.getSession("taxi_id", OngoingAct.this));
                                    j.put("company_id", SessionSave.getSession("company_id", OngoingAct.this));
                                    j.put("driver_reply", "C");
                                    j.put("field", "");
                                    j.put("flag", "1");
                                    final String canceltrip_url = "type=driver_reply";
                                    new CancelTrip(canceltrip_url, j);
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
                    });
                    button_failure.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            // TODO
                            // Auto-generated
                            // method stub
                            mcancelDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        // This onclick method used to move from this activity to home activity.
        backup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {

                showLoading(OngoingAct.this);
                try {
                    stopLocationUpdates();
                    map = null;
                    if (c_marker != null && a_marker != null) {
                        c_marker = null;
                        a_marker = null;
                    }
                } catch (Exception ex) {
                }
                backup.setEnabled(false);
                Intent jobintent = new Intent(OngoingAct.this, MyStatus.class);
                startActivity(jobintent);
                finish();
            }
        });
        // This onclick method used to move navigator application with pickup and drop place lat/lng.
        navigator_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    //     Log.e("URL_Test" + mMyStatus.getOnstatus(), "hai");
                    if (mMyStatus.getOnstatus().equalsIgnoreCase("Complete")) {
                        if (pickupLatLng.latitude != 0.0 && pickupLatLng.longitude != 0.0) {
                            //                                final String locationurl = "http://maps.google.com/maps?saddr=" + latitude1 + "," + longitude1 + "&daddr=" + mMyStatus.getOnpickupLatitude() + "," + mMyStatus.getOnpickupLongitude() + "";
                            String locationurl = "";
                            if (dropLatLng != null) {
                                locationurl = "http://maps.google.com/maps?saddr=" + pickupLatLng.latitude + "," + pickupLatLng.longitude + "&daddr=" + dropLatLng.latitude + "," + dropLatLng.longitude + "";
                                //    Log.e("URL_Test*" + mMyStatus.getOnstatus(), locationurl);
                            } else {
                                locationurl = "http://maps.google.com/maps?saddr=" + pickupLatLng.latitude + "," + pickupLatLng.longitude + "";
                                //Log.e("URL_Test**" + mMyStatus.getOnstatus(), locationurl);
                            }
                            // Log.e("URL_Test***" + mMyStatus.getOnstatus(), locationurl);
                            final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(locationurl));
                            startActivity(intent);
                        }
                    }

//                    else {
//                        if (mMyStatus.getOnstatus().equalsIgnoreCase("On")) {
//                            if (mMyStatus.getOnpickupLatitude() != null && mMyStatus.getOnpickupLongitude() != null) {
//                                if ( mLastLocation.getLatitude() != 0.0 && longitude1 != 0.0 && mMyStatus.getOnpickupLatitude() != "0.0" && mMyStatus.getOnpickupLongitude() != "0.0") {
//                                    //                                final String locationurl = "http://maps.google.com/maps?saddr=" + latitude1 + "," + longitude1 + "&daddr=" + mMyStatus.getOnpickupLatitude() + "," + mMyStatus.getOnpickupLongitude() + "";
//                                    final String locationurl = "http://maps.google.com/maps?saddr=" + mLastLocation.getLatitude() + "," +  mLastLocation.getLongitude() + "&daddr=" + (mMyStatus.getOnpickupLatitude() + "," + mMyStatus.getOnpickupLongitude()) + "";
//                                    Log.e("URL_Test", locationurl);
//
//                                    final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(locationurl));
//                                    startActivity(intent);
//                                }
//                            }
//                        }


                    else {
                        if (mLastLocation.getLatitude() != 0.0 && mLastLocation.getLongitude() != 0.0 && pickupLatLng.latitude != 0.0 && pickupLatLng.longitude != 0.0) {
                            //                                final String locationurl = "http://maps.google.com/maps?saddr=" + latitude1 + "," + longitude1 + "&daddr=" + mMyStatus.getOnpickupLatitude() + "," + mMyStatus.getOnpickupLongitude() + "";
                            final String locationurl = "http://maps.google.com/maps?saddr=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "&daddr=" + pickupLatLng.latitude + "," + pickupLatLng.longitude + "";
                            //  Log.e("URL_Test" + mMyStatus.getOnstatus(), locationurl);

                            final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(locationurl));
                            startActivity(intent);
                        }
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        // This onclick method used to handle the three state in ongoing trip page(Arrived,Start and End).In each phase will use different API.
        butt_onboard.setOnClickListener(new OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(final View v) {

                System.out.println("mMyStatus" + MainActivity.mMyStatus.getOnstatus());
                try {
                    butt_onboard.setClickable(false);
                    // If the trip in accepted not get arrived
                    if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("On")) {
                        //nonactiityobj.stopServicefromNonActivity(OngoingAct.this);
                        WaitingTimerRun.ClearSession(OngoingAct.this);
                        JSONObject jDriverArrived = new JSONObject();
                        jDriverArrived.put("trip_id", SessionSave.getSession("trip_id", OngoingAct.this));
                        final String arrived_url = "type=driver_arrived";
                        new DriverArrived(arrived_url, jDriverArrived);
                    }
                    // If trip in arrived state and going to start the trip
                    else if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Arrivd")) {
                        retrycount = 1;
                        //SessionSave.saveSession("locationhistory", "", getApplicationContext());
                        //                        nonactiityobj.stopServicefromNonActivity(OngoingAct.this);
                        if (latitude1 != 0.0 && longitude1 != 0.0) {
                            JSONObject jstart = new JSONObject();
                            jstart.put("driver_id", SessionSave.getSession("Id", OngoingAct.this));
                            jstart.put("latitude", latitude1);
                            jstart.put("longitude", longitude1);
                            jstart.put("status", "A");
                            jstart.put("trip_id", SessionSave.getSession("trip_id", OngoingAct.this));
                            final String driver_status_update = "type=driver_status_update";
                            SessionSave.saveSession("slat", "" + latitude1, OngoingAct.this);
                            SessionSave.saveSession("slng", "" + longitude1, OngoingAct.this);
                            CommonData.last_getlatitude = latitude1;
                            CommonData.last_getlongitude = longitude1;
                            new Onboard(driver_status_update, jstart);
                            actual_p_latitude = latitude1;
                            actual_p_longtitude = longitude1;
                        } else {
                            SessionSave.saveSession("locationhistory", "", getApplicationContext());

                            SessionSave.setDistance((float) 0.0, getApplicationContext());
                            JSONObject jstart = new JSONObject();
                            jstart.put("driver_id", SessionSave.getSession("Id", OngoingAct.this));
                            jstart.put("latitude", "");
                            jstart.put("longitude", "");
                            jstart.put("status", "A");
                            jstart.put("trip_id", SessionSave.getSession("trip_id", OngoingAct.this));
                            final String driver_status_update = "type=driver_status_update";
                            new Onboard(driver_status_update, jstart);
                        }
                    }
                    // If trip in progress and going to end the trip
                    else if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Complete")) {
                        butt_onboard.setClickable(true);
//                        new Address_s(OngoingAct.this, new LatLng((latitude1), (longitude1))).execute();

                        if (NetworkStatus.isOnline(OngoingAct.this)) {
                            CompleteTrip(OngoingAct.this);
                        } else {
                            ShowToast(OngoingAct.this, "" + getResources().getString(R.string.check_net_connection));
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    butt_onboard.setClickable(true);
                }
            }
        });


        // getAndStoreStringValues(SessionSave.getSession("wholekey", OngoingAct.this));
    }

    public float pxtoDp(int px) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        System.out.println("pxxxxxx" + dp + "___" + px + "__" + metrics.densityDpi);
//    if(metrics.densityDpi<=250){
//        System.out.println("pxxxxxxT"+dp+"___"+px+"__"+metrics.densityDpi);
//        return (25);}
        return dp;

    }

    @Override
    public void onResume() {
        super.onResume();
        //LocalDistanceCalculation.registerDistanceInterface(OngoingAct.this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            gpscheckalert(OngoingAct.this, false);
        }

    }

    public static void gpscheckalert(final Context mContext, boolean isconnect) {

        if (!isconnect) {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            gpsdialog = new Dialog(mContext, R.style.NewDialog);
            gpsdialog.setContentView(view);
            FontHelper.applyFont(mContext, gpsdialog.findViewById(R.id.alert_id));
            gpsdialog.setCancelable(false);
            if (mContext instanceof SplashAct) {
                LinearLayout sub_can = (LinearLayout) gpsdialog.findViewById(R.id.sub_can);
                sub_can.setPadding(0, 10, 0, 10);
            }
            if (!gpsdialog.isShowing())
                gpsdialog.show();
            final TextView title_text = (TextView) gpsdialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) gpsdialog.findViewById(R.id.message_text);
            final Button button_success = (Button) gpsdialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) gpsdialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText("" + NC.getResources().getString(R.string.location_disable));
            String message = "";
            if (!isNetworkEnabled(mContext))
                message = mContext.getString(R.string.location_enable);
            else
                message = mContext.getString(R.string.change_network);

            message_text.setText("" + message);
            button_success.setText("" + NC.getResources().getString(R.string.enable));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(mIntent);
                    gpsdialog.dismiss();
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    gpsdialog.dismiss();
                }
            });
        } else {
            try {
                //          System.out.println("________called" + SessionSave.getSession("trip_id", mContext));
                if (!SessionSave.getSession("Id", mContext).trim().equals("") &&
                (SessionSave.getSession("shift_status", mContext).equals("IN"))) {
                    Intent locationService = new Intent(mContext, LocationUpdate.class);
                    mContext.startService(new Intent(locationService));
                }
                gpsdialog.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
                //				e.printStackTrace();
            }
        }
    }

    /**
     * This method is called once the map initializtion is ready
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        try {
            final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(OngoingAct.this);
            if (resultCode == ConnectionResult.SUCCESS) {
                new Getimg().execute();

                System.gc();
                MapsInitializer.initialize(OngoingAct.this);
                mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
                mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));
                map.getUiSettings().setZoomControlsEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setMyLocationEnabled(false);
                map.setPadding(0, 0, 0, 120);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mapsupport_lay.setVisibility(View.VISIBLE);
                nodataTxt.setVisibility(View.GONE);
                nodataLay.setVisibility(View.GONE);

                System.err.println("animate camera:" + latitude1 + "lng" + longitude1);

             //   map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocationUpdate.latitude1, LocationUpdate.longitude1), zoom));
                if (mapWrapperLayout != null && !mapWrapperLayout.isShown())
                    mapWrapperLayout.setVisibility(View.VISIBLE);
            } else {
                mapsupport_lay.setVisibility(View.GONE);
                nodataTxt.setVisibility(View.VISIBLE);
                nodataLay.setVisibility(View.VISIBLE);
                nodataTxt.setText("" + "" + NC.getResources().getString(R.string.device_not_support_map));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    //// elumalai.......................29-05-2018

   /* @Override
    public void haversineResult(Boolean success) {

       *//* if (success) {
            //do the complete trip process....
            CompleteSuccessClick();
        }*//*

    }*/


   /* @Override
    public void positiveButtonClick(DialogInterface dialog, int id, String s) {
        switch (s) {
            case "1":
                try {
                    dialog.dismiss();
                    final Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + MainActivity.mMyStatus.getpassengerphone()));
                    startActivity(callIntent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                break;
            case "2":
                dialog.dismiss();
//                WaitingTimerRun.ClearSession(OngoingAct.this);
                butt_onboard.setClickable(true);
                showLoading(OngoingAct.this);
                mLastLocation = GetCurrentLocationProvider();
                if (mLastLocation != null) {

                    latitude1 = mLastLocation.getLatitude();
                    longitude1 = mLastLocation.getLongitude();
                    new Address_s(OngoingAct.this, new LatLng((latitude1), (longitude1))).execute();
                } else {
                    cancelLoading();
                    RetryLocationPopUp();
                }
                break;
            case "3":
                try {
                    dialog.dismiss();
                    // TODO Auto-generated method stub
                    if (SessionSave.getSession("status", OngoingAct.this).equalsIgnoreCase("A"))
                        Utils.alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.you_are_in_trip), "" + NC.getResources().getString(R.string.ok), "", true, OngoingAct.this, "4");
                    else if (SessionSave.getSession("trip_id", OngoingAct.this).length() == 0)
                        finish();
                    else {
                        nonactiityobj.stopServicefromNonActivity(OngoingAct.this);
                        JSONObject j = new JSONObject();
                        j.put("pass_logid", SessionSave.getSession("trip_id", OngoingAct.this));
                        j.put("driver_id", SessionSave.getSession("Id", OngoingAct.this));
                        j.put("taxi_id", SessionSave.getSession("taxi_id", OngoingAct.this));
                        j.put("company_id", SessionSave.getSession("company_id", OngoingAct.this));
                        j.put("driver_reply", "C");
                        j.put("field", "");
                        j.put("flag", "1");
                        final String canceltrip_url = "type=driver_reply";
                        new CancelTrip(canceltrip_url, j);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                break;
            case "4":
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void negativeButtonClick(DialogInterface dialog, int id, String s) {
        switch (s) {
            case "1":
                dialog.dismiss();
                break;
            case "2":
                butt_onboard.setClickable(true);
                dialog.dismiss();
                break;
            case "3":
                dialog.dismiss();
                break;
            default:
                break;
        }
    }*/

    @Override
    public void onCameraMoveStarted(int i) {
        if (!MapWrapperLayout.ismMapIsTouched()) {
            navigator_layout.setVisibility(View.GONE);
            butt_navigator.setVisibility(View.VISIBLE);         ////ewwwwww
        } else {
            navigator_layout.setVisibility(View.VISIBLE);
            butt_navigator.setVisibility(View.GONE);
        }
    }


    /**
     * This class is used to parse address by given latlon
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
            geocoder = new Geocoder(context, Locale.getDefault());
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            SessionSave.saveSession("notes", "", mContext);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                System.out.println("address size11:" + latitude + "%$#" + longitude);

                if (Geocoder.isPresent()) {
                    System.out.println("address size:" + latitude + "%$#" + longitude);
                    addresses = geocoder.getFromLocation(latitude, longitude, 3);
                    System.out.println("address size:" + addresses.size());
                    if (addresses.size() == 0) {
                        new convertLatLngtoAddressApi(mContext, latitude, longitude);
                    } else {
                        for (int i = 0; i < addresses.size(); i++) {
                            Address += addresses.get(0).getAddressLine(i) + ", ";
                        }
                        if (Address.length() > 0)
                            Address = Address.substring(0, Address.length() - 2);
                    }
                } else {
                    if (NetworkStatus.isOnline(mContext))
                        new convertLatLngtoAddressApi(mContext, latitude, longitude);
//                else {
//                    BookTaxiNewFrag.setfetch_address();
//                }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (NetworkStatus.isOnline(mContext))
                    new convertLatLngtoAddressApi(mContext, latitude, longitude);
//            else {
//                BookTaxiNewFrag.setfetch_address();
//            }
            }
            return Address;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // TaxiUtil.pAddress = "" + Address;
            if (Address.length() != 0) {
                System.out.println("address size11:" + latitude + "%$#" + longitude + Address);
                SessionSave.saveSession("drop_location", Address, OngoingAct.this);
                CompleteTrip(OngoingAct.this);
                // LocalDistanceCalculation.newInstance(OngoingAct.this).haversine(lastknownlatitude, lastknowlongitude, latitude1, longitude1);

//                String lat = details.getStringExtra("lat");
//                String lon = details.getStringExtra("lon");
//                String distance = details.getStringExtra("distance");
//                String waitingHr = details.getStringExtra("waitingHr");
//                String drop_location = details.getStringExtra("drop_location");
//                String url = "type=complete_trip";
//                new CompleteTrip(url, lat, lon, distance, waitingHr, Address);
                // BookTaxiNewFrag.sf.setPickuplocTxt(Address);
//                BookTaxiNewFrag.sf.setPickuplat(latitude);
//                BookTaxiNewFrag.sf.setPickuplng(longitude);
            } else {
                //  BookTaxiNewFrag.setfetch_address();
            }
            result = null;

        }


        /**
         * This class  is used to convert latlon to address
         */
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

        public void setLocation(String inputJson) {

            try {
                JSONObject object = new JSONObject("" + inputJson);
                JSONArray array = object.getJSONArray("results");
                object = array.getJSONObject(0);
                System.out.println("address size11:" + latitude + "%$#" + longitude + object.getString("formatted_address"));
                SessionSave.saveSession("drop_location", object.getString("formatted_address"), OngoingAct.this);
                //   BookTaxiNewFrag.sf.setPickuplocTxt(object.getString("formatted_address"));
                CompleteTrip(OngoingAct.this);
//                String lat = details.getStringExtra("lat");
//                String lon = details.getStringExtra("lon");
//                String distance = details.getStringExtra("distance");
//                String waitingHr = details.getStringExtra("waitingHr");
//                String drop_location = details.getStringExtra("drop_location");
//                String url = "type=complete_trip";
//                new CompleteTrip(url, lat, lon, distance, waitingHr, object.getString("formatted_address"));
            } catch (Exception ex) {
                ex.printStackTrace();
                if (mContext != null)
                    if (!NetworkStatus.isOnline(mContext))
                        Toast.makeText(mContext, NC.getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, NC.getString(R.string.c_tryagain), Toast.LENGTH_SHORT).show();
                //  BookTaxiNewFrag.setfetch_address();
            }

        }


        /**
         * This method  is used to save drop location and complete trip
         */
      /*  public void setLocation(String inputJson) {

            try {
                JSONObject object = new JSONObject("" + inputJson);
                JSONArray array = object.getJSONArray("results");
                object = array.getJSONObject(0);
                System.out.println("address size11:" + latitude + "%$#" + longitude + object.getString("formatted_address"));
                SessionSave.saveSession("drop_location", object.getString("formatted_address"), OngoingAct.this);
                //   BookTaxiNewFrag.sf.setPickuplocTxt(object.getString("formatted_address"));
                CompleteTrip(OngoingAct.this);
//                String lat = details.getStringExtra("lat");
//                String lon = details.getStringExtra("lon");
//                String distance = details.getStringExtra("distance");
//                String waitingHr = details.getStringExtra("waitingHr");
//                String drop_location = details.getStringExtra("drop_location");
//                String url = "type=complete_trip";
//                new CompleteTrip(url, lat, lon, distance, waitingHr, object.getString("formatted_address"));
            } catch (Exception ex) {
                ex.printStackTrace();
                GEOCODE_EXPIRY = true;
                if (mContext != null)
                    if (!NetworkStatus.isOnline(mContext))
                        Toast.makeText(mContext, NC.getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, NC.getString(R.string.c_tryagain), Toast.LENGTH_SHORT).show();
                //  BookTaxiNewFrag.setfetch_address();
            }
        }*/

    }

    /**
     * This is method for confirmation for complete the trip
     */
    public void CompleteTrip(final Context context) {
        try {
            if (cDialog != null)
                cDialog.dismiss();
            final View view = View.inflate(OngoingAct.this, R.layout.netcon_lay, null);
            cDialog = new Dialog(OngoingAct.this, R.style.dialogwinddow);
            cDialog.setContentView(view);
            cDialog.setCancelable(false);
            cDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FontHelper.applyFont(OngoingAct.this, cDialog.findViewById(R.id.alert_id));
        final TextView title_text = (TextView) cDialog.findViewById(R.id.title_text);
        final TextView message_text = (TextView) cDialog.findViewById(R.id.message_text);
        final Button button_success = (Button) cDialog.findViewById(R.id.button_success);
        final Button button_failure = (Button) cDialog.findViewById(R.id.button_failure);
        button_failure.setVisibility(View.VISIBLE);
        title_text.setText("" + NC.getResources().getString(R.string.message));
        message_text.setText("" + NC.getResources().getString(R.string.confirm_complete));
        button_success.setText("" + NC.getResources().getString(R.string.yes));
        button_failure.setText("" + NC.getResources().getString(R.string.no));
        button_success.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO
                // Auto-generated
                // method stub
                butt_onboard.setClickable(true);
                try {
                    System.out.println("Errror in okkk");
                    //  nonactiityobj.stopServicefromNonActivity(OngoingAct.this);
                    MainActivity.mMyStatus.setOnstatus("Complete");
                    stopService(new Intent(OngoingAct.this, WaitingTimerRun.class));
                    myHandler.removeCallbacks(r);
                    float h = 0.0f;
                    if (waitingTime.equals(""))
                        waitingTime = "00:00:00";
                    String waitNoArabic = FontHelper.convertfromArabic(waitingTime);
                    System.out.println("Errror in okkkk" + waitNoArabic + "---" + waitingTime);
                    String[] split = waitNoArabic.split(":");
                    int hr = Integer.parseInt(split[0]);
                    float min = Integer.parseInt(split[1]);
                    float sec = Float.parseFloat(split[2]);
                    System.out.println("Hour:" + hr + "min:" + min + "sec:" + sec);
                    min = (Float) min / 60;
                    sec = sec / 3600;
                    waitingHr = hr + min + sec;
                    MainActivity.mMyStatus.setDriverWaitingHr(Float.toString(waitingHr));
                    SessionSave.saveSession("waitingHr", Float.toString(waitingHr), OngoingAct.this);
                    // SessionSave.saveSession("drop_location", SessionSave.getSession("Driver_locations_home", OngoingAct.this), OngoingAct.this);
                    cDialog.dismiss();

                    if (NetworkStatus.isOnline(OngoingAct.this)) {
                        if (actual_p_latitude == null && !SessionSave.getSession("slat", OngoingAct.this).isEmpty()) {
                            actual_p_latitude = Double.parseDouble(SessionSave.getSession("slat", OngoingAct.this));
                            actual_p_longtitude = Double.parseDouble(SessionSave.getSession("slng", OngoingAct.this));
                        }


                        if (latitude1 != 0.0 && actual_p_latitude != 0.0) {

                            new FindApproxDistance(actual_p_latitude, actual_p_longtitude, latitude1, longitude1, 2);
                        } else {
                            ShowToast(OngoingAct.this, getString(R.string.server_error));
                        }
                    } else {

                        ShowToast(OngoingAct.this, getString(R.string.check_net_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Errror in okkkk" + e);
                    // TODO: handle exception
                }
            }
        });
        button_failure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO
                // Auto-generated
                // method stub
                butt_onboard.setClickable(true);
                cDialog.dismiss();
            }
        });
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


    /**
     * Onstart method by default it called when activity is open.
     */
    @Override
    public void onStart() {

        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

//    protected void startLocationUpdates() {
//        try {
//            if (mGoogleApiClient != null && mLocationRequest != null && mGoogleApiClient.isConnected())
//                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(OngoingAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(OngoingAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(OngoingAct.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GPS);

            return;
        } else {
            getGPS();
        }

    }


    /**
     * This method is used to get gps connection
     */
    private void getGPS() {
        try {
            if (mGoogleApiClient == null)
                System.out.println("gpsnull");
            if (mLocationRequest == null)
                System.out.println("locnull");
            if (mGoogleApiClient.isConnected())
                System.out.println("connnnull");
            if (mGoogleApiClient != null && mLocationRequest != null && mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, OngoingAct.this);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {

        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        stopLocationUpdates();
        myHandler.removeCallbacks(r);
        String status = "" + WaitingTxt.getTag().toString();
//        SessionSave.setWaitingTime(WaitingTimerRun.finalTime, OngoingAct.this);//
        System.out.println("timer  ongoing  destroy" + WaitingTimerRun.finalTime);
        if (CommonData.speed_waiting_stop) {
            //            stopService(new Intent(OngoingAct.this, TimerRun.class));
            myHandler.removeCallbacks(t);
        }
        if (animator != null && animator.isRunning()) {

            animator.cancel();
            map = null;
            if (c_marker != null) {
                c_marker.setVisible(false);
                c_marker.remove();
            } else if (a_marker != null) {
                a_marker = null;
            }
        }
    }


    /**
     * This method is store string values in local
     */
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
                    if (element2.getAttribute("name").equals("pressBack"))

                        //  NC.nfields_value.add(element2.getTextContent());
                        NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

//                System.out.println("nagaSsss___"+element2.getAttribute("name"));
//                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
//                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
                }
            }
            getValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to get string details
     */
    synchronized void getValueDetail() {
        Field[] fieldss = R.string.class.getDeclaredFields();
        // fields =new int[fieldss.length];
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "string", getPackageName());
            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
                NC.fields.add(fieldss[i].getName());
                NC.fields_value.add(getResources().getString(id));
                NC.fields_id.put(fieldss[i].getName(), id);

            } else {
            }
//        for(int h=0;h<NC.nfields_byName.size();h++){
//            System.out.println("NagarrrHIIIIIII&&&"+fields_id.get(NC.nfields_byName.get(h))+"____"+NC.nfields_byName.get(h));
//        NC.nfields_byID.put(fields_id.get(NC.nfields_byName.get(h)),NC.nfields_byName.get(h));}
        }


        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            NC.nfields_byID.put(NC.fields_id.get(h), NC.nfields_byName.get(h));
            // do stuff
        }

    }


    /**
     * To get current location as address.
     */
    private void location() {

        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            if (mLastLocation != null) {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            final Address address = addresses.get(0);
            Address = getString(R.string.address_output_string, address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality(), address.getCountryName());
            // SessionSave.saveSession("drop_location", Address, OngoingAct.this);
        } else
            Address = "";
    }


    /**
     * To get pickup/drip location as address and place the pickup/drop markers on map.
     */
    private class GetPickdropLoc extends AsyncTask<Void, Void, Void> {
        private LatLng HAMBURG;

        @Override
        protected Void doInBackground(final Void... params) {
            // TODO Auto-generated method stub
            try {
                location();
            } catch (final Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {

            super.onPostExecute(result);
            try {
                map.clear();
                startLocationUpdates();
                System.err.println("mLastLocation" + mLastLocation);
                if (mLastLocation != null) {
                    latitude1 = mLastLocation.getLatitude();
                    longitude1 = mLastLocation.getLongitude();
                    bearing = mLastLocation.getBearing();
                    currentLatLng = new LatLng(latitude1, longitude1);
                }
                if (bearing >= 0)
                    bearing = bearing + 90;
                else
                    bearing = bearing - 90;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                HAMBURG = new LatLng(latitude1, longitude1);
                bearing = 0;
                route = new Route();
                pickUpDropMarker();
//                if (p_latitude != null && p_latitude != 0.0 && p_longtitude != null && p_longtitude != 0.0) {
//                    p_marker = map.addMarker(new MarkerOptions().position(new LatLng(p_latitude, p_longtitude)).title("" + NC.getResources().getString(R.string.pickuploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(true));
//                    pickupLatLng = new LatLng(p_latitude, p_longtitude);
//                }
//                if (d_latitude != null && d_latitude != 0.0 && d_longtitude != null && d_longtitude != 0.0) {
//                    int px = getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
//                    Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
//                    Canvas canvas = new Canvas(mDotMarkerBitmap);
//                    Drawable shape = getResources().getDrawable(R.drawable.cust_progress);
//                    shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
//                    shape.draw(canvas);
//                    d_marker = map.addMarker(new MarkerOptions().position(new LatLng(d_latitude, d_longtitude)).flat(true).title("" + NC.getResources().getString(R.string.droploc)).icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)).draggable(true));
//                    dropLatLng = new LatLng(d_latitude, d_longtitude);
//                }
                if (driver_latitude != null && driver_latitude != 0.0 && driver_longtitude != null && driver_longtitude != 0.0) {
                    currentLatLng = new LatLng(driver_latitude, driver_longtitude);
                }
                // System.out.println("HHHHHHHHHHHHHHHH++++1");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ROUTE_DRAW_ON_START = true;
                        mHandler.sendEmptyMessage(1);
                    }
                }, 5000);

                if (!Address.equals("")) {
                    //  SessionSave.saveSession("drop_location", Address, OngoingAct.this);
                    MainActivity.mMyStatus.setOndropLocation(Address);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void pickUpDropMarker() {
        try {
            if (map != null) {
                if (p_latitude != null && p_latitude != 0.0 && p_longtitude != null && p_longtitude != 0.0) {
                    if (p_marker != null)
                        p_marker.remove();
                    p_marker = map.addMarker(new MarkerOptions().position(new LatLng(p_latitude, p_longtitude)).title("" + NC.getResources().getString(R.string.pickuploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(true));
                    pickupLatLng = new LatLng(p_latitude, p_longtitude);
                }
                if (d_latitude != null && d_latitude != 0.0 && d_longtitude != null && d_longtitude != 0.0) {
                    if (d_marker != null)
                        d_marker.remove();
                    int px = getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
                    Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(mDotMarkerBitmap);
                    Drawable shape = getResources().getDrawable(R.drawable.cust_progress);
                    shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
                    shape.draw(canvas);

                    d_marker = map.addMarker(new MarkerOptions().position(new LatLng(d_latitude, d_longtitude)).flat(true).title("" + NC.getResources().getString(R.string.droploc)).icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)).draggable(true));
                    dropLatLng = new LatLng(d_latitude, d_longtitude);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private final Runnable t = new Runnable() {
        @Override
        public void run() {

            CancelTxt.setText(String.format(Locale.UK, TimerRun.sTimer));
            myHandler.postDelayed(t, 0);
            waitingTime = TimerRun.sTimer;
        }
    };
    private long locationUpdatedAt = Long.MIN_VALUE;
    private double lastlatitude = 0.0;
    private double lastlongitude = 0.0;
    private Location currentLocation;
    /**
     * The thread for update the taxi speed,waiting time and distance traveled on UI.
     */
    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            String metricss = "";
            if (SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
                metricss = " KM/hr";
            } else {
                metricss = " Miles/hr";
            }
            HeadTitle.setText(" " + NC.getResources().getString(R.string.ongoing_journey) + "\n" + speed + metricss);
            speedtext.setText(String.format(Locale.UK, WaitingTimerRun.sTimer));
            //speedtext.setText(String.format(Locale.ENGLISH, "x%d", WaitingTimerRun.sTimer));
            speedlimit = speed_txt.getText().toString();

            double totalKmValue = 0.0;
            if (CommonData.travel_km != 0) {
//                if (SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
//                    total_km.setText("Total KM");
//                    totalKmValue = CommonData.travel_km;
//                } else {
//                    total_km.setText("Total Miles");
//                    totalKmValue = (CommonData.travel_km / 1.60934);
//                   // totalKmValue =    CommonData.travel_km;
//                    System.out.println(CommonData.travel_km+"___"+totalKmValue);
//                }


//                totalKmValue = CommonData.travel_km;
//                totalkm = String.format("%.2f", totalKmValue);
//                total_km_val.setText("" + String.format(Locale.UK, "%.2f", totalKmValue));
//                backup.setVisibility(View.INVISIBLE);
            }
            //   Log.w("Speed:" + CommonData.travel_km, "" + speed + "taxi_speed" + SessionSave.getSession("taxi_speed", OngoingAct.this));


            double p_minspeed = Double.parseDouble(SessionSave.getSession("taxi_speed", OngoingAct.this));

            waitingTime = WaitingTimerRun.sTimer;

            //timerrrrrr speeee
            //  if(f)
            if (p_minspeed >= speed) {
                //  speed =20;
                CommonData.km_calc = 0;
                if (CommonData.speed_waiting_stop) {

                    final Intent i = new Intent(OngoingAct.this, WaitingTimerRun.class);
                    startService(i);
                    myHandler.postDelayed(r, 0);
                    speedtext.setText(String.format(Locale.UK, WaitingTimerRun.sTimer));
                } else {
                    speedtext.setText(String.format(Locale.UK, WaitingTimerRun.sTimer));
                    LowSpeed = speedtext.getText().toString();
                }
            } else {

                //  System.out.println("timer started ongoing" + SessionSave.getWaitingTime(OngoingAct.this));

                CommonData.km_calc = 1;
                stopService(new Intent(OngoingAct.this, WaitingTimerRun.class));
                //    myHandler.removeCallbacks(r);
                speedtext.setText(String.format(Locale.UK, WaitingTimerRun.sTimer));
                LowSpeed = speedtext.getText().toString();

            }
//            /** Below code for add the distance with current runnable distance from fleet **/
//            String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//            System.out.println("mLastUpdateTime ---  " + mLastUpdateTime);
//            if (mLastLocation != null && mLastLocation.getSpeed() > 5 && mLastLocation.hasAccuracy() && mLastLocation.getAccuracy() <= 50) {
//                boolean updateLocationandReport = false;
//                if (currentLocation == null) {
//                    currentLocation = mLastLocation;
//                    locationUpdatedAt = System.currentTimeMillis();
//                    updateLocationandReport = true;
//                } else {
//                    long secondsElapsed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - locationUpdatedAt);
//                    if (secondsElapsed >= 3) {
//                        // check location accuracy here
//                        currentLocation = mLastLocation;
//                        locationUpdatedAt = System.currentTimeMillis();
//                        updateLocationandReport = true;
//                    }
//
//                    System.out.println("Location DB  Distance ---  " + updateLocationandReport + "secondsElapsed " + secondsElapsed + " FATEST_INTERVAL " + FATEST_INTERVAL);
//                }
//                if (updateLocationandReport) {
//                    System.out.println("Location DB  Distance ---  " + SessionSave.getDistance(OngoingAct.this) + "mLastUpdateTime " + mLastUpdateTime + "calulatedTime " );
//                    if (lastlatitude != 0.0 && lastlongitude != 0.0) {
//                        LocationUpdate.oLocation += currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "," + mLastLocation.getAccuracy() + "," + mLastUpdateTime + "," + speed + "," + SessionSave.getDistance(OngoingAct.this) + "|";
//                        haversine(lastlatitude, lastlongitude, currentLocation.getLatitude(), currentLocation.getLongitude());
//                    }
//                    lastlatitude = currentLocation.getLatitude();
//                    lastlongitude = currentLocation.getLongitude();
//                }
//            }
            myHandler.postDelayed(r, 1000);
        }
    };

//    /**
//     * This Function is used for calculate the distance travelled
//     */
//    public synchronized void haversine(double lat1, double lon1, double lat2, double lon2) {
//        // TODO Auto-generated method stub
//        //Getting both the coordinates
//        LatLng from = new LatLng(lat1, lon1);
//        LatLng to = new LatLng(lat2, lon2);
//
//        //Calculating the distance in meters
//        Float distance = (float) SphericalUtil.computeDistanceBetween(from, to) / 1000;
//
//        distance += SessionSave.getDistance(OngoingAct.this);
//        SessionSave.setDistance(distance, OngoingAct.this);
//
//    }


    /**
     * Used to call the driver arrived Api and parse the response
     */
    private class DriverArrived implements APIResult {
        public DriverArrived(final String url, JSONObject data) {

            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(OngoingAct.this, this, data, false).execute(url);
                } else {
                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        /**
         * Parse the response and update the UI.
         */
        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    butt_onboard.setClickable(true);
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Ongoing", "ongoing", OngoingAct.this);
                        MainActivity.mMyStatus.setOnstatus("Arrivd");
                        HeadTitle.setText("" + NC.getResources().getString(R.string.heading_ongoing));
                        HeadTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        butt_onboard.setText("" + NC.getResources().getString(R.string.pass_onboard));
                        SessionSave.saveSession("status", "B", OngoingAct.this);
                        SessionSave.setWaitingTime(0L, OngoingAct.this);
                        if(SessionSave.getSession("shift_status", OngoingAct.this)
                                .equals("IN"))
                        nonactiityobj.startServicefromNonActivity(OngoingAct.this);
                        CancelTxt.setText(String.format(Locale.UK, "00:00:00"));
                        // navigator_layout.setVisibility(View.GONE);
//                        p_pickloc = detail.getString("pickup_location");
//                        p_droploc = detail.getString("drop_location");
//                        MainActivity.mMyStatus.setOnpickupLocation(p_pickloc);
//                        MainActivity.mMyStatus.setOndropLocation(p_droploc);
//                        final String pickup = "<b>" + NC.getResources().getString(R.string.picklocation) + " :\n " + "</b>" + MainActivity.mMyStatus.getOnpickupLocation();
//                        CurrentlocationTxt.setText(Html.fromHtml(pickup));
//                        if (!MainActivity.mMyStatus.getPassengerOndropLocation().equals("")) {
//                            final String drop = "<b>" + NC.getResources().getString(R.string.droploc) + " : \n" + "</b>" + MainActivity.mMyStatus.getPassengerOndropLocation();
//                            droplocationTxt.setText(Html.fromHtml(drop));
//                            navigator_layout.setVisibility(View.VISIBLE);
//                        } else {
//                            droplocationTxt.setVisibility(View.GONE);
//                            navigator_layout.setVisibility(View.GONE);
                        new GetPickdropLoc().execute();
                        // new GetPickdropLoc();
                    } else if (json.getInt("status") == -1) {
                        SessionSave.saveSession("status", "F", OngoingAct.this);
                        MainActivity.mMyStatus.settripId("");
                        SessionSave.saveSession("trip_id", "", OngoingAct.this);
                        MainActivity.mMyStatus.setOnstatus("On");
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        SessionSave.saveSession("Ongoing", "farecal", OngoingAct.this);
                        final Intent jobintent = new Intent(OngoingAct.this, MyStatus.class);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", json.getString("message"));
                        jobintent.putExtras(extras);
                        startActivity(jobintent);
                        finish();
                    } else {
                        CancelTxt.setText(String.format(Locale.UK, "00:00:00"));
                        nonactiityobj.startServicefromNonActivity(OngoingAct.this);
                    }
                } else {
                    butt_onboard.setClickable(true);
                    //                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                    runOnUiThread(new Runnable() {
                        public void run() {

                            ShowToast(OngoingAct.this, getString(R.string.server_error));
                        }
                    });
                }
            } catch (final Exception e) {
                e.printStackTrace();
                butt_onboard.setClickable(true);
            }
        }
    }

    /**
     * Used to call the driver_status_update API and parse the response.
     */
    private class Onboard implements APIResult {
        String p_pickloc = "";
        String p_droploc = "";

        public Onboard(final String url, JSONObject data) {

            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(OngoingAct.this, this, data, false).execute(url);
                } else {
                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        /**
         * Parse the response and update the UI.
         */
        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    butt_onboard.setClickable(true);
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        final JSONObject detail = json.getJSONObject("detail");
                        SessionSave.saveSession("Metric", detail.getString("metric"), OngoingAct.this);
                        if (SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
                            total_km.setText("Total KM");
                        } else {
                            total_km.setText("Total Miles");
                        }
                        HeadTitle.setText("" + NC.getResources().getString(R.string.ongoing_journey));
                        HeadTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        WaitingTxt.setText("" + NC.getResources().getString(R.string.duration_time_txt));
                        TripcancelTxt.setVisibility(View.GONE);
                        // backup.setVisibility(View.INVISIBLE);
                        butt_onboard.setText("" + NC.getResources().getString(R.string.arvd_destination));
                        speed_lay.setVisibility(View.VISIBLE);

                        speed_txt.setVisibility(View.VISIBLE);
                        speedtext.setVisibility(View.VISIBLE);
                        // backup.setVisibility(View.VISIBLE);
                        //  backup.setBackgroundResource(R.drawable.bacdfsddsk);
                        //km_lay.setVisibility(View.VISIBLE);
                        System.out.println("drop location..." + mMyStatus.getOndropLatitude());
                        //                        if (d_latitude==0.0||d_longtitude==0.0)
                        //                            navigator_layout.setVisibility(View.GONE);
                        //                        else
                        //                            navigator_layout.setVisibility(View.VISIBLE);
                        //  SessionSave.saveSession("speedwaiting", "", OngoingAct.this);
                        SessionSave.setWaitingTime(0L, OngoingAct.this);
                        Intent i = new Intent(OngoingAct.this, WaitingTimerRun.class);
                        startService(i);
                        myHandler.postDelayed(r, 0);
                        SessionSave.saveSession("travel_status", "2", OngoingAct.this);
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setdistance("");
                        SessionSave.saveSession("status", "A", OngoingAct.this);
                        if(SessionSave.getSession("shift_status", OngoingAct.this).equals("IN"))
                        nonactiityobj.startServicefromNonActivity(OngoingAct.this);
                        p_pickloc = detail.getString("pickup_location");
                        p_droploc = detail.getString("drop_location");
                        MainActivity.mMyStatus.setOnpickupLocation(p_pickloc);
                        MainActivity.mMyStatus.setOndropLocation(p_droploc);
                        final String pickup = MainActivity.mMyStatus.getOnpickupLocation();
//                        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/HelveticaNeue_Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
//                        String pas = "</body></html>";
//                        String myHtmlString = pish +Html.fromHtml(pickup) + pas;
//                        webView.loadDataWithBaseURL("file:///android_asset/",myHtmlString, "text/html", "UTF-8", null);
                        String s = Html.fromHtml(pickup).toString();
                        CurrentlocationTxt.setText(s);
                        FontHelper.applyFont(OngoingAct.this, CurrentlocationTxt);

                        if (!MainActivity.mMyStatus.getPassengerOndropLocation().equals("")) {
                            final String drop = MainActivity.mMyStatus.getPassengerOndropLocation();

                            if (!drop.trim().equals("")) {
                                dropVisible();
                                droplocationTxt.setText(Html.fromHtml(drop));
//                                System.out.println("nagaaaaaatdraw");
//                                System.out.println("nagaaaaaatdraw" + dropLocation.longitude+"  "+dropLocation.latitude+"P__"+pickLocation.latitude+"  "+pickLocation.longitude);
                            }

                            navigator_layout.setVisibility(View.VISIBLE);
                        } else {
                            droplocationTxt.setVisibility(View.GONE);
                            // navigator_layout.setVisibility(View.GONE);
                        }
                        new GetPickdropLoc().execute();
                    }
//                    } else if (json.getInt("status") == 1) {
//                        final Intent jobintent = new Intent(OngoingAct.this, HomeActivity.class);
//                        Bundle extras = new Bundle();
//                        extras.putString("alert_message", json.getString("message"));
//                        jobintent.putExtras(extras);
//                        startActivity(jobintent);
//                        finish();
                    else if (json.getInt("status") == -1) {
                        final Intent jobintent = new Intent(OngoingAct.this, TripHistoryAct.class);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", json.getString("message"));
                        jobintent.putExtras(extras);
                        startActivity(jobintent);
                        finish();
                    }
                } else {
                    butt_onboard.setClickable(true);
                    //  alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                    runOnUiThread(new Runnable() {
                        public void run() {

                            ShowToast(OngoingAct.this, getString(R.string.server_error));
                        }
                    });
                }
            } catch (final Exception e) {
                e.printStackTrace();
                butt_onboard.setClickable(true);
            }
        }
    }


    /**
     * This method is used to visible and invisible pickup and dropup address.
     */
    public void dropVisible() {

        dropppp.setVisibility(View.VISIBLE);
        // pick_fav.setImageResource(R.drawable.star);
        pickup_pinlay.setVisibility(View.VISIBLE);
        pickup_pin.setVisibility(View.GONE);
        if (SessionSave.getSession("Lang", OngoingAct.this).equals("ar") || SessionSave.getSession("Lang", OngoingAct.this).equals("fa"))
            pickupp.setBackgroundResource(R.drawable.search_pickup_ar);
        else
            pickupp.setBackgroundResource(R.drawable.search_pickup);
    }

    /**
     * Used to call the driver_fare_update API and parse the response.
     */
    private class FreeUpdate implements APIResult {
        public FreeUpdate(final String url) {

            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(OngoingAct.this, this, "", true).execute(url);
                } else {
                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: hande exception
                e.printStackTrace();
            }
        }

        /**
         * Parse the response and update the UI.
         */
        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    SessionSave.saveSession("Ongoing", "farecal", OngoingAct.this);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(OngoingAct.this, getString(R.string.server_error));
                        }
                    });
                }
                //                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to call the complete_trip API and parse the response.
     */


  /*  private class CompleteTrip implements APIResult {
        public CompleteTrip(final String url, final Double latitude, final Double longitude) {

            try {
                JSONObject j = new JSONObject();
                j.put("trip_id", SessionSave.getSession("trip_id", OngoingAct.this));
                j.put("drop_latitude", Double.toString(latitude1));
                j.put("drop_longitude", Double.toString(longitude1));
                j.put("drop_location", Uri.encode(SessionSave.getSession("drop_location", OngoingAct.this).replaceAll("\n", " ")));
                j.put("distance", SessionSave.getDistance(OngoingAct.this));
                j.put("actual_distance", SessionSave.getDistance(OngoingAct.this));
                j.put("waiting_hour", SessionSave.getSession("waitingHr", OngoingAct.this));
                // j.putOpt("location_history", SessionSave.getSession("locationhistory", getApplicationContext()));
                // j.put("location_history_accuracy", SessionSave.getSession("locationhistory_array_accry", getApplicationContext()));
                j.put("waypoints", SessionSave.ReadGoogleWaypoints(OngoingAct.this));


                // System.out.println("actual_distance_checking" + "..." + SessionSave.getDistance(OngoingAct.this));
                //  System.out.println("location_history..elu" + "..." + SessionSave.getSession("locationhistory", getApplicationContext()));
                //  System.out.println("location_history_accuracy..elu" + "..." + SessionSave.getSession("locationhistory_array_accry", getApplicationContext()));

                String curVersion = "";
                PackageManager manager = getPackageManager();
                try {
                    curVersion = manager.getPackageInfo(getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                j.put("driver_app_version", curVersion);

                System.out.println("elumalai...." + " " + j);

                if (isOnline()) {
                    new APIService_Retrofit_JSON(OngoingAct.this, this, j, false).execute(url);
                } else {
//                    final String completeUrl = "type=complete_trip";
//                    new CompleteTrip(completeUrl, latitude1, longitude1);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        *//**
     * Parse the response and update the UI.
     *//*
        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {
                final JSONObject json = new JSONObject(result);
                if (isSuccess) {
                    SessionSave.saveSession("locationhistory", "", getApplicationContext());
                    SessionSave.setDistance((float) 0.0, getApplicationContext());
                    if (cDialog.isShowing())
                        cDialog.dismiss();
                    butt_onboard.setClickable(true);
                    if (json.getInt("status") == 4) {
                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        SessionSave.saveSession("Ongoing", "farecal", OngoingAct.this);
                        SessionSave.saveSession("travel_status", "5", OngoingAct.this);
                        //  SessionSave.saveSession("model_fare_type",json.getJSONObject("detail").getString("model_fare_type"),OngoingAct.this);
                        if (json.getJSONObject("detail").has("model_fare_type")) {
                            SessionSave.saveSession("model_fare_type", json.getJSONObject("detail").getString("model_fare_type"), OngoingAct.this);
                        }
                        CommonData.currentspeed = "";
                        speedtext.setText("" + String.format(Locale.UK, NC.getResources().getString(R.string.m_timer)));
                        SessionSave.saveSession("speedwaiting", "", OngoingAct.this);
                        SessionSave.setWaitingTime(0L, OngoingAct.this);
                        Intent i = new Intent(OngoingAct.this, WaitingTimerRun.class);
                        stopService(i);
                        myHandler.removeCallbacks(r);
                        MainActivity.mMyStatus.setsaveTime(timeclear);
                        showLoading(OngoingAct.this);
                        final Intent farecal = new Intent(OngoingAct.this, FarecalcAct.class);
                        farecal.putExtra("from", "direct");
                        farecal.putExtra("message", result);
                        startActivity(farecal);
                        finish();
                    } else if (json.getInt("status") == -1) {
                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        MainActivity.mMyStatus.setOndriverLatitude("");
                        MainActivity.mMyStatus.setOndriverLongitude("");
                        SessionSave.saveSession("status", "F", OngoingAct.this);
                        SessionSave.saveSession("trip_id", "", OngoingAct.this);
                        final String status_update = "type=driver_status_update&driver_id=" + SessionSave.getSession("Id", OngoingAct.this) + "&latitude=" + latitude1 + "&longitude=" + longitude1 + "&status=" + "F" + "&trip_id=";
                        SessionSave.saveSession("Ongoing", "flagger", OngoingAct.this);
                        new FreeUpdate(status_update);
                        showLoading(OngoingAct.this);
                        final Intent jobintent = new Intent(OngoingAct.this, MyStatus.class);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", json.getString("message"));
                        jobintent.putExtras(extras);
                        startActivity(jobintent);
                        finish();
                    } else {
                        alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    butt_onboard.setClickable(true);
                    //                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    //                    final String completeUrl = "type=complete_trip";
                    //                    new CompleteTrip(completeUrl, latitude1, longitude1);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            ShowToast(OngoingAct.this, getString(R.string.server_error));
                        }
                    });
                }
            } catch (final Exception e) {
                e.printStackTrace();
                butt_onboard.setClickable(true);
            }
        }
    }*/


    /**
     * Initially update the trip details based on get_trip_detail response.
     */
    private void init() {

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) OngoingAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), OngoingAct.this);
        //   System.out.println("_________________OOOO" + MainActivity.mMyStatus.getOnstatus());
        if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("on")) {
            HeadTitle.setText("" + NC.getResources().getString(R.string.tripdetails));
            HeadTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            butt_onboard.setText("" + NC.getResources().getString(R.string.ive_arrived));
            butt_onboard.setVisibility(View.VISIBLE);
            passnameTxt.setText(MainActivity.mMyStatus.getOnpassengerName());
            final String pickup = MainActivity.mMyStatus.getOnpickupLocation();

            CurrentlocationTxt.setText(Html.fromHtml(pickup));
            FontHelper.applyFont(OngoingAct.this, CurrentlocationTxt);

            if (!MainActivity.mMyStatus.getPassengerOndropLocation().equals("")) {
                final String drop = MainActivity.mMyStatus.getPassengerOndropLocation();
                //  droplocationTxt.setText(Html.fromHtml(drop));
                if (!drop.trim().equals("")) {
                    dropVisible();
                    droplocationTxt.setText(Html.fromHtml(drop));
                }
            } else
                droplocationTxt.setVisibility(View.GONE);
            if (!MainActivity.mMyStatus.getpassengerNotes().equals("")) {
                final String notes = MainActivity.mMyStatus.getpassengerNotes();
                tv_notes.setText(Html.fromHtml(notes));
            } else
                tv_notes.setVisibility(View.GONE);
            if (MainActivity.mMyStatus.getOnpickupLatitude().length() != 0)
                p_latitude = Double.parseDouble(MainActivity.mMyStatus.getOnpickupLatitude());
            if (MainActivity.mMyStatus.getOnpickupLongitude().length() != 0)
                p_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOnpickupLongitude());
            if (MainActivity.mMyStatus.getOndropLatitude().length() != 0)
                d_latitude = Double.parseDouble(MainActivity.mMyStatus.getOndropLatitude());
            if (MainActivity.mMyStatus.getOndropLongitude().length() != 0)
                d_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOndropLongitude());
            if (MainActivity.mMyStatus.getOndriverLatitude().length() != 0)
                driver_latitude = Double.parseDouble(MainActivity.mMyStatus.getOndriverLatitude());
            if (MainActivity.mMyStatus.getOndriverLongitude().length() != 0)
                driver_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOndriverLongitude());
            new GetPickdropLoc().execute();
            navigator_layout.setVisibility(View.VISIBLE);
        } else if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Arrivd")) {
            //  navigator_layout.setVisibility(View.GONE);
            HeadTitle.setText("" + NC.getResources().getString(R.string.heading_ongoing));
            HeadTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            butt_onboard.setText("" + NC.getResources().getString(R.string.pass_onboard));
            passnameTxt.setText(MainActivity.mMyStatus.getOnpassengerName());
            butt_onboard.setVisibility(View.VISIBLE);
            final String pickup = MainActivity.mMyStatus.getOnpickupLocation();
            CurrentlocationTxt.setText(Html.fromHtml(pickup));
            if (!MainActivity.mMyStatus.getPassengerOndropLocation().equals("")) {
                final String drop = MainActivity.mMyStatus.getPassengerOndropLocation();
                if (!drop.trim().equals("")) {
                    dropVisible();
                    droplocationTxt.setText(Html.fromHtml(drop));
                }
            } else
                droplocationTxt.setVisibility(View.GONE);
            if (!MainActivity.mMyStatus.getpassengerNotes().equals("")) {
                final String notes = MainActivity.mMyStatus.getpassengerNotes();
                tv_notes.setText(Html.fromHtml(notes));
            } else
                tv_notes.setVisibility(View.GONE);
            if (MainActivity.mMyStatus.getOnpickupLatitude().length() != 0)
                p_latitude = Double.parseDouble(MainActivity.mMyStatus.getOnpickupLatitude());
            if (MainActivity.mMyStatus.getOnpickupLongitude().length() != 0)
                p_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOnpickupLongitude());
            if (MainActivity.mMyStatus.getOndropLatitude().length() != 0)
                d_latitude = Double.parseDouble(MainActivity.mMyStatus.getOndropLatitude());
            if (MainActivity.mMyStatus.getOndropLongitude().length() != 0)
                d_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOndropLongitude());
            if (MainActivity.mMyStatus.getOndriverLatitude().length() != 0)
                driver_latitude = Double.parseDouble(MainActivity.mMyStatus.getOndriverLatitude());
            if (MainActivity.mMyStatus.getOndriverLongitude().length() != 0)
                driver_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOndriverLongitude());
            new GetPickdropLoc().execute();
        } else if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Complete")) {
            //   CancelTxt.setText("00:00:00");
            TripcancelTxt.setVisibility(View.GONE);
            HeadTitle.setText("" + NC.getResources().getString(R.string.ongoing_journey));
            HeadTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            // backup.setVisibility(View.INVISIBLE);
            butt_onboard.setText("" + NC.getResources().getString(R.string.arvd_destination));
            passnameTxt.setText(MainActivity.mMyStatus.getOnpassengerName());
            final String pickup = MainActivity.mMyStatus.getOnpickupLocation();
            CurrentlocationTxt.setText(Html.fromHtml(pickup));
            FontHelper.applyFont(OngoingAct.this, CurrentlocationTxt);

            if (!MainActivity.mMyStatus.getPassengerOndropLocation().equals("")) {
                final String drop = MainActivity.mMyStatus.getPassengerOndropLocation();
                if (!drop.trim().equals("")) {
                    dropVisible();
                    droplocationTxt.setText(Html.fromHtml(drop));
                }
            } else
                droplocationTxt.setVisibility(View.GONE);
            if (!MainActivity.mMyStatus.getpassengerNotes().equals("")) {
                final String notes = MainActivity.mMyStatus.getpassengerNotes();
                tv_notes.setText(Html.fromHtml(notes));
            } else
                tv_notes.setVisibility(View.GONE);
            if (MainActivity.mMyStatus.getOnpickupLatitude().length() != 0)
                p_latitude = Double.parseDouble(MainActivity.mMyStatus.getOnpickupLatitude());
            if (MainActivity.mMyStatus.getOnpickupLongitude().length() != 0)
                p_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOnpickupLongitude());
            if (MainActivity.mMyStatus.getOndropLatitude().length() != 0)
                d_latitude = Double.parseDouble(MainActivity.mMyStatus.getOndropLatitude());
            if (MainActivity.mMyStatus.getOndropLongitude().length() != 0)
                d_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOndropLongitude());
            if (MainActivity.mMyStatus.getOndriverLatitude().length() != 0)
                driver_latitude = Double.parseDouble(MainActivity.mMyStatus.getOndriverLatitude());
            if (MainActivity.mMyStatus.getOndriverLongitude().length() != 0)
                driver_longtitude = Double.parseDouble(MainActivity.mMyStatus.getOndriverLongitude());
            new GetPickdropLoc().execute();

            // mHandler.sendEmptyMessage(1);
            str = WaitingTxt.getTag().toString();
            speed_lay.setVisibility(View.VISIBLE);
            //  km_lay.setVisibility(View.VISIBLE);
            //  backup.setVisibility(View.INVISIBLE);
            myHandler.postDelayed(r, 0);
            butt_onboard.setVisibility(View.VISIBLE);
//            if (mMyStatus.getOndropLatitude() == null || mMyStatus.getOndropLongitude() == null || mMyStatus.getOndropLatitude().equalsIgnoreCase("0.0") || mMyStatus.getOndropLongitude().equalsIgnoreCase("0.0") || mMyStatus.getOndropLatitude().equalsIgnoreCase("0") || mMyStatus.getOndropLongitude().equalsIgnoreCase("0"))
//                navigator_layout.setVisibility(View.GONE);
//            else
//                navigator_layout.setVisibility(View.VISIBLE);
        } else {
            new GetPickdropLoc().execute();
            butt_onboard.setVisibility(View.INVISIBLE);
            passnameTxt.setText("" + NC.getResources().getString(R.string.you));
            nodataTxt.setText("" + NC.getResources().getString(R.string.no_trip_available));
            nodataTxt.setVisibility(View.VISIBLE);
            mapsupport_lay.setVisibility(View.GONE);
            nodataLay.setVisibility(View.VISIBLE);
//            navigator_layout.setVisibility(View.GONE);
        }
    }

    /**
     * Used to call the cancel_trip API and parse the response.
     */
    private class CancelTrip implements APIResult {
        private String msg;

        public CancelTrip(final String url, JSONObject data) {

            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(OngoingAct.this, this, data, false).execute(url);
                } else {
                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        /**
         * Parse the response and update the UI.
         */
        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 3) {
                        msg = json.getString("message");
                        JSONObject jsonDriver = json.getJSONObject("driver_statistics");
                        SessionSave.saveSession("driver_statistics", "" + jsonDriver, OngoingAct.this);
                        SessionSave.saveSession("status", "F", OngoingAct.this);
                        MainActivity.mMyStatus.settripId("");
                        SessionSave.saveSession("trip_id", "", OngoingAct.this);
                        MainActivity.mMyStatus.setOnstatus("On");
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setPassengerOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        MainActivity.mMyStatus.setOndriverLatitude("");
                        MainActivity.mMyStatus.setOndriverLongitude("");
                        SessionSave.saveSession("Ongoing", "farecal", OngoingAct.this);
                        //						ShowToast(OngoingAct.this, "" + json.getString("message"));
//                        final Intent jobintent = new Intent(OngoingAct.this, MyStatus.class);
//                        Bundle extras = new Bundle();
//                        extras.putString("alert_message", json.getString("message"));
//                        jobintent.putExtras(extras);
//                        startActivity(jobintent);
//                        finish();


                        Intent cancelIntent = new Intent();
                        cancelIntent.putExtra("alert_message", json.getString("message"));
                        cancelIntent.setAction(Intent.ACTION_MAIN);
                        cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        ComponentName cn = new ComponentName(OngoingAct.this, MyStatus.class);
                        cancelIntent.setComponent(cn);

                        startActivity(cancelIntent);
                        finish();
                    } else {
                        msg = json.getString("message");
                        alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            ShowToast(OngoingAct.this, getString(R.string.server_error));
                        }
                    });
                }
                //                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Dialog mProgressdialog;
    private LatLng viaLatlng;


    /**
     * This handler helps to draw the route between driver place to pickup place and pickup place to drop place.
     */
    Handler mHandler = new Handler() {
        private CountDownTimer countDownTimer;

        @Override
        public void handleMessage(final android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    try {
                        //   System.out.println("vvv____drawRoute_else" + MainActivity.mMyStatus.getOnstatus());
                        if (!LOCATION_UPDATE_STOPPED || ROUTE_DRAW_ON_START) {
                            //  stopLocationUpdates();

                            LOCATION_UPDATE_STOPPED = true;
                            ROUTE_DRAW_ON_START = false;
                            if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Complete") || MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Arrivd")) {
                                map.clear();
                                pickUpDropMarker();
                                ArrayList<LatLng> pp = new ArrayList<>();
                                pp.add(pickupLatLng);
                                pp.add(dropLatLng);
                                if (viaLatlng != null)
                                    pp.add(viaLatlng);
///tamil
                                if (pickupLatLng != null && pickupLatLng.latitude != 0.0 && pickupLatLng.longitude != 0.0)
                                    p_marker = map.addMarker(new MarkerOptions().position(new LatLng(pickupLatLng.latitude, pickupLatLng.longitude)).title("" + NC.getResources().getString(R.string.pickuploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(true));
                                // System.out.println("HHHHHHHHHHHHHHHH++++3" + dropLatLng);
                                if (dropLatLng != null && dropLatLng.latitude != 0.0 && dropLatLng.longitude != 0.0) {
                                    d_marker = map.addMarker(new MarkerOptions().position(new LatLng(dropLatLng.latitude, dropLatLng.longitude)).title("" + NC.getResources().getString(R.string.droploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).draggable(true));
                                    //  route.drawRoute(map, OngoingAct.this, pickupLatLng, dropLatLng, "en", Color.parseColor("#00BFFF"));
                                    route.drawRoute(map, OngoingAct.this, pp, "en", true);
                                    //  System.out.println("HHHHHHHHHHHHHHHH++++3route");
                                }
                            } else if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("On")) {


                                ArrayList<LatLng> pp = new ArrayList<>();
                                pp.add(currentLatLng);
                                pp.add(pickupLatLng);
                                if (viaLatlng != null)
                                    pp.add(viaLatlng);
                                //   System.out.println("vvv____drawRoute_elseaa");
                                //  route.drawRoute(map, OngoingAct.this, pickupLatLng, dropLatLng, "en", Color.parseColor("#00BFFF"));
                                if (pp != null)
                                    route.drawRoute(map, OngoingAct.this, pp, "en", true);
                                //route.drawRoute(map, OngoingAct.this, currentLatLng, pickupLatLng, "en", Color.parseColor("#00BFFF"));
                            } else {
                                ArrayList<LatLng> pp = new ArrayList<>();
                                pp.add(pickupLatLng);
                                pp.add(dropLatLng);
                                if (viaLatlng != null)
                                    pp.add(viaLatlng);
                                // System.out.println("vvv____drawRoute_else");
                                //  route.drawRoute(map, OngoingAct.this, pickupLatLng, dropLatLng, "en", Color.parseColor("#00BFFF"));
                                try {
                                    if (pp != null && map != null)
                                        route.drawRoute(map, OngoingAct.this, pp, "en", true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                startLocationUpdates();
//                                stopLocationUpdates();
                                    LOCATION_UPDATE_STOPPED = false;
                                }
                            }, 50000);
                        }

                    } catch (final Exception e) {
                        mHandler.sendEmptyMessage(5);
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    final View view = View.inflate(OngoingAct.this, R.layout.progress_bar, null);
                    mProgressdialog = new Dialog(OngoingAct.this, R.style.NewDialog);
                    mProgressdialog.setContentView(view);
                    mProgressdialog.setCancelable(false);
                    mProgressdialog.show();

                    ImageView iv = (ImageView) mProgressdialog.findViewById(R.id.giff);
                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                    Glide.with(OngoingAct.this)
                            .load(R.raw.loading_anim)
                            .into(imageViewTarget);

                    mHandler.sendEmptyMessage(1);
                    break;
                case 3:
                    showLog("dismiss handler");
                    mProgressdialog.dismiss();
                    break;
                case 4:
                    countDownTimer.cancel();
                    break;
                case 5:
                    try {
                        route.drawRoute(map, OngoingAct.this, pickupLatLng, dropLatLng, "en", Color.parseColor("#00BFFF"));
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        ;
    };


    /**
     * This asynchronous task to update the passenger in UI.
     */
    public class Getimg extends AsyncTask<Void, Void, Void> {
        String imagepath = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            try {
                if (!SessionSave.getSession("p_image", OngoingAct.this).equals("")) {
                    imagepath = "" + SessionSave.getSession("p_image", OngoingAct.this);
                    //  Log.i("Imagepath in session", SessionSave.getSession("p_image", OngoingAct.this));
                } else
                    imagepath = SessionSave.getSession("noimage_base", OngoingAct.this);
            } catch (final Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {

            // Log.i("Imagepath", imagepath);
            imageLoader.displayImage(imagepath, proimg);
            super.onPostExecute(result);
        }
    }

    //    // This method helps to update the driver current lat/lng in map view.
//    private void MyLocationListener1() {
//
//        if (mLastLocation != null) {
//            latitude1 = mLastLocation.getLatitude();
//            longitude1 = mLastLocation.getLongitude();
//            bearing = mLastLocation.getBearing();
//            getCurrentAddress(latitude1, longitude1);
//        }
//        if (bearing >= 0)
//            bearing = bearing + 90;
//        else
//            bearing = bearing - 90;
//        if (c_marker != null) {
//            c_marker.remove();
//        }
//        currentLatLng = new LatLng(latitude1, longitude1);
//        c_marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).rotation(bearing).title(Address).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude1, longitude1), zoom));
//        bearing = 0;
//    }
//

    @Override
    public void ClickMethod(final View v) {

        Intent i;
        switch (v.getId()) {
            case R.id.menu_me:
                i = new Intent(OngoingAct.this, MeAct.class);
                startActivity(i);
                menu.toggle();
                finish();
                break;
            case R.id.menu_home:
                i = new Intent(OngoingAct.this, MyStatus.class);
                startActivity(i);
                menu.toggle();
                finish();
                break;
            case R.id.menu_ongoing:
                menu.toggle();
                break;
            case R.id.menu_logout:
                logout(OngoingAct.this);
                break;
            case R.id.menu_mystatus:
                i = new Intent(OngoingAct.this, MyStatus.class);
                startActivity(i);
                menu.toggle();
                finish();
                break;
        }
    }

    // This method helps to update the driver current lat/lng in map view when activity get starts.
    @Override
    public void onConnected(final Bundle connectionHint) {

        try {
            startLocationUpdates();
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                latitude1 = mLastLocation.getLatitude();
                longitude1 = mLastLocation.getLongitude();
                viaLatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                currentLatLng = new LatLng(latitude1, longitude1);
                final LatLng coordinate = new LatLng(latitude1, longitude1);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));
                mapWrapperLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    double roundTwoDecimals(double d) {

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    // This method helps to update the driver current lat/lng in map view by using MyLocationListener() function.
//    @Override
//    public void onLocationChanged(final Location location) {
//        System.out.println("HHHHHHHHHHHHHHHH_ll" + speed);
//        mLastLocation = location;
//        if (mapWrapperLayout != null && !mapWrapperLayout.isShown())
//            mapWrapperLayout.setVisibility(View.VISIBLE);
//        latitude1 = location.getLatitude();
//        longitude1 = location.getLongitude();
//        System.out.println("p_minspeed");
//        mLastLocation = location;
//        speed = roundDecimal(convertSpeed(location.getSpeed()), 2);
//        try {
//            if (Route.line == null && map != null)
//                mHandler.sendEmptyMessage(5);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (!SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
//            try {
//                speed = (roundTwoDecimals(speed / 1.60934));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("vvv_________change" + p_travelstatus);
//
//        if (dropLatLng != null && p_travelstatus.trim().equals("2"))
//            if (!checkLocationInRoute()) {
//                viaLatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                mHandler.sendEmptyMessage(1);
//            }
//        String metricss = "";
//        if (SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
//            metricss = " KM/hr";
//        } else {
//            metricss = " Miles/hr";
//        }
//
//        if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Complete"))
//            HeadTitle.setText(" " + NC.getResources().getString(R.string.ongoing_journey) + "\n" + speed + metricss);
//        float _speed = location.getSpeed();
//        System.out.println("_______nnnnn" + speed);
//        bearing = location.getBearing();
//        zoom = map.getCameraPosition().zoom;
//
//        if (bearing >= 0)
//            bearing = bearing + 90;
//        else
//            bearing = bearing - 90;
//
//        try {
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            // marker Animation Function
//            if (!animLocation) {
//                listPoint.add(latLng);
//            } else {
//                savedpoint.add(latLng);
//            }
//            System.out.println("listPoint size" + listPoint.size());
//            if (listPoint.size() > 1) {
//
//                if (a_marker != null) {
//                    a_marker.setVisible(false);
//                    a_marker.remove();
//                }
//
//                if (!animStarted) {
//                    if (savedLatLng != null) {
//                        listPoint.set(0, savedLatLng);
//                        System.out.println("savedLatLng animation fst" + listPoint.get(0));
//                    }
//                    if (speed > 2) {
//                        animStarted = true;
//                        animLocation = true;
//                        c_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(bearing).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
//                        c_marker.setVisible(true);
//                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));
//                        savedLatLng = listPoint.get(listPoint.size() - 1);
//                        animateLine(listPoint, c_marker, bearing);
//                    } else {
//                        if (c_marker != null) {
//                            c_marker.setVisible(false);
//                            c_marker.remove();
//                        }
//                        System.out.println("GpsStatus.ischecked  out" + GpsStatus.ischecked);
//
//                        if (GpsStatus.ischecked == 0) {
//                            System.out.println("GpsStatus.ischecked " + GpsStatus.ischecked);
//                            GpsStatus.ischecked = 1;
//                            a_marker = map.addMarker(new MarkerOptions().position(latLng).rotation(bearing).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
//                            a_marker.setVisible(true);
//                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//                        } else {
//                            a_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(bearing).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
//                            a_marker.setVisible(true);
//                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));
//                        }
//                    }
//                }
//
//            }
//
//            bearing = 0;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//        }
//    }

    @Override
    public void onLocationChanged(final Location location) {
        // System.out.println("HHHHHHHHHHHHHHHH_ll" + speed);
        mLastLocation = location;
        if (mapWrapperLayout != null && !mapWrapperLayout.isShown())
            mapWrapperLayout.setVisibility(View.VISIBLE);
        latitude1 = location.getLatitude();
        longitude1 = location.getLongitude();
        // System.out.println("p_minspeed");
        mLastLocation = location;
        speed = roundDecimal(convertSpeed(location.getSpeed()), 2);
        if (!SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
            try {
                speed = (roundTwoDecimals(speed / 1.60934));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // System.out.println("vvv_________change" + p_travelstatus + "__" + dropLatLng);
        //SessionSave.getSession("travel_status",OngoingAct.this).equals("2")

        if (dropLatLng != null && p_travelstatus.trim().equals("2"))
            if (!checkLocationInRoute()) {
                // mov_cur_loc.setVisibility(View.GONE);
                viaLatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mHandler.sendEmptyMessage(1);
            }
        String metricss = "";
        if (SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
            metricss = " KM/hr";
        } else {
            metricss = " Miles/hr";
        }

        if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Complete"))
            HeadTitle.setText(" " + NC.getResources().getString(R.string.ongoing_journey) + "\n" + speed + metricss);
        float _speed = location.getSpeed();
        //    System.out.println("_______nnnnn" + speed);
        bearing = location.getBearing();
        bearings = location.getBearing();
        if (map != null)
            zoom = map.getCameraPosition().zoom;

        //  System.out.println("Bearing:" + location.getBearing());
        if (bearing >= 0)
            bearing = bearing + 90;
        else
            bearing = bearing - 90;

        try {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // marker Animation Function
            if (!animLocation) {
                listPoint.add(latLng);
            } else {
                savedpoint.add(latLng);
            }
            //     System.out.println("listPoint size" + listPoint.size());
            if (listPoint.size() > 1) {

                if (a_marker != null) {
                    a_marker.setVisible(false);
                    a_marker.remove();
                }

                if (!animStarted) {
                    if (savedLatLng != null) {
                        listPoint.set(0, savedLatLng);
                        //               System.out.println("savedLatLng animation fst" + listPoint.get(0));
                    }
                    if (speed > 2 && map != null) {
                        animStarted = true;
                        animLocation = true;
                        c_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(90).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
                        c_marker.setVisible(true);
                        //              System.out.println("bearing" + bearing);
                        if (map != null) {
                            CameraPosition camPos = CameraPosition
                                    .builder(
                                            map.getCameraPosition() // current Camera
                                    )
                                    .bearing(bearings)
                                    .build();

                            //map.animateCamera(CameraUpdateFactory.newLatLngZoom(camPos, zoom));
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
                        }
                        savedLatLng = listPoint.get(listPoint.size() - 1);
                        animateLine(listPoint, c_marker, bearings);
                    } else {
                        if (c_marker != null) {
                            c_marker.setVisible(false);
                            c_marker.remove();
                        }
                        //           System.out.println("GpsStatus.ischecked  out" + GpsStatus.ischecked);

                        if (GpsStatus.ischecked == 0) {
                            //              System.out.println("GpsStatus.ischecked " + GpsStatus.ischecked);
                            GpsStatus.ischecked = 1;
                            a_marker = map.addMarker(new MarkerOptions().position(latLng).rotation(90).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
                            a_marker.setVisible(true);
                            //  map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

                            CameraPosition camPos = CameraPosition
                                    .builder(
                                            map.getCameraPosition() // current Camera
                                    )
                                    .bearing(bearings)
                                    .build();
                            // map.animateCamera(CameraUpdateFactory.newLatLngZoom(camPos, zoom));
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));


                        } else {

                            a_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(90).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
                            a_marker.setVisible(true);
                            // map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));

                            CameraPosition camPos = CameraPosition
                                    .builder(
                                            map.getCameraPosition() // current Camera
                                    )
                                    .bearing(bearings)
                                    .build();
                            // map.animateCamera(CameraUpdateFactory.newLatLngZoom(camPos, zoom));
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));


                        }
                    }
                }

            }

            bearing = 0;
            bearings = 0;
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    /**
     * This method is to check current location is in the route.
     */
    private boolean checkLocationInRoute() {
        boolean inside = true;
        if (Route.line != null) {
            //   System.out.println("vvv_________" + PolyUtil.isLocationOnEdge(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), Route.line.getPoints(), true, 2000) + "______" + mLastLocation.getLatitude() + "__" + mLastLocation.getLongitude() + "_____" + Route.line.getPoints().size());
            inside = PolyUtil.isLocationOnEdge(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), Route.line.getPoints(), true, 1500);
        }
        return inside;
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
//        startActivity(new Intent(OngoingAct.this, MyStatus.class));
//        finish();
    }

    /**
     * convert Speed
     */
    private double convertSpeed(double speed) {
        return ((speed * 3600) * 0.001);
    }

    /**
     * This method is used to round the decimal value
     */
    private double roundDecimal(double value, final int decimalPlace) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        value = bd.doubleValue();
        return value;
    }


    /**
     * This class is used to get the trip details when activity in opened, It calls the API and parse the response.
     */
    private class Tripdetails implements APIResult {
        String p_logid = "";
        String p_name = "";
        String p_pickloc = "";
        String p_droploc = "";
        String p_picklat = "";
        String p_picklng = "";
        String p_droplat = "";
        String p_droplng = "";
        String p_driverlat = "";
        String p_driverlng = "";

        private String p_image = "";
        private String p_phone = "";
        private String p_notes = "";
        private String p_driverstatus = "", p_taxi_speed = "";

        public Tripdetails(final String url, JSONObject data) {

            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(OngoingAct.this, this, data, false).execute(url);
                } else {
                    alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {
                //          System.out.println("result" + result);
                if (isSuccess) {
                    tripInfo.setVisibility(View.VISIBLE);
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        final JSONObject detail = json.getJSONObject("detail");

                        if (detail.getString("street_pickup_trip").trim().equals("1")) {
                            startActivity(new Intent(OngoingAct.this, StreetPickUpAct.class));
                            Toast.makeText(OngoingAct.this, getString(R.string.you_are_in_trip), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            p_logid = detail.getString("trip_id");
                            p_name = detail.getString("passenger_name");
                            p_pickloc = detail.getString("current_location");
                            p_droploc = detail.getString("drop_location");
                            p_picklat = detail.getString("pickup_latitude");
                            p_picklng = detail.getString("pickup_longitude");
                            p_droplat = detail.getString("drop_latitude");
                            p_droplng = detail.getString("drop_longitude");
                            p_driverlat = detail.getString("driver_latitute");
                            p_driverlng = detail.getString("driver_longtitute");
                            p_travelstatus = detail.getString("travel_status");
                            p_driverstatus = detail.getString("driver_status");
                            p_notes = detail.getString("notes");
                            p_phone = detail.getString("passenger_phone");
                            p_image = detail.getString("passenger_image");
                            p_taxi_speed = detail.getString("taxi_min_speed");
                            SessionSave.saveSession("Metric", detail.getString("metric"), OngoingAct.this);
                            SessionSave.saveSession("p_image", p_image, OngoingAct.this);
                            SessionSave.saveSession("travel_status", p_travelstatus, OngoingAct.this);


                            //  || (p_driverstatus.equalsIgnoreCase("A") && p_travelstatus.equalsIgnoreCase("2"))
                            if ((p_driverstatus.equalsIgnoreCase("F") || p_driverstatus.equalsIgnoreCase("B") || (p_driverstatus.equalsIgnoreCase("A"))) && !p_travelstatus.equalsIgnoreCase("5")) {
                                if (p_travelstatus.equalsIgnoreCase("3")) {
                                    HeadTitle.setText(NC.getResources().getString(R.string.waitingpassenger));
                                    MainActivity.mMyStatus.setOnstatus("Arrivd");
                                } else if (p_travelstatus.equalsIgnoreCase("2")) {
                                    HeadTitle.setText(NC.getResources().getString(R.string.tripprogress));
                                    MainActivity.mMyStatus.setOnstatus("Complete");

                                } else if (p_travelstatus.equalsIgnoreCase("9")) {
                                    HeadTitle.setText(NC.getResources().getString(R.string.tripdetails));
                                    MainActivity.mMyStatus.setOnstatus("On");
                                } else {
                                    HeadTitle.setText(NC.getResources().getString(R.string.tripdetails));
                                }
                                p_pickloc = p_pickloc.trim();
                                if (p_pickloc.length() > 0 && SessionSave.getSession("Lang", OngoingAct.this).equals("en")) {
                                    p_pickloc = Character.toUpperCase(p_pickloc.charAt(0)) + p_pickloc.substring(1);
                                    p_droploc = p_droploc.trim();
                                }
                                if (p_droploc.length() > 0) {
                                    p_droploc = Character.toUpperCase(p_droploc.charAt(0)) + p_droploc.substring(1);
                                }
                                if (p_name.length() > 0) {
                                    p_name = Character.toUpperCase(p_name.charAt(0)) + p_name.substring(1);
                                }
                                if (p_taxi_speed.length() > 0 && !p_taxi_speed.equals(null)) {
                                    SessionSave.saveSession("taxi_speed", p_taxi_speed, OngoingAct.this);
                                    // SessionSave.saveSession("taxi_speed", "-100", OngoingAct.this);
                                }
                                if (p_notes.length() > 0) {
                                    p_notes = Character.toUpperCase(p_notes.charAt(0)) + p_notes.substring(1);
                                }
                                MainActivity.mMyStatus.setOnpickupLocation(p_pickloc);
                                MainActivity.mMyStatus.setOndropLocation(p_droploc);
                                MainActivity.mMyStatus.setPassengerOndropLocation(p_droploc);
                                MainActivity.mMyStatus.setOnpickupLatitude(p_picklat);
                                MainActivity.mMyStatus.setOnpickupLongitude(p_picklng);
                                MainActivity.mMyStatus.setOndriverLatitude(p_driverlat);
                                MainActivity.mMyStatus.setOndriverLongitude(p_driverlng);
                                MainActivity.mMyStatus.setOnpassengerName(p_name);
                                MainActivity.mMyStatus.settripId(p_logid);
                                SessionSave.saveSession("trip_id", p_logid, OngoingAct.this);
                                MainActivity.mMyStatus.setpickupLoc(p_pickloc);
                                MainActivity.mMyStatus.setOndropLatitude(p_droplat);
                                MainActivity.mMyStatus.setOndropLongitude(p_droplng);
                               /* if (!p_droplat.equals("") && !p_droplng.equals("")) {

                                } else {
                                    MainActivity.mMyStatus.setOndropLatitude("0.0");
                                    MainActivity.mMyStatus.setOndropLongitude("0.0");
                                }*/
                              /*  MainActivity.mMyStatus.setOndropLatitude(p_droplat);
                                MainActivity.mMyStatus.setOndropLongitude(p_droplng);*/
                                MainActivity.mMyStatus.setdropLoc(p_droploc);
                                MainActivity.mMyStatus.setpassengerId(p_logid);
                                MainActivity.mMyStatus.setphoneNo(p_phone);
                                MainActivity.mMyStatus.setOnPassengerImage(p_image);
                                MainActivity.mMyStatus.setpassengerNotes(p_notes);
                                MainActivity.mMyStatus.setpassengerphone(p_phone);
                                init();
                                new Getimg().execute();
                            } else if (p_driverstatus.equalsIgnoreCase("A") && p_travelstatus.equalsIgnoreCase("5")) {
                                //   Toast.makeText(OngoingAct.this, "dsfsdfds", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(OngoingAct.this, FarecalcAct.class);
                                i.putExtra("from", "pending");
                                i.putExtra("lat", detail.getString("drop_latitude"));
                                i.putExtra("lon", detail.getString("drop_longitude"));
                                i.putExtra("distance", detail.getString("distance"));
                                i.putExtra("waitingHr", detail.getString("waiting_time"));
                                i.putExtra("drop_location", detail.getString("drop_location"));
                                startActivity(i);
                                finish();
                            } else {
                                ShowToast(OngoingAct.this, "" + NC.getResources().getString(R.string.you_are_in_trip));
                                Intent i = new Intent(OngoingAct.this, TripHistoryAct.class);
                                startActivity(i);
                                finish();
                            }
                            tripInfo.post(new Runnable() {
                                @Override
                                public void run() {

                                    layoutheight = tripInfo.getHeight() - 20;
                                    map.setPadding(0, layoutheight, 0, 120);
                                }
                            });
                        }
                    } else {
                        Intent i = new Intent(OngoingAct.this, TripHistoryAct.class);
                        startActivity(i);
                        finish();
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(OngoingAct.this, getString(R.string.server_error));
                        }
                    });
                    Intent i = new Intent(OngoingAct.this, TripHistoryAct.class);
                    startActivity(i);
                    finish();
                }
            } catch (final Exception e) {
                // TODO: handle exception
                //    System.out.println("pass---j" + e);
                e.printStackTrace();
                Intent i = new Intent(OngoingAct.this, TripHistoryAct.class);
                startActivity(i);
                finish();
            }
        }

    }

    /**
     * Checking google play service connection
     */
    private boolean servicesConnected() {

        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else
            return false;
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
    }

//    public void getCurrentAddress(double lat, double lon) {
//        Geocoder geocoder;
//        List<Address> addresses = null;
//        String address = "";
//        String city = "";
//        String country = "";
//        geocoder = new Geocoder(this, Locale.UK);
//        try {
//            addresses = geocoder.getFromLocation(lat, lon, 1);
//            address = addresses.get(0).getAddressLine(0);
//            city = addresses.get(0).getAddressLine(1);
//            country = addresses.get(0).getAddressLine(2);
//            SessionSave.saveSession("Driver_locations", "" + address + "\n" + city + "\n" + country, OngoingAct.this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * Marker Animation with array of latlng
     */
    public void animateLine(ArrayList<LatLng> Trips, Marker marker, float bearings) {
        _trips.clear();
        _trips.addAll(Trips);
        _marker = marker;
        animteBearing = bearings;
        animateMarker();
    }

    /**
     * Marker Animation with array of latlng
     */
    public void animateMarker() {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                return _latLngInterpolator.interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");


        for (int i = 0; i < _trips.size(); i++) {
            animator = ObjectAnimator.ofObject(_marker, property, typeEvaluator, _trips.get(i));
        }

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {
                //           System.out.println("Animation Cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //           System.out.println("Animation Repeat");
            }

            @Override
            public void onAnimationStart(Animator animation) {

                //            System.out.println("Status--> started " + _trips.size() + " Animation started ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listPoint.clear();
                //      System.out.println("Status--> ended " + _trips.size() + " Animation ended ");
                if (c_marker != null && map != null) {
                    c_marker.setVisible(false);
                    c_marker.remove();
                    a_marker = map.addMarker(new MarkerOptions().position(savedLatLng).rotation(90).anchor(0.5f, 0.5f).title("" + NC.getResources().getString(R.string.you_are_here)).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
                    a_marker.setVisible(true);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(savedLatLng, zoom));
                    if (savedpoint.size() > 1) {
                        for (int i = 0; i < savedpoint.size(); i++) {
                            listPoint.add(savedpoint.get(i));
                        }
                        savedpoint.clear();
                        animStarted = false;
                        animLocation = true;
                    } else

                    {
                        animStarted = false;
                        animLocation = false;
                    }
                }

            }
        });
        animator.setDuration(5000);
        animator.start();
    }

   /* public void CompleteSuccessClick() {
        butt_onboard.setClickable(true);
        try {

//            WaitingTimerRun.ClearSession();

            System.out.println("Errror in okkk");
//            nonactiityobj.stopServicefromNonActivity(OngoingAct.this);
            MainActivity.mMyStatus.setOnstatus("Complete");
            stopService(new Intent(OngoingAct.this, WaitingTimerRun.class));
            myHandler.removeCallbacks(r);
            float h = 0.0f;
            if (waitingTime.equals(""))
                waitingTime = "00:00:00";
            String waitNoArabic = FontHelper.convertfromArabic(waitingTime);
            System.out.println("Errror in okkkk" + waitNoArabic + "---" + waitingTime);
            String[] split = waitNoArabic.split(":");
            int hr = Integer.parseInt(split[0]);
            float min = Integer.parseInt(split[1]);
            float sec = Float.parseFloat(split[2]);
            System.out.println("Hour:" + hr + "min:" + min + "sec:" + sec);
            min = (Float) min / 60;
            sec = sec / 3600;
            waitingHr = hr + min + sec;
            MainActivity.mMyStatus.setDriverWaitingHr(Float.toString(waitingHr));
            SessionSave.saveSession("waitingHr", Float.toString(waitingHr), OngoingAct.this);
            // SessionSave.saveSession("drop_location", SessionSave.getSession("Driver_locations_home", OngoingAct.this), OngoingAct.this);
            final String completeUrl = "type=complete_trip";
            new CompleteTrip(completeUrl, latitude1, longitude1);

//                    long handlerTime = 1000;
//                    if (startLocationService(new Intent(OngoingAct.this, LocationUpdate.class)) != null) {
//                        Toast.makeText(getBaseContext(), "Service is already running", Toast.LENGTH_SHORT).show();
//                        float[] diss = new float[2];
//                        Location.distanceBetween(lastlatitude, lastlongitude, LocationUpdate.currentLocation.getLatitude(), LocationUpdate.currentLocation.getLongitude(), diss);
//                        if (diss[0] > 300) {
//                            handlerTime = 5000;
//                        }
//                        System.out.println("WayDistance***" + diss[0]);
//
//                    } else {
//                        Toast.makeText(getBaseContext(), "There is no service running, starting service..", Toast.LENGTH_SHORT).show();
//                        nonactiityobj.startServicefromNonActivity(OngoingAct.this);
//                        handlerTime = 10000;
//                    }
//
//                    boolean distanceCalcInprogress = false;
//
//                    JSONArray wayData = SessionSave.ReadGoogleWaypoints(OngoingAct.this);
//                    System.out.println("WayDistance**" + wayData);
//                    try {
//                        for (int i = 0; i < wayData.length(); i++) {
//                            WayPointsData wayPointsData = new Gson().fromJson(wayData.get(i).toString(), WayPointsData.class);
//                            if (wayPointsData.getDist() == 0.0)
//                                distanceCalcInprogress = true;
//                            System.out.println("WayDistance" + wayPointsData.getDist());
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (distanceCalcInprogress) {
//                        Toast.makeText(OngoingAct.this, NC.getString(R.string.calculation_progress), Toast.LENGTH_SHORT).show();
//                    } else {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                new CompleteTrip(completeUrl, currentLatitude, currentLongtitude);
//                            }
//                        }, handlerTime);
//                    }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errror in okkkk" + e);
            // TODO: handle exception
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showLoading(OngoingAct.this);
        String Address;
        Double obtainedlatitude, obtainedlongitude;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300) {
            if (data != null) {
                Bundle res = data.getExtras();
                Address = res.getString("param_result");
                obtainedlatitude = res.getDouble("lat");
                obtainedlongitude = res.getDouble("lng");
                latitude1 = obtainedlatitude;
                longitude1 = obtainedlongitude;
                SessionSave.saveSession("drop_location", Address, OngoingAct.this);
                Double lastknownlatitude = Double.parseDouble(SessionSave.getSession("lastknowlats", OngoingAct.this));
                Double lastknowlongitude = Double.parseDouble(SessionSave.getSession("lastknowlngs", OngoingAct.this));
                // LocalDistanceCalculation.newInstance(OngoingAct.this).haversine(lastknownlatitude, lastknowlongitude, latitude1, longitude1);
            }
        }
    }

    public Location GetCurrentLocationProvider() {
        startLocationUpdates();
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

   /* private void RetryLocationPopUp() {
        cancelLoading();
        alert_view_dialog(OngoingAct.this, NC.getResources().getString(R.string.message), NC.getString(R.string.address_cant_fetch), NC.getString(R.string.retry), NC.getString(R.string.use_map), false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retrycount++;
                showLoading(OngoingAct.this);
                final Location mLocation = GetCurrentLocationProvider();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mLocation != null) {
                            latitude1 = mLocation.getLatitude();
                            longitude1 = mLocation.getLongitude();
                            new Address_s(OngoingAct.this, new LatLng((latitude1), (longitude1))).execute();
                        } else {
                            cancelLoading();
                            RetryLocationPopUp();
                        }
                    }
                }, 2000);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // startActivityForResult((new Intent(OngoingAct.this, SelectDropLocationAct.class)), 300);
            }
        }, "");
//        try {
//            if (cDialog != null)
//                cDialog.dismiss();
//            final View view = View.inflate(OngoingAct.this, R.layout.netcon_lay, null);
//            cDialog = new Dialog(OngoingAct.this, R.style.dialogwinddow);
//            cDialog.setContentView(view);
//            cDialog.setCancelable(false);
//            cDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        FontHelper.applyFont(OngoingAct.this, cDialog.findViewById(R.id.alert_id));
//        final TextView title_text = (TextView) cDialog.findViewById(R.id.title_text);
//        final TextView message_text = (TextView) cDialog.findViewById(R.id.message_text);
//        final Button button_success = (Button) cDialog.findViewById(R.id.button_success);
//        final Button button_failure = (Button) cDialog.findViewById(R.id.button_failure);
//        final View view_line = (View) cDialog.findViewById(R.id.retry_view_line);
//        if (retrycount > 2) {
//            button_failure.setVisibility(View.VISIBLE);
//            view_line.setVisibility(View.VISIBLE);
//        } else {
//            button_failure.setVisibility(View.GONE);
//            view_line.setVisibility(View.GONE);
//        }
//        title_text.setText("" + NC.getResources().getString(R.string.message));
//        message_text.setText(NC.getString(R.string.address_cant_fetch));
//        button_success.setText(NC.getString(R.string.retry));
//        button_failure.setText(NC.getString(R.string.use_map));
//        button_success.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                retrycount++;
//                cDialog.dismiss();
//                showLoading(OngoingAct.this);
//                final Location mLocation = GetCurrentLocationProvider();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mLocation != null) {
//                            currentLatitude = mLocation.getLatitude();
//                            currentLongtitude = mLocation.getLongitude();
//                            new Address_s(OngoingAct.this, new LatLng((currentLatitude), (currentLongtitude))).execute();
//                        } else {
//                            cancelLoading();
//                            RetryLocationPopUp();
//                        }
//                    }
//                }, 2000);
//            }
//        });
//        button_failure.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                // TODO
//                // Auto-generated
//                // method stub
//                cDialog.dismiss();
//                startActivityForResult((new Intent(OngoingAct.this, SelectDropLocationAct.class)), 300);
//            }
//        });
    }*/

    public void alert_view_dialog(final Context mContext, String title, String message,
                                  String success_txt, String failure_txt,
                                  Boolean cancelable_val, final DialogInterface.OnClickListener postive_dialogInterface, final DialogInterface.OnClickListener negative_dialogInterface, final String s) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setCancelable(cancelable_val);
        dialog.setMessage(message);
        dialog.setPositiveButton(success_txt, postive_dialogInterface)
                .setNegativeButton(failure_txt, negative_dialogInterface);

        alert = dialog.create();
        alert.setOnShowListener(
                new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        if (retrycount > 2) {
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
                        } else {
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                        }
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.button_accept));
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.black));
                    }
                });
        alert.show();
    }

    public class FindApproxDistance implements APIResult {
        int type = 0;

        public FindApproxDistance(double P_latitude, double P_longitude,
                                  double D_latitude, double D_longitude, int _type) {
            ArrayList<LatLng> points = new ArrayList<>();
            this.type = _type;
            if (P_latitude != 0 && D_latitude != 0) {
                LatLng pick = new LatLng(P_latitude, P_longitude);
                LatLng drop = new LatLng(D_latitude, D_longitude);
                points.add(pick);
                points.add(drop);
                String url = GetDistanceTime(OngoingAct.this, points, "en", false);
                System.out.println("UUUUUUUUU" + url);

                if (url != null && !url.equals(""))
                    new APIService_Retrofit_JSON(OngoingAct.this, this, null, true, url, true).execute();
            }


        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                try {
                    System.out.println("carmodel" + result);
                    if (new JSONObject(result).getString("status").equalsIgnoreCase("OK") &&
                            new JSONObject(result).getJSONArray("routes").length() != 0) {
                        JSONObject obj = new JSONObject(result).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
                        JSONObject ds = obj.getJSONObject("distance");
                        String dis = ds.getString("value");
                        JSONObject timee = obj.getJSONObject("duration");
                        String end_address = obj.getString("end_address");
                        SessionSave.saveSession("drop_location", "" + end_address, OngoingAct.this);
                        String time = timee.getString("value");
                        double times = Double.parseDouble(time) / 60;
                        double dist = Double.parseDouble(dis) / 1000;
//                        SessionSave.saveSession("local_distance", "" + dist, OngoingAct.this);
//                        System.out.println("dist dist ----------->    " + dist + "travel_km -- > " + CommonData.travel_km);
//
//                        System.out.println("Location dist dist ----------->    " + SessionSave.getDistance(OngoingAct.this));
//
                        if (CommonData.travel_km < 1) {
                            CommonData.travel_km = dist;
                        } else if (CommonData.travel_km < SessionSave.getDistance(OngoingAct.this)) {
                            CommonData.travel_km = SessionSave.getDistance(OngoingAct.this);
                        }
//                        else {
//                            CommonData.travel_km = SessionSave.getDistance(OngoingAct.this);
//                        }
//                        CommonData.travel_km = dist;

//                        CommonData.travel_km = SessionSave.getDistance(OngoingAct.this);
                        final String completeUrl = "type=complete_trip";
                        new CompleteActualTrip(completeUrl, latitude1, longitude1, CommonData.travel_km);

                    } else {

                        CommonData.travel_km = SessionSave.getDistance(OngoingAct.this);
                        final String completeUrl = "type=complete_trip";
                        new CompleteActualTrip(completeUrl, latitude1, longitude1, CommonData.travel_km);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
            }
        }
    }

    public String GetDistanceTime(Context c, ArrayList<LatLng> points, String language, boolean optimize) {
        context = c;
        if (points.size() == 2) {
            String url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, "driving");
            // new connectAsyncTask(url, false).execute();

            System.out.println("GetDistanceTime " + url);
            return url;
        }

        return "";
    }

    private String makeURL(double sourcelat, double sourcelog, double destlat, double destlog, String mode) {
        StringBuilder urlString = new StringBuilder();
        if (mode == null)
            mode = "driving";
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=" + mode + "&alternatives=true&language=" + SessionSave.getSession("Lang", OngoingAct.this));
        //change this key with buisness key
        urlString.append("&key=" + "AIzaSyD7zv0GV1pqixXaPGTaiys_GoGDKlitjuY");
        return urlString.toString();
    }


    private class CompleteActualTrip implements APIResult {
        public CompleteActualTrip(final String url, final Double latitude, final Double longitude, final Double distance) {

            try {
                JSONObject j = new JSONObject();
                j.put("trip_id", SessionSave.getSession("trip_id", OngoingAct.this));
                j.put("drop_latitude", Double.toString(latitude));
                j.put("drop_longitude", Double.toString(longitude));
                j.put("drop_location", Uri.encode(SessionSave.getSession("drop_location", OngoingAct.this).replaceAll("\n", " ")));
                j.put("distance", "" + distance);
                j.put("actual_distance", "" + distance);
                j.put("waiting_hour", SessionSave.getSession("waitingHr", OngoingAct.this));

                if (!APP_VERSION.equals(""))
                    j.put("driver_app_version", APP_VERSION);
                else {
                    PackageManager manager = getPackageManager();
                    String curVersion = manager.getPackageInfo(getPackageName(), 0).versionName;
                    j.put("driver_app_version", curVersion);
                }

                j.put("waypoints", SessionSave.ReadGoogleWaypoints(OngoingAct.this));
                System.out.println("complete trip dist ----------->   after  " + j.toString());
                if (isOnline()) {
                    new APIService_Retrofit_JSON(OngoingAct.this, this, j, false).execute(url);
                } else {
                    final String completeUrl = "type=complete_trip";
                    new CompleteActualTrip(completeUrl, latitude1, longitude1, distance);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        /**
         * Parse the response and update the UI.
         */
        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {
                final JSONObject json = new JSONObject(result);
                if (isSuccess) {
                    if (cDialog.isShowing())
                        cDialog.dismiss();
                    butt_onboard.setClickable(true);
                    if (json.getInt("status") == 4) {

                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        SessionSave.saveSession("Ongoing", "farecal", OngoingAct.this);
                        SessionSave.saveSession("travel_status", "5", OngoingAct.this);
                        CommonData.currentspeed = "";
                        speedtext.setText("" + String.format(Locale.UK, NC.getResources().getString(R.string.m_timer)));
                        SessionSave.saveSession("speedwaiting", "", OngoingAct.this);
                        SessionSave.setWaitingTime(0L, OngoingAct.this);
                        WaitingTimerRun.ClearSession(OngoingAct.this);
                        Intent i = new Intent(OngoingAct.this, WaitingTimerRun.class);
                        stopService(i);
                        myHandler.removeCallbacks(r);
                        MainActivity.mMyStatus.setsaveTime(timeclear);
                        showLoading(OngoingAct.this);
                        final Intent farecal = new Intent(OngoingAct.this, FarecalcAct.class);
                        farecal.putExtra("from", "direct");
                        farecal.putExtra("message", result);
                        startActivity(farecal);
                        finish();
                    } else if (json.getInt("status") == -1) {
                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        MainActivity.mMyStatus.setOndriverLatitude("");
                        MainActivity.mMyStatus.setOndriverLongitude("");
                        SessionSave.saveSession("status", "F", OngoingAct.this);
                        SessionSave.saveSession("trip_id", "", OngoingAct.this);
                        final String status_update = "type=driver_status_update&driver_id=" + SessionSave.getSession("Id", OngoingAct.this) + "&latitude=" + latitude1 + "&longitude=" + longitude1 + "&status=" + "F" + "&trip_id=";
                        SessionSave.saveSession("Ongoing", "flagger", OngoingAct.this);
                        new FreeUpdate(status_update);
                        showLoading(OngoingAct.this);
                        final Intent jobintent = new Intent(OngoingAct.this, MyStatus.class);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", json.getString("message"));
                        jobintent.putExtras(extras);
                        startActivity(jobintent);
                        finish();
                    } else {
                        alert_view(OngoingAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    butt_onboard.setClickable(true);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            ShowToast(OngoingAct.this, result);
                        }
                    });
                }
            } catch (final Exception e) {
                e.printStackTrace();
                butt_onboard.setClickable(true);
            }
        }
    }

}