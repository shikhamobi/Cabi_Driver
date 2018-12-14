//package com.Taximobility.driver;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cabi.driver.data.CommonData;
//import com.cabi.driver.interfaces.APIResult;
//import com.cabi.driver.service.APIService_Retrofit_JSON;
//import com.cabi.driver.service.NonActivity;
//import com.cabi.driver.utils.Colorchange;
//import com.cabi.driver.utils.FontHelper;
//import com.cabi.driver.utils.NC;
//import com.cabi.driver.utils.SessionSave;
//
//import org.json.JSONObject;
//
//import java.util.Locale;
//
///**
// * This class is used to show the main home page including all menus
// */
//public class HomeActivity extends MainActivity implements OnClickListener {
//    // Class members declarations.
//    private CheckBox chkShift;
//    private TextView txtTrackTrip, txtProfile, txtMyJobs;
//    private TextView txtTotalJobs, txtRejected, txtTimeDriven, txtTotalEarnings, txtCurrentLocation, txtcancelled, txttotearning, txttottrip;
//    private boolean doubleBackToExitPressedOnce = false;
//    private String strTotalJobs = "", strRejected = "", strTimeDriven = "", strTotalEarnings = "", strCancelled = "", strTodayEarnings = "", strTotalTrip;
//    NonActivity nonactiityobj = new NonActivity();
//    private static final int WHAT = 1;
//    private static final int TIME_TO_WAIT = 4000;
//    public Handler regularHandler;
//    String checked = "OUT";
//    private String alert_msg;
//    private Bundle alert_bundle = new Bundle();
//    private TextView ontxt;
//    private Dialog mlangDialog;
//
//    // Set the layout to activity.
//    @Override
//    public int setLayout() {
//        setLocale();
//        return R.layout.home_layout;
//    }
//
//    // Initialize the views on layout
//    @Override
//    public void Initialize() {
//
//        try {
//            CommonData.mActivitylist.add(this);
//            System.err.println("oncreate called");
//            alert_bundle = getIntent().getExtras();
//            if (alert_bundle != null) {
//                alert_msg = alert_bundle.getString("alert_message");
//                System.err.println("oncreate called....." + alert_msg);
//            }
//            if (alert_msg != null && alert_msg.length() != 0)
//                alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
//            CommonData.sContext = this;
//            CommonData.current_act = "HomeActivity";
//            nonactiityobj.stopServicefromNonActivity(HomeActivity.this);
//
//            Colorchange.ChangeColor((ViewGroup) (((ViewGroup) HomeActivity.this
//                    .findViewById(android.R.id.content)).getChildAt(0)), HomeActivity.this);
//
//            FontHelper.applyFont(this, findViewById(R.id.id_home));
//            chkShift = (CheckBox) findViewById(R.id.chkshift);
//            txtTrackTrip = (TextView) findViewById(R.id.txttracktrip);
//            txtProfile = (TextView) findViewById(R.id.txtprofile);
//            txtMyJobs = (TextView) findViewById(R.id.txtmyjob);
//            txtTotalJobs = (TextView) findViewById(R.id.txtjobs);
//            txtRejected = (TextView) findViewById(R.id.txtrejected);
//            txtTimeDriven = (TextView) findViewById(R.id.txttimedriven);
//            txtTotalEarnings = (TextView) findViewById(R.id.totalearnings);
//            txtcancelled = (TextView) findViewById(R.id.txtcancelled);
//            txttotearning = (TextView) findViewById(R.id.txttotearning);
//            txttottrip = (TextView) findViewById(R.id.txttottrip);
//            txtCurrentLocation = (TextView) findViewById(R.id.currentlocation);
//            ontxt = (TextView) findViewById(R.id.ontxt);
//            findViewById(R.id.headerlogo).setVisibility(View.VISIBLE);
//            /* Get notification messages from bundle and show */
//            txtTrackTrip.setOnClickListener(this);
//            txtProfile.setOnClickListener(this);
//            txtMyJobs.setOnClickListener(this);
//            chkShift.setOnClickListener(this);
//            try {
//                /* Update the UI about driver statistics if any changes is there. */
//                if (!SessionSave.getSession("driver_statistics", HomeActivity.this).equals("")) {
//                    JSONObject jsonDriverObject = new JSONObject(SessionSave.getSession("driver_statistics", HomeActivity.this));
//                    strTotalJobs = jsonDriverObject.getString("completed_trip");
//                    strTotalEarnings = jsonDriverObject.getString("total_earnings");
//                    System.out.println("convert" + Uri.encode(strTotalEarnings));
//                    strRejected = jsonDriverObject.getString("overall_rejected_trips");
//                    strTimeDriven = jsonDriverObject.getString("time_driven");
//                    strCancelled = jsonDriverObject.getString("cancelled_trips");
//                    strTodayEarnings = jsonDriverObject.getString("today_earnings");
//                    strTotalTrip = jsonDriverObject.getString("total_trip");
//                    settingValues();
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////                getWindow().setStatusBarColor(getResources().getColor(R.color.colorprimary));
////            }
//
//            new requestStatisticsApi();
//            /* Timer to update current location from location update */
//            regularHandler = new Handler(new Handler.Callback() {
//                public boolean handleMessage(Message msg) {
//                    // System.out.println("------nagahandler"+msg);
//                    // Do stuff
//                    regularHandler.sendEmptyMessageDelayed(WHAT, TIME_TO_WAIT);
//                    if (SessionSave.getSession("Driver_locations_home", HomeActivity.this).equals("")) {
//                        txtCurrentLocation.setText("" + NC.getResources().getString(R.string.locating_you));
//                    } else {
//                        txtCurrentLocation.setText("" + SessionSave.getSession("Driver_locations_home", HomeActivity.this));
//                    }
//                    return true;
//                }
//            });
//
//            if (SessionSave.getSession("trip_id", HomeActivity.this).equals("")) {
//                if (OngoingAct.activity != null) {
//                    OngoingAct.activity.finish();
//                }
//
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
////    public void nagalat() {
////        try {
////            ArrayList<LatLng> data = new ArrayList<LatLng>();
////            StringBuilder buf = new StringBuilder();
////            InputStream json = getAssets().open("latlongt");
////            BufferedReader in =
////                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
////            String str;
////
////            while ((str = in.readLine()) != null) {
////                buf.append(str);
////            }
////
////            in.close();
////            JSONArray jo = new JSONArray(buf.toString());
////            for (int i = 0; i < jo.length(); i++) {
////                JSONObject jj = jo.getJSONObject(i);
////                String s = jj.getString("lat/lng");
////                String sa[] = s.split(",");
////                Double Lat = Double.parseDouble(sa[0]);
////                Double Longt = Double.parseDouble(sa[1]);
////                data.add(new LatLng(Lat, Longt));
////
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    @Override
//    protected void onResume() {
//
//        try {
//            super.onResume();
//            SessionSave.saveSession("speedwaiting", "", HomeActivity.this);
//            regularHandler.sendEmptyMessageDelayed(WHAT, TIME_TO_WAIT);
//
//
//
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//        try {
//            regularHandler.removeMessages(WHAT);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        // TODO Auto-generated method stub
//
//        CommonData.closeDialog(mlangDialog);
//        try {
//            regularHandler.removeMessages(WHAT);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//        super.onStop();
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if (doubleBackToExitPressedOnce) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            super.onBackPressed();
//            return;
//        }
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "" + NC.getResources().getString(R.string.please_click_back_again_exit), Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//    }
//
//    /**
//     * Click method used to move from one activity to another activity.
//     */
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.txttracktrip:
//                showLoading(HomeActivity.this);
//                if (!SessionSave.getSession("trip_id", HomeActivity.this).equals("")) {
//                    Intent intent = new Intent(HomeActivity.this, OngoingAct.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(HomeActivity.this, MyStatus.class);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.txtprofile:
//                showLoading(HomeActivity.this);
//                Intent intent = new Intent(HomeActivity.this, MeAct.class);
//                startActivity(intent);
//                break;
//            case R.id.txtmyjob:
//                showLoading(HomeActivity.this);
//                Intent intentJobsAct = new Intent(HomeActivity.this, TripHistoryAct.class);
//                startActivity(intentJobsAct);
//                break;
//            case R.id.chkshift:
//                chkShift.setClickable(false);
//                new RequestingCheckBox();
//                break;
//        }
//    }
//
//    /**
//     * This method used to change driver shift
//     */
//    public void checkChange(boolean on) {
//        if (on) {
//            ontxt.setText(NC.getString(R.string.ontxt));
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.FILL_PARENT);
//            params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//            params.setMargins(0, 0, 8, 0);
//            ontxt.setLayoutParams(params);
//            chkShift.setBackgroundResource(R.drawable.on_btn);
//        } else {
//            ontxt.setText(NC.getString(R.string.offtxt));
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.FILL_PARENT);
//            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
//            params.setMargins(8, 0, 0, 0);
//            ontxt.setLayoutParams(params);
//            chkShift.setBackgroundResource(R.drawable.off_btn);
//        }
//
//    }
//
//    /**
//     * Click method used to change the language and update the UI based on selected language.
//     */
//    public void language_settings(View v) {
//        // nagalat();
//        final View view = View.inflate(HomeActivity.this, R.layout.lang_list, null);
//        mlangDialog = new Dialog(HomeActivity.this, R.style.dialogwinddow);
//        mlangDialog.setContentView(view);
//        FontHelper.applyFont(HomeActivity.this, mlangDialog.findViewById(R.id.id_lang));
//        try {
//            Colorchange.ChangeColor((ViewGroup)  mlangDialog.findViewById(R.id.id_lang), HomeActivity.this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mlangDialog.setCancelable(true);
//        mlangDialog.show();
//        final LinearLayout lay_fav_res1 = (LinearLayout) mlangDialog.findViewById(R.id.lay_fav_res1);
//        final LinearLayout lay_fav_res2 = (LinearLayout) mlangDialog.findViewById(R.id.lay_fav_res2);
//        final LinearLayout lay_fav_res3 = (LinearLayout) mlangDialog.findViewById(R.id.lay_fav_res3);
//        final LinearLayout lay_fav_res4 = (LinearLayout) mlangDialog.findViewById(R.id.lay_fav_res4);
//        final LinearLayout lay_fav_res5 = (LinearLayout) mlangDialog.findViewById(R.id.lay_fav_res5);
//        final LinearLayout lay_fav_res6 = (LinearLayout) mlangDialog.findViewById(R.id.lay_fav_res6);
//        final LinearLayout lay_fav_res7 = (LinearLayout) mlangDialog.findViewById(R.id.lay_fav_res7);
//        getIntent().removeExtra("alert_message");
//        lay_fav_res1.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!SessionSave.getSession("Lang", HomeActivity.this).equals("en")) {
//                    SessionSave.saveSession("Lang", "en", HomeActivity.this);
//                    SessionSave.saveSession("Lang_Country", "en_GB", HomeActivity.this);
//                    mlangDialog.dismiss();
//                    RefreshAct();
//                } else {
//                    mlangDialog.dismiss();
//                }
//            }
//        });
//        lay_fav_res2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!SessionSave.getSession("Lang", HomeActivity.this).equals("tr")) {
//                    SessionSave.saveSession("Lang", "tr", HomeActivity.this);
//                    SessionSave.saveSession("Lang_Country", "tr_TR", HomeActivity.this);
//                    mlangDialog.dismiss();
//                    RefreshAct();
//                } else {
//                    mlangDialog.dismiss();
//                }
//
//            }
//        });
//        lay_fav_res3.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!SessionSave.getSession("Lang", HomeActivity.this).equals("ar")) {
//                    SessionSave.saveSession("Lang", "ar", HomeActivity.this);
//                    SessionSave.saveSession("Lang_Country", "ar_EG", HomeActivity.this);
//                    mlangDialog.dismiss();
//                    RefreshAct();
//                } else {
//                    mlangDialog.dismiss();
//                }
//
//            }
//        });
//        lay_fav_res4.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!SessionSave.getSession("Lang", HomeActivity.this).equals("ru")) {
//                    SessionSave.saveSession("Lang", "ru", HomeActivity.this);
//                    SessionSave.saveSession("Lang_Country", "ru_RU", HomeActivity.this);
//                    mlangDialog.dismiss();
//                    RefreshAct();
//                } else {
//                    mlangDialog.dismiss();
//                }
//
//            }
//        });
//        lay_fav_res5.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!SessionSave.getSession("Lang", HomeActivity.this).equals("de")) {
//                    SessionSave.saveSession("Lang", "de", HomeActivity.this);
//                    SessionSave.saveSession("Lang_Country", "de_DE", HomeActivity.this);
//                    mlangDialog.dismiss();
//                    RefreshAct();
//                } else {
//                    mlangDialog.dismiss();
//                }
//
//            }
//        });
//        lay_fav_res6.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!SessionSave.getSession("Lang", HomeActivity.this).equals("fr")) {
//                    SessionSave.saveSession("Lang", "fr", HomeActivity.this);
//                    SessionSave.saveSession("Lang_Country", "fr_FR", HomeActivity.this);
//                    mlangDialog.dismiss();
//                    RefreshAct();
//                } else {
//                    mlangDialog.dismiss();
//                }
//
//            }
//        });
//        lay_fav_res7.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!SessionSave.getSession("Lang", HomeActivity.this).equals("fr")) {
//                    SessionSave.saveSession("Lang", "es", HomeActivity.this);
//                    SessionSave.saveSession("Lang_Country", "es_US", HomeActivity.this);
//                    mlangDialog.dismiss();
//                    RefreshAct();
//                } else {
//                    mlangDialog.dismiss();
//                }
//
//            }
//        });
//    }
//
//    /**
//     * This class for API call to change the driver_statistics changes based on API result
//     */
//    private class requestStatisticsApi implements APIResult {
//        public requestStatisticsApi() {
//
//            try {
//                String driver_number = SessionSave.getSession("Id", HomeActivity.this);
//                JSONObject j = new JSONObject();
//                j.put("driver_id", driver_number);
//                String requestingUrl = "type=driver_statistics";
//                if (isOnline())
//                    new APIService_Retrofit_JSON(HomeActivity.this, this, j, false).execute(requestingUrl);
//                else
//                    alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_internet), "" + NC.getResources().getString(R.string.ok), "");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void getResult(boolean isSuccess, final String result) {
//
//            try {
//                System.out.println("ShiftIII___" + result);
//                if (isSuccess) {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (jsonObject.getInt("status") == 1) {
//                        jsonObject = jsonObject.getJSONObject("detail");
//                        strRejected = jsonObject.getString("overall_rejected_trips");
//                        strTimeDriven = jsonObject.getString("time_driven");
//                        strTotalEarnings = jsonObject.getString("total_earnings");
//                        strTotalJobs = jsonObject.getString("completed_trip");
//                        txtcancelled.setText("" + strCancelled);
//                        txttotearning.setText("" + SessionSave.getSession("site_currency", HomeActivity.this) + " " + String.format(Locale.UK, "%.2f", Double.parseDouble(strTotalEarnings)));
//                        txttottrip.setText("" + strTotalTrip);
//                        SessionSave.saveSession("shift_status", jsonObject.getString("shift_status"), HomeActivity.this);
//
//                        settingValues();
//                    } else {
//                        alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + jsonObject.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                    }
//                    nonactiityobj.startServicefromNonActivity(HomeActivity.this);
//                } else {
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            ShowToast(HomeActivity.this, getString(R.string.server_error));
//                        }
//                    });
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * This Method will refresh screen after changing language
//     */
//    private void RefreshAct() {
//
//        Intent intent = getIntent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        finish();
//        startActivity(intent);
//    }
//
//    /**
//     * Update the UI about driver statistics
//     */
//    private void settingValues() {
//
//        txtTotalJobs.setText("" + strTotalJobs);
//        txtRejected.setText("" + strRejected);
//        txtTimeDriven.setText("" + strTimeDriven);
//        txtcancelled.setText("" + strCancelled);
//        txttotearning.setText("" + SessionSave.getSession("site_currency", HomeActivity.this) + " " + String.format(Locale.UK, "%.2f", Double.parseDouble(strTotalEarnings)));
//        txttottrip.setText("" + strTotalTrip);
//        txtTotalEarnings.setText("" + SessionSave.getSession("site_currency", HomeActivity.this) + " " + String.format(Locale.UK, "%.2f", Double.parseDouble(strTodayEarnings)));
//        txtCurrentLocation.setText(SessionSave.getSession("Driver_locations_home", HomeActivity.this));
//        // System.out.println("ShiftIII___"+SessionSave.getSession("shift_status", HomeActivity.this));
//
//        System.out.println("______" + SessionSave.getSession("shift_status", HomeActivity.this));
//        if (SessionSave.getSession("shift_status", HomeActivity.this).equals("IN")) {
//            chkShift.setChecked(true);
//            checkChange(true);
//
//            nonactiityobj.startServicefromNonActivity(HomeActivity.this);
//        } else {
//            chkShift.setChecked(false);
//            checkChange(false);
//
//            nonactiityobj.stopServicefromNonActivity(HomeActivity.this);
//        }
//    }
//
//    /**
//     * This class for API call to change the driver shift changes and Check Box value based on API result
//     */
//    private class RequestingCheckBox implements APIResult {
//        public RequestingCheckBox() {
//
//            try {
//                if (chkShift.isChecked())
//                    checked = "IN";
//                else
//                    checked = "OUT";
//                JSONObject j = new JSONObject();
//                j.put("driver_id", SessionSave.getSession("Id", HomeActivity.this));
//                j.put("shiftstatus", checked);
//                String requestingCheckBox = "type=driver_shift";
//                if (isOnline())
//                    new APIService_Retrofit_JSON(HomeActivity.this, this, j, false).execute(requestingCheckBox);
//                else {
//                    chkShift.setClickable(true);
//                    alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
//                    if (checked.equals("IN")) {
//                        chkShift.setChecked(true);
//                        checkChange(true);
//                    } else {
//                        chkShift.setChecked(false);
//                        checkChange(true);
//
//                    }
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void getResult(boolean isSuccess, final String result) {
//
//            try {
//                if (isSuccess) {
//                    chkShift.setClickable(true);
//
//                    JSONObject object = new JSONObject(result);
//                    if (object.getInt("status") == 1) {
//                        alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                        chkShift.setChecked(true);
//                        checkChange(true);
//                        SessionSave.saveSession("shift_status", "IN", HomeActivity.this);
//                        SessionSave.saveSession("Shiftupdate_Id", object.getString("update_id"), HomeActivity.this);
//                        nonactiityobj.startServicefromNonActivity(HomeActivity.this);
//                    } else if (object.getInt("status") == 2) {
//                        alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                        chkShift.setChecked(false);
//                        checkChange(false);
//                        SessionSave.saveSession("shift_status", "OUT", HomeActivity.this);
//                        nonactiityobj.stopServicefromNonActivity(HomeActivity.this);
//                    } else if (object.getInt("status") == -4) {
//                        alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                        chkShift.setChecked(true);
//                        checkChange(true);
//                    } else {
//                        alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                        if (checked.equals("IN")) {
//                            chkShift.setChecked(true);
//                            checkChange(true);
//                        } else {
//                            chkShift.setChecked(false);
//                            checkChange(false);
//                        }
//                    }
//                } else {
//
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            ShowToast(HomeActivity.this, getString(R.string.server_error));
//                        }
//                    });
//                    chkShift.setClickable(true);
//                    // alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
//                    if (checked.equals("IN")) {
//                        chkShift.setChecked(true);
//                        checkChange(true);
//                    } else {
//                        chkShift.setChecked(false);
//                        checkChange(false);
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                chkShift.setClickable(true);
//                alert_view(HomeActivity.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
//                if (checked.equals("IN")) {
//                    chkShift.setChecked(true);
//                    checkChange(true);
//                } else {
//                    chkShift.setChecked(false);
//                    checkChange(false);
//                }
//            }
//        }
//    }
//}