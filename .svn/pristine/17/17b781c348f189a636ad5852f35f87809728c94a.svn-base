package com.cabi.driver;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.DatePicker_CardExpiry;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.FourDigitCardFormatWatcher;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 *  This class  is used to complete the payment by new card
 */
public class PayuncardAct extends MainActivity implements OnClickListener, DatePicker_CardExpiry.DialogInterface {
    private EditText cardEdt;
    private TextView expireEdt;
    private TextView CancelBtn;
    private TextView DoneBtn;
    private EditText cvvEdt;
    private TextView HeadTitle;
    private String message;
    private String f_tripid;
    private String f_distance;
    private String f_nightfareapplicable;
    private String f_nightfare;
    private String f_eveningfare_applicable;
    private String f_eveningfare;
    private String f_passengerdiscount;
    private String f_waitingtime;
    private String f_waitingcost;
    private String f_taxamount;
    private String f_tripfare;
    private String f_total;
    private String f_fare;
    private String f_tips;
    private String creditcard_no;
    private String creditcard_cvv;
    private String expmonth;
    private String expyear;
    private String expdate;
    private int mMonth;
    private int mDay;
    private int mYear;
    private static final int DATE_DIALOG_ID = 0;
    private String f_paymodid = "3";
    private String group_id;
    private String account_id;
    private String info;
    private String f_minutes_traveled;
    private String f_minutes_fare;
    private DatePicker_CardExpiry editNameDialog;

    private String company_tax;
    private String base_fare;
    private String promodiscount_amount;
    private String fare_calculation_type;


    /**
     *  set layout to the activity
     */
    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        setLocale();
        return R.layout.payuncard_lay;
    }


    /**
     *  Initializing UI Components
     */
    @SuppressLint("NewApi")
    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        Bundle bun = getIntent().getExtras();
        CommonData.current_act = "PayuncardAct";
        FontHelper.applyFont(this, findViewById(R.id.id_paylay));

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) PayuncardAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), PayuncardAct.this);

        if (bun != null) {
            HeadTitle = (TextView) findViewById(R.id.signup_title);
            cardEdt = (EditText) findViewById(R.id.cardEdt);
            cardEdt.addTextChangedListener(new FourDigitCardFormatWatcher());
            expireEdt = (TextView) findViewById(R.id.expireEdt);
            cvvEdt = (EditText) findViewById(R.id.cvvEdt);
            //CancelBtn = (TextView) findViewById(R.id.cancelBtn);
            DoneBtn = (TextView) findViewById(R.id.doneBtn);
            //DoneBtn.setBackground(getResources().getDrawable(R.drawable.draw_timer_bg));
            HeadTitle.setText("" + NC.getResources().getString(R.string.heading_payuncard));
            info = bun.getString("info");
            if (info.equals("Account")) {
                message = bun.getString("message");
                f_total = bun.getString("f_total");
                f_fare = bun.getString("f_fare");
                f_tips = bun.getString("f_tips");
                group_id = bun.getString("gid");
                account_id = bun.getString("aid");
             //   Log.i("f_total", "" + f_total);
             //   Log.i("f_fare", "" + f_fare);
              //  Log.i("f_tips", "" + f_tips);
              //  Log.i("group", "" + group_id);
               // Log.i("account", "" + account_id);
            } else if (info.equals("Uncard")) {
                message = bun.getString("message");
                f_total = bun.getString("f_total");
                f_fare = bun.getString("f_fare");
                f_tips = bun.getString("f_tips");
             /*   Log.i("f_total", "" + f_total);
                Log.i("f_fare", "" + f_fare);
                Log.i("f_tips", "" + f_tips);*/
            }
            //  CancelBtn.setText("" + NC.getResources().getString(R.string.back));
            DoneBtn.setText("" + NC.getResources().getString(R.string.done));
            Calendar cal = Calendar.getInstance();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            setonclickListener();
            try {
              //  Log.i("message", "" + message);
                JSONObject detail = new JSONObject(message);
                JSONObject json = detail.getJSONObject("detail");
                f_tripid = json.getString("trip_id");
                f_distance = json.getString("distance");
                f_nightfareapplicable = json.getString("nightfare_applicable");
                f_nightfare = json.getString("nightfare");
                f_passengerdiscount = json.getString("passenger_discount");
                f_waitingtime = json.getString("waiting_time");
                f_waitingcost = json.getString("waiting_cost");
                f_taxamount = json.getString("tax_amount");
                f_tripfare = json.getString("trip_fare");
                f_eveningfare_applicable = json.getString("eveningfare_applicable");
                f_eveningfare = json.getString("eveningfare");
                f_minutes_traveled = json.getString("minutes_traveled");
                f_minutes_fare = json.getString("minutes_fare");
                company_tax=json.getString("company_tax");
                base_fare=json.getString("base_fare");
                promodiscount_amount=json.getString("promodiscount_amount");
                fare_calculation_type=json.getString("fare_calculation_type");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     *  Setting on click listeners
     */
    private void setonclickListener() {

        //  CancelBtn.setOnClickListener(this);
        DoneBtn.setOnClickListener(this);
        expireEdt.setOnClickListener(this);
    }



    /**
     *  Donebtn onclick
     */
    @Override
    public void onClick(View v) {

        try {
            Calendar cl = Calendar.getInstance();
            int cl_month = (cl.get(Calendar.MONTH)) + 1;
            int cl_year = cl.get(Calendar.YEAR);
            if (v == DoneBtn) {
                creditcard_no = cardEdt.getText().toString().trim().replaceAll("\\s", "");
                creditcard_cvv = cvvEdt.getText().toString().trim();
                expdate = expireEdt.getText().toString().trim();
                if (creditcard_no.length() == 0) {
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_ccard_no), "" + NC.getResources().getString(R.string.ok), "");
                } else if (creditcard_no.length() < 9) {
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_chk_ccard), "" + NC.getResources().getString(R.string.ok), "");
                } else if (creditcard_no.length() > 16) {
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_chk_ccard), "" + NC.getResources().getString(R.string.ok), "");
                } else if (expdate.length() == 0) {
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_exp_date), "" + NC.getResources().getString(R.string.ok), "");
                }
//                else if (mMonth < cl_month) {
//                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_exp_month_gt), "" + NC.getResources().getString(R.string.ok), "");
//                }
                else if (mYear < cl_year) {
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_exp_year_gt), "" + NC.getResources().getString(R.string.ok), "");
                } else if (creditcard_cvv.length() == 0) {
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_cvv), "" + NC.getResources().getString(R.string.ok), "");
                } else if (creditcard_cvv.length() < 3) {
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.ent_chk_cvv), "" + NC.getResources().getString(R.string.ok), "");
                } else if (creditcard_cvv.length() > 4) {
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), NC.getResources().getString(R.string.ent_chk_cvv), "" + NC.getResources().getString(R.string.ok), "");
                } else {
                    expmonth = Integer.toString(mMonth);
                    if (expmonth.length() == 1) {
                        expmonth = "0" + expmonth;
                    }
                    expyear = Integer.toString(mYear);
                    callurl();
                }
            } else if (v == CancelBtn) {
                finish();
            } else if (v == expireEdt) {
                FragmentManager fm = getSupportFragmentManager();
                editNameDialog = new DatePicker_CardExpiry();

                editNameDialog.show(fm, "fragment_edit_name");

                // showDialog(DATE_DIALOG_ID,null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }



    /**
     * Updating trip fare and response parsing
     */
    private void callurl() {

        try {
            String url = "type=tripfare_update";
            JSONObject j = new JSONObject();
            j.put("trip_id", f_tripid);
            j.put("distance", f_distance);
            j.put("actual_distance", MainActivity.mMyStatus.getdistance());
            j.put("actual_amount", "" + f_total);
            j.put("trip_fare", f_tripfare);
            j.put("fare", "" + f_fare);
            j.put("tips", "" + f_tips);
            j.put("passenger_promo_discount", f_passengerdiscount);
            j.put("tax_amount", f_taxamount);
            j.put("remarks", "");
            j.put("nightfare_applicable", f_nightfareapplicable);
            j.put("nightfare", f_nightfare);
            j.put("eveningfare_applicable", f_eveningfare_applicable);
            j.put("eveningfare", f_eveningfare);
            j.put("waiting_time", f_waitingtime);
            j.put("waiting_cost", f_waitingcost);
            j.put("creditcard_no", creditcard_no);
            j.put("creditcard_cvv", creditcard_cvv);
            j.put("expmonth", "" + expmonth);
            j.put("expyear", "" + expyear);
            j.put("pay_mod_id", f_paymodid);
            j.put("passenger_discount", "");
            j.put("minutes_traveled", f_minutes_traveled);
            j.put("minutes_fare", f_minutes_fare);

            j.put("company_tax", company_tax);
            j.put("base_fare", base_fare);
            j.put("promodiscount_amount", promodiscount_amount);
            j.put("fare_calculation_type",fare_calculation_type);
            new FareUpdate(url, j);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onSuccess(int monthOfYear, int year) {
        mYear = year;
        mMonth = monthOfYear + 1;
        mDay = 2;
        editNameDialog.dismiss();
        expireEdt.setText(new StringBuilder().append(mMonth).append("/").append(mYear).append(" "));
        //updateDate();

    }

    @Override
    public void failure(String inputText) {
        editNameDialog.dismiss();
    }



    /**
     * FareUpdate API response parsing.
     */
    private class FareUpdate implements APIResult {
        String msg = "";

        public FareUpdate(String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(PayuncardAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        JSONObject jsonDriver = json.getJSONObject("driver_statistics");
                        SessionSave.saveSession("driver_statistics", "" + jsonDriver, PayuncardAct.this);
                        String history_Url = "type=driver_location_history";

                        //updating trip history

                        new HistoryUpdate(history_Url);

                        msg = json.getString("message");
                        Intent jobintent = new Intent(PayuncardAct.this, JobdoneAct.class);
                        Bundle bun = new Bundle();
                        bun.putString("message", result);
                        jobintent.putExtras(bun);
                        startActivity(jobintent);
                    } else {
                        msg = json.getString("message");
                    }
                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                } else {
//                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(PayuncardAct.this, getString(R.string.server_error));
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TripHistoryUpdate API response parsing.
     */
    private class HistoryUpdate implements APIResult {
        public HistoryUpdate(String url) {
            // TODO Auto-generated constructor stub
            try {
                String historyWaypoints = MainActivity.mMyStatus.getOnwaypoints();
                if (historyWaypoints.length() > 0 && historyWaypoints.charAt(historyWaypoints.length() - 1) == '|') {
                    historyWaypoints = historyWaypoints.substring(0, historyWaypoints.length() - 1);
                }
                JSONObject j = new JSONObject();
                j.put("driver_id", SessionSave.getSession("Id", PayuncardAct.this));
                j.put("trip_id", SessionSave.getSession("trip_id", PayuncardAct.this));
                j.put("locations", historyWaypoints);
                j.put("status", "A");
                new APIService_Retrofit_JSON(PayuncardAct.this, this, j, false).execute(url);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    MainActivity.mMyStatus.setOnwaypoints("");
                    MainActivity.mMyStatus.settripId("");
                    SessionSave.saveSession("trip_id", "", PayuncardAct.this);
                    MainActivity.mMyStatus.setOnwaypoints("");
                    MainActivity.mMyStatus.setStatus("F");
                    SessionSave.saveSession("status", "F", PayuncardAct.this);
                    MainActivity.mMyStatus.setOnstatus("flagger");
                    MainActivity.mMyStatus.setOnPassengerImage("");
                    MainActivity.mMyStatus.setOnpassengerName("");
                    MainActivity.mMyStatus.setOndropLocation("");
                    MainActivity.mMyStatus.setOnpickupLatitude("");
                    MainActivity.mMyStatus.setOnpickupLongitude("");
                    MainActivity.mMyStatus.setOndropLatitude("");
                    MainActivity.mMyStatus.setOndropLongitude("");
                    MainActivity.mMyStatus.setOndriverLatitude("");
                    MainActivity.mMyStatus.setOndriverLongitude("");
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(PayuncardAct.this, getString(R.string.server_error));
                        }
                    });
//                    alert_view(PayuncardAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * Date picker
     */
    private DatePickerDialog.OnDateSetListener mDateSetListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            expireEdt.setText(new StringBuilder().append(mMonth).append("/").append(mYear).append(" "));
            // updateDate();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
            /*
             * return new DatePickerDialog(this, mDateSetListner, mYear, mMonth, mDay);
			 */
                DatePickerDialog datePickerDialog = this.customDatePicker();
                return datePickerDialog;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
//    protected void updateDate() {
//
//        int localMonth = mMonth;
//        String monthString = localMonth < 10 ? "0" + localMonth : Integer.toString(localMonth);
//        String localYear = Integer.toString(mYear).substring(2);
//        Integer.toString(mDay);
//        Calendar cal = Calendar.getInstance();
//        int curMonth = cal.get(Calendar.MONTH) + 1;
//        int curYear = cal.get(Calendar.YEAR);
//        System.out.println("nagnagnaag" + mYear + "--" + mMonth + "--" + curMonth + "--" + curYear);
//        if (mYear < curYear) {
//            expireEdt.setText(new StringBuilder().append(curMonth).append("/").append(curYear).append(" "));
//            showDialog(DATE_DIALOG_ID);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    expireEdt.callOnClick();
//                    Toast.makeText(PayuncardAct.this, NC.getResources().getString(R.string.enter_the_valid_expiry_date), Toast.LENGTH_LONG).show();
//                }
//            }, 100);
//
//
//        } else if (mYear == curYear && mMonth < curMonth) {
//            expireEdt.setText(new StringBuilder().append(curMonth).append("/").append(curYear).append(" "));
//            showDialog(DATE_DIALOG_ID);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    expireEdt.callOnClick();
//                    Toast.makeText(PayuncardAct.this, NC.getResources().getString(R.string.enter_the_valid_expiry_date), Toast.LENGTH_LONG).show();
//                }
//            }, 100);
//
//
//        } else {
//            expireEdt.setText(new StringBuilder().append(monthString).append("/").append(localYear).append(" "));
//            // showDialog(DATE_DIALOG_ID);
//        }
//    }

    /**
     * Date picker custom dialog
     */
    private DatePickerDialog customDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListner, mYear, mMonth, mDay);
        try {
            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
             //   Log.i("datepicker name", "" + datePickerDialogField.getName());
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }
}
