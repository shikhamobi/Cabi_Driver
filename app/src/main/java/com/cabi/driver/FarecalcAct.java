package com.cabi.driver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cabi.driver.data.CommonData;
import com.cabi.driver.data.apiData.ApiRequestData;
import com.cabi.driver.data.apiData.StreetCompleteResponse;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.CoreClient;
import com.cabi.driver.service.ServiceGenerator;
import com.cabi.driver.service.WaitingTimerRun;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.LocationDb;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class is used to calculate the trip fare.
 */

public class FarecalcAct extends MainActivity {
    // Class members declarations.
    private String message;
    private String f_tripid;
    private String f_distance;
    private String f_metric;
    private String f_totalfare;
    private String f_nightfareapplicable;
    private String f_nightfare;
    private String f_eveningfare_applicable = "0";

    private String f_eveningfare;
    private String f_pickup = "", drop_location = "";
    private String f_waitingtime;
    private String f_waitingcost;
    private String f_waitingmin;
    private String f_taxamount;
    private String f_tripfare;
    private String f_creditcard;
    private String f_farediscount = "";
    private String promotax = "";
    private String promoamt = "";
    private String f_paymodid = "";
    private String p_dis = "";
    private String f_walletamt = "";
    private String f_payamt = "";
    private double m_distance;
    private double m_tripfare;
    private double m_totalfare;
    private double m_taxamount;
    private double m_waitingcost;
    private double m_walletamt;
    private double m_payamt;
    private double f_fare;
    private double f_tips;
    private double f_total;
    private EditText farecalTxt;
    private EditText tipsTxt;
    private TextView HeadTitle;
    //    private TextView paycashTxt;
//    private TextView paycardTxt;
//    private TextView payuncardTxt;
    //   private TextView accountTxt;
    private TextView totalamountTxt;
    private TextView actdistanceTxt;
    private TextView metricTxt;
    private TextView promopercentTxt;
    private TextView b_farecalCurrency;
    private TextView b_tipsCurrency;
    private TextView b_pickuplocation;
    private TextView b_droplocation;
    private TextView b_total_amt_curency;
    private TextView b_waitingcost, b_tax, b_discount, b_roundtrip, v_trip_fare;
    private TextView remarks;
    private TextView walletamountTxt;
    private TextView amountpayTxt;
    private TextView idwaitingcost;
    private Dialog mDialog;
    private LinearLayout lay_fare;
    private LinearLayout walletlay;
    private LinearLayout paylay;
    private boolean istipsTxt_focus = false;
    private String cmpTax = "";
    private LinearLayout promoLayout, tax_lay;
    private TextView txtCmp, slideImg;// backup;
    public static Activity mFlagger;
    public static FarecalcAct activity;
    Intent details;
    private String promodiscount_amount;
    RadioButton radiowalletButton;
    TextView radiocashButton, radiocardButton, radiouncardButton;
    private String f_minutes_traveled;
    private String f_minutes_fare;
    private String Cvv;
    View vid_discount;
    LocationDb objLocationDb;
    private LinearLayout noWallet;
    private boolean isArab;
    private String base_fare = "";
    private boolean fromStreetPickUp;
    private String fare_calculation_type = "3";
    private LinearLayout distance_lay, minutes_lay;
    private TextView minutes_value;
    private TextView walletamountCurrency;
    private TextView amountpayCurrency;
    private LinearLayout eve_fare_lay;
    TextView eve_fare;
    public static int Payment_status = 0;
    private boolean doubleBackToExitPressedOnce = false;


    // Set the layout to activity.
    @Override
    public int setLayout() {

        setLocale();
        return R.layout.farecalc_lay;
        // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    // Initialize the views on layout
    @Override
    public void Initialize() {

        CommonData.sContext = this;
        CommonData.current_act = "FarecalcAct";
        CommonData.mActivitylist.add(this);
        activity = this;
        mFlagger = this;

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) FarecalcAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), FarecalcAct.this);

        objLocationDb = new LocationDb(FarecalcAct.this);
        FontHelper.applyFont(this, findViewById(R.id.id_farelay));
        b_pickuplocation = (TextView) findViewById(R.id.pickuplocTxt);
        b_droplocation = (TextView) findViewById(R.id.droplocTxt);
        b_farecalCurrency = (TextView) findViewById(R.id.farecalCurrency);
        actdistanceTxt = (TextView) findViewById(R.id.actdistanceTxt);
        metricTxt = (TextView) findViewById(R.id.metricTxt);
        b_tipsCurrency = (TextView) findViewById(R.id.tipsCurrency);
        farecalTxt = (EditText) findViewById(R.id.farecalTxt);
        tipsTxt = (EditText) findViewById(R.id.tipsTxt);
        tipsTxt.setText("0");

        HeadTitle = (TextView) findViewById(R.id.headerTxt);
        distance_lay = (LinearLayout) findViewById(R.id.distance_lay);
        minutes_lay = (LinearLayout) findViewById(R.id.minutes_lay);

        minutes_value = (TextView) findViewById(R.id.min_value);
        eve_fare_lay = (LinearLayout) findViewById(R.id.eve_fare_lay);
        eve_fare = (TextView) findViewById(R.id.eve_fare);
//        paycardTxt = (TextView) findViewById(R.id.paycard);
//        payuncardTxt = (TextView) findViewById(R.id.payuncard);
        // accountTxt = (TextView) findViewById(R.id.payaccount);
        totalamountTxt = (TextView) findViewById(R.id.totalamountTxt);
        promopercentTxt = (TextView) findViewById(R.id.promopercentage);
        walletamountTxt = (TextView) findViewById(R.id.walletamountTxt);
        walletamountCurrency = (TextView) findViewById(R.id.walletamountCurrency);
        amountpayCurrency = (TextView) findViewById(R.id.amountpayCurrency);
        amountpayTxt = (TextView) findViewById(R.id.amountpayTxt);
        tax_lay = (LinearLayout) findViewById(R.id.tax_lay);
        txtCmp = (TextView) findViewById(R.id.txtcmpTax);
        //  slideImg = (TextView) findViewById(R.id.slideImg);
        //backup = (TextView) findViewById(R.id.backup);
        walletlay = (LinearLayout) findViewById(R.id.walletlay);
        paylay = (LinearLayout) findViewById(R.id.paylay);
        noWallet = (LinearLayout) findViewById(R.id.noWallet);
        //  slideImg.setVisibility(View.VISIBLE);
        // slideImg.setText("");
        //backup.setVisibility(View.GONE);
        promoLayout = (LinearLayout) findViewById(R.id.discountlayout);
        lay_fare = (LinearLayout) findViewById(R.id.lay_fare);
        remarks = (TextView) findViewById(R.id.remarks);
        b_total_amt_curency = (TextView) findViewById(R.id.toatalamtCurrency);
        b_waitingcost = (TextView) findViewById(R.id.waitingcost);
        idwaitingcost = (TextView) findViewById(R.id.idwaitingcost);
        b_tax = (TextView) findViewById(R.id.tax);
        b_discount = (TextView) findViewById(R.id.discount);
        b_roundtrip = (TextView) findViewById(R.id.roundtrip);
        v_trip_fare = (TextView) findViewById(R.id.v_trip_fare);
        vid_discount = (View) findViewById(R.id.vid_discount);
        radiocashButton = (TextView) findViewById(R.id.rbtn_cash);
        radiowalletButton = (RadioButton) findViewById(R.id.rbtn_wallet);
        radiocardButton = (TextView) findViewById(R.id.rbtn_card);
        radiouncardButton = (TextView) findViewById(R.id.rbtn_uncard);
        HeadTitle.setText("" + NC.getResources().getString(R.string.fare_calculator));
        details = getIntent();
        isArab = SessionSave.getSession("Lang", this).equals("ar") || SessionSave.getSession("Lang", this).equals("fa");

       /* slideImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarecalcAct.this, MyStatus.class);
                startActivity(intent);
                finish();
            }
        });*/

        try {
            // If Directly comes from end trip page(OngoingAct)
            if (details.getStringExtra("from").equalsIgnoreCase("direct")) {
                message = details.getStringExtra("message");


                // This for update the fare calculator page with API result.
                setFareCalculatorScreen();
                if (details.getBooleanExtra("from_split", false))
                    fromStreetPickUp = true;
            }
            // If comes from Pending bookings(JobsAct).
            else {
                String lat = details.getStringExtra("lat");
                String lon = details.getStringExtra("lon");
                String distance = details.getStringExtra("distance");
                String waitingHr = details.getStringExtra("waitingHr");
                String drop_location = details.getStringExtra("drop_location");
                String url = "type=complete_trip";
                new CompleteTrip(url, lat, lon, distance, waitingHr, drop_location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (Payment_status == 1) {
            showDialog();
            String url = "type=check_payment_status";
            try {
                JSONObject j = new JSONObject();
                j.put("trip_id", f_tripid);
                new Check_paymentstatus(url, j);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class Check_paymentstatus implements APIResult {
        String msg = "";

        public Check_paymentstatus(String url, JSONObject data) {

            if (isOnline()) {
                new APIService_Retrofit_JSON(FarecalcAct.this, this, data, false).execute(url);
            } else {
                alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_internet), "" + NC.getResources().getString(R.string.ok), "");
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            if (isSuccess) {
                closeDialog();
                try {
                    JSONObject json = new JSONObject(result);
                    msg = json.getString("message");

                    if (json.getString("status").equals("1")) {
                        // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        ShowToast(FarecalcAct.this, msg);
                        System.out.print("Payment status" + "......" + json);

                        SessionSave.saveSession("travel_status", "", FarecalcAct.this);
                        SessionSave.saveSession("trip_id", "", FarecalcAct.this);
                        SessionSave.saveSession("status", "F", FarecalcAct.this);

                        MainActivity.mMyStatus.setdistance("");
                        msg = json.getString("message");
                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setStatus("F");
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("On");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        JSONObject jsonDriver = json.getJSONObject("driver_statistics");
                        SessionSave.saveSession("driver_statistics", "" + jsonDriver, FarecalcAct.this);
                        CommonData.hstravel_km = "";
                        WaitingTimerRun.sTimer = "00:00:00";
                        WaitingTimerRun.finalTime = 0L;
                        WaitingTimerRun.timeInMillies = 0L;
                        SessionSave.saveSession("waitingHr", "", FarecalcAct.this);
                        CommonData.travel_km = 0;
                        SessionSave.setGoogleDistance(0f, FarecalcAct.this);
                        SessionSave.setDistance(0f, FarecalcAct.this);
                        SessionSave.saveGoogleWaypoints(null, null, "", 0.0, "", FarecalcAct.this);
                        SessionSave.saveWaypoints(null, null, "", 0.0, "", FarecalcAct.this);
                        Intent jobintent = new Intent(FarecalcAct.this, JobdoneAct.class);
                        Bundle bun = new Bundle();
                        bun.putString("message", result);
                        jobintent.putExtras(bun);
                        startActivity(jobintent);
                        finish();
                    } else {
                        ShowToast(FarecalcAct.this, msg);
                    }


                } catch (Exception e) {
                    closeDialog();
                    e.printStackTrace();
                }
            } else {
                closeDialog();
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(FarecalcAct.this, getString(R.string.server_error));
                    }
                });
                lay_fare.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        CommonData.closeDialog(mDialog);
        super.onStop();
    }

    /**
     * This for update the fare calculator page with API result.
     */
    @SuppressLint("SdCardPath")
    private void setFareCalculatorScreen() {

        if (details != null) {
            try {
                JSONObject obj = new JSONObject(message);
                JSONObject json = obj.getJSONObject("detail");
                f_tripid = json.getString("trip_id");
                f_distance = json.getString("distance");
                f_metric = json.getString("metric");
                f_totalfare = json.getString("subtotal_fare");
                f_nightfareapplicable = json.getString("nightfare_applicable");
                f_nightfare = json.getString("nightfare");
                f_eveningfare_applicable = json.getString("eveningfare_applicable");
                f_eveningfare = json.getString("eveningfare");
                f_pickup = json.getString("pickup");
                drop_location = json.getString("drop");
                f_waitingtime = json.getString("waiting_time");
                f_waitingcost = json.getString("waiting_cost");
                f_waitingmin = json.getString("waiting_cost");
                f_taxamount = json.getString("tax_amount");
                f_tripfare = json.getString("trip_fare");
                f_payamt = json.getString("total_fare");
                f_walletamt = json.getString("wallet_amount_used");
                f_minutes_traveled = json.getString("minutes_traveled");
                f_minutes_fare = json.getString("minutes_fare");
                f_creditcard = json.getString("credit_card_status");
                f_farediscount = json.getString("promodiscount_amount");
                base_fare = json.getString("base_fare");
                cmpTax = json.getString("company_tax");
                if (f_eveningfare_applicable.equalsIgnoreCase("1")) {
                    eve_fare.setText("" + f_eveningfare);
                    eve_fare_lay.setVisibility(View.VISIBLE);
                }
                try {

                    fare_calculation_type = json.getString("fare_calculation_type");
                    if (fare_calculation_type.trim().equals("1"))
                        minutes_lay.setVisibility(View.GONE);
                    else if (fare_calculation_type.trim().equals("2"))
                        distance_lay.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (f_walletamt.length() != 0)
                    m_walletamt = Double.parseDouble(f_walletamt);
                f_walletamt = String.format(Locale.UK, "%.2f", m_walletamt);
                if (f_payamt.length() != 0)
                    m_payamt = Double.parseDouble(f_payamt);
                f_payamt = String.format(Locale.UK, "%.2f", m_payamt);
                if (f_waitingcost.length() != 0)
                    m_waitingcost = Double.parseDouble(f_waitingcost);
                f_waitingcost = String.format(Locale.UK, "%.2f", m_waitingcost);
                if (f_totalfare.length() != 0)
                    m_totalfare = Double.parseDouble(f_totalfare);
                f_totalfare = String.format(Locale.UK, "%.2f", m_totalfare);
                if (f_distance.length() != 0)
                    m_distance = Double.parseDouble(f_distance);
                f_distance = String.format(Locale.UK, "%.2f", m_distance);
                if (f_tripfare.length() != 0)
                    m_tripfare = Double.parseDouble(f_tripfare);
                f_tripfare = String.format(Locale.UK, "%.2f", m_tripfare);
                if (f_taxamount.length() != 0)
                    m_taxamount = Double.parseDouble(f_taxamount);
                f_taxamount = String.format(Locale.UK, "%.2f", m_taxamount);
                idwaitingcost.setText("" + NC.getResources().getString(R.string.waiting_cost) + "(" + f_waitingtime + ")");
                if (!cmpTax.trim().equals("0"))
                    txtCmp.setText("" + "Tax" + cmpTax + "" + "%)");
                else
                    tax_lay.setVisibility(View.GONE);
                farecalTxt.setText(f_totalfare);
//                if(isArab)
//                    v_trip_fare.setText("" +f_tripfare+" "+ SessionSave.getSession("site_currency", FarecalcAct.this) );
//                else
                v_trip_fare.setText("" + SessionSave.getSession("site_currency", FarecalcAct.this) + " " + f_tripfare);
                p_dis = json.getString("promo_discount_per");
                promodiscount_amount = json.getString("promo_discount_per");
                if (!p_dis.trim().equals("")) {
                    if (!promodiscount_amount.equals("0") && json.getString("promo_type").equals("2"))
                        promopercentTxt.setText("" + NC.getResources().getString(R.string.discount) + "(" + promodiscount_amount + "" + NC.getResources().getString(R.string.tax_percent));
                    else
                        promopercentTxt.setText("" + NC.getResources().getString(R.string.discount));
//                    if(isArab)
//                        b_discount.setText("" +f_farediscount+ " " +SessionSave.getSession("site_currency", FarecalcAct.this) );
//                    else
                    b_discount.setText("" + SessionSave.getSession("site_currency", FarecalcAct.this) + " " + f_farediscount);
                    promoLayout.setVisibility(View.VISIBLE);
                    vid_discount.setVisibility(View.GONE);
                } else {
                    promoLayout.setVisibility(View.GONE);
                    vid_discount.setVisibility(View.GONE);
                }
                if (promoamt.equals("0")) {
                    promoLayout.setVisibility(View.GONE);
                }
                metricTxt.setText(f_metric);
                actdistanceTxt.setText("" + f_distance);
                minutes_value.setText(f_minutes_traveled + " " + NC.getString(R.string.mins));
                b_pickuplocation.setText(f_pickup);
//                if (SessionSave.getSession("drop_location", FarecalcAct.this).length() != 0)
//                    b_droplocation.setText("" + SessionSave.getSession("drop_location", FarecalcAct.this));
//                else
                b_droplocation.setText("" + drop_location);
                b_total_amt_curency.setText("" + SessionSave.getSession("site_currency", FarecalcAct.this) + " ");
//                if(!isArab)
                b_waitingcost.setText("" + SessionSave.getSession("site_currency", FarecalcAct.this) + " " + f_waitingcost);
//                else
                //                b_waitingcost.setText(""  + f_waitingcost+" "+ SessionSave.getSession("site_currency", FarecalcAct.this) );
//                if(!isArab)
                b_tax.setText("" + SessionSave.getSession("site_currency", FarecalcAct.this) + " " + f_taxamount);
                //  else
                //    b_tax.setText("" +f_taxamount+" "+SessionSave.getSession("site_currency", FarecalcAct.this) );
                b_roundtrip.setText("" + json.getString("roundtrip"));
                tipsTxt.setHint("0");
                remarks.setText("" + objLocationDb.getdistance(f_tripid));
                if (SessionSave.getSession("site_currency", FarecalcAct.this) != null) {
                    b_farecalCurrency.setText(SessionSave.getSession("site_currency", FarecalcAct.this) + " ");
                    b_tipsCurrency.setText(SessionSave.getSession("site_currency", FarecalcAct.this) + " ");
                }
                f_fare = m_totalfare;
                if (tipsTxt.length() != 0) {
                    f_tips = Double.parseDouble(Uri.decode(tipsTxt.getText().toString()));
                }
                f_total = f_fare + f_tips;
                totalamountTxt.setText("" + String.format(Locale.UK, "%.2f", f_total));
                JSONArray ary = new JSONArray(json.getString("gateway_details"));
                // the following code for handle the payment mode dynamically.
                int length = ary.length();

                walletamountCurrency.setText(SessionSave.getSession("site_currency", FarecalcAct.this) + " ");
                amountpayCurrency.setText(SessionSave.getSession("site_currency", FarecalcAct.this) + " ");
                if (m_walletamt > 0) {
                    walletlay.setVisibility(View.VISIBLE);
                    walletamountTxt.setText(f_walletamt);
                    paylay.setVisibility(View.VISIBLE);
                    amountpayTxt.setText(f_payamt);
                }
                amountpayTxt.setText(f_payamt);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorprimary));
//                }
                for (int i = 0; i < length; i++) {
                    String paymentModeDefault = ary.getJSONObject(i).getString("pay_mod_default");
                    String paymentMode_Id = ary.getJSONObject(i).getString("pay_mod_id");
                    if (paymentMode_Id.equalsIgnoreCase("5")) {
                        radiowalletButton.setVisibility(View.VISIBLE);
                        if (paymentModeDefault.equals("1")) {
                            radiowalletButton.setTextColor(Color.DKGRAY);
                        }
                    } else if (paymentMode_Id.equalsIgnoreCase("1")) {
                        radiocashButton.setVisibility(View.VISIBLE);
                        if (paymentModeDefault.equals("1")) {
                            radiocashButton.setTextColor(Color.DKGRAY);
                        }
                    } else if (paymentMode_Id.equalsIgnoreCase("2")) {
                        radiocardButton.setVisibility(View.VISIBLE);
                        if (paymentModeDefault.equals("1")) {
                            radiocardButton.setTextColor(Color.DKGRAY);
                        }
                    } else if (paymentMode_Id.equalsIgnoreCase("3")) {

                        radiouncardButton.setVisibility(View.VISIBLE);
                        if (paymentModeDefault.equals("1")) {
                            radiouncardButton.setTextColor(Color.DKGRAY);
                        }
                    } else if (paymentMode_Id.equalsIgnoreCase("4")) {

//                        radioaccButton.setVisibility(View.VISIBLE);
//                        if (paymentModeDefault.equals("1")) {
//                            radioaccButton.setTextColor(Color.DKGRAY);
//                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // The following process will done while select the payment mode as cash.
        radiocashButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromStreetPickUp)
                    completeStreetTrip();
                else {
                    radiocardButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.credit_card_unfocus, 0, 0);
                    radiocashButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.cash_unfocus, 0, 0);
                    radiouncardButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.newcard_unfocus, 0, 0);

                    radiocashButton.setTextColor(Color.DKGRAY);
                    radiocardButton.setTextColor(Color.LTGRAY);
                    radiouncardButton.setTextColor(Color.LTGRAY);
//                radioaccButton.setTextColor(Color.LTGRAY);
                    if (farecalTxt.length() != 0) {
                        f_fare = Double.parseDouble(((FontHelper.convertfromArabic(f_totalfare)).replace(",", ".")));
                    }
                    if (tipsTxt.length() != 0) {
                        f_tips = Double.parseDouble((FontHelper.convertfromArabic((tipsTxt.getText().toString())).replace(",", ".")));
                    }
                    f_total = f_fare + f_tips;
                    // totalamountTxt.setText("" + Double.toString(f_total));
                    f_paymodid = "1";
                    callurl();
                }
            }
        });
        // The following process will done while select the payment mode as wallet.
        radiowalletButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                radiowalletButton.setTextColor(Color.DKGRAY);
                radiocashButton.setTextColor(Color.LTGRAY);
                radiocardButton.setTextColor(Color.LTGRAY);
                radiouncardButton.setTextColor(Color.LTGRAY);
                // radioaccButton.setTextColor(Color.LTGRAY);
                if (farecalTxt.length() != 0) {
                    f_fare = Double.parseDouble(FontHelper.convertfromArabic(farecalTxt.getText().toString()));
                }
                if (tipsTxt.length() != 0) {
                    f_tips = Double.parseDouble(FontHelper.convertfromArabic(tipsTxt.getText().toString()));
                }
                f_total = f_fare + f_tips;
                totalamountTxt.setText("" + String.format(Locale.UK, "%.2f", f_total));
                f_paymodid = "5";
                callurl();
            }
        });
        // The following process will done while select the payment mode as card. And it shows the dialog to get the CVV number.
        radiocardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                radiocashButton.setTextColor(Color.LTGRAY);
                radiocardButton.setTextColor(Color.DKGRAY);
                radiouncardButton.setTextColor(Color.LTGRAY);

                radiocardButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.credit_card_unfocus, 0, 0);
                radiocashButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.cash_unfocus, 0, 0);
                radiouncardButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.newcard_unfocus, 0, 0);
                // radioaccButton.setTextColor(Color.LTGRAY);
                if (farecalTxt.length() != 0) {
                    f_fare = Double.parseDouble(FontHelper.convertfromArabic(farecalTxt.getText().toString()));
                }
                if (tipsTxt.length() != 0) {
                    f_tips = Double.parseDouble(FontHelper.convertfromArabic(tipsTxt.getText().toString()));
                }
                f_total = f_fare + f_tips;
                // totalamountTxt.setText("" + Double.toString(f_total));
                f_paymodid = "2";
                // if (f_creditcard.equalsIgnoreCase("1") && f_total > 0) {

                callurl();
//                try {
//                    final View view = View.inflate(FarecalcAct.this, R.layout.forgot_popup, null);
//                    mDialog = new Dialog(FarecalcAct.this, R.style.dialogwinddow);
//                    FontHelper.applyFont(FarecalcAct.this, view);
//
//                    mDialog.setContentView(view);
//                    mDialog.setCancelable(true);
//                    mDialog.show();
//                    try {
//                        Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.inner_content), FarecalcAct.this);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    final TextView t = (TextView) mDialog.findViewById(R.id.message);
//                    final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
//                    final Button OK = (Button) mDialog.findViewById(R.id.okbtn);
//                    final Button Cancel = (Button) mDialog.findViewById(R.id.cancelbtn);
//                    t.setText("" + NC.getResources().getString(R.string.ent_cvv));
//                    mail.setHint("" + NC.getResources().getString(R.string.ent_cvv));
//                    mail.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    int maxLength = 4;
//                    InputFilter[] FilterArray = new InputFilter[1];
//                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
//                    mail.setFilters(FilterArray);
//                    OK.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(final View v) {
//                            // TODO Auto-generated method stub
//                            Cvv = mail.getText().toString().trim();
//                            if (Cvv.length() == 0)
//                                alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_cvv), "" + NC.getResources().getString(R.string.ok), "");
//                            else if (Cvv.length() < 3)
//                                alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_chk_cvv), "" + NC.getResources().getString(R.string.ok), "");
//                            else {
//                                mDialog.dismiss();
//                                callurl();
//                            }
//                        }
//                    });
//                    Cancel.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(final View v) {
//                            // TODO Auto-generated method stub
//                            mDialog.dismiss();
//                        }
//                    });
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//                } else {
//                    callurl();
//                }
            }
        });
        // The following process will done while select the payment mode as uncard.
        radiouncardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                radiocardButton.performClick();  //Local

               /* radiocashButton.setTextColor(Color.LTGRAY);
                radiocardButton.setTextColor(Color.LTGRAY);
                radiouncardButton.setTextColor(Color.DKGRAY);
                radiocardButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.credit_card_unfocus, 0, 0);
                radiocashButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.cash_unfocus, 0, 0);
                radiouncardButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.newcard_unfocus, 0, 0);
                //  radioaccButton.setTextColor(Color.LTGRAY);
                if (f_total > 0) {
                    Intent payintent = new Intent(FarecalcAct.this, PayuncardAct.class);
                    Bundle bun = new Bundle();
                    bun.putString("info", "Uncard");
                    bun.putString("message", message);
                    if (!SessionSave.getSession("Lang", FarecalcAct.this).equals("en")) {
                        bun.putString("f_fare", Double.toString(f_fare));
                        bun.putString("f_tips", Double.toString(f_tips));
                        bun.putString("f_total", Double.toString(f_total));
                    } else {
                        bun.putString("f_fare", FontHelper.convertfromArabic(Double.toString(f_fare)));
                        bun.putString("f_tips", FontHelper.convertfromArabic(Double.toString(f_tips)));
                        bun.putString("f_total", FontHelper.convertfromArabic(Double.toString(f_total)));
                    }
                    payintent.putExtras(bun);
                    startActivity(payintent);
                }*/
            }
        });
        // setonclickListener();
    }


    /**
     * Common API for fareupdate the following method for arrange the inputs and calls the API.
     */
    private void callurl() {

        String url = "type=tripfare_update";
        try {
            JSONObject j = new JSONObject();
            j.put("trip_id", f_tripid);
            j.put("distance", f_distance);
            j.put("actual_distance", MainActivity.mMyStatus.getdistance());
            j.put("actual_amount", "" + f_total);
            j.put("trip_fare", f_tripfare);
            j.put("base_fare", base_fare);
            //Karthick Update
            j.put("promodiscount_amount", f_farediscount);
            j.put("fare", "" + f_payamt);
            j.put("tips", "" + tipsTxt.getText().toString());
            j.put("passenger_promo_discount", promotax);
            j.put("tax_amount", f_taxamount);
            j.put("remarks", "");
            j.put("nightfare_applicable", f_nightfareapplicable);
            j.put("nightfare", f_nightfare);
            j.put("eveningfare_applicable", f_eveningfare_applicable);
            j.put("eveningfare", f_eveningfare);
            j.put("waiting_time", f_waitingtime);
            j.put("waiting_cost", f_waitingcost);
            j.put("creditcard_no", "");
            j.put("creditcard_cvv", Cvv);
            j.put("company_tax", cmpTax);
            j.put("expmonth", "");
            j.put("expyear", "");
            j.put("pay_mod_id", f_paymodid);
            j.put("passenger_discount", p_dis);
            j.put("minutes_traveled", f_minutes_traveled);
            j.put("minutes_fare", f_minutes_fare);
            j.put("fare_calculation_type", fare_calculation_type);

            j.put("model_fare_type", SessionSave.getSession("model_fare_type", FarecalcAct.this));
            // System.out.println("durrrrrr" + url + "___" + j);

            if (f_paymodid.equals("2"))
                new Telerpayment(url, j);
            else
                new FareUpdate(url, j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * This class helps to call the Fare Update API,get the result and parse it.
     */
    private class FareUpdate implements APIResult {
        String msg = "";

        public FareUpdate(String url, JSONObject data) {

            if (isOnline()) {
                new APIService_Retrofit_JSON(FarecalcAct.this, this, data, false).execute(url);
            } else {
                alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_internet), "" + NC.getResources().getString(R.string.ok), "");
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);

                    if (json.getInt("status") == 1) {

                        SessionSave.saveSession("travel_status", "", FarecalcAct.this);
                        SessionSave.saveSession("trip_id", "", FarecalcAct.this);
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        WaitingTimerRun.ClearSession(FarecalcAct.this);
                       /* CommonData.mBlnLowBalanceShowStatus = false;
                        if (SessionSave.getSession("wallet_notification_status", FarecalcAct.this).equals("1")) {
                            CommonData.mBlnLowBalance = true;
                        } else {
                            CommonData.mBlnLowBalance = false;
                        }*/
                        MainActivity.mMyStatus.setdistance("");
                        msg = json.getString("message");
                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setStatus("F");
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("On");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        JSONObject jsonDriver = json.getJSONObject("driver_statistics");
                        SessionSave.saveSession("driver_statistics", "" + jsonDriver, FarecalcAct.this);
                        CommonData.hstravel_km = "";
                        WaitingTimerRun.sTimer = "00:00:00";
                        WaitingTimerRun.finalTime = 0L;
                        WaitingTimerRun.timeInMillies = 0L;
                        SessionSave.saveSession("waitingHr", "", FarecalcAct.this);
                        CommonData.travel_km = 0;
                        SessionSave.setGoogleDistance(0f, FarecalcAct.this);
                        SessionSave.setDistance(0f, FarecalcAct.this);
                        SessionSave.saveGoogleWaypoints(null, null, "", 0.0, "", FarecalcAct.this);
                        SessionSave.saveWaypoints(null, null, "", 0.0, "", FarecalcAct.this);
                        Intent jobintent = new Intent(FarecalcAct.this, JobdoneAct.class);
                        Bundle bun = new Bundle();
                        bun.putString("message", result);
                        jobintent.putExtras(bun);
                        startActivity(jobintent);
                        finish();
                    }
                    /*if (json.getInt("status") == 1) {
                        SessionSave.saveSession("travel_status", "", FarecalcAct.this);
                        SessionSave.saveSession("trip_id", "", FarecalcAct.this);
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        CommonData.mBlnLowBalanceShowStatus = false;
                        if(SessionSave.getSession("wallet_notification_status",FarecalcAct.this).equals("1"))
                        {
                            CommonData. mBlnLowBalance = true;
                        }
                        else
                        {
                            CommonData. mBlnLowBalance = false;
                        }

                        MainActivity.mMyStatus.setdistance("");
                        msg = json.getString("message");
                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setStatus("F");
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("On");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        SessionSave.setDistance(0f, FarecalcAct.this);
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        JSONObject jsonDriver = json.getJSONObject("driver_statistics");
                        SessionSave.saveSession("driver_statistics", "" + jsonDriver, FarecalcAct.this);
                        CommonData.hstravel_km = "";
                        WaitingTimerRun.sTimer = "00:00:00";
                        SessionSave.saveSession("waitingHr", "", FarecalcAct.this);
                        Intent jobintent = new Intent(FarecalcAct.this, MyStatus.class);
//                        Bundle bun = new Bundle();
//                        bun.putString("message", result);
//                        jobintent.putExtras(bun);
                        startActivity(jobintent);
                        finish();
                    }*/
                    else if (json.getInt("status") == -9) {
                        msg = json.getString("message");
                        lay_fare.setVisibility(View.VISIBLE);
                        alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                    } else if (json.getInt("status") == 0) {
                        msg = json.getString("message");
                        lay_fare.setVisibility(View.VISIBLE);
                        alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                    } else if (json.getInt("status") == -1) {
                        msg = json.getString("message");
                        alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                        SessionSave.saveSession("trip_id", "", FarecalcAct.this);
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        JSONObject jsonDriver = json.getJSONObject("driver_statistics");
                        SessionSave.saveSession("driver_statistics", "" + jsonDriver, FarecalcAct.this);
                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setStatus("F");
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("On");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        MainActivity.mMyStatus.setOndriverLatitude("");
                        MainActivity.mMyStatus.setOndriverLongitude("");
                        Intent intent = new Intent(FarecalcAct.this, MyStatus.class);
                        startActivity(intent);
                        finish();
                    } else {
                        msg = json.getString("message");
                        lay_fare.setVisibility(View.VISIBLE);
                        alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (Exception e) {
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(FarecalcAct.this, getString(R.string.server_error));
                    }
                });
                lay_fare.setVisibility(View.VISIBLE);
            }
        }


    }

    /***********************************************************/


    private class Telerpayment implements APIResult {
        String msg = "";

        public Telerpayment(String url, JSONObject data) {

            if (isOnline()) {
                new APIService_Retrofit_JSON(FarecalcAct.this, this, data, false).execute(url);
            } else {
                alert_view(FarecalcAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_internet), "" + NC.getResources().getString(R.string.ok), "");
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            if (isSuccess) {

                try {
                    alert_view(FarecalcAct.this, "" + "", "" +
                            "Trip payment process going on in passenger side, Please wait!", "" +
                            NC.getResources().getString(R.string.ok), "");

                  /*  Intent int_obj = new Intent(FarecalcAct.this, Waitingforpayment.class);
                    startActivity(int_obj);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(FarecalcAct.this, getString(R.string.server_error));
                    }
                });
                lay_fare.setVisibility(View.VISIBLE);
            }
        }


    }

    /**
     * completeStreetTrip API response parsing.
     */

    private void completeStreetTrip() {
        CoreClient client = new ServiceGenerator(FarecalcAct.this).createService(CoreClient.class);
        ApiRequestData.StreetPickComplete request = new ApiRequestData.StreetPickComplete();
        request.pay_mod_id = "1";
        request.trip_fare = f_tripfare;
       /* public String eveningfare;
        public String eveningfare_applicable;
        public String waiting_cost;
        public String fare;
        public String minutes_traveled;
        public String remarks;
        public String actual_amount;
        public String trip_id;
        public String distance;
        public String base_fare;
        public String company_tax;
        public String actual_distance;
        public String tax_amount;
        public String minutes_fare;
        public String nightfare;
        public String tips;
        public String nightfare_applicable;
        public String waiting_time;*/

        request.eveningfare_applicable = f_eveningfare_applicable;
        request.eveningfare = f_eveningfare;
        request.waiting_cost = f_waitingcost;
        request.fare = String.valueOf(f_fare);
        request.minutes_traveled = f_minutes_traveled;
        request.remarks = "";
        request.actual_amount = String.valueOf(f_total);
        request.trip_id = f_tripid;
        request.distance = f_distance;
        request.base_fare = base_fare;
        request.company_tax = cmpTax;
        request.actual_distance = MainActivity.mMyStatus.getdistance();
        request.tax_amount = f_taxamount;
        request.minutes_fare = f_minutes_fare;
        request.nightfare = f_nightfare;
        request.tips = tipsTxt.getText().toString();
        request.nightfare_applicable = f_nightfareapplicable;
        request.waiting_time = f_waitingtime;
        SessionSave.saveSession("status", "F", FarecalcAct.this);

        Call<StreetCompleteResponse> response = client.completeStreetPickUpdate(ServiceGenerator.COMPANY_KEY, request, "en", ServiceGenerator.DYNAMIC_AUTH_KEY);
        showDialog();
        response.enqueue(new Callback<StreetCompleteResponse>() {
            @Override
            public void onResponse(Call<StreetCompleteResponse> call, Response<StreetCompleteResponse> response) {
                closeDialog();
                StreetCompleteResponse data = response.body();
                if (data != null) {
                    String msg = data.message;
                    if (data.status.trim().equals("1")) {
                        SessionSave.saveSession("travel_status", "", FarecalcAct.this);
                        SessionSave.saveSession("trip_id", "", FarecalcAct.this);
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        MainActivity.mMyStatus.setdistance("");
                        SessionSave.saveSession("street_completed", "", FarecalcAct.this);


                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setStatus("F");
                        SessionSave.saveSession("status", "F", FarecalcAct.this);
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnstatus("On");
                        MainActivity.mMyStatus.setOnstatus("Complete");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
//                    data.detail
//                    JSONObject jsonDriver = json.getJSONObject("driver_statistics");
//                    SessionSave.saveSession("driver_statistics", "" + jsonDriver, FarecalcAct.this);

                        //{"message":"Trip Fare Updated Successfully","detail":{"fare":12.94,"pickup":"333, Marudhamalai Road, Mullai Nagar, PN Pudur, Coimbatore, Tamil Nadu 641041","trip_id":"6173"},"status":1}


                        CommonData.hstravel_km = "";
                        WaitingTimerRun.sTimer = "00:00:00";
                        SessionSave.saveSession("waitingHr", "", FarecalcAct.this);
                        Intent jobintent = new Intent(FarecalcAct.this, JobdoneAct.class);
                        Bundle bun = new Bundle();
                        Gson gson = new GsonBuilder().create();
                        String result = gson.toJson(data);
                        bun.putString("message", result);
                        jobintent.putExtras(bun);
                        startActivity(jobintent);
                        finish();


                    } else {
                        Toast.makeText(FarecalcAct.this, msg, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FarecalcAct.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }

//                if (json.getInt("status") == 1) {
//                    SessionSave.saveSession("travel_status", "", FarecalcAct.this);
//                    SessionSave.saveSession("trip_id", "", FarecalcAct.this);
//                    SessionSave.saveSession("status", "F", FarecalcAct.this);
//                    MainActivity.mMyStatus.setdistance("");
//                    msg = json.getString("message");
//                    MainActivity.mMyStatus.setOnstatus("");
//                    MainActivity.mMyStatus.setStatus("F");
//                    SessionSave.saveSession("status", "F", FarecalcAct.this);
//                    MainActivity.mMyStatus.setOnPassengerImage("");
//                    MainActivity.mMyStatus.setOnstatus("On");
//                    MainActivity.mMyStatus.setOnstatus("Completed");
//                    MainActivity.mMyStatus.setOnpassengerName("");
//                    MainActivity.mMyStatus.setOndropLocation("");
//                    MainActivity.mMyStatus.setOndropLocation("");
//                    MainActivity.mMyStatus.setOnpickupLatitude("");
//                    MainActivity.mMyStatus.setOnpickupLongitude("");
//                    MainActivity.mMyStatus.setOndropLatitude("");
//                    MainActivity.mMyStatus.setOndropLongitude("");
//                    JSONObject jsonDriver = json.getJSONObject("driver_statistics");
//                    SessionSave.saveSession("driver_statistics", "" + jsonDriver, FarecalcAct.this);
//                    CommonData.hstravel_km = "";
//                    WaitingTimerRun.sTimer = "00:00:00";
//                    SessionSave.saveSession("waitingHr", "", FarecalcAct.this);
//                    Intent jobintent = new Intent(FarecalcAct.this, JobdoneAct.class);
//                    Bundle bun = new Bundle();
//                    bun.putString("message", result);
//                    jobintent.putExtras(bun);
//                    startActivity(jobintent);
//                    finish();
//                }

            }

            @Override
            public void onFailure(Call<StreetCompleteResponse> call, Throwable t) {
                closeDialog();
            }
        });
    }

    /**
     * Closing the alert dialog.
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
     * Showing the alert dialog
     */
    public void showDialog() {
        try {
            if (NetworkStatus.isOnline(FarecalcAct.this)) {
                View view = View.inflate(FarecalcAct.this, R.layout.progress_bar, null);
                mDialog = new Dialog(FarecalcAct.this, R.style.dialogwinddow);
                mDialog.setContentView(view);
                mDialog.setCancelable(false);
                mDialog.show();

                ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(FarecalcAct.this)
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);

            } else {

            }
        } catch (Exception e) {

        }

    }

    /**
     * CompleteTrip API response parsing.
     */
    private class CompleteTrip implements APIResult {
        public CompleteTrip(String url, String latitude, String longitude, String distance, String waitingHr, String drop_location) {

            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
//                nameValuePairs.add(new BasicNameValuePair("trip_id", SessionSave.getSession("trip_id", FarecalcAct.this)));
//                nameValuePairs.add(new BasicNameValuePair("drop_latitude", latitude));
//                nameValuePairs.add(new BasicNameValuePair("drop_longitude", longitude));
//                nameValuePairs.add(new BasicNameValuePair("drop_location", Uri.encode(drop_location)));
//                nameValuePairs.add(new BasicNameValuePair("distance", distance));
//                nameValuePairs.add(new BasicNameValuePair("actual_distance", ""));
//                nameValuePairs.add(new BasicNameValuePair("waiting_hour", waitingHr));
                JSONObject j = new JSONObject();
                j.put("trip_id", SessionSave.getSession("trip_id", FarecalcAct.this));
                j.put("drop_latitude", latitude);
                j.put("drop_longitude", longitude);
                j.put("drop_location", drop_location);
                j.put("distance", distance);
                j.put("actual_distance", SessionSave.getDistance(FarecalcAct.this));
                j.put("waiting_hour", waitingHr);
                j.putOpt("location_history", SessionSave.getSession("locationhistory", getApplicationContext()));
                new APIService_Retrofit_JSON(FarecalcAct.this, this, j, false).execute(url);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                SessionSave.saveSession("locationhistory", "", getApplicationContext());
                SessionSave.setDistance((float) 0.0, getApplicationContext());
                try {
                    message = result;
                    setFareCalculatorScreen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
            return;
        } else {
            doubleBackToExitPressedOnce = false;
            Toast.makeText(this, "Without payment completion.You Cannot Exit this page", Toast.LENGTH_SHORT).show();

        }

    }
}
