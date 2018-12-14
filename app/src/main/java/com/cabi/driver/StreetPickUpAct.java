package com.cabi.driver;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Property;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cabi.driver.data.CommonData;
import com.cabi.driver.data.apiData.ApiRequestData;
import com.cabi.driver.data.apiData.EndStreetPickupResponse;
import com.cabi.driver.data.apiData.GetTripDetailResponse;
import com.cabi.driver.data.apiData.StreetPickUpResponse;
import com.cabi.driver.earningchart.EarningsAct;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.interfaces.StreetPickupInterface;
import com.cabi.driver.route.Route;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.CoreClient;
import com.cabi.driver.service.NonActivity;
import com.cabi.driver.service.ServiceGenerator;
import com.cabi.driver.service.WaitingTimerRun;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.GpsStatus;
import com.cabi.driver.utils.LatLngInterpolator;
import com.cabi.driver.utils.LocationUtils;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.cabi.driver.utils.StreetMapWrapperLayout;
import com.cabi.driver.utils.drawable_program.Drawables_program;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cabi.driver.data.CommonData.sContext;

/**
 * Created by developer on 10/11/16.
 */

/**
 * This class is used to show StreetPickUp Page from menu and allow to start trip by driver itself
 */
public class StreetPickUpAct extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnCameraChangeListener,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, StreetPickupInterface {

    private static String FETCH_ADDRESS;
    private static Double D_latitude;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private float zoom = 16f;
    private static final int MY_PERMISSIONS_REQUEST_GPS = 222;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private double latitude1;
    private double longitude1;
    private LatLng currentLatLng;
    private StreetMapWrapperLayout mapWrapperLayout;
    private double P_latitude;
    private double P_longitude;
    private Handler handlerServercall1;
    private Runnable callAddress;
    private Address_s address;
    public static int z = 1;
    boolean timer_started = false;

    LatLngInterpolator _latLngInterpolator = new LatLngInterpolator.Spherical();
    private RelativeLayout searchlay;
    private FrameLayout
            pickup_pinlay;
    private ImageView
            drop_pin, pickup_pin, pick_fav, drop_close;
    private LinearLayout
            pickupp, lay_pick_fav, dropppp, drop_fav;
    private TextView
            currentlocTxt, droplocEdt;
    private Double pickuplat = 0.0;
    private Double pickuplng = 0.0;
    private Double droplat = 0.0;
    private String fav_place_type;
    private String P_Address;
    private Dialog alertmDialog;
    private Double D_longitude;
    private String droplocTxt = "";
    private String pickuplocTxt = "";

    private RelativeLayout lay_model_home;
    private LinearLayout home_lay;
    private ImageView home_iv;
    private TextView home_txt;
    private LinearLayout earnings_lay;
    private ImageView earnings_iv;
    private TextView earnings_txt;
    private LinearLayout profile_lay;
    private ImageView profile_iv;
    private TextView profile_txt;
    private LinearLayout streetpick_lay;
    private ImageView streetpick_iv;
    private TextView streetPick_txt;


    //Trip started bottom Lay
    private LinearLayout carlayout;
    private android.support.design.widget.CoordinatorLayout trip_started_lay;
    private ImageView botton_layout, botton_navi;
    private LinearLayout design_bottom_sheet;
    private TextView fare_estimate;
    private TextView min_fare;
    private TextView below_miles_val, distance_fare;
    private TextView below_miles_txt;
    private TextView above_miles_val;
    private TextView above_miles_txt;
    private LinearLayout book_now_r;
    private Button butt_start_end;
    private Dialog mDialog;
    private ImageView map_icon;
    private boolean cancelClicked;
    private LinearLayout progresss;
    private float bearing;
    private boolean animLocation;
    private final Handler myHandler = new Handler();
    ArrayList<LatLng> listPoint = new ArrayList<LatLng>();
    ArrayList<LatLng> savedpoint = new ArrayList<LatLng>();
    ArrayList<String> savedlocation = new ArrayList<String>();
    private Marker a_marker;
    private boolean animStarted = false;
    private LatLng savedLatLng;
    private double speed = 0.0;
    private Marker c_marker;
    private ObjectAnimator animator;
    private String Address = "Driver location";
    ArrayList<LatLng> _trips = new ArrayList<LatLng>();
    private Marker _marker;
    private float animteBearing;
    private static int UPDATE_INTERVAL = 5000; // 10 sec
    private static int FATEST_INTERVAL = 1000; // 5 sec
    private final int DISPLACEMENT = 200; // 10 meters
    private String METRIC;

    private TextView btn_shift;

    String checked = "OUT";
    NonActivity nonactiityobj = new NonActivity();
    private boolean NOT_FIRST_TIME;
    private CountDownTimer cTimer;
    private Dialog errorDialog;
    private Dialog cDialog;
    private String waitingTime = "";
    private TextView waiting_time;
    private TextView waiting_time_txt;
    private float bearings;
    private Location latlongfromService;

    // Initialize the views on layout and variable declarations
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street_pickup_lay);
        btn_shift = (TextView) findViewById(R.id.btn_shift);
        buildGoogleApiClient();
        init();
        sContext = StreetPickUpAct.this;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                init();
//            }
//        }, 50);


    }

    public void init() {
        FontHelper.applyFont(StreetPickUpAct.this, findViewById(R.id.ongoing_lay));
        //   Glide.with(this).load(SessionSave.getSession("image_path", this) + "headerLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.headicon));
        if (SessionSave.getSession("MetricsThis", StreetPickUpAct.this).trim().equals("Miles"))
            METRIC = "Miles";
        else
            METRIC = "Km";

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) StreetPickUpAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), StreetPickUpAct.this);


        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);


        searchlay = (RelativeLayout) findViewById(R.id.searchlay);
        butt_start_end = (Button) findViewById(R.id.butt_start_end);
        pickup_pinlay = (FrameLayout) findViewById(R.id.pickup_pinlay);
        drop_pin = (ImageView) findViewById(R.id.drop_pin);
        pickupp = (LinearLayout) findViewById(R.id.pickupp);
        pickup_pin = (ImageView) findViewById(R.id.pickup_pin);
        currentlocTxt = (TextView) findViewById(R.id.currentlocTxt);
        lay_pick_fav = (LinearLayout) findViewById(R.id.lay_pick_fav);
        pick_fav = (ImageView) findViewById(R.id.pick_fav);
        botton_navi = (ImageView) findViewById(R.id.botton_navi);
        dropppp = (LinearLayout) findViewById(R.id.dropppp);
        droplocEdt = (TextView) findViewById(R.id.droplocTxt);
        drop_fav = (LinearLayout) findViewById(R.id.drop_fav);
        drop_close = (ImageView) findViewById(R.id.drop_close);
        currentlocTxt.setSelected(true);
        droplocEdt.setSelected(true);
        droplocEdt.setText(NC.getString(R.string.droploc));
        map_icon = (ImageView) findViewById(R.id.map_icon);

        //setting map icon dynamically
        Glide.with(this).load(SessionSave.getSession("image_path", this) + "setPickupPin.png").placeholder(R.drawable.mappin).error(R.drawable.mappin).into((ImageView) findViewById(R.id.map_icon));

        lay_pick_fav.setVisibility(View.VISIBLE);
        carlayout = (LinearLayout) findViewById(R.id.carlayout);
        trip_started_lay = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.trip_started_lay);
        botton_layout = (ImageView) findViewById(R.id.botton_layout);


        Log.e("_imageurl_", SessionSave.getSession("image_path", this) + "mapmove.png");

        Glide.with(this).load(SessionSave.getSession("image_path", this) + "mapDirection.png").placeholder(R.drawable.mapmove).error(R.drawable.mapmove).into((ImageView) findViewById(R.id.botton_layout));


        design_bottom_sheet = (LinearLayout) findViewById(R.id.design_bottom_sheet);
        fare_estimate = (TextView) findViewById(R.id.fare_estimate);
        waiting_time = (TextView) findViewById(R.id.waiting_time);
        waiting_time_txt = (TextView) findViewById(R.id.waiting_time_txt);
        min_fare = (TextView) findViewById(R.id.min_fare);
        below_miles_val = (TextView) findViewById(R.id.below_miles_val);
        below_miles_txt = (TextView) findViewById(R.id.below_miles_txt);
        above_miles_val = (TextView) findViewById(R.id.above_miles_val);
        above_miles_txt = (TextView) findViewById(R.id.above_miles_txt);
        book_now_r = (LinearLayout) findViewById(R.id.book_now_r);
        butt_start_end = (Button) findViewById(R.id.butt_start_end);
        distance_fare = (TextView) findViewById(R.id.distance_fare);

        lay_model_home = (RelativeLayout) findViewById(R.id.lay_model_home);
        home_lay = (LinearLayout) findViewById(R.id.home_lay);
        home_iv = (ImageView) findViewById(R.id.home_iv);
        home_txt = (TextView) findViewById(R.id.home_txt);
        earnings_lay = (LinearLayout) findViewById(R.id.earnings_lay);
        earnings_iv = (ImageView) findViewById(R.id.earnings_iv);
        earnings_txt = (TextView) findViewById(R.id.earnings_txt);
        profile_lay = (LinearLayout) findViewById(R.id.profile_lay);
        profile_iv = (ImageView) findViewById(R.id.profile_iv);
        profile_txt = (TextView) findViewById(R.id.profile_txt);
        streetpick_lay = (LinearLayout) findViewById(R.id.streetpick_lay);
        progresss = (LinearLayout) findViewById(R.id.progresss);

        ImageView iv = (ImageView) findViewById(R.id.nodataTxt);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(StreetPickUpAct.this)
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        Glide.with(this).load(SessionSave.getSession("image_path", this) + "streetPickupFocus.png").placeholder(R.drawable.st_pickup_focus).error(R.drawable.st_pickup_focus).into((ImageView) findViewById(R.id.streetpick_iv));

        streetpick_iv = (ImageView) findViewById(R.id.streetpick_iv);
        streetPick_txt = (TextView) findViewById(R.id.streetPick_txt);
        mapWrapperLayout = (StreetMapWrapperLayout) findViewById(R.id.map_relative_layout);
        //streetpick_iv.setImageResource(R.drawable.st_pickup_focus);
        currentlocTxt.setText(R.string.fetching_address);
        setPickuplocTxt(NC.getResources().getString(R.string.fetching_address));
        //SessionSave.saveSession("trip_id", "6201", StreetPickUpAct.this);
//         SessionSave.saveSession("street_completed", "", StreetPickUpAct.this);
        //  callGetTripDetail();

        btn_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_shift.setClickable(false);
                new RequestingCheckBox();
            }
        });
        Glide.with(this).load(SessionSave.getSession("image_path", this) + "currentLocation.png").placeholder(R.drawable.mapmove).error(R.drawable.mapmove).into((ImageView) findViewById(R.id.botton_navi));
        botton_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movetoCurrentloc();

            }
        });
        botton_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationurl;
                if (latitude1 != 0.0 && longitude1 != 0.0 && getDroplat() != 0.0 && getDroplat() != 0.0) {
                    locationurl = "http://maps.google.com/maps?saddr=" + latitude1 + "," + longitude1 + "&daddr=" + getDroplat() + "," + getDroplng() + "";
                    // final String locationurl = "http://maps.google.com/maps?saddr=" + SessionSave.getSession("Driver_locations", OngoingAct.this) + "&daddr=" + mMyStatus.getPassengerOndropLocation().replace("|", "") + "";
                    Log.e("URL_Test", locationurl);
                    final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(locationurl));
                    startActivity(intent);
                } else {
                    locationurl = "http://maps.google.com/maps?saddr=" + latitude1 + "," + longitude1;
                    // final String locationurl = "http://maps.google.com/maps?saddr=" + SessionSave.getSession("Driver_locations", OngoingAct.this) + "&daddr=" + mMyStatus.getPassengerOndropLocation().replace("|", "") + "";
                    Log.e("URL_Test", locationurl);

                }
                final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(locationurl));
                startActivity(intent);
            }
        });
        //  SessionSave.saveSession("status", "A", StreetPickUpAct.this);
//        SessionSave.saveSession("trip_id", "", StreetPickUpAct.this);

        pickupp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                LocationRequestedBy = "P";
//                Bundle b = new Bundle();
//                b.putString("type", "P");
//                Intent i = new Intent(StreetPickUpAct.this, LocationSearchActivity.class);
//                i.putExtras(b);
//                startActivityForResult(i, CommonData.LocationResult);
            }
        });
        drop_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drop_fav.setVisibility(View.VISIBLE);
                lay_pick_fav.setVisibility(View.VISIBLE);
                pick_fav.setImageResource(R.drawable.plus1);
                dropppp.setVisibility(View.GONE);
                if (SessionSave.getSession("Lang", StreetPickUpAct.this).equals("ar") || SessionSave.getSession("Lang", StreetPickUpAct.this).equals("fa"))
                    pickupp.setBackgroundResource(R.drawable.search_only_pickup);
                else
                    pickupp.setBackgroundResource(R.drawable.search_only_pickup);
                pickup_pin.setVisibility(View.VISIBLE);
                pickup_pinlay.setVisibility(View.GONE);
                droplocEdt.setText("");
            }
        });
        dropppp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LocationRequestedBy = "D";
                Bundle b = new Bundle();
                b.putString("type", "D");
                Intent i = new Intent(StreetPickUpAct.this, LocationSearchActivity.class);
                i.putExtras(b);
                startActivityForResult(i, CommonData.LocationResult);
            }
        });


        earnings_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StreetPickUpAct.this, EarningsAct.class);
                startActivity(intent);
                finish();

            }
        });

        home_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StreetPickUpAct.this, MyStatus.class);
                startActivity(intent);
                finish();
            }
        });

        profile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StreetPickUpAct.this, MeAct.class);
                startActivity(intent);
                finish();
            }
        });

        streetpick_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(StreetPickUpAct.this, StreetPickUpAct.class);
//                startActivity(intent);
//                finish();
            }
        });

        if (SessionSave.getSession("shift_status", StreetPickUpAct.this).equals("IN")) {

            // btn_shift.setBackgroundResource(R.drawable.shiftbg);
            btn_shift.setText(NC.getString(R.string.online));
            Drawables_program.shift_on(btn_shift);
            SessionSave.saveSession(CommonData.SHIFT_OUT, false, StreetPickUpAct.this);
            nonactiityobj.startServicefromNonActivity(StreetPickUpAct.this);

        } else {

//            btn_shift.setBackgroundResource(R.drawable.shiftbggrey);
            btn_shift.setText(NC.getString(R.string.offline));
            Drawables_program.shift_bg_grey(btn_shift);
            SessionSave.saveSession(CommonData.SHIFT_OUT, true, StreetPickUpAct.this);
            nonactiityobj.stopServicefromNonActivity(StreetPickUpAct.this);
        }

        preUI();
        FETCH_ADDRESS = NC.getResources().getString(R.string.fetching_address);
        handlerServercall1 = new Handler(Looper.getMainLooper());


        callAddress = new Runnable() {
            @Override
            public void run() {
                // System.out.println("__________" + z + "____" + StreetPickUpAct.this);
                if (z == 1 && StreetPickUpAct.this != null) {
                    try {
                        if (address != null)
                            address.cancel(true);
                        address = new Address_s(StreetPickUpAct.this, new LatLng(P_latitude, P_longitude));
                        address.execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // FindNearestlocal();
                }
            }
        };
        movetoCurrentloc();
        butt_start_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  System.out.println("_______________________btn");
                if (NetworkStatus.isOnline(StreetPickUpAct.this)) {
                    if (!currentlocTxt.getText().toString().trim().equals(NC.getString(R.string.fetching_address)))
                        if (SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals(""))
                            callStartTrip();
                        else {
                            //    System.out.println("_______________________fdshndjsh");
                            cancelClicked = true;
                            //    showDialog();
                            CompleteTrip(StreetPickUpAct.this);

                            //  callEndTrip();}
                        }
                } else
                    Toast.makeText(StreetPickUpAct.this, getString(R.string.check_net_connection), Toast.LENGTH_SHORT).show();
            }
        });
//        Intent in = new Intent(StreetPickUpAct.this, OngoingAct.class);
//        startActivity(in);
        // startService(new Intent(StreetPickUpAct.this, LocationUpdate.class));


//        SessionSave.saveSession("trip_id", "5962", StreetPickUpAct.this);
//        SessionSave.saveSession("status", "A", StreetPickUpAct.this);
        //      SessionSave.saveSession("Id", "1531", StreetPickUpAct.this);
        //  finish();
        // trip_started_lay.setVisibility(View.VISIBLE);
        cTimer = new CountDownTimer(6000000, 1000) {

            @Override
            public void onTick(long l) {
              /*  System.out.println("Timer started");

                if (!SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals("")) {
                    if (!CommonData.DISTANCE_FARE.trim().equals(""))
                        System.out.println("Timer started"+CommonData.DISTANCE_FARE+"......"+CommonData.travel_km);
                        distance_fare.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + CommonData.DISTANCE_FARE);
                    timer_started = true;
                    fare_estimate.setText(String.valueOf(CommonData.travel_km) + " " + METRIC);
                    FontHelper.applyFont(StreetPickUpAct.this, fare_estimate, "digital.ttf");
                }*/
            }

            @Override
            public void onFinish() {

            }
        };


        //  System.out.println("__________+" + SessionSave.getSession("trip_id", StreetPickUpAct.this).trim());
        if (!SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals("")) {
            map_icon.setVisibility(View.GONE);
            trip_started_lay.setVisibility(View.VISIBLE);

            updateStartTripUI();
//            myHandler.postDelayed(r, 0);
            if (!SessionSave.getSession("street_completed", StreetPickUpAct.this).trim().equals("")) {
                final Intent farecal = new Intent(StreetPickUpAct.this, FarecalcAct.class);
                Gson gson = new GsonBuilder().create();
                String message = SessionSave.getSession("street_completed", StreetPickUpAct.this);
                //   System.out.println("_______________" + message);
                farecal.putExtra("from", "direct");
                farecal.putExtra("from_split", true);
                farecal.putExtra("message", message);
                startActivity(farecal);
                finish();
            } else
                callGetTripDetail();
        } else {

            map_icon.setVisibility(View.VISIBLE);
            trip_started_lay.setVisibility(View.GONE);
            //SessionSave.saveSession("street_completed", "", StreetPickUpAct.this);
            progresss.setVisibility(View.GONE);

        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(StreetPickUpAct.this, MyStatus.class));
//        finish();
//    }


    /**
     * Showing progress dialog
     */
    public void showDialog() {
        try {
            if (NetworkStatus.isOnline(StreetPickUpAct.this)) {
                View view = View.inflate(StreetPickUpAct.this, R.layout.progress_bar, null);
                mDialog = new Dialog(StreetPickUpAct.this, R.style.dialogwinddow);
                mDialog.setContentView(view);
                mDialog.setCancelable(false);
                mDialog.show();

                ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(StreetPickUpAct.this)
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);

            } else {

            }
        } catch (Exception e) {

        }

    }


    /**
     * Close dialog
     */
    public void closeDialog() {
        try {
            if (mDialog != null)
                if (mDialog.isShowing())
                    mDialog.dismiss();
        } catch (Exception e) {

        }
    }

    /**
     * This is method for confirmation for complete the trip
     */
    public void CompleteTrip(final Context context) {
        try {
            if (cDialog != null)
                cDialog.dismiss();
            final View view = View.inflate(StreetPickUpAct.this, R.layout.netcon_lay, null);
            cDialog = new Dialog(StreetPickUpAct.this, R.style.dialogwinddow);
            cDialog.setContentView(view);
            cDialog.setCancelable(false);
            cDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FontHelper.applyFont(StreetPickUpAct.this, cDialog.findViewById(R.id.alert_id));
        final TextView title_text = (TextView) cDialog.findViewById(R.id.title_text);
        final TextView message_text = (TextView) cDialog.findViewById(R.id.message_text);
        final Button button_success = (Button) cDialog.findViewById(R.id.button_success);
        final Button button_failure = (Button) cDialog.findViewById(R.id.button_failure);
        button_failure.setVisibility(View.VISIBLE);
        title_text.setText("" + NC.getResources().getString(R.string.message));
        message_text.setText("" + NC.getResources().getString(R.string.confirm_complete));
        button_success.setText("" + NC.getResources().getString(R.string.yes));
        button_failure.setText("" + NC.getResources().getString(R.string.cancel));
        button_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO
                // Auto-generated
                // method stub
                try {
                    //  System.out.println("Errror in okkk");
                    myHandler.removeCallbacks(r);
//                    nonactiityobj.stopServicefromNonActivity(OngoingAct.this);
//                    MainActivity.mMyStatus.setOnstatus("Complete");
//                    stopService(new Intent(OngoingAct.this, WaitingTimerRun.class));
//                    myHandler.removeCallbacks(r);
//                    float h = 0.0f;
//                    if (waitingTime.equals(""))
//                        waitingTime = "00:00:00";
//                    String waitNoArabic = FontHelper.convertfromArabic(waitingTime);
//                    System.out.println("Errror in okkkk" + waitNoArabic + "---" + waitingTime);
//                    String[] split = waitNoArabic.split(":");
//                    int hr = Integer.parseInt(split[0]);
//                    float min = Integer.parseInt(split[1]);
//                    float sec = Float.parseFloat(split[2]);
//                    System.out.println("Hour:" + hr + "min:" + min + "sec:" + sec);
//                    min = (Float) min / 60;
//                    sec = sec / 3600;
//                    waitingHr = hr + min + sec;
//                    MainActivity.mMyStatus.setDriverWaitingHr(Float.toString(waitingHr));
//                    SessionSave.saveSession("waitingHr", Float.toString(waitingHr), OngoingAct.this);
                    cDialog.dismiss();
                    showDialog();
                    new Address_s(StreetPickUpAct.this, new LatLng(latitude1, longitude1)).execute();
                    // SessionSave.saveSession("drop_location", SessionSave.getSession("Driver_locations_home", OngoingAct.this), OngoingAct.this);
                    // final String completeUrl = "type=complete_trip";
                    //new OngoingAct.CompleteTrip(completeUrl, latitude1, longitude1);
                } catch (Exception e) {
                    e.printStackTrace();
                    //    System.out.println("Errror in okkkk" + e);
                    // TODO: handle exception
                }
            }
        });
        button_failure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO
                // Auto-generated
                // method stub
                //  butt_onboard.setClickable(true);
                cDialog.dismiss();
            }
        });
        // TODO Auto-generated method stub
    }

    /**
     * TripDetail API response parsing.
     */
    private void callGetTripDetail() {
        CoreClient client = new ServiceGenerator(StreetPickUpAct.this).createService(CoreClient.class);
        ApiRequestData.TripDetailRequest request = new ApiRequestData.TripDetailRequest();
        request.trip_id = SessionSave.getSession("trip_id", this);
        //  request.trip_id = "5962";


        // Call<StreetPickUpResponse> response = client.startStreetTrip(request, SessionSave.getSession("Lang", StreetPickUpAct.this));
        Call<GetTripDetailResponse> responsed = client.getTripDetail(ServiceGenerator.COMPANY_KEY, request, "en", ServiceGenerator.DYNAMIC_AUTH_KEY);
        responsed.enqueue(new Callback<GetTripDetailResponse>() {
            @Override
            public void onResponse(Call<GetTripDetailResponse> call, Response<GetTripDetailResponse> response) {

                if (StreetPickUpAct.this != null && response != null) {
                    GetTripDetailResponse data = response.body();
                    //  System.out.println("__________*********"+data.Detail);
                    // SessionSave.saveSession("status", "A", StreetPickUpAct.this);
                    //  trip_started_lay.setVisibility(View.VISIBLE);
                    //      System.out.println("_______________sdfdsffd" + data.detail.trip_id);
                    progresss.setVisibility(View.GONE);
                    String p_taxi_speed = data.detail.taxi_min_speed;
                    if (p_taxi_speed.length() > 0 && !p_taxi_speed.equals(null)) {
                        SessionSave.saveSession("taxi_speed", p_taxi_speed, StreetPickUpAct.this);
                        // SessionSave.saveSession("taxi_speed", "-100", OngoingAct.this);
                    }
                    // SessionSave.saveSession("trip_id", String.valueOf(data.detail.trip_id), StreetPickUpAct.this);
                    if (data.detail.street_pickup_trip.trim().equals("1")) {
                        SessionSave.saveSession("travel_status", "2", StreetPickUpAct.this);
                        if (!data.detail.drop_location.trim().equals("") && !data.detail.drop_location.trim().equals(NC.getResources().getString(R.string.droploc).trim())) {
                            map.clear();
                            ArrayList<LatLng> pp = new ArrayList<>();
                            pp.add(new LatLng(Double.parseDouble(data.detail.pickup_latitude), Double.parseDouble(data.detail.pickup_longitude)));
                            pp.add(new LatLng(Double.parseDouble(data.detail.drop_latitude), Double.parseDouble(data.detail.drop_longitude)));
                            setDroplocTxt(data.detail.drop_location);

                            Route route = new Route();
                            if (pp.size() > 1) {
                                route.drawRoute(map, StreetPickUpAct.this, pp, "en", true);
                                map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data.detail.pickup_latitude), Double.parseDouble(data.detail.pickup_longitude))).title("" + NC.getResources().getString(R.string.pickuploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(false));
                                map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data.detail.drop_latitude), Double.parseDouble(data.detail.drop_longitude))).title("" + NC.getResources().getString(R.string.pickuploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).draggable(true));
                            } else {
                                alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.getting_gps_low), "" + NC.getResources().getString(R.string.ok), "");
                                map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data.detail.pickup_latitude), Double.parseDouble(data.detail.pickup_longitude))).title("" + NC.getResources().getString(R.string.pickuploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(false));
                            }


                        } else {
                            lay_pick_fav.setVisibility(View.GONE);
                            dropppp.setVisibility(View.GONE);


                        }
                        myHandler.postDelayed(r, 0);

                        setPickuplocTxt(data.detail.current_location);
                        setPickuplat(Double.parseDouble(data.detail.pickup_latitude));
                        setPickuplng(Double.parseDouble(data.detail.pickup_longitude));
                        updateStartTripUI();
                        drop_fav.setVisibility(View.GONE);
                        currentlocTxt.setOnClickListener(null);
                        dropppp.setOnClickListener(null);
                        movetoCurrentloc();
                    } else {
                        startActivity(new Intent(StreetPickUpAct.this, OngoingAct.class));
                        Toast.makeText(StreetPickUpAct.this, NC.getString(R.string.you_are_in_trip), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<GetTripDetailResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        myHandler.removeCallbacks(r);
        super.onDestroy();
    }

    /**
     * Start Trip API response parsing.
     */
    private void callStartTrip() {
        CoreClient client = new ServiceGenerator(StreetPickUpAct.this).createService(CoreClient.class);
        ApiRequestData.StreetPickRequest request = new ApiRequestData.StreetPickRequest();
        request.driver_id = SessionSave.getSession("Id", this);
        SessionSave.saveSession("locationhistory", "", getApplicationContext());
        SessionSave.setDistance((float) 0.0, getApplicationContext());
//        request.driver_id = "1531";
        String curVersion = "";
        PackageManager manager = getPackageManager();
        try {
            curVersion = manager.getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        request.driver_app_version = curVersion;
        request.pickup_latitude = String.valueOf(pickuplat);
        request.pickup_longitude = String.valueOf(pickuplng);
        request.drop_latitude = String.valueOf(droplat);
        request.drop_longitude = String.valueOf(droplng);
        request.pickup_location = getPickuplocTxt();
        request.drop_location = getDroplocTxt();

        request.approx_distance = "";
        request.approx_fare = "";
        request.approx_duration = "";
        request.time_to_reach_passen = "";
        request.cityname = "";
        request.motor_model = "";

        // Log.e("StartTrip", request.toString());
        showDialog();
        // Call<StreetPickUpResponse> response = client.startStreetTrip(request, SessionSave.getSession("Lang", StreetPickUpAct.this));
        Call<StreetPickUpResponse> response = client.startStreetTrip(ServiceGenerator.COMPANY_KEY, request, SessionSave.getSession("Lang", StreetPickUpAct.this), ServiceGenerator.DYNAMIC_AUTH_KEY);
        response.enqueue(new Callback<StreetPickUpResponse>() {
            @Override
            public void onResponse(Call<StreetPickUpResponse> call, Response<StreetPickUpResponse> response) {
                closeDialog();
                StreetPickUpResponse data = response.body();
                //  System.out.println("__________*********" + data.detail.base_fare);

                //detail":{"driver_tripid":5979,"base_fare":"12","min_fare":"10",
                // "below_km":"2.2","above_km":"2.2","below_above_km":"12"}
                //  data.detail.
                SessionSave.saveSession("travel_status", "2", StreetPickUpAct.this);
                if (data != null && StreetPickUpAct.this != null) {
                    if (cTimer != null)
                        cTimer.start();
                    if (data.status == 1) {
                        SessionSave.setWaitingTime(0L, StreetPickUpAct.this);
                        Intent i = new Intent(StreetPickUpAct.this, WaitingTimerRun.class);
                        startService(i);
                        myHandler.postDelayed(r, 0);
                        CommonData.travel_km = 0.0;
                        CommonData.DISTANCE_FARE = "";
                        lay_model_home.setVisibility(View.GONE);
                        SessionSave.saveSession("status", "A", StreetPickUpAct.this);
                        trip_started_lay.setVisibility(View.VISIBLE);
                        String p_taxi_speed = data.detail.taxi_min_speed;
                        if (p_taxi_speed != null && p_taxi_speed.length() > 0 && !p_taxi_speed.equals(null)) {
                            SessionSave.saveSession("taxi_speed", p_taxi_speed, StreetPickUpAct.this);
                            // SessionSave.saveSession("taxi_speed", "-100", OngoingAct.this);
                        }
                        //  Log.e("_tripid_", String.valueOf(data.detail.driver_tripid));
                        SessionSave.saveSession("trip_id", String.valueOf(data.detail.driver_tripid), StreetPickUpAct.this);
                        SessionSave.saveSession("min_fare", data.detail.min_fare, StreetPickUpAct.this);
                        SessionSave.saveSession("km_wise_fare", data.detail.km_wise_fare, StreetPickUpAct.this);
                        SessionSave.saveSession("additional_fare_per_km", data.detail.additional_fare_per_km, StreetPickUpAct.this);
                        SessionSave.saveSession("min_km", data.detail.km_wise_fare, StreetPickUpAct.this);
                        SessionSave.saveSession("above_km", data.detail.above_km, StreetPickUpAct.this);
                        SessionSave.saveSession("below_km", data.detail.below_km, StreetPickUpAct.this);
                        SessionSave.saveSession("below_above_km", data.detail.below_above_km, StreetPickUpAct.this);
                        SessionSave.saveSession("MetricsThis", data.detail.metric, StreetPickUpAct.this);
                        SessionSave.saveSession("street_fare", data.detail.min_fare, StreetPickUpAct.this);
                        SessionSave.saveSession("minute_fare", data.detail.minutes_fare, StreetPickUpAct.this);

                        // Toast.makeText(StreetPickUpAct.this, "", Toast.LENGTH_SHORT).show();
                        //           System.out.println("_____________" + SessionSave.getSession("MetricsThis", StreetPickUpAct.this));
                        if (SessionSave.getSession("MetricsThis", StreetPickUpAct.this).trim().equalsIgnoreCase("Miles"))
                            METRIC = "Miles";
                        else
                            METRIC = "Km";
                        if (!getDroplocTxt().trim().equals("")) {
                            map.clear();
                            ArrayList<LatLng> pp = new ArrayList<>();
                            pp.add(new LatLng(getPickuplat(), getPickuplng()));
                            pp.add(new LatLng(getDroplat(), getDroplng()));
                            //setDroplocTxt(data.detail.drop_location);
                            Route route = new Route();
                            if (pp.size() > 1) {
                                route.drawRoute(map, StreetPickUpAct.this, pp, "en", true);
                                map.addMarker(new MarkerOptions().position(new LatLng(getPickuplat(), getPickuplng())).title("" + NC.getResources().getString(R.string.pickuploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(false));
                                map.addMarker(new MarkerOptions().position(new LatLng(getDroplat(), getDroplng())).title("" + NC.getResources().getString(R.string.droploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).draggable(true));
                            } else {
                                alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.getting_gps_low), "" + NC.getResources().getString(R.string.ok), "");
                            }
                        } else
                            lay_pick_fav.setVisibility(View.GONE);
//                        setPickuplat(latitude1));
//                        setPickuplng(Double.parseDouble(data.detail.pickup_longitude));
                        if (droplat == 0.0)
                            dropGone();
                        updateStartTripUI();
                    } else {
                        Toast.makeText(StreetPickUpAct.this, data.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StreetPickUpResponse> call, Throwable t) {
                closeDialog();
            }
        });
    }


    /**
     * Updating UI after trip start
     * String.format(Locale.UK, "%.2f", sharedPreferences.getFloat("DISTANCE", 0));
     */
    public void updateStartTripUI() {
        //   System.out.println("_____________________starteeeeeeee" + SessionSave.getSession("min_fare", StreetPickUpAct.this));
        if (!SessionSave.getSession("km_wise_fare", StreetPickUpAct.this).isEmpty()) {
            boolean km_wise_fare = Integer.parseInt(SessionSave.getSession("km_wise_fare", StreetPickUpAct.this)) == 1 ? true : false;
            if (!km_wise_fare) {
                above_miles_val.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + SessionSave.getSession("above_km", StreetPickUpAct.this) + "/" + METRIC);
                below_miles_val.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + SessionSave.getSession("below_km", StreetPickUpAct.this) + "/" + METRIC);
                above_miles_txt.setText(NC.getString(R.string.above) + " " + SessionSave.getSession("below_above_km", StreetPickUpAct.this) + " " + METRIC);
                below_miles_txt.setText(NC.getString(R.string.below) + " " + SessionSave.getSession("below_above_km", StreetPickUpAct.this) + " " + METRIC);
            } else {
                above_miles_val.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + SessionSave.getSession("additional_fare_per_km", StreetPickUpAct.this) + "/" + METRIC);
                below_miles_val.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + SessionSave.getSession("minute_fare", StreetPickUpAct.this));
                above_miles_txt.setText(NC.getString(R.string.above) + " " + SessionSave.getSession("min_km", StreetPickUpAct.this) + " " + METRIC);
                below_miles_txt.setText(NC.getString(R.string.min_fare));
            }

        }


        min_fare.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + SessionSave.getSession("min_fare", StreetPickUpAct.this));

        butt_start_end.setText(NC.getString(R.string.end_trip));

        try {
            distance_fare.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + CommonData.DISTANCE_FARE);
            fare_estimate.setText(String.valueOf(CommonData.travel_km) + " " + METRIC);


            waitingTime = WaitingTimerRun.sTimer;
//            if (waitingTime.equals(""))
//                waitingTime = "00:00:00";
//            String waitNoArabic = FontHelper.convertfromArabic(waitingTime);
//            System.out.println("Errror in okkkk" + waitNoArabic + "---" + waitingTime);
//            String[] split = waitNoArabic.split(":");
//            int hr = Integer.parseInt(split[0]);
//            float min = Integer.parseInt(split[1]);
//            float sec = Float.parseFloat(split[2]);
//            System.out.println("Hour:" + hr + "min:" + min + "sec:" + sec);
//            min = (Float) min / 60;
//            sec = sec / 3600;
//            float waitingHr = hr + min + sec;
//            MainActivity.mMyStatus.setDriverWaitingHr(Float.toString(waitingHr));
//            SessionSave.saveSession("waitingHr", Float.toString(waitingHr), StreetPickUpAct.this);
            //  waiting_time.setText(""+waitingTime);
            FontHelper.applyFont(StreetPickUpAct.this, fare_estimate, "digital.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        map_icon.setVisibility(View.GONE);
        botton_navi.setVisibility(View.GONE);
        currentlocTxt.setOnClickListener(null);
        dropppp.setOnClickListener(null);
        pick_fav.setVisibility(View.GONE);
        lay_model_home.setVisibility(View.GONE);
        drop_close.setVisibility(View.GONE);
        btn_shift.setVisibility(View.GONE);
        if (lay_pick_fav != null)
            lay_pick_fav.setVisibility(View.GONE);
        Glide.with(this).load(SessionSave.getSession("image_path", this) + "currentLocation.png").placeholder(R.drawable.mapmove).error(R.drawable.mapmove).into((ImageView) findViewById(R.id.botton_navi));

        if (map != null)
            map.addMarker(new MarkerOptions().position(new LatLng(getPickuplat(), getPickuplng())).title("" + NC.getResources().getString(R.string.pickuploc)).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(false));
//        mapWrapperLayout.init(map, getPixelsFromDp(StreetPickUpAct.this, 39 + 20), false);
        if (cTimer != null)
            cTimer.start();

        //   startLocationUpdates();
    }


    /**
     * The thread for update the taxi speed,waiting time and distance traveled on UI.
     */
    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            String metricss = "";
            if (CommonData.travel_km != 0) {
            }
            // Log.w("Speed:" + CommonData.travel_km, "__" + speed + "taxi_speed" + SessionSave.getSession("taxi_speed", StreetPickUpAct.this));

            if (SessionSave.getSession("taxi_speed", StreetPickUpAct.this).trim().equals(""))
                SessionSave.saveSession("taxi_speed", "0.0", StreetPickUpAct.this);
            double p_minspeed = Double.parseDouble(SessionSave.getSession("taxi_speed", StreetPickUpAct.this));
            System.out.println("p_minspeed" + p_minspeed + "speed" + speed);


            waitingTime = WaitingTimerRun.sTimer;
            waiting_time.setText("" + waitingTime);

            waiting_time_txt.setText("" + String.format(Locale.UK, "%.2f", speed) + "(" + METRIC + "/hr)");
            //timerrrrrr speeee
            //  if(f)
            if (p_minspeed >= speed) {
                //  speed =20;
                CommonData.km_calc = 0;
                if (CommonData.speed_waiting_stop) {

                    final Intent i = new Intent(StreetPickUpAct.this, WaitingTimerRun.class);
                    startService(i);
                    myHandler.postDelayed(r, 0);
                } else {
                }
            } else {

                //      System.out.println("timer started ongoing" + SessionSave.getWaitingTime(StreetPickUpAct.this));

                CommonData.km_calc = 1;
                stopService(new Intent(StreetPickUpAct.this, WaitingTimerRun.class));
                //    myHandler.removeCallbacks(r);

            }
            myHandler.postDelayed(r, 1000);
        }
    };


    /**
     * End Trip API response parsing.
     */
    private void callEndTrip() {
        SessionSave.saveSession("locationhistory", "", getApplicationContext());
        SessionSave.setDistance((float) 0.0, getApplicationContext());
        timer_started = false;
        CoreClient client = new ServiceGenerator(StreetPickUpAct.this).createService(CoreClient.class);
        ApiRequestData.EndStreetPickup request = new ApiRequestData.EndStreetPickup();
        request.trip_id = SessionSave.getSession("trip_id", this);

        request.drop_latitude = String.valueOf(latitude1);
        request.drop_longitude = String.valueOf(longitude1);
        waitingTime = WaitingTimerRun.sTimer;
        if (waitingTime.equals(""))
            waitingTime = "00:00:00";
        String waitNoArabic = FontHelper.convertfromArabic(waitingTime);
        //  System.out.println("Errror in okkkk" + waitNoArabic + "---" + waitingTime);
        String[] split = waitNoArabic.split(":");
        int hr = Integer.parseInt(split[0]);
        float min = Integer.parseInt(split[1]);
        float sec = Float.parseFloat(split[2]);
        //    System.out.println("Hour:" + hr + "min:" + min + "sec:" + sec);
        min = (Float) min / 60;
        sec = sec / 3600;
        float waitingHr = hr + min + sec;
        MainActivity.mMyStatus.setDriverWaitingHr(Float.toString(waitingHr));
        SessionSave.saveSession("waitingHr", Float.toString(waitingHr), StreetPickUpAct.this);
        String curVersion = "";
        PackageManager manager = getPackageManager();
        try {
            curVersion = manager.getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        request.driver_app_version = curVersion;
        //have // TODO: 14/11/16
        request.distance = "";
        request.waiting_hour = SessionSave.getSession("waitingHr", StreetPickUpAct.this);
        request.drop_location = getDroplocTxt();
        request.actual_distance = String.valueOf(SessionSave.getDistance(StreetPickUpAct.this));
        request.distance_time = "";
        //SessionSave.saveSession("status", "F", StreetPickUpAct.this);
        //   SessionSave.saveSession("trip_id", "", StreetPickUpAct.this);
        //   Call<EndStreetPickupResponse> response = client.endStreetTrip(request, SessionSave.getSession("Lang", StreetPickUpAct.this));
        Call<EndStreetPickupResponse> response = client.endStreetTrip(ServiceGenerator.COMPANY_KEY, request, "en", ServiceGenerator.DYNAMIC_AUTH_KEY);
        showDialog();
        response.enqueue(new Callback<EndStreetPickupResponse>() {
            @Override
            public void onResponse(Call<EndStreetPickupResponse> call, Response<EndStreetPickupResponse> response) {
                closeDialog();
                EndStreetPickupResponse data = response.body();
                if (data != null && StreetPickUpAct.this != null)
                    if (data.status.equals("1") || data.status.equals("4")) {
                        SessionSave.saveSession("travel_status", "", StreetPickUpAct.this);
                        if (cTimer != null)
                            cTimer.cancel();
                        stopService(new Intent(StreetPickUpAct.this, WaitingTimerRun.class));
                        myHandler.removeCallbacks(r);
                        float h = 0.0f;
//                        if (waitingTime.equals(""))
//                            waitingTime = "00:00:00";
                        CommonData.DISTANCE_FARE = "";
                        btn_shift.setVisibility(View.VISIBLE);
                        //  StreetPickUpAct.this.startActivity(new Intent(StreetPickUpAct.this, FarecalcAct.class));
                        final Intent farecal = new Intent(StreetPickUpAct.this, FarecalcAct.class);
//                String lat = details.getStringExtra("lat");
//                String lon = details.getStringExtra("lon");
//                String distance = details.getStringExtra("distance");
//                String waitingHr = details.getStringExtra("waitingHr");
//                String drop_location = details.getStringExtra("drop_location");


                        Gson gson = new GsonBuilder().create();
                        String message = gson.toJson(data);
                        SessionSave.saveSession("street_fare", "0.00", StreetPickUpAct.this);
                        SessionSave.saveSession("street_completed", message, StreetPickUpAct.this);
                        //        System.out.println("_______________" + message);
                        farecal.putExtra("from", "direct");
                        farecal.putExtra("from_split", true);
                        farecal.putExtra("message", message);
//                farecal.putExtra("lon",String.valueOf(pickuplng));
//                farecal.putExtra("distance", String.valueOf(data.detail.distance));
//                farecal.putExtra("waitingHr", String.valueOf(data.detail.waiting_cost));
//                farecal.putExtra("drop_location", getDroplocTxt());


                        startActivity(farecal);
                        finish();
                    } else
                        Toast.makeText(StreetPickUpAct.this, data.message, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(StreetPickUpAct.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<EndStreetPickupResponse> call, Throwable t) {
                //  System.out.println("_______________uu");
                t.printStackTrace();
                closeDialog();
            }
        });
    }


    /**
     * Getting Drop location longitude
     */
    public Double getDroplng() {
        return droplng;
    }

    public void setDroplng(Double droplng) {
        this.droplng = droplng;
        D_longitude = droplng;
    }

    /**
     * Getting Drop location latitude
     */
    public Double getDroplat() {
        return droplat;
    }


    /**
     * set Drop location latitude
     */
    public void setDroplat(Double droplat) {
        this.droplat = droplat;
        StreetPickUpAct.D_latitude = droplat;
    }

    /**
     * get pickup longitude
     */
    public Double getPickuplng() {
        return pickuplng;
    }


    /**
     * set pickup longitude
     */
    public void setPickuplng(Double pickuplng) {
        this.pickuplng = pickuplng;
        P_longitude = pickuplng;
    }


    /**
     * get pickup latitude
     */
    public Double getPickuplat() {
        return pickuplat;

    }

    /**
     * set pickup latitude
     */
    public void setPickuplat(Double pickuplat) {
        this.pickuplat = pickuplat;
        P_latitude = pickuplat;
    }

    private Double droplng = 0.0;
    private Dialog r_mDialog;
    private static String LocationRequestedBy = "";


    /**
     * Getting pickup location txt
     */
    public String getPickuplocTxt() {
        return currentlocTxt.getText().toString();
    }


    /**
     * Setting pickup location
     */
    public void setPickuplocTxt(String pickuplocTxt) {
        this.pickuplocTxt = pickuplocTxt;
        currentlocTxt.setText(pickuplocTxt.replace(", null", ""));
    }

    /**
     * Getting Drop location txt
     */
    public String getDroplocTxt() {
        String dropLoc = "";
        if (droplocEdt.getText() != null)
            if (droplocEdt.getText().toString().trim().equals(NC.getResources().getString(R.string.droploc).trim()))
                dropLoc = "";

            else
                dropLoc = droplocEdt.getText().toString();
        return dropLoc;
    }


    /**
     * Setting drop location text
     */
    public void setDroplocTxt(String droplocTxt) {
        this.droplocTxt = droplocTxt;
        droplocEdt.setText(droplocTxt);

        dropVisible();
    }

    /**
     * To intiate googleApiClient
     */
    protected synchronized void buildGoogleApiClient() {
        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_SECONDS);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(LocationUtils.FAST_CEILING_IN_SECONDS);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

    }

    /**
     * visibling and invisibling pickup and dropup address
     */
    public void dropVisible() {

        dropppp.setVisibility(View.VISIBLE);
        // pick_fav.setVisibility(View.GONE);
        pickup_pinlay.setVisibility(View.VISIBLE);
        pickup_pin.setVisibility(View.GONE);
        if (lay_pick_fav != null)
            lay_pick_fav.setVisibility(View.GONE);
//        if (SessionSave.getSession("Lang", this).equals("ar"))
//            pickupp.setBackgroundResource(R.drawable.search_pickup_ar);
//        else
        pickupp.setBackgroundResource(R.drawable.search_pickup);
    }

    public void dropGone() {

        dropppp.setVisibility(View.GONE);
        // pick_fav.setVisibility(View.GONE);
        pickup_pinlay.setVisibility(View.GONE);
        pickup_pin.setVisibility(View.VISIBLE);
        if (lay_pick_fav != null)
            lay_pick_fav.setVisibility(View.VISIBLE);
//        if (SessionSave.getSession("Lang", this).equals("ar"))
//            pickupp.setBackgroundResource(R.drawable.search_pickup_ar);
//        else
        pickupp.setBackgroundResource(R.drawable.search_only_pickup);
    }

    @Override
    public void onStart() {

        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void stopLocationUpdates() {
        //  System.out.println("LocationSpeeeeedstopped");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStop() {
        if (errorDialog != null && errorDialog.isShowing())
            errorDialog.dismiss();
        if (mGoogleApiClient != null)
            stopLocationUpdates();
        if (handlerServercall1 != null)
            handlerServercall1.removeCallbacks(callAddress);
        if (cTimer != null)
            cTimer.cancel();
        if (cDialog != null)
            cDialog.dismiss();
        super.onStop();
    }

    /**
     * This function will be called initially after page load
     */
    public void preUI() {
        drop_fav.setVisibility(View.VISIBLE);
        lay_pick_fav.setVisibility(View.VISIBLE);
        dropppp.setVisibility(View.GONE);
        pickup_pin.setVisibility(View.VISIBLE);
        pickup_pinlay.setVisibility(View.GONE);
        if (SessionSave.getSession("Lang", StreetPickUpAct.this).equals("ar") || SessionSave.getSession("Lang", StreetPickUpAct.this).equals("fa"))
            pickupp.setBackgroundResource(R.drawable.search_only_pickup);
        else
            pickupp.setBackgroundResource(R.drawable.search_only_pickup);
//    intializeHome();
        lay_pick_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {

                dropVisible();

            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        try {


            startLocationUpdates();

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLastLocation == null && StreetPickUpAct.this != null) {
                        try {
                            errorInSplash(getString(R.string.error_in_getting_gps));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 30000);
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            //     System.out.println("________________" + latitude1);
            if (mLastLocation != null) {
                latitude1 = mLastLocation.getLatitude();
                longitude1 = mLastLocation.getLongitude();

                currentLatLng = new LatLng(latitude1, longitude1);
                final LatLng coordinate = new LatLng(latitude1, longitude1);
                if (map != null) {
                    // map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));
                    movetoCurrentloc();

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * Getting pixels from dp
     */
    public static int getPixelsFromDp(final Context context, final float dp) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onCameraChange(CameraPosition pos) {
        if (NetworkStatus.isOnline(StreetPickUpAct.this)) {
            //  Log.d("onCameraChange", "onCameraChange" + pos.target.latitude + "---");
            // System.gc();
            try {
                //  if (book_again_msg != null) {
//                if (book_again_msg) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            book_again_msg = false;
//                        }
//                    }, 5000);
//                } else {
                if (SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals("") && StreetPickUpAct.this != null) {
                    if (!(pos.target.latitude == 0.0)) {
                        P_latitude = pos.target.latitude;
                        P_longitude = pos.target.longitude;

                        handlerServercall1.removeCallbacks(callAddress);
                        if (!NOT_FIRST_TIME) {
                            handlerServercall1.postDelayed(callAddress, 1000);
                            NOT_FIRST_TIME = true;
                        } else {
                            handlerServercall1.postDelayed(callAddress, 2000);
                        }
                    }
                }
                //  }
//

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }

    /**
     * Starting location updates
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(StreetPickUpAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(StreetPickUpAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(StreetPickUpAct.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GPS);

            return;
        } else {
            getGPS();
        }

    }


    /**
     * Getting gps state
     */
    private void getGPS() {
        //  System.out.println("LocationSpeeeeedstarted");
        try {
            if (mGoogleApiClient != null && mLocationRequest != null && mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public void errorInSplash(String message) {
        try {
            if (true) {
                if (errorDialog != null && errorDialog.isShowing())
                    errorDialog.dismiss();
                //   System.out.println("setCanceledOnTouchOutside" + message);
                final View view = View.inflate(StreetPickUpAct.this, R.layout.netcon_lay, null);
                errorDialog = new Dialog(StreetPickUpAct.this, R.style.dialogwinddow);
                errorDialog.setContentView(view);
                errorDialog.setCancelable(false);
                errorDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(StreetPickUpAct.this, errorDialog.findViewById(R.id.alert_id));
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


                        Activity activity = (Activity) StreetPickUpAct.this;

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
                    errorDialog.dismiss();
//                    if (SplashAct.this != null) {
//                        Intent intent = new Intent(SplashAct.this, SplashAct.this.getClass());
//                        SplashAct.this.startActivity(intent);
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
     * convert Speed
     */
    private double convertSpeed(double speed) {
        return ((speed * 3600) * 0.001);
    }


    @Override
    public void onLocationChanged(final Location location) {
        //    System.out.println("HHHHHHHHHHHHHHHH_ll" + speed);
        mLastLocation = location;
        if (mapWrapperLayout != null && !mapWrapperLayout.isShown())
            mapWrapperLayout.setVisibility(View.VISIBLE);
        latitude1 = location.getLatitude();
        longitude1 = location.getLongitude();
        //    System.out.println("p_minspeed");
        mLastLocation = location;
        speed = roundDecimal(convertSpeed(location.getSpeed()), 2);

        if (!SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals("")) {
            if (!CommonData.DISTANCE_FARE.trim().equals(""))
                System.out.println("Timer started" + CommonData.DISTANCE_FARE + "......" + CommonData.travel_km);
            distance_fare.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + CommonData.DISTANCE_FARE);
            timer_started = true;
            fare_estimate.setText(String.valueOf(CommonData.travel_km) + " " + METRIC);
            FontHelper.applyFont(StreetPickUpAct.this, fare_estimate, "digital.ttf");
        }


//        if (!SessionSave.getSession("Metric", OngoingAct.this).equals("KM")) {
//            try {
//                speed = (roundTwoDecimals(speed / 1.60934));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        //System.out.println("vvv_________change" + p_travelstatus);
        //SessionSave.getSession("travel_status",OngoingAct.this).equals("2")

//        if (dropLatLng != null && p_travelstatus.trim().equals("2"))
//            if (!checkLocationInRoute()) {
//                // mov_cur_loc.setVisibility(View.GONE);
//                viaLatlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                mHandler.sendEmptyMessage(1);
//            }
        String metricss = "";
        if (SessionSave.getSession("Metric", StreetPickUpAct.this).equals("KM")) {
            metricss = " KM/hr";
        } else {
            metricss = " Miles/hr";
        }

//        if (MainActivity.mMyStatus.getOnstatus().equalsIgnoreCase("Complete"))
//            HeadTitle.setText(" " + NC.getResources().getString(R.string.ongoing_journey) + "\n" + speed + metricss);
        float _speed = location.getSpeed();
        //    System.out.println("_______nnnnn" + speed);
        bearing = location.getBearing();
        bearings = location.getBearing();
        zoom = map.getCameraPosition().zoom;

        //   System.out.println("Bearing:" + location.getBearing());
        if (bearing >= 0)
            bearing = bearing + 90;
        else
            bearing = bearing - 90;

        animate(location);
    }

    private void animate(Location location) {
        try {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // marker Animation Function
            if (!animLocation) {
                listPoint.add(latLng);
            } else {
                savedpoint.add(latLng);
            }
            //      System.out.println("listPoint size" + listPoint.size());
            if (listPoint.size() > 1) {

                if (a_marker != null) {
                    a_marker.setVisible(false);
                    a_marker.remove();
                }

                if (!animStarted) {
                    if (savedLatLng != null) {
                        listPoint.set(0, savedLatLng);
                        //         System.out.println("savedLatLng animation fst" + listPoint.get(0));
                    }
                    if (speed > 2) {
                        animStarted = true;
                        animLocation = true;
                        c_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(90).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
                        c_marker.setVisible(true);
                        //  System.out.println("bearing" + bearing);

                        CameraPosition camPos = CameraPosition
                                .builder(
                                        map.getCameraPosition() // current Camera
                                )
                                .bearing(bearings)
                                .build();

                        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(camPos, zoom));
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));

                        savedLatLng = listPoint.get(listPoint.size() - 1);
                        animateLine(listPoint, c_marker, bearings);
                    } else {
                        if (c_marker != null) {
                            c_marker.setVisible(false);
                            c_marker.remove();
                        }
                        //  System.out.println("GpsStatus.ischecked  out" + GpsStatus.ischecked);

                        if (GpsStatus.ischecked == 0) {
                            //   System.out.println("GpsStatus.ischecked " + GpsStatus.ischecked);
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


//    @Override
//    public void onLocationChanged(final Location location) {
//        System.out.println("LocationSpeeeeedchanging" + location);
//        if (location != null && StreetPickUpAct.this != null && map != null) {
//            mLastLocation = location;
//            latitude1 = location.getLatitude();
//            longitude1 = location.getLongitude();
//            speed = roundDecimal(convertSpeed(location.getSpeed()), 2);
//            System.out.println("LocationSpeeeeed" + speed);
//            currentLatLng = new LatLng(latitude1, longitude1);
//            if (!SessionSave.getSession("trip_id", StreetPickUpAct.this).equals("")) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        latitude1 = location.getLatitude();
//                        longitude1 = location.getLongitude();
//                        currentLatLng = new LatLng(latitude1, longitude1);
//
//                        final LatLng coordinate = new LatLng(latitude1, longitude1);
//                        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17f));
//                        // mapWrapperLayout.init(map, getPixelsFromDp(StreetPickUpAct.this, 39 + 20), true);
//
//
//                        bearing = location.getBearing();
//                        zoom = map.getCameraPosition().zoom;
//
//                        if (bearing >= 0)
//                            bearing = bearing + 90;
//                        else
//                            bearing = bearing - 90;
//
//                        try {
//                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                            // marker Animation Function
//                            if (!animLocation) {
//                                listPoint.add(latLng);
//                            } else {
//                                savedpoint.add(latLng);
//                            }
//                            System.out.println("listPoint size" + listPoint.size());
//                            if (listPoint.size() > 1) {
//
//                                if (a_marker != null) {
//                                    a_marker.setVisible(false);
//                                    a_marker.remove();
//                                }
//
//                                if (!animStarted) {
//                                    System.out.println("listPoindsfdst sizesavedLatLng animation fst" + listPoint.get(0));
//                                    if (savedLatLng != null) {
//                                        listPoint.set(0, savedLatLng);
//                                        System.out.println("listPoint sizesavedLatLng animation fst" + listPoint.get(0));
//                                    }
//                                    if (speed > 2) {
//                                        animStarted = true;
//                                        animLocation = true;
//                                        c_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(bearing).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
//                                        c_marker.setVisible(true);
//                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));
//                                        savedLatLng = listPoint.get(listPoint.size() - 1);
//                                        System.out.println("listPoint dfdsd sizesavedLatLng animation fst" + listPoint.get(0));
//                                        animateLine(listPoint, c_marker, bearing);
//                                    } else {
//                                        if (c_marker != null) {
//                                            c_marker.setVisible(false);
//                                            c_marker.remove();
//                                        }
//                                        System.out.println("GpsStatus.ischecked  out" + GpsStatus.ischecked);
//
//                                        if (GpsStatus.ischecked == 0) {
//                                            System.out.println("GpsStatus.ischecked " + GpsStatus.ischecked);
//                                            GpsStatus.ischecked = 1;
//                                            a_marker = map.addMarker(new MarkerOptions().position(latLng).rotation(bearing).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
//                                            a_marker.setVisible(true);
//                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//                                        } else {
//                                            a_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(bearing).anchor(0.5f, 0.5f).title("" + Address).icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_img)));
//                                            a_marker.setVisible(true);
//                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));
//                                        }
//                                    }
//                                }
//
//                            }
//
//                            bearing = 0;
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 300);
//
//
//            }
//        }
//    }


    /**
     * Animate the map marker
     */
    public void animateLine(ArrayList<LatLng> Trips, Marker marker, float bearings) {
        _trips.clear();
        _trips.addAll(Trips);
        _marker = marker;
        animteBearing = bearings;
        animateMarker();
    }

    /**
     * Animate the map marker
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
                //       System.out.println("Animation Cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //     System.out.println("Animation Repeat");
            }

            @Override
            public void onAnimationStart(Animator animation) {

                //      System.out.println("Status--> started " + _trips.size() + " Animation started ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listPoint.clear();
                //    System.out.println("Status--> ended " + _trips.size() + " Animation ended ");
                if (c_marker != null) {
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

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
     * Set fetching address
     */
    public static void setfetch_address() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = "";
        try {
            double lat = 0.0, lng = 0.0;
            try {
                if (StreetPickUpAct.this != null)
                    startLocationUpdates();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (data != null) {
                Bundle res = data.getExtras();
                result = res.getString("param_result");
                lat = res.getDouble("lat");
                lng = res.getDouble("lng");
                // Toast.makeText(StreetPickUpAct.this, String.valueOf(lat), Toast.LENGTH_SHORT).show();
                // currentlocTxt.setText(result);
            }
            if (LocationRequestedBy.equals("P") && result != null && !result.trim().equals("")) {
                currentlocTxt.setText(result);
                pickuplat = lat;
                pickuplng = lng;
                P_latitude = lat;
                P_longitude = lng;
                movetoCurrentloc();
            } else if (LocationRequestedBy.equals("D") && result != null && !result.trim().equals("")) {
                droplocEdt.setText(result);
                droplat = lat;
                droplng = lng;
                D_latitude = lat;
                D_longitude = lng;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * move the map camera view to current location of the user
     */
    public void movetoCurrentloc() {
        Location mLastLocation = null;
        if (mGoogleApiClient != null)
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if (map != null) {
                LatLng ll = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                if (ll != null && map != null) {

//                    map.setOnCameraChangeListener(StreetPickUpAct.this);
                    mapWrapperLayout.init(map, getPixelsFromDp(StreetPickUpAct.this, 39 + 20), true);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16f));
                }
            }
        } else {
            //     Toast.makeText(StreetPickUpAct.this, "nnnnn", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        map.getUiSettings().setZoomControlsEnabled(true);
//        map.setPadding(0,0,0,550);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals("")) {
                    updateStartTripUI();
                } else {
                    map.setOnCameraChangeListener(StreetPickUpAct.this);
                    movetoCurrentloc();
                }
            }
        }, 500);

    }

    @Override
    public void updateFare(String travelKm, String distanceFare, Location ll) {
        // System.out.println("updating fare__________________");
        if (ll != null)
            latlongfromService = ll;

        SessionSave.saveSession("street_fare", distanceFare, StreetPickUpAct.this);
        if (!SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals("")) {
            if (!distanceFare.trim().equals(""))
                distance_fare.setText(SessionSave.getSession("site_currency", StreetPickUpAct.this) + " " + CommonData.DISTANCE_FARE);


            waitingTime = WaitingTimerRun.sTimer;
            if (waitingTime.equals(""))
                waitingTime = "00:00:00";
//            String waitNoArabic = FontHelper.convertfromArabic(waitingTime);
//            System.out.println("Errror in okkkk" + waitNoArabic + "---" + waitingTime);
//            String[] split = waitNoArabic.split(":");
//            int hr = Integer.parseInt(split[0]);
//            float min = Integer.parseInt(split[1]);
//            float sec = Float.parseFloat(split[2]);
//            System.out.println("Hour:" + hr + "min:" + min + "sec:" + sec);
//            min = (Float) min / 60;
//            sec = sec / 3600;
//            float waitingHr = hr + min + sec;
//            MainActivity.mMyStatus.setDriverWaitingHr(Float.toString(waitingHr));
//            SessionSave.saveSession("waitingHr", Float.toString(waitingHr), StreetPickUpAct.this);
            //   waiting_time.setText(""+waitingTime);
            fare_estimate.setText(String.valueOf(CommonData.travel_km) + " " + METRIC);
            FontHelper.applyFont(StreetPickUpAct.this, fare_estimate, "digital.ttf");
        }
    }


    /**
     * Address parsing from given lat and long
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
                //  System.out.println("address size11ssss:" + latitude + "%$#" + longitude);

                if (Geocoder.isPresent()) {
                    //  System.out.println("address size:" + latitude + "%$#" + longitude);
                    addresses = geocoder.getFromLocation(latitude, longitude, 3);
                    //     System.out.println("address size:" + addresses.size());
                    if (addresses.size() == 0) {
                        new convertLatLngtoAddressApi(mContext, latitude, longitude);
                    } else {
                        for (int i = 0; i < addresses.size(); i++) {
                            Address += addresses.get(0).getAddressLine(i) + ", ";
                        }
                        //    System.out.println("____________aaa" + Address);
                        if (Address.length() > 0)
                            Address = Address.substring(0, Address.length() - 2);
                    }
                } else {
                    if (NetworkStatus.isOnline(mContext))
                        new convertLatLngtoAddressApi(mContext, latitude, longitude);
                    else {
                        StreetPickUpAct.setfetch_address();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (NetworkStatus.isOnline(mContext))
                    new convertLatLngtoAddressApi(mContext, latitude, longitude);
                else {
                    StreetPickUpAct.setfetch_address();
                }
            }
            return Address;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            closeDialog();
            // TaxiUtil.pAddress = "" + Address;
            if (Address.length() != 0 && StreetPickUpAct.this != null) {

                if (SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals("")) {
                    //  System.out.println("address size11:" + latitude + "%$#" + longitude + Address);
                    SessionSave.saveSession("drop_location", Address, StreetPickUpAct.this);

                    StreetPickUpAct.this.setPickuplocTxt(Address);
                    StreetPickUpAct.this.setPickuplat(latitude);
                    StreetPickUpAct.this.setPickuplng(longitude);
                } else {

                    if (cancelClicked) {
                        StreetPickUpAct.this.setDroplocTxt(Address);
                        callEndTrip();
                        cancelClicked = false;
                    } else
                        setDroplocTxt(Address);
                }
            } else {
                StreetPickUpAct.setfetch_address();

            }
            result = null;

        }


        /**
         * Convert lat and lon to address
         */
       /* public class convertLatLngtoAddressApi implements APIResult {
            public convertLatLngtoAddressApi(Context c, double lati, double longi) {

                if (GEOCODE_EXPIRY) {
                    // googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
                    String googleMapUrl = SessionSave.getSession("base_url", StreetPickUpAct.this) + "?type=google_geocoding";
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
                if (result != null)
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
                // cancelLoading();
                if (result != null)
                    setLocation(result.toString());
            }
        }


        /**
         * Set location and response parsing
         */
        public void setLocation(String inputJson) {

            try {

                JSONObject object = new JSONObject("" + inputJson);
                JSONArray array = object.getJSONArray("results");
                if (array.length() > 0) {
                    object = array.getJSONObject(0);
                    //   System.out.println("address size11:" + latitude + "%$#" + longitude + object.getString("formatted_address"));
                    //   SessionSave.saveSession("drop_location", object.getString("formatted_address"), StreetPickUpAct.this);
                    if (SessionSave.getSession("trip_id", StreetPickUpAct.this).trim().equals("")) {
                        StreetPickUpAct.this.setPickuplocTxt(object.getString("formatted_address"));
                        StreetPickUpAct.this.setPickuplocTxt(Address);
                        StreetPickUpAct.this.setPickuplat(latitude);
                        StreetPickUpAct.this.setPickuplng(longitude);
                    } else {

                        if (cancelClicked) {
                            callEndTrip();
                            cancelClicked = false;
                        } else
                            setDroplocTxt(Address);
                    }
                } else
                    Toast.makeText(mContext, NC.getString(R.string.c_tryagain), Toast.LENGTH_SHORT).show();

            } catch (Exception ex) {
                ex.printStackTrace();
                if (mContext != null)
                    if (!NetworkStatus.isOnline(mContext))
                        Toast.makeText(mContext, NC.getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, NC.getString(R.string.c_tryagain), Toast.LENGTH_SHORT).show();
                StreetPickUpAct.setfetch_address();
            }
        }

    }

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
                j.put("driver_id", SessionSave.getSession("Id", StreetPickUpAct.this));
                j.put("shiftstatus", checked);
                j.put("update_id", SessionSave.getSession("Shiftupdate_Id", StreetPickUpAct.this));
                j.put("reason", "");
                //  Log.e("shiftbefore ", j.toString());


                String requestingCheckBox = "type=driver_shift_status";
                if (isOnline())
                    new APIService_Retrofit_JSON(StreetPickUpAct.this, this, j, false).execute(requestingCheckBox);
                else {
                    btn_shift.setClickable(true);
                    alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                    if (checked.equals("IN")) {
                        btn_shift.setText(NC.getString(R.string.online));
                        // btn_shift.setBackgroundResource(R.drawable.shiftbg);
                        Drawables_program.shift_on(btn_shift);
                    } else {
//                        btn_shift.setText(NC.getString(R.string.offline));
//                        btn_shift.setBackgroundResource(R.drawable.shiftbggrey);
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

                if (isSuccess && StreetPickUpAct.this != null) {
                    btn_shift.setClickable(true);

                    JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {
                        if (checked.equals("IN")) {
                            alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                            btn_shift.setText(NC.getString(R.string.online));
                            Drawables_program.shift_on(btn_shift);
                            SessionSave.saveSession("shift_status", "IN", StreetPickUpAct.this);
                            SessionSave.saveSession(CommonData.SHIFT_OUT, false, StreetPickUpAct.this);
                            SessionSave.saveSession("Shiftupdate_Id", object.getJSONObject("detail").getString("update_id"), StreetPickUpAct.this);
                            //   Log.e("sess", SessionSave.getSession("shift_status", StreetPickUpAct.this));
                            if(SessionSave.getSession("shift_status", StreetPickUpAct.this).equals("IN"))
                            nonactiityobj.startServicefromNonActivity(StreetPickUpAct.this);
                        } else {
                            alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                            btn_shift.setText(NC.getString(R.string.offline));
                            Drawables_program.shift_bg_grey(btn_shift);
                            SessionSave.saveSession("shift_status", "OUT", StreetPickUpAct.this);
                            SessionSave.saveSession(CommonData.SHIFT_OUT, true, StreetPickUpAct.this);
                            //  Log.e("sess", SessionSave.getSession("shift_status", StreetPickUpAct.this));
                            nonactiityobj.stopServicefromNonActivity(StreetPickUpAct.this);
                        }
                    } else if (object.getInt("status") == -4) {
                        alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                        btn_shift.setText(NC.getString(R.string.online));
//                        Drawables_program.shift_on(btn_shift);
                    } else {
                        alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
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

                            Toast toast = Toast.makeText(StreetPickUpAct.this, getString(R.string.please_check_internet), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                    btn_shift.setClickable(true);
                    // alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
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
                //  System.out.println("thambiError" + ex.getLocalizedMessage());
                btn_shift.setClickable(true);
                alert_view(StreetPickUpAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.server_error), "" + NC.getResources().getString(R.string.ok), "");
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


    /**
     * General alert message
     */
    public Dialog alertDialog;

    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        if (alertDialog != null)
            if (alertDialog.isShowing())
                alertDialog.dismiss();
        final View view = View.inflate(mContext, R.layout.alert_view, null);
        alertDialog = new Dialog(mContext, R.style.NewDialog);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        FontHelper.applyFont(mContext, alertDialog.findViewById(R.id.alert_id));
        alertDialog.show();
        final TextView title_text = (TextView) alertDialog.findViewById(R.id.title_text);
        final TextView message_text = (TextView) alertDialog.findViewById(R.id.message_text);
        final Button button_success = (Button) alertDialog.findViewById(R.id.button_success);
        final Button button_failure = (Button) alertDialog.findViewById(R.id.button_failure);
        button_failure.setVisibility(View.GONE);
        title_text.setText(title);
        message_text.setText(message);
        button_success.setText(success_txt);
        button_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
            }
        });
    }


    /**
     * Checking network connectivity
     */
    public boolean isOnline() {

        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        try {
            if (latlongfromService != null && !SessionSave.getSession("trip_id", StreetPickUpAct.this).equals("")) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlongfromService.getLatitude(), latlongfromService.getLongitude()), 16f));
                animate(latlongfromService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//            }
//        }, 500);

        //   LocationUpdate.registerInterface(StreetPickUpAct.this);
        if (SessionSave.getSession("shift_status", StreetPickUpAct.this).equals("IN")) {

            //   btn_shift.setBackgroundResource(R.drawable.shiftbg);
            btn_shift.setText(NC.getString(R.string.online));
            Drawables_program.shift_on(btn_shift);
            nonactiityobj.startServicefromNonActivity(StreetPickUpAct.this);

        } else {

            btn_shift.setBackgroundResource(R.drawable.shiftbggrey);
            btn_shift.setText(NC.getString(R.string.offline));
            Drawables_program.shift_bg_grey(btn_shift);
            nonactiityobj.stopServicefromNonActivity(StreetPickUpAct.this);
        }

        // if (!SessionSave.getSession("trip_id", StreetPickUpAct.this).equals("")) {
        startLocationUpdates();
        //}
    }

    @Override
    public void onBackPressed() {

        // super.onBackPressed();

        Intent intent = new Intent(StreetPickUpAct.this, MyStatus.class);
        startActivity(intent);
        finish();
    }

}
