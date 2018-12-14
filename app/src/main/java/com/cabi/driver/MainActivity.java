package com.cabi.driver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cabi.driver.data.CommonData;
import com.cabi.driver.data.MystatusData;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.GcmIntentService;
import com.cabi.driver.service.LocationUpdate;
import com.cabi.driver.service.NonActivity;
import com.cabi.driver.service.ServiceGenerator;
import com.cabi.driver.utils.CL;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.fabric.sdk.android.Fabric;

/**
 * This class is the parent abstract class for all other activities
 */
public abstract class MainActivity extends FragmentActivity {
    public final String TAG = getClass().getSimpleName();
    public static MystatusData mMyStatus;
    public Typeface tf;
    public static Dialog mgpsDialog;
    public static Dialog mshowDialog;
    public static Dialog mconnDialog;
    static Calendar calendar;
    public static String returnString;
    public int _hour;
    public String _ampm;
    Dialog lDialog;
    public static Dialog sDialog;
    private final static String CONTENT_FONT_NAME = "DroidSans.ttf";
    private static Typeface ContenttypeFace;
    public static String APP_VERSION = "";
    Bundle BsavedInstanceState;
    private Dialog mlogoutDialog;
    public static MainActivity context;

    /**
     * Enum class for validation
     */
    public enum ValidateAction {
        NONE, isValueNULL,isVoucherNULL, isValidPassword, isValidSalutation, isValidFirstname, isValidLastname, isValidCard, isValidExpiry, isValidMail, isValidConfirmPassword
    }


    /**
     * Storing color values from server to local hashmap
     *
     * @param result -> response from color file url got from company domain response
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

            //    System.out.println("lislength" + nList.getLength());
            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
                    if (element2.getAttribute("name").equals("pressBack"))
                        //       System.out.println("ss___ize" + chhh + "___" + element2.getTextContent());
                        CL.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

                }
            }

            getColorValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Storing String values from server to local hashmap
     *
     * @param result -> response from String file url got from company domain response
     */
    private synchronized void getAndStoreStringValues(String result) {
        try {


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
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


    /**
     * Getting String values Splits string , key ,id and save in hashmap.
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
     * Getting Color values Splits color , key ,id and save in hashmap.
     */
    synchronized void getColorValueDetail() {
        Field[] fieldss = R.color.class.getDeclaredFields();
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


    /**
     * This is method for set up the base data for the child activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mMyStatus = new MystatusData(MainActivity.this);
        Fabric.with(this, new Crashlytics());
        GcmIntentService.MAIN_ACT = this;
        BsavedInstanceState = savedInstanceState;
        context = this;
        // TODO: Move this to where you establish a user session
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                if (!(SessionSave.getSession("wholekeyColor", MainActivity.this).trim().equals(""))) {
                    getAndStoreStringValues(SessionSave.getSession("wholekey", MainActivity.this));
                    getAndStoreColorValues(SessionSave.getSession("wholekeyColor", MainActivity.this));
                }
                if (SessionSave.getSession("base_url", MainActivity.this).trim().equals("")) {
                    ServiceGenerator.API_BASE_URL = SessionSave.getSession("base_url", MainActivity.this);
                    getAndStoreStringValues(SessionSave.getSession("wholekey", MainActivity.this));
                    getAndStoreColorValues(SessionSave.getSession("wholekeyColor", MainActivity.this));
                }


                if (APP_VERSION == null) {
                    PackageInfo info = null;
                    PackageManager manager = getPackageManager();
                    try {
                        info = manager.getPackageInfo(getPackageName(), 0);
                        APP_VERSION = info.versionName;
                        //   System.out.println("apppversion----" + APP_VERSION);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, 200);

        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        int view = setLayout();
        setLocale();
        if (view != 0) {
            setContentView(view);
            if (MainActivity.this != null)
                Initialize();


            try {
                if (mshowDialog.isShowing() && mshowDialog != null)
                    mshowDialog.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }


    /**
     * Abstract method to set layout
     */
    public abstract int setLayout();

    /**
     * Abstract method to initialize variable
     */
    public abstract void Initialize();

    /**
     * This is method for show the toast
     */
    public void ShowToast(Context contex, String message) {

        Toast toast = Toast.makeText(contex, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    /**
     * This is method for show the Log
     */
    public void showLog(String msg) {

        //  Log.i(TAG, msg);
    }

    /**
     * This is method for check the mail is valid by the use of regex class.
     */
    public boolean validdmail(String string) {
        // TODO Auto-generated method stub
        boolean isValid = false;
        String expression = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(string);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    /**
     * This is method for check the Internet connection
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

    /**
     * This is method for show progress bar over all activity
     */
    public void showLoading(Context context) {

        try {
            if (mshowDialog != null)
                if (mshowDialog.isShowing())
                    mshowDialog.dismiss();
            View view = View.inflate(context, R.layout.progress_bar, null);
            mshowDialog = new Dialog(context, R.style.dialogwinddow);
            mshowDialog.setContentView(view);
            mshowDialog.setCancelable(false);

            mshowDialog.show();

            ImageView iv = (ImageView) mshowDialog.findViewById(R.id.giff);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
            Glide.with(MainActivity.this)
                    .load(R.raw.loading_anim)
                    .into(imageViewTarget);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * This is method for convert the string value into MD5
     *
     * @param pass - String to convert to MD5
     */
    public String convertPassMd5(String pass) {

        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }

    /**
     * This is method for logout the user from their current session.
     *
     * @param context
     */
    public void logout(final Context context) {

        try {
            final View view = View.inflate(context, R.layout.netcon_lay, null);
            mlogoutDialog = new Dialog(context, R.style.dialogwinddow);
            FontHelper.applyFont(context, view);
            mlogoutDialog.setContentView(view);
            mlogoutDialog.setCancelable(false);
            mlogoutDialog.show();
            FontHelper.applyFont(context, mlogoutDialog.findViewById(R.id.alert_id));

            Colorchange.ChangeColor((ViewGroup) mlogoutDialog.findViewById(R.id.alert_id), context);

            final TextView title_text = (TextView) mlogoutDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mlogoutDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mlogoutDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mlogoutDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.VISIBLE);
            title_text.setText("" + NC.getResources().getString(R.string.message));
            message_text.setText("" + NC.getResources().getString(R.string.confirmlogout));
            button_success.setText("" + NC.getResources().getString(R.string.m_logout));
            button_failure.setText("" + NC.getResources().getString(R.string.cancel));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    try {
                        mlogoutDialog.dismiss();
                        // TODO Auto-generated method stub
                        JSONObject j = new JSONObject();
                        j.put("driver_id", SessionSave.getSession("Id", context));
                        j.put("shiftupdate_id", SessionSave.getSession("Shiftupdate_Id", context));

                        Log.d("driver id ==",SessionSave.getSession("Id", context));
                        Log.d("shiftupdate id ==",SessionSave.getSession("Shiftupdate_Id", context));

                        NonActivity nonactiityobj = new NonActivity();
                        nonactiityobj.stopServicefromNonActivity(context);
                        String url = "type=user_logout";
                        new Logout(url, j);
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
     * This is method for set the language configuration.
     */
    public void setLocale() {
        //  Toast.makeText(MainActivity.this, "Locale", Toast.LENGTH_SHORT).show();
        if (SessionSave.getSession("Lang", MainActivity.this).equals("")) {
            SessionSave.saveSession("Lang", "en", MainActivity.this);
            SessionSave.saveSession("Lang_Country", "en_GB", MainActivity.this);
        }
        //  System.out.println("Lang" + SessionSave.getSession("Lang", MainActivity.this));
        // System.out.println("Lang_Country" + SessionSave.getSession("Lang_Country", MainActivity.this));
        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", MainActivity.this);
        String arry[] = langcountry.split("_");
        config.locale = new Locale(SessionSave.getSession("Lang", MainActivity.this), arry[1]);
        Locale.setDefault(new Locale(SessionSave.getSession("Lang", MainActivity.this), arry[1]));
        MainActivity.this.getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    /**
     * This is class for logout API call and process the response
     * Clear their current session.
     */
    private class Logout implements APIResult {
        public Logout(String url, JSONObject data) {

            //          System.out.println("" + url);
            //System.out.println("" + data);
            if (isOnline()) {
                new APIService_Retrofit_JSON(MainActivity.this, this, data, false).execute(url);
            } else {
                alert_view(MainActivity.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_internet), "" + NC.getResources().getString(R.string.ok), "");
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        Intent locationService = new Intent(MainActivity.this, LocationUpdate.class);
                        stopService(new Intent(locationService));
                        clearsession(MainActivity.this);
                        SessionSave.saveSession(CommonData.SHIFT_OUT,true,MainActivity.this);
                        final View view = View.inflate(MainActivity.this, R.layout.alert_view, null);
                        FontHelper.applyFont(MainActivity.this, view);

                        lDialog = new Dialog(MainActivity.this, R.style.NewDialog);

                        Colorchange.ChangeColor((ViewGroup) view, MainActivity.this);

                        lDialog.setContentView(view);
                        lDialog.setCancelable(false);
                        lDialog.show();
                        final TextView title_text = (TextView) lDialog.findViewById(R.id.title_text);
                        final TextView message_text = (TextView) lDialog.findViewById(R.id.message_text);
                        final Button button_success = (Button) lDialog.findViewById(R.id.button_success);
                        title_text.setText("" + NC.getResources().getString(R.string.message));
                        message_text.setText("" + json.getString("message"));
                        button_success.setText("" + NC.getResources().getString(R.string.ok));
                        button_success.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                // TODO Auto-generated method stub
                                int length = CommonData.mActivitylist.size();
                                if (length != 0) {
                                    for (int i = 0; i < length; i++) {
                                        CommonData.mActivitylist.get(i).finish();
                                    }
                                }
                                lDialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, UserLoginAct.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        alert_view(MainActivity.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(MainActivity.this, getString(R.string.server_error));
                    }
                });
            }
        }
    }

    /**
     * Method to show Gcm notification
     */
    public void checkGCM() {
        String dialogMessage = SessionSave.getSession("GCMnotification", this);
        try {
            if (dialogMessage != null && !dialogMessage.trim().equals("")) {
//                show_dialog(dialogMessage);
                //  Toast.makeText(MainActivity.this, dialogMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the activity comes to onresume state.
     * Check whether driver is logged in or not
     * if id is empty userlogin activity is called.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkStatus.isOnline(getApplicationContext())) {

        }
        if (MainActivity.this != null)
            if (SessionSave.getSession("Id", MainActivity.this).trim().equals("")) {
                /*if (!((this instanceof UserLoginAct) || (this instanceof SplashAct))) {
                    Intent i = new Intent(MainActivity.this, UserLoginAct.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }*/
            }

        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
//        super.registerReceiver(new NetworkStatus(), filters);


    }

    /**
     * clear all driver session variables used except getcoreconfig details
     *
     * @param ctx - Context
     */
    public static void clearsession(Context ctx) {

        try {
            SessionSave.saveSession("status", "", ctx);
            SessionSave.saveSession("Id", "", ctx);
            SessionSave.saveSession("Driver_locations", "", ctx);
            SessionSave.saveSession("driver_id", "", ctx);
            SessionSave.saveSession("Name", "", ctx);
            SessionSave.saveSession("company_id", "", ctx);
            SessionSave.saveSession("bookedby", "", ctx);
            SessionSave.saveSession("p_image", "", ctx);
            SessionSave.saveSession("Email", "", ctx);
            SessionSave.saveSession("phone_number", "", ctx);
            SessionSave.saveSession("driver_password", "", ctx);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public Dialog alertDialog;

    /**
     * Custom alert dialog used in entire project.can call from anywhere with the following param Context,title,message,success and failure button text.
     */
    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        if (alertDialog != null)
            if (alertDialog.isShowing())
                alertDialog.dismiss();
        final View view = View.inflate(mContext, R.layout.alert_view, null);

        alertDialog = new Dialog(mContext, R.style.NewDialog);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        FontHelper.applyFont(mContext, alertDialog.findViewById(R.id.alert_id));
        Colorchange.ChangeColor((ViewGroup) alertDialog.findViewById(R.id.alert_id), mContext);

        alertDialog.show();
        final TextView title_text = (TextView) alertDialog.findViewById(R.id.title_text);
        final TextView message_text = (TextView) alertDialog.findViewById(R.id.message_text);
        final Button button_success = (Button) alertDialog.findViewById(R.id.button_success);
        final Button button_failure = (Button) alertDialog.findViewById(R.id.button_failure);
        button_failure.setVisibility(View.GONE);
        title_text.setText(title);
        message_text.setText(message);
        button_success.setText(success_txt);
        button_success.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
            }
        });
//

    }

    /**
     * Cancel dialog Loading
     */
    public void cancelLoading() {
        try {
            if (mshowDialog != null)
                if (mshowDialog.isShowing() && MainActivity.this != null)
                    mshowDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    /**
     * Showing gps alert enable
     */
    public static void gpsalert(final Context mContext, boolean isconnect) {

        if (!isconnect) {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            mgpsDialog = new Dialog(mContext, R.style.NewDialog);
            mgpsDialog.setContentView(view);
            FontHelper.applyFont(mContext, mgpsDialog.findViewById(R.id.alert_id));
            mgpsDialog.setCancelable(false);
            if (mContext instanceof SplashAct) {
                LinearLayout sub_can = (LinearLayout) mgpsDialog.findViewById(R.id.sub_can);
                sub_can.setPadding(0, 10, 0, 10);
            }
            if (!mgpsDialog.isShowing())
                mgpsDialog.show();
            final TextView title_text = (TextView) mgpsDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mgpsDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mgpsDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mgpsDialog.findViewById(R.id.button_failure);
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
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mgpsDialog.dismiss();
                }
            });
        } else {
            try {
                //          System.out.println("________called" + SessionSave.getSession("trip_id", mContext));
                if (!SessionSave.getSession("Id", mContext).trim().equals("")&& (SessionSave.getSession("shift_status", mContext).equals("IN"))) {
                    Intent locationService = new Intent(mContext, LocationUpdate.class);
                    mContext.startService(new Intent(locationService));
                }
                mgpsDialog.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
                //				e.printStackTrace();
            }
        }
    }

//    /**
//     * This is method to format date(dd-MM-yyyy hh:mm:ss a) eg:(21-MAY-2015 11:15:34 AM)
//     */
//    public static String date_conversion(String date_time) {
//
//        try {
//            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            calendar = Calendar.getInstance(Locale.UK);
//            java.util.Date yourDate = parser.parse(date_time);
//            calendar.setTime(yourDate);
//            String month = monthFullName(calendar.get(Calendar.MONTH) + 1);
//            String dayName = daytwodigit(calendar.get(Calendar.DAY_OF_MONTH) + 1);
//            String timeformat = timetwodigit(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
//            returnString = (dayName + "-" + month + "-" + calendar.get(Calendar.
//                    YEAR) + " " + timeformat);
//        } catch (Exception e) {
//            e.printStackTrace();
//            calendar = Calendar.getInstance(Locale.UK);
//            String month = monthFullName(calendar.get(Calendar.MONTH) + 1);
//            String dayName = daytwodigit(calendar.get(Calendar.DAY_OF_MONTH) + 1);
//            String timeformat = timetwodigit(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
//            returnString = (dayName + "-" + month + "-" + calendar.get(Calendar.YEAR) + " " + timeformat);
//        }
//        return returnString;
//    }

//    /**
//     * This is method to format month value (MMM) eg:(MAY)
//     */
//    private static String monthFullName(int monthnumber) {
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.MONTH, monthnumber - 1);
//        SimpleDateFormat sf = new SimpleDateFormat("MMM");
//        sf.setCalendar(cal);
//        String monthName = sf.format(cal.getTime());
//        return monthName;
//    }

//    /**
//     * This is method to format month value (dd) eg:(21)
//     */
//    private static String daytwodigit(int daynumber) {
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_MONTH, daynumber - 1);
//        SimpleDateFormat sf = new SimpleDateFormat("dd");
//        sf.setCalendar(cal);
//        String day2digit = sf.format(cal.getTime());
//        return day2digit;
//    }

//    /**
//     * This is method to format month value (hh:mm:ss a) eg:(11:15:34 AM)
//     */
//    private static String timetwodigit(int year, int month, int day, int hr, int min, int sec) {
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(year, month, day, hr, min, sec);
//        SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss a");
//        sf.setCalendar(cal);
//        String time2digit = sf.format(cal.getTime());
//        return time2digit.toUpperCase();
//    }

    /**
     * This is method to validate the field like Mail,Password,Name,Salutation etc and show the appropriate alert message.
     *
     * @param con              -context
     * @param VA               -validation action element
     * @param stringtovalidate -String to validate
     */
    public boolean validations(ValidateAction VA, Context con, String stringtovalidate) {

        String message = "";
        boolean result = false;
        switch (VA) {
            case isValueNULL:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_mobile_number);
                else
                    result = true;
                break;
            case isVoucherNULL:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + getString(R.string.enter_voucher);
                else
                    result = true;
                break;
            case isValidPassword:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_password);
                else if (stringtovalidate.length() < 5)
                    message = "" + NC.getResources().getString(R.string.pwd_min);
                else if (stringtovalidate.length() > 32)
                    message = "" + NC.getResources().getString(R.string.s_pass_max);
                else
                    result = true;
                break;
            case isValidFirstname:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_first_name);
                else
                    result = true;
                break;
            case isValidLastname:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_last_name);
                else
                    result = true;
                break;
            case isValidCard:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_card_number);
                else if (stringtovalidate.length() < 9 || stringtovalidate.length() > 16)
                    message = "" + NC.getResources().getString(R.string.enter_the_valid_card_number);
                else
                    result = true;
                break;
            case isValidExpiry:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_expiry_date);
                else
                    result = true;
                break;
            case isValidMail:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_email);
                else if (!validdmail(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_valid_email);
                else
                    result = true;
                break;
            case isValidConfirmPassword:
                if (TextUtils.isEmpty(stringtovalidate))
                    message = "" + NC.getResources().getString(R.string.enter_the_confirmation_password);
                else
                    result = true;
                break;
        }
        if (!message.equals("")) {
            alert_view(con, "" + NC.getResources().getString(R.string.message), "" + message, "" + NC.getResources().getString(R.string.ok), "");
        }
        return result;
    }


    /**
     * Slider menu used to move from one activity to another activity.
     *
     * @param v View that get clicked.
     */
    public void ClickMethod(View v) {

        Intent i;
        switch (v.getId()) {
            case R.id.menu_me:
                i = new Intent(MainActivity.this, MeAct.class);
                startActivity(i);
                finish();
                break;
            case R.id.menu_home:
                i = new Intent(MainActivity.this, MyStatus.class);
                startActivity(i);
                finish();
                break;
            case R.id.menu_logout:
                logout(MainActivity.this);
                break;
            case R.id.menu_mystatus:
                i = new Intent(MainActivity.this, MyStatus.class);
                startActivity(i);
                finish();
                break;
        }
    }

    /**
     * Called when activity is
     * Close all dialog to avoid memory leakage error
     */
    @Override
    protected void onStop() {
        CommonData.closeDialog(mconnDialog);
        if (!(this instanceof OngoingAct)) {
            CommonData.closeDialog(mgpsDialog);
        }
        CommonData.closeDialog(mlogoutDialog);
        CommonData.closeDialog(mshowDialog);
        super.onStop();
    }
}
