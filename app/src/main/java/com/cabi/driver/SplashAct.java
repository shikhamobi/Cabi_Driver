package com.cabi.driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.data.apiData.ApiRequestData;
import com.cabi.driver.data.apiData.CompanyDomainResponse;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.location.LocationRequestHelper;
import com.cabi.driver.location.LocationUpdatesBroadcastReceiver;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.APIService_Retrofit_JSON_NoProgress;
import com.cabi.driver.service.AuthService;
import com.cabi.driver.service.CoreClient;
import com.cabi.driver.service.ServiceGenerator;
import com.cabi.driver.utils.CL;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.LocationDb;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class will be called initially on app launch.Here we will load all basic attributes from back end to run app further
 */
public class SplashAct extends MainActivity implements android.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int MY_PERMISSIONS_REQUEST_GPS = 111;
    public static String CURRENT_COUNTRY_ISO_CODE = "";
    public static String CURRENT_COUNTRY_CODE = BuildConfig.CURRENT_COUNTRY_CODE;
    private static boolean CORE_CALLED = false;
    public static String REG_ID = "";
    private String mDeviceid;
    Button langEn, langRu;
    private LocationManager mLocationManager;
    private boolean isGPSEnabled;
    public static Dialog mProgressdialog;
    LocationDb objLocationDb;
    private TextView buildnoTxt;
    private String VersionNo = "";
    private static Dialog mverDialog;
    private Context context;
    private GoogleCloudMessaging gcm;
    private String SENDER_ID = "731540038622";
    private int id = 0;
    private Dialog mDialog;
    VideoView vv;
    public boolean askDomain = false;
    public static boolean NO_NEED_TO_PLAY = true;
    private int types = 1;
    private Dialog alertmDialog;
    private long getCore_Utc;
    private String getCoreLangTime;
    private String getCoreColorTime;
    private boolean companyDomainError;
    private Dialog errorDialog;


    private static final String TAG = SplashAct.class.getSimpleName();
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    // FIXME: 5/16/17
    private static final long UPDATE_INTERVAL = 1 * 1000;
    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value, but they may be less frequent.
     */
    // FIXME: 5/14/17
    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    /**
     * The max time before batched results are delivered by location services. Results may be
     * delivered sooner than this interval.
     */
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;
    /**
     * The entry point to Google Play Services.
     */
    private GoogleApiClient mGoogleApiClient;

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        int curVersion = 0;
        try {
            PackageManager manager = getPackageManager();
            try {
                curVersion = manager.getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (curVersion != 0 && SessionSave.getSession("trip_id", SplashAct.this).equals(""))
                if (SessionSave.getSession(String.valueOf(curVersion), this).trim().equals("")) {

                    SessionSave.saveSession("base_url", "", SplashAct.this);
                    SessionSave.saveSession("api_key", "", SplashAct.this);
                    SessionSave.saveSession("encode", "", SplashAct.this);
                    SessionSave.saveSession("image_path", "", SplashAct.this);
                    SessionSave.saveSession(String.valueOf(curVersion), "No", SplashAct.this);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  gcmRegister();

        return R.layout.splash_lay;
    }

    @SuppressLint("NewApi")
    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        // turnGPSOn(SplashAct.this);
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.cabi.driver", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        try {
            CommonData.closeDialog(mverDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }


    /**
     * This method is used to register Gcm for notification
     */
    public String gcmRegister() {
        try {
            context = getApplicationContext();
            gcm = GoogleCloudMessaging.getInstance(this);
            if (TextUtils.isEmpty(SENDER_ID)) {
                // Toast.makeText(getApplicationContext(), "GCM Sender ID not set.",
                // Toast.LENGTH_LONG).show();
                return null;
            }
            if (TextUtils.isEmpty(REG_ID)) {
                //registerInBackground();
            }
            return REG_ID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return REG_ID;
    }


    /**
     * Getting GCM Registration ID
     */
    private void registerInBackground() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    REG_ID = gcm.register(SENDER_ID);

                    //  Log.d("RegisterActivity", "registerInBackground - regId: "
                    //         + REG_ID);
                    msg = "Device registered, registration ID=" + REG_ID;

                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    msg = "Error :" + ex.getMessage();
                    //      Log.d("RegisterActivity", "Error: " + msg);
                }
                //   Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    if (REG_ID != null)
                        if (REG_ID.length() != 0) {
                            SessionSave.saveSession("device_token", "" + REG_ID,
                                    SplashAct.this);
                        }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }.execute(null, null, null);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * This method is used to load all basic initialisation
     */
    public void init() {
        FontHelper.applyFont(this, findViewById(R.id.rootlay));
        objLocationDb = new LocationDb(SplashAct.this);
        buildnoTxt = (TextView) findViewById(R.id.buildnoTxt);
        mDeviceid = Secure.getString(SplashAct.this.getContentResolver(), Secure.ANDROID_ID);
        CommonData.current_act = "SplashAct";
        langEn = (Button) findViewById(R.id.english);
        langRu = (Button) findViewById(R.id.russian);
        CommonData.mDevice_id = mDeviceid;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        if (SessionSave.getSession("Lang", SplashAct.this).equals("")) {
            SessionSave.saveSession("Lang", "en", SplashAct.this);
            SessionSave.saveSession("Lang_Country", "en_GB", SplashAct.this);
        }


        try {
            VersionNo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            buildnoTxt.setText(NC.getResources().getString(R.string.app_buildversion) + ":" + VersionNo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private boolean VersionCheck() {
        try {
            System.out.println("_callingrrrrgeeeAPP_VERSION" + APP_VERSION);
            if (APP_VERSION != null) {
                System.out.println("_callingrrrrgeeeAPP_VERSION" + APP_VERSION);
                String newVersion = SessionSave.getSession("play_store_version", SplashAct.this).equals("") ? "0" :
                        SessionSave.getSession("play_store_version", SplashAct.this);
                int curVersion = 0;
                PackageManager manager = getPackageManager();
                try {
                    curVersion = manager.getPackageInfo(getPackageName(), 0).versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                System.err.println("New version" + newVersion + "curVersion" + curVersion);
                if (curVersion < value(newVersion))
                    return true;
                else
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * check application version with play store
     */
    @SuppressLint("NewApi")
//    private boolean VersionCheck() {
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            String curVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
//            Package pack = SplashAct.this.getClass().getPackage();
//            String packtxt = pack.toString();
//            String packarr[] = packtxt.split(" ");
//            String newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en").timeout(30000).userAgent("Mozilla/" +
//                    ".0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").get().select("div[itemprop=softwareVersion]").first().ownText();
//            System.err.println("New version" + newVersion + "curVersion" + curVersion);
//            if (value(curVersion) < value(newVersion))
//                return true;
//            else
//                return false;
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * Return long value for given string
     */
    private long value(String string) {
        string = string.trim();
        if (string.contains(".")) {
            final int index = string.lastIndexOf(".");
            return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
        } else {
            return Long.valueOf(string);
        }
    }

    private Date stringToDate(String aDate, String aFormat) {

        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    @Override
    protected void onResume() {
        super.onResume();


        String token = FirebaseInstanceId.getInstance().getToken();
//        System.out.println("resume1" + token);
        if (false) {
            gpsalert(SplashAct.this, false);
//            System.out.println("resumegps");
        } else if (NetworkStatus.isOnline(SplashAct.this)) {
//            System.out.println("resumeonline");
            try {

//                System.out.println("colooooooooo" + SessionSave.getSession("image_path", SplashAct.this) + "signInLogo.png");
                //       Glide.with(SplashAct.this).load(SessionSave.getSession("image_path", SplashAct.this) + "signInLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.body_iv));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!NO_NEED_TO_PLAY) {
                vv = (VideoView) findViewById(R.id.videoView);


                //Playing Splash Screen video
                Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
                vv.setVideoURI(path);
                vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (!CORE_CALLED && !companyDomainError)
                            showLoading(SplashAct.this);
                    }
                });


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            init();
                            if (ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                ActivityCompat.requestPermissions(SplashAct.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_GPS);

                                return;
                            } else {
                                //       System.out.println("____________+");
                                getGPS();
                            }
                            buildGoogleApiClient();
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }, 100);


//        try {
//            init();
//
//            if (ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//
//
//                ActivityCompat.requestPermissions(SplashAct.this,
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_GPS);
//
//                return;
//            } else {
//                System.out.println("____________+");
//                getGPS();
//            }
//
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
            } else {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            init();
                            if (ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                ActivityCompat.requestPermissions(SplashAct.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_GPS);
                                return;
                            } else {
                                //        System.out.println("____________+");
                                getGPS();
                            }
                            buildGoogleApiClient();
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }, 100);
            }
        } else {
            //    System.out.println("resumemnotconn");
            isConnect(SplashAct.this, false);
        }
    }


    public void isConnect(final Context mContext, final boolean isconnect) {

        try {

            if (!isconnect) {
                if (sDialog != null)
                    sDialog.dismiss();
                final View view = View.inflate(mContext, R.layout.netcon_lay, null);
                sDialog = new Dialog(mContext, R.style.dialogwinddow);

                try {
                    Colorchange.ChangeColor((ViewGroup) view, mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sDialog.setContentView(view);
                sDialog.setCancelable(true);
                FontHelper.applyFont(mContext, sDialog.findViewById(R.id.alert_id));
                if (!((Activity) mContext).isFinishing()) {
                    sDialog.show();
                }
                final TextView title_text = (TextView) sDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) sDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) sDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) sDialog.findViewById(R.id.button_failure);
                title_text.setText("" + NC.getResources().getString(R.string.message));
                message_text.setText("" + NC.getResources().getString(R.string.check_net_connection));
                button_success.setText("" + NC.getResources().getString(R.string.c_tryagain));
                button_failure.setText("" + NC.getResources().getString(R.string.cancell));
                button_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub
                        if (NetworkStatus.isOnline(mContext)) {


                            Intent intent = new Intent(SplashAct.this, SplashAct.class);
                            Activity activity = (Activity) mContext;
                            sDialog.dismiss();
                            SplashAct.this.finish();
                            SplashAct.this.startActivity(intent);
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
                    sDialog.dismiss();
                    if (mContext != null) {
                        Intent intent = new Intent(mContext, mContext.getClass());
                        mContext.startActivity(intent);
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


    /**
     * Getting gps and all basic urls
     */
    public void getGPS() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
        isGPSEnabled = true;
        if (isGPSEnabled) {

            //  gps_alert();
            if (isOnline()) {
                if (!(VersionCheck())) {
                    callApi();
                } else
                    versionAlert(SplashAct.this);
            } else {
                ShowToast(SplashAct.this, "" + NC.getResources().getString(R.string.check_net_connection));
            }
        } else {
            gpsalert(SplashAct.this, false);
        }
    }

    void callApi() {
        //     System.out.println("____" + id++);

        if (SessionSave.getSession("base_url", SplashAct.this).trim().equals(""))
            if (askDomain)
                getUrl();
            else
                urlApi("");
        else {
            setLocale();
            getAndStoreStringValues(SessionSave.getSession("wholekey", SplashAct.this));
            getAndStoreColorValues(SessionSave.getSession("wholekeyColor", SplashAct.this));
            if (!SessionSave.getSession("base_url", SplashAct.this).trim().equals("")) {
                ServiceGenerator.API_BASE_URL = SessionSave.getSession("base_url", SplashAct.this);

                if (!NO_NEED_TO_PLAY) {

                    vv.start();
                    vv.setVisibility(View.VISIBLE);
                    NO_NEED_TO_PLAY = !NO_NEED_TO_PLAY;
                } else
                    showLoading(SplashAct.this);
                new GetAuthKey();
            } else {
                if (askDomain)
                    getUrl();
                else
                    urlApi("");

            }
        }
    }

    /**
     * Setting Language Configuration
     */
    public void setLocale() {
        if (SessionSave.getSession("Lang", SplashAct.this).equals("")) {
            SessionSave.saveSession("Lang", "en", SplashAct.this);
            SessionSave.saveSession("Lang_Country", "en_GB", SplashAct.this);
        }


        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", SplashAct.this);
        String arry[] = langcountry.split("_");
        config.locale = new Locale(SessionSave.getSession("Lang", SplashAct.this), arry[1]);
        Locale.setDefault(new Locale(SessionSave.getSession("Lang", SplashAct.this), arry[1]));
        SplashAct.this.getBaseContext().getResources().updateConfiguration(config, SplashAct.this.getResources().getDisplayMetrics());

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        final String text = "Connection suspended";
        Log.w(TAG, text + ": Error code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        final String text = "Exception while connecting to Google Play services";
        Log.w(TAG, text + ": " + connectionResult.getErrorMessage());
    }


    /**
     * Getting authkey API and response parsing<br><br>
     * url-->get_authentication<br>
     * <b>Input jsonParams:</b><br>
     * 1.mobilehost-->website domain <br>
     * 2.encode -->previous authentication key<br>
     * <p>
     * <p>
     * <b>Result:</b><br>
     * status:1  -->success *encode params saved<br>
     * status:-1 -->failure *display error message <br>
     */
    private class GetAuthKey implements APIResult {

        public GetAuthKey() {
            // TODO Auto-generated constructor stub
            String url = "type=get_authentication";
            JSONObject j = new JSONObject();
            try {
                URL urls = null;
                String host = "";
                try {
                    urls = new URL(SessionSave.getSession("base_url", SplashAct.this));
                    host = urls.getHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //    System.out.println("domianurl_________" + host + "__" + SessionSave.getSession("encode", SplashAct.this));
                j.put("mobilehost", host);
                j.put("encode", SessionSave.getSession("encode", SplashAct.this));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            new APIService_Retrofit_JSON(SplashAct.this, this, j, false).execute(url);
        }


        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();
                        SessionSave.saveSession("auth_last_call_type", String.valueOf(ts), SplashAct.this);
                        try {
                            System.out.println("authkey:" + json.getString("encode") + "____" + result.trim().replaceAll("\\s", ""));
//                            if(!json.getString("encode").trim().equals(""))
//                            SessionSave.saveSession("api_key",json.getString("encode"), SplashAct.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (SessionSave.getSession("wholekey", SplashAct.this).equals(""))
                            new callString("");
                        else {
                            String url = "type=getcoreconfig";
                            new CoreConfigCall(url);
                        }
                    } else {
                        errorInSplash(json.getString("message"));
                        Toast.makeText(SplashAct.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                        cancelLoading();
                    }
                } else {

                    cancelLoading();
                    errorInSplash(getString(R.string.server_error));
//
                }
            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                errorInSplash(getString(R.string.server_error));
                e.printStackTrace();
            }
        }
    }


    public void errorInSplash(String message) {
        try {
            if (true) {
                if (errorDialog != null && errorDialog.isShowing())
                    errorDialog.dismiss();
//                System.out.println("setCanceledOnTouchOutside" + message);
                final View view = View.inflate(SplashAct.this, R.layout.netcon_lay, null);
                errorDialog = new Dialog(SplashAct.this, R.style.dialogwinddow);
                errorDialog.setContentView(view);
                errorDialog.setCancelable(false);
                errorDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(SplashAct.this, errorDialog.findViewById(R.id.alert_id));
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


                        Activity activity = (Activity) SplashAct.this;

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
     * Getting Language Files from Server
     */
    private class callString implements APIResult {
        public callString(final String url) {
            // TODO Auto-generated constructor stub

            String urls = SessionSave.getSession("currentStringUrl", SplashAct.this);
            if (urls.equals("")) {
                urls = SessionSave.getSession(SessionSave.getSession("LANGDef", SplashAct.this), SplashAct.this);
                if (SessionSave.getSession("LANGTempDef", SplashAct.this).trim().equalsIgnoreCase("RTL")) {
                    SessionSave.saveSession("Lang_Country", "ar_EG", SplashAct.this);
                    SessionSave.saveSession("Lang", "ar", SplashAct.this);
                    Configuration config = new Configuration();
                    String langcountry = SessionSave.getSession("Lang_Country", SplashAct.this);
                    String arry[] = langcountry.split("_");
                    config.locale = new Locale(SessionSave.getSession("Lang", SplashAct.this), arry[1]);
                    Locale.setDefault(new Locale(SessionSave.getSession("Lang", SplashAct.this), arry[1]));
                }
            }
            new APIService_Retrofit_JSON_NoProgress(SplashAct.this, this, null, true, urls, true).execute();
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {

                setLocale();
                getAndStoreStringValues(result);
                SessionSave.saveSession("wholekey", result, SplashAct.this);

                if (SessionSave.getSession("wholekeyColor", SplashAct.this).trim().equals("") || !SessionSave.getSession(CommonData.PASSENGER_COLOR_TIME, SplashAct.this).equals(getCoreColorTime))
                    new callColor("");
                else {
                    Intent i = null;
                    if (CommonData.isCurrentTimeZone(getCore_Utc)) {
                        if (SessionSave.getSession("Id", SplashAct.this).equals("")) {
                            i = new Intent(SplashAct.this, UserLoginAct.class);
                            startActivity(i);
                            finish();
                        } else {
                            if (SessionSave.getSession("trip_id", SplashAct.this).equals("")) {
                                i = new Intent(SplashAct.this, MyStatus.class);
                                startActivity(i);
                                finish();
                            } else {
                                if (SessionSave.getSession("travel_status", SplashAct.this).equals("5")) {
                                    i = new Intent(SplashAct.this, TripHistoryAct.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    i = new Intent(SplashAct.this, OngoingAct.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    } else {
                        cancelLoading();
                        CORE_CALLED = true;
                        alert_view_date(SplashAct.this, NC.getString(R.string.message), NC.getString(R.string.date_change), NC.getString(R.string.ok), NC.getString(R.string.cancel));
                    }
                    if (!SessionSave.getSession("base_url", SplashAct.this).trim().equals("")) {
                        ServiceGenerator.API_BASE_URL = SessionSave.getSession("base_url", SplashAct.this);
                        new callColor("");

                    }

                }

            } else {
                errorInSplash(getString(R.string.error_in_string));
            }
        }
    }

    /**
     * Getting Subdomain
     */
    private void getUrl() {
        final View view = View.inflate(SplashAct.this, R.layout.forgot_popup, null);
        if (mDialog != null && mDialog.isShowing())
            mDialog.cancel();
        mDialog = new Dialog(SplashAct.this, R.style.NewDialog);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        FontHelper.applyFont(SplashAct.this, mDialog.findViewById(R.id.inner_content));
        mDialog.setCancelable(true);
        mDialog.show();
        final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
        mail.setHint(NC.getResources().getString(R.string.domain_hint));
        final Button OK = (Button) mDialog.findViewById(R.id.okbtn);
        final Button Cancel = (Button) mDialog.findViewById(R.id.cancelbtn);
        Cancel.setVisibility(View.GONE);
        int maxLength = 64;
        setEditTextMaxLength(maxLength, mail);

        OK.setOnClickListener(new OnClickListener() {
            private String Email;

            @Override
            public void onClick(final View v) {

                try {
                    Email = mail.getText().toString();
                    if (Email.length() > 2) {
                        urlApi(Email);
                        mail.setText("");
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(SplashAct.this, "Please enter valid access key", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });

    }

    public void setEditTextMaxLength(int length, EditText edt_text) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        edt_text.setFilters(FilterArray);
    }

    /**
     * Getting base path
     */
    private void urlApi(String keyy) {
        String url = "";
        try {

            // url= "http://192.168.1.114:7090/driverapi112/index/";  /*local priyanka*/

         //   url = "http://cabi.know3.com/driverapi112/index/";/* demo*/

            url = "http://ksa.cabi.me/driverapi113/index/";/* live client*/


            url = "http://162.243.20.44/driverapi113/index/";/* development server*/

            //   url = "http://cabi.know3.com/mobileapi118/index/";/* kumares*/

//            url = "http://192.168.1.175:1088/driverapi112/index/"; /*devirani*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        CoreClient client = new ServiceGenerator(url, SplashAct.this, false).createService(CoreClient.class);
        ApiRequestData.BaseUrl request = new ApiRequestData.BaseUrl();
       // request.company_domain = "tagmytaxi";
        request.company_domain = "cabi";          /* development server*/
        //request.company_main_domain = "cabi.know3.com";
        //request.company_main_domain = "ksa.cabi.me";
        request.company_main_domain = "162.243.20.44";
        request.device_type = "1";
        if (!NO_NEED_TO_PLAY) {
            vv.start();
            vv.setVisibility(View.VISIBLE);

            NO_NEED_TO_PLAY = !NO_NEED_TO_PLAY;
        } else
            showLoading(SplashAct.this);
        Call<CompanyDomainResponse> response = client.callData(ServiceGenerator.COMPANY_KEY, request);
        response.enqueue(new Callback<CompanyDomainResponse>() {
            @Override
            public void onResponse(Call<CompanyDomainResponse> call, Response<CompanyDomainResponse> response) {
                CompanyDomainResponse cr = response.body();
                cancelLoading();
                if (cr != null)
                    if (cr.status.trim().equals("1")) {
                        SessionSave.saveSession("base_url", cr.baseurl, SplashAct.this);
                        SessionSave.saveSession("api_key", cr.apikey, SplashAct.this);
                        SessionSave.saveSession("encode", cr.encode, SplashAct.this);
                        //     System.out.println("_______" + cr.baseurl + "*********" + cr.apikey);
                        ServiceGenerator.API_BASE_URL = cr.baseurl;
                        SessionSave.saveSession("image_path", cr.androidPaths.static_image, SplashAct.this);
                        // Log.e("language_size ", String.valueOf(cr.androidPaths.driver_language.size()));
                        String totalLanguage = "";
                        //       System.out.println("LLLLLLLL" + SessionSave.getSession("lang_json", SplashAct.this));
                        for (int i = 0; i < cr.androidPaths.driver_language.size(); i++) {
                            String key_ = "";
                            totalLanguage += (cr.androidPaths.driver_language.get(i).language).replaceAll(".xml", "") + "____";
                            SessionSave.saveSession("LANG" + String.valueOf(i), cr.androidPaths.driver_language.get(i).language, SplashAct.this);
                            SessionSave.saveSession("LANGTemp" + String.valueOf(i), cr.androidPaths.driver_language.get(i).design_type, SplashAct.this);

                            SessionSave.saveSession("LANGCode" + String.valueOf(i), cr.androidPaths.driver_language.get(i).language_code, SplashAct.this);

                            SessionSave.saveSession(cr.androidPaths.driver_language.get(i).language, cr.androidPaths.driver_language.get(i).url, SplashAct.this);
                            if (cr.androidPaths.driver_language.get(i).language.contains("English")) {
                                SessionSave.saveSession("LANGTempDef", cr.androidPaths.driver_language.get(i).design_type, SplashAct.this);
                                SessionSave.saveSession("LANGDef", cr.androidPaths.driver_language.get(i).language, SplashAct.this);
                            }
                        }
                        if (SessionSave.getSession("LANGDef", SplashAct.this).trim().equals(""))
                            SessionSave.saveSession("LANGDef", SessionSave.getSession("LANG0", SplashAct.this), SplashAct.this);
                        if (SessionSave.getSession("LANGTempDef", SplashAct.this).trim().equals(""))
                            SessionSave.saveSession("LANGTempDef", SessionSave.getSession("LANGTemp0", SplashAct.this), SplashAct.this);
                        SessionSave.saveSession("lang_json", totalLanguage, SplashAct.this);
                        if (SessionSave.getSession("Lang", SplashAct.this).equals(""))
                            SessionSave.saveSession("Lang", cr.androidPaths.driver_language.get(0).language_code.replaceAll(".xml", ""), SplashAct.this);
                        SessionSave.saveSession("colorcode", cr.androidPaths.colorcode, SplashAct.this);
                        if (mDialog != null)
                            mDialog.dismiss();
                        String url = "type=getcoreconfig";
                        new CoreConfigCall(url);

                    } else {
                        companyDomainError = true;
                        alert_view_company(SplashAct.this, getString(R.string.message), cr.message, getString(R.string.ok), getString(R.string.cancel));
                    }
                else {
                    companyDomainError = true;
                    Toast.makeText(SplashAct.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyDomainResponse> call, Throwable t) {
                cancelLoading();
                companyDomainError = true;
                Toast.makeText(SplashAct.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Adding string files to Local hashmap
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
                    NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());
                }
            }
            //   System.out.println("sizelllll" + NC.fields_id.size());
            getValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Getting String values from local
     */
    synchronized void getValueDetail() {
        Field[] fieldss = R.string.class.getDeclaredFields();
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "string", getPackageName());
            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
                NC.fields.add(fieldss[i].getName());
                NC.fields_value.add(getResources().getString(id));
                NC.fields_id.put(fieldss[i].getName(), id);

            } else {
            }
        }


        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            NC.nfields_byID.put(NC.fields_id.get(h), NC.nfields_byName.get(h));
            // do stuff
        }

    }


    /**
     * Getting Color values from local hash map
     */
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
        }

        // System.out.println("NagarrrBye"+CL.fields.size()+"___"+CL.fields_value.size()+"___"+ CL.fields_id.size());
    }


    /**
     * Getting Color Files from Server and response parsing
     */
    private class callColor implements APIResult {
        public callColor(final String url) {

            // SessionSave.saveSession("colorcode", cr.androidPaths.colorcode, SplashAct.this);
            // TODO Auto-generated constructor stub


            new APIService_Retrofit_JSON_NoProgress(SplashAct.this, this, null, true, SessionSave.getSession("colorcode", SplashAct.this).replace("DriverAppColor", "driverAppColors"), true).execute();


        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                // System.out.println("nagaaaa"+result);
                getAndStoreColorValues(result);
                SessionSave.saveSession("wholekeyColor", result, SplashAct.this);


                Intent i = null;

                if (CommonData.isCurrentTimeZone(getCore_Utc)) {
                    if (SessionSave.getSession("Id", SplashAct.this).equals("")) {
                        i = new Intent(SplashAct.this, UserLoginAct.class);
                        startActivity(i);
                        finish();
                    } else {
                        if (SessionSave.getSession("trip_id", SplashAct.this).equals("")) {
                            i = new Intent(SplashAct.this, MyStatus.class);
                            startActivity(i);
                            finish();
                        } else {
                            i = new Intent(SplashAct.this, OngoingAct.class);
                            startActivity(i);
                            finish();
                        }
                    }
                } else {
                    cancelLoading();
                    CORE_CALLED = true;
                    alert_view_date(SplashAct.this, NC.getString(R.string.message), NC.getString(R.string.date_change), NC.getString(R.string.ok), NC.getString(R.string.cancel));
                }


            } else {
                errorInSplash(getString(R.string.error_in_color));
            }

        }
    }


    /**
     * Adding color files to Local hashmap
     */
    private synchronized void getAndStoreColorValues(String result) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

            // System.out.println("lislength" + nList.getLength());
            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

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
     * CoreConfig method API call and response parsing.
     */
    public class CoreConfigCall implements APIResult {
        public CoreConfigCall(final String url) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON_NoProgress(SplashAct.this, this, "", true).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            //   System.out.println(result);
            if (isSuccess) {

                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        startService(new Intent(SplashAct.this, AuthService.class));
                        JSONArray jArry = json.getJSONArray("detail");
                        if (json.has("mobile_socket_http_url")) {
                            SessionSave.saveSession(CommonData.NODE_URL,"http://"+ json.getString("mobile_socket_http_url")+"/", SplashAct.this);
                        }
                        if (json.has("mobile_socket_http_domain")) {
                            SessionSave.saveSession(CommonData.NODE_DOMAIN, json.getString("mobile_socket_http_domain"), SplashAct.this);
                        }
                        SessionSave.saveSession("api_base", jArry.getJSONObject(0).getString("api_base"), SplashAct.this);
                        SessionSave.saveSession("isFourSquare", jArry.getJSONObject(0).getString("android_foursquare_status"), SplashAct.this);
                        SessionSave.saveSession("android_foursquare_api_key", jArry.getJSONObject(0).getString("android_foursquare_api_key"), SplashAct.this);
                        SessionSave.saveSession("facebook_key", "164776140970697", SplashAct.this);
                        SessionSave.saveSession("play_store_version", jArry.getJSONObject(0).getString("android_driver_version"), SplashAct.this);
                        //      System.out.println("***************" + jArry.getJSONObject(0).getString("country_iso_code"));
                        CURRENT_COUNTRY_ISO_CODE = jArry.getJSONObject(0).getString("country_iso_code");
                        SessionSave.saveSession("android_web_key", jArry.getJSONObject(0).getString("android_google_api_key"), SplashAct.this);
                        int length = jArry.length();
                        for (int i = 0; i < length; i++) {
                            SessionSave.saveSession("noimage_base", jArry.getJSONObject(i).getString("noimage_base"), getApplicationContext());
                            SessionSave.saveSession("site_currency", jArry.getJSONObject(i).getString("site_currency") + " ", getApplicationContext());
                            SessionSave.saveSession("invite_txt", jArry.getJSONObject(i).getString("aboutpage_description"), getApplicationContext());
                            SessionSave.saveSession("referal", jArry.getJSONObject(i).getString("driver_referral_settings"), getApplicationContext());
                            SessionSave.saveSession("Metric", jArry.getJSONObject(i).getString("metric"), SplashAct.this);
                            CommonData.METRIC = jArry.getJSONObject(i).getString("metric");
                            //	SessionSave.saveSession("android_google_api_key", jArry.getJSONObject(i).getString("android_google_api_key"), SplashAct.this);
                            //android_google_api_key
                        }


                        try {
                            getCoreLangTime = json.getJSONObject("language_color_status").getString("android_driver_language");
                            getCoreColorTime = json.getJSONObject("language_color_status").getString("android_driver_colorcode");
                            getCore_Utc = jArry.getJSONObject(0).getLong("utc_time");


                            boolean deflanAvail = false;
                            String totalLanguage = "";
                            JSONArray pArray = json.getJSONObject("language_color").getJSONObject("android").getJSONArray("driver_language");
                            for (int i = 0; i < pArray.length(); i++) {
//
                                String key_ = "";

                                totalLanguage += pArray.getJSONObject(i).getString("language").replaceAll(".xml", "") + "____";

                                SessionSave.saveSession("LANG" + String.valueOf(i), pArray.getJSONObject(i).getString("language"), SplashAct.this);
                                SessionSave.saveSession("LANGTemp" + String.valueOf(i), pArray.getJSONObject(i).getString("design_type"), SplashAct.this);
                                SessionSave.saveSession("LANGCode" + String.valueOf(i), pArray.getJSONObject(i).getString("language_code"), SplashAct.this);
                                SessionSave.saveSession(pArray.getJSONObject(i).getString("language"), pArray.getJSONObject(i).getString("url"), SplashAct.this);
                                if (!SessionSave.getSession("LANGDef", SplashAct.this).equals("") && pArray.getJSONObject(i).getString("language").contains(SessionSave.getSession("LANGDef", SplashAct.this))) {
                                    deflanAvail = true;
                                }

                            }
                            //     System.out.println("___________defff" + deflanAvail);
                            if (SessionSave.getSession("LANGDef", SplashAct.this).trim().equals("") || !deflanAvail) {
                                SessionSave.saveSession("LANGDef", SessionSave.getSession("LANG0", SplashAct.this), SplashAct.this);
//                            if (SessionSave.getSession("LANGTempDef", SplashAct.this).trim().equals(""))

                                SessionSave.saveSession("LANGTempDef", SessionSave.getSession("LANGTemp0", SplashAct.this), SplashAct.this);
                                SessionSave.saveSession("Lang", pArray.getJSONObject(0).getString("language_code").replaceAll(".xml", ""), SplashAct.this);
                                String url = SessionSave.getSession(SessionSave.getSession("LANG" + String.valueOf(0), SplashAct.this), SplashAct.this);
                                SessionSave.saveSession("currentStringUrl", url, SplashAct.this);
                            }

                            SessionSave.saveSession("lang_json", totalLanguage, SplashAct.this);

                            SessionSave.saveSession("colorcode", json.getJSONObject("language_color").getJSONObject("android").getString("driverColorCode"), SplashAct.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent i = null;

                        if (!SessionSave.getSession(CommonData.PASSENGER_LANGUAGE_TIME, SplashAct.this).trim().equals(getCoreLangTime)) {
                            new callString(getCoreColorTime);
                        } else if (!SessionSave.getSession(CommonData.PASSENGER_COLOR_TIME, SplashAct.this).trim().equals(getCoreColorTime)) {
                            new callColor(getCoreLangTime);
                        } else {
                            if (CommonData.isCurrentTimeZone(jArry.getJSONObject(0).getLong("utc_time"))) {
                                if (SessionSave.getSession("Id", SplashAct.this).equals("")) {
                                    i = new Intent(SplashAct.this, UserLoginAct.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    if (SessionSave.getSession("trip_id", SplashAct.this).equals("")) {
                                        i = new Intent(SplashAct.this, MyStatus.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        if (SessionSave.getSession("travel_status", SplashAct.this).equals("5")) {
                                            i = new Intent(SplashAct.this, TripHistoryAct.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            i = new Intent(SplashAct.this, OngoingAct.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                }
                            } else {
                                cancelLoading();
                                CORE_CALLED = true;
                                alert_view_date(SplashAct.this, NC.getString(R.string.message), NC.getString(R.string.date_change), NC.getString(R.string.ok), NC.getString(R.string.cancel));
                            }

                        }

                    } else {
                        errorInSplash(json.getString("message"));

                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    errorInSplash(getString(R.string.server_error));
                } catch (final NullPointerException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    errorInSplash(getString(R.string.server_error));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    errorInSplash(getString(R.string.server_error));
                }
            } else {
                errorInSplash(getString(R.string.server_error));
            }
        }

    }


    public void alert_view_company(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            if (alertmDialog != null && alertmDialog.isShowing())
                alertmDialog.dismiss();
            final View view = View.inflate(mContext, R.layout.netcon_lay, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id), mContext);


            alertmDialog.show();

            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_failure.setVisibility(View.VISIBLE);
            button_failure.setText(failure_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                    getUrl();
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                    finish();

                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    public void alert_view_date(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            if (alertmDialog != null && alertmDialog.isShowing())
                alertmDialog.dismiss();
            final View view = View.inflate(mContext, R.layout.netcon_lay, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id), mContext);


            alertmDialog.show();

            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_failure.setVisibility(View.VISIBLE);
            button_failure.setText(failure_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                    finish();

                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * Alert dialog to show version alert
     */
    public void versionAlert(final Context mContext) {
        try {
            if (mverDialog != null)
                mverDialog.dismiss();
            final View view = View.inflate(mContext, R.layout.netcon_lay, null);
            mverDialog = new Dialog(mContext, R.style.NewDialog);
            mverDialog.setContentView(view);
            FontHelper.applyFont(mContext, mverDialog.findViewById(R.id.alert_id));
            mverDialog.setCancelable(false);
            if (!mverDialog.isShowing())
                mverDialog.show();
            final TextView title_text = (TextView) mverDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mverDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mverDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mverDialog.findViewById(R.id.button_failure);
            final View view_id = (View) mverDialog.findViewById(R.id.view_id);
            view_id.setVisibility(View.GONE);
            button_failure.setVisibility(View.GONE);
            title_text.setText("" + NC.getResources().getString(R.string.version_up_title));
            message_text.setText("" + NC.getResources().getString(R.string.version_up_message));
            button_success.setText("" + NC.getResources().getString(R.string.version_up_now));
            button_failure.setText("" + NC.getResources().getString(R.string.version_up_later));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName()));
                    mContext.startActivity(intent);
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mverDialog.dismiss();
                    callApi();
//                    String url = "type=getcoreconfig";
//                    new CoreConfigCall(url);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    buildGoogleApiClient();
                } else {

                    finish();
                }
                return;
            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);

    }

    private void buildGoogleApiClient() {

        System.out.println("buildGoogleApiClient 1 ");
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();
        createLocationRequest();

    }

    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates");
            LocationRequestHelper.setRequesting(this, true);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            LocationRequestHelper.setRequesting(this, false);
            e.printStackTrace();
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
