package com.cabi.driver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cabi.driver.adapter.Withdraw_history_adapter;
import com.cabi.driver.adapter.Withdraw_referalhistory_adapter;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.utils.CL;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by developer on 5/10/16.
 */


/**
 * This class is used to view withdraw history for both referal and trip
 */
public class WithdrawHistoryAct extends MainActivity {


    private ArrayList<HashMap<String, String>> data;
    private ListView list;
    private TextView back_home,btn_referal,btn_trip,btn_back;
    private ImageView btn_filter;

    private TextView txt_amt,txt_date,txt_status,txt_view;

    private TextView txt_fromdate,txt_todate;

    Spinner Statusspn;
    private int viewType = 1;

    private int _hour = 0;
    private int _min = 0;
    private int _date = 0;
    private int _month = 0;
    private int _year = 0;
    private String _ampm = "AM";

    private String fromtime =" 00:00";
    private String totime =" 23:59";

    int temp_status =0;

    private ArrayAdapter<String> status_adapter;

     List<String> statusList = new ArrayList<String>();
    private Dialog dt_mDialog;
    private LinearLayout no_data_txt;
    RelativeLayout lay_list;
    private View trip_underline;
    private View referl_underline;

    @Override
    public int setLayout() {
        return R.layout.listview;
    }


    /**
     * Initialize the views on layout
     */
    @Override
    public void Initialize() {
        list = (ListView) findViewById(R.id.listView);
        back_home=(TextView)findViewById(R.id.back_home);
        btn_referal=(TextView)findViewById(R.id.btnreferal);
        btn_trip=(TextView)findViewById(R.id.btntrip);
        btn_back=(TextView)findViewById(R.id.slideImg);
        txt_view=(TextView)findViewById(R.id.view);
        trip_underline=findViewById(R.id.trip_underline);
        referl_underline=findViewById(R.id.referl_underline);
        txt_amt=(TextView)findViewById(R.id.request_amounth);
        txt_date=(TextView)findViewById(R.id.request_taxih);
        txt_status=(TextView)findViewById(R.id.statush);
        btn_filter=(ImageView)findViewById(R.id.filter);
        no_data_txt=(LinearLayout)findViewById(R.id.no_data);
        lay_list=(RelativeLayout)findViewById(R.id.lay_list);


        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) WithdrawHistoryAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), WithdrawHistoryAct.this);

        ImageView iv= (ImageView)findViewById(R.id.progress_history);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(WithdrawHistoryAct.this)
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        //default refer list will be called
        SetWithdrawList();

        btn_filter.setVisibility(View.INVISIBLE);

       /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        Button button = new Button(this);
        button.setLayoutParams(params);*/

        btn_referal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewType = 1;
                if(data != null)
                    data.clear();
                list.setAdapter(null);
                btn_filter.setVisibility(View.INVISIBLE);
                SetWithdrawList();
            }
        });

        btn_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewType = 2;
                if(data != null)
                    data.clear();
                list.setAdapter(null);
                btn_filter.setVisibility(View.VISIBLE);
                SetWithdrawList();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WithdrawHistoryAct.this, MyStatus.class));
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withDrawFilter();

            }
        });

    }

    /**
     * showing Withdraw filter popup
     */
    public void withDrawFilter() {
        try {

            final View view = View.inflate(WithdrawHistoryAct.this, R.layout.withdrawfilter, null);
            final Dialog mcancelDialog = new Dialog(WithdrawHistoryAct.this, R.style.dialogwinddow);
            mcancelDialog.setContentView(view);
            mcancelDialog.setCancelable(true);
            mcancelDialog.show();
            Colorchange.ChangeColor((ViewGroup) mcancelDialog.findViewById(R.id.alert_id), WithdrawHistoryAct.this);
            FontHelper.applyFont(WithdrawHistoryAct.this, mcancelDialog.findViewById(R.id.alert_id));

             txt_fromdate = (TextView)view.findViewById(R.id.fromdateTxt);
             txt_todate = (TextView)view.findViewById(R.id.todateTxt);
            // txt_status = (TextView)view.findViewById(R.id.statusTxt);
             Statusspn = (Spinner)view.findViewById(R.id.statusspn);


            ArrayAdapter adapter =  ArrayAdapter.createFromResource(
                   this, R.array.Status, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            Statusspn.setAdapter(adapter);
            txt_fromdate.setText("yyyy-mm-dd");
            txt_todate.setText("yyyy-mm-dd");

            txt_fromdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pickdate(1);
                }
            });

            txt_todate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pickdate(2);
                }
            });



            Statusspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    temp_status = parent.getSelectedItemPosition();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });

            final Button button_success = (Button) mcancelDialog.findViewById(R.id.okbtn);
            final Button button_cancel = (Button) mcancelDialog.findViewById(R.id.cancelbtn);

            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    if((!txt_fromdate.getText().toString().isEmpty()) && (!txt_todate.getText().toString().isEmpty()))
                    {
                        lay_list.setVisibility(View.GONE);
                        list.setAdapter(null);
                        mcancelDialog.dismiss();
                        new FilterApi(txt_fromdate.getText().toString(),txt_todate.getText().toString(),temp_status);
                    }
                    else
                    {
                        alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.please_choosedate), "" + NC.getResources().getString(R.string.ok), "");
                    }

                }
            });

            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    mcancelDialog.dismiss();


                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        viewType = 1;

        if(data != null)
            data.clear();
        btn_filter.setVisibility(View.INVISIBLE);
        SetWithdrawList();
    }

    /**
     * Pick date selection
     * Type:1(fromdate)
     * Type:2(todate)
     */
    private void Pickdate(final int type) {
        final Context context =this;
        try {
            final View r_view = View.inflate(context, R.layout.date_time_picker_dialog, null);

            FontHelper.applyFont(context, r_view.findViewById(R.id.inner_content));
            dt_mDialog = new Dialog(context, R.style.dialogwinddow);
            dt_mDialog.setContentView(r_view);
            dt_mDialog.setCancelable(true);
            dt_mDialog.show();
            Colorchange.ChangeColor((ViewGroup) dt_mDialog.findViewById(R.id.inner_content), WithdrawHistoryAct.this);
            final DatePicker _datePicker = (DatePicker) dt_mDialog.findViewById(R.id.datePicker1);
            final TimePicker _timePicker = (TimePicker) dt_mDialog.findViewById(R.id.timePicker1);
            _timePicker.setVisibility(View.GONE);
            FontHelper.overrideFonts(context, _datePicker);
            FontHelper.overrideFonts(context, _timePicker);
            Calendar c = Calendar.getInstance();
            _timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY) + 1);
            _timePicker.setCurrentMinute(c.get(Calendar.MINUTE) + 1);
            _timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    // TODO Auto-generated method stub
//                            View v=FontHelper.applyFont(context,view,0);
//                            FontHelper.applyFont(context,view);
                  /*  Calendar c = Calendar.getInstance();
                    if (_datePicker.getDayOfMonth() == c.get(Calendar.DAY_OF_MONTH)) {
                        if ((c.get(Calendar.HOUR_OF_DAY) + 1) > hourOfDay) {
                            _timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY) + 1);
                            _timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
                        }
                        if ((c.get(Calendar.HOUR_OF_DAY) + 1) >= hourOfDay && (c.get(Calendar.MINUTE)) > minute) {
                            _timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY) + 1);
                            _timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
                        }
                    }*/
                }
            });
            Time now = new Time();
           // now.setToNow();
          //  _datePicker.updateDate(now.year, now.month, now.monthDay);
           // _datePicker.setMinDate(c.getTimeInMillis() - 1000);
            Button butConfirmTime = (Button) dt_mDialog.findViewById(R.id.butConfirmTime);
            butConfirmTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getCurrentDateAndTime(_timePicker, _datePicker);
                    String seletecedString = "" + _year + "-" + _month + "-" + _date ;

                    if(type == 1) {
                     //   fromtime = String.valueOf(_hour) + String.valueOf(_min);
                        txt_fromdate.setText(seletecedString);
                    }
                    else {
//                        totime = String.valueOf(_hour) + String.valueOf(_min);
                        txt_todate.setText(seletecedString);
                    }

                    dt_mDialog.dismiss();
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    /**
     * Getting current date and time from time picker
     */
    public void getCurrentDateAndTime(TimePicker _timePicker, DatePicker _datePicker) {

        _hour = _timePicker.getCurrentHour();
        _min = _timePicker.getCurrentMinute();
        _date = _datePicker.getDayOfMonth();
        _month = _datePicker.getMonth() + 1;
        _year = _datePicker.getYear();
        ampmValidation(_hour);
    }



    /**
     * Am and Pm Validation
     */
    private String ampmValidation(int inputHour) {

        if (inputHour >= 13) {
            _hour = inputHour - 12;
            _ampm = "PM";
        } else if (inputHour == 12) {
            _ampm = "PM";
        } else if (inputHour == 0) {
            _hour = 12;
            _ampm = "AM";
        }
        return _ampm;
    }


    /**
     * This method will be called from referal on click and trip onclick listener
     * viewType - 1(show referal history)
     *  viewType - 2(show trip history)
     */
    public void SetWithdrawList()
    {
        if(viewType == 1)
        {
           // btn_referal.setBackgroundResource(R.drawable.book_select);
            btn_referal.setTextColor(CL.getResources().getColor(R.color.button_accept));

            referl_underline.setBackgroundColor(CL.getResources().getColor(R.color.button_accept));
            trip_underline.setBackgroundColor(CL.getResources().getColor(R.color.linebottom_light));
       //     btn_trip.setBackgroundResource(R.drawable.book_unselect);
            btn_trip.setTextColor(CL.getResources().getColor(R.color.hintcolor));

            txt_view.setVisibility(View.GONE);

            new ReferalApi();
        }
        else
        {
           // btn_referal.setBackgroundResource(R.drawable.book_unselect);
            btn_referal.setTextColor(CL.getResources().getColor(R.color.hintcolor));
          //  btn_trip.setBackgroundResource(R.drawable.book_select);
            btn_trip.setTextColor(CL.getResources().getColor(R.color.button_accept));
            referl_underline.setBackgroundColor(CL.getResources().getColor(R.color.linebottom_light));
            trip_underline.setBackgroundColor(CL.getResources().getColor(R.color.button_accept));
            txt_view.setVisibility(View.VISIBLE);

            new requestingApi();
        }
    }

    /**
     * Filter withdrawlist API response parsing.
     */
    public class FilterApi implements APIResult {
        public FilterApi(String fromdate,String todate,int status) {

            try {
                JSONObject j = new JSONObject();
                j.put("driver_id",SessionSave.getSession("Id", WithdrawHistoryAct.this));
                if(fromdate.contains("yyyy-mm-dd"))
                    j.put("from","");
                else
                j.put("from",fromdate +fromtime);
                if(todate.contains("yyyy-mm-dd"))
                    j.put("to","");
                else
                j.put("to",todate +totime);
                j.put("status",status);


             //   Log.e("JsonValue",j.toString());

                String driverTripRequesting = "type=search_driver_withdraw_list";
                if (isOnline()) {
                    new APIService_Retrofit_JSON(WithdrawHistoryAct.this, this, j, false).execute(driverTripRequesting);
                } else {
                    alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            try {
              //  Log.e("_Result_",result);

                //Toast.makeText(WithdrawHistoryAct.this, result, Toast.LENGTH_SHORT).show();
                if (isSuccess) {
                    data = new ArrayList<HashMap<String, String>>();
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {
                        JSONArray jobject = object.getJSONArray("details");
                        for (int i = 0; i < jobject.length(); i++) {
                            JSONObject jo = jobject.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<String, String>();
//                            "wallet_request_id": "1",
//                                    "wallet_request_amount": "150.00",
//                                    "status": "Approved",
//                                    "wallet_request_date": "2016-05-06 18:16:33"
                            /*hm.put("wallet_request_id", jo.getString("wallet_request_id"));
                            hm.put("wallet_request_amount", jo.getString("wallet_request_amount"));
                            hm.put("status", jo.getString("status"));
                            hm.put("wallet_request_date", jo.getString("wallet_request_date"));*/

                            hm.put("wallet_request_id", jo.getString("withdraw_request_id"));
                            hm.put("wallet_request_amount", jo.getString("withdraw_amount"));
                            hm.put("status", jo.getString("request_status"));
                            hm.put("wallet_request_date", jo.getString("request_date"));

                            data.add(hm);


                        }
                   //     System.out.println("_______ddd"+data.size());
                        list.setAdapter(new Withdraw_history_adapter(WithdrawHistoryAct.this, data, viewType));
                        if(data.size()<=0){
                            no_data_txt.setVisibility(View.VISIBLE);
                            lay_list.setVisibility(View.GONE);
                            findViewById(R.id.progress_history).setVisibility(View.GONE);
                        }
                        else{
                            no_data_txt.setVisibility(View.GONE);
                            lay_list.setVisibility(View.VISIBLE);
                            findViewById(R.id.progress_history).setVisibility(View.GONE);
                           // findViewById(R.id.no_data).setVisibility(View.GONE);
                        }
                    } else {
                        no_data_txt.setVisibility(View.VISIBLE);
                        lay_list.setVisibility(View.GONE);
//                        findViewById(R.id.no_data_txt).setVisibility(View.VISIBLE);
                        findViewById(R.id.progress_history).setVisibility(View.GONE);
                    }
//                    else {
//                      //  alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                    }
                } else {
                    // check net connection
//                    alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                        }
                    });
                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    }

    /**
     * Trip Withdraw history API response parsing.
     */
    public class requestingApi implements APIResult {
        public requestingApi() {

            try {
                JSONObject j = new JSONObject();
                j.put("driver_id",SessionSave.getSession("Id", WithdrawHistoryAct.this) );

                String driverTripRequesting = "type=driver_withdraw_list";
                if (isOnline()) {
                    new APIService_Retrofit_JSON(WithdrawHistoryAct.this, this, j, false).execute(driverTripRequesting);
                } else {
                    alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            try {
                //Toast.makeText(WithdrawHistoryAct.this, result, Toast.LENGTH_SHORT).show();
                if (isSuccess) {
                    data = new ArrayList<HashMap<String, String>>();
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {
                        JSONArray jobject = object.getJSONArray("details");
                        for (int i = 0; i < jobject.length(); i++) {
                            JSONObject jo = jobject.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<String, String>();
//                            "wallet_request_id": "1",
//                                    "wallet_request_amount": "150.00",
//                                    "status": "Approved",
//                                    "wallet_request_date": "2016-05-06 18:16:33"
                            /*hm.put("wallet_request_id", jo.getString("wallet_request_id"));
                            hm.put("wallet_request_amount", jo.getString("wallet_request_amount"));
                            hm.put("status", jo.getString("status"));
                            hm.put("wallet_request_date", jo.getString("wallet_request_date"));*/

                            hm.put("wallet_request_id", jo.getString("withdraw_request_id"));
                            hm.put("wallet_request_amount", jo.getString("withdraw_amount"));
                            hm.put("status", jo.getString("request_status"));
                            hm.put("wallet_request_date", jo.getString("request_date"));

                            data.add(hm);


                        }
                        list.setAdapter(new Withdraw_history_adapter(WithdrawHistoryAct.this, data, viewType));
                     //   System.out.println("_____DDD"+data.size());
                        if(data.size()<=0){
                            no_data_txt.setVisibility(View.VISIBLE);
                            lay_list.setVisibility(View.GONE);
                         //   findViewById(R.id.no_data_txt).setVisibility(View.VISIBLE);
                            findViewById(R.id.progress_history).setVisibility(View.GONE);
                        }
                        else{
                            no_data_txt.setVisibility(View.GONE);
                            lay_list.setVisibility(View.VISIBLE);
                            findViewById(R.id.progress_history).setVisibility(View.GONE);
                         //   findViewById(R.id.no_data).setVisibility(View.GONE);
                        }
                    } else {
                        no_data_txt.setVisibility(View.VISIBLE);
                        lay_list.setVisibility(View.GONE);
                      //  alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    // check net connection
//                    alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                        }
                    });
                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    }


    /**
     * Referal Withdraw history API response parsing.
     */
    public class ReferalApi implements APIResult {
        public ReferalApi() {

            try {
                JSONObject j = new JSONObject();
                j.put("driver_id",SessionSave.getSession("Id", WithdrawHistoryAct.this) );

                String driverTripRequesting = "type=driver_wallet";
                if (isOnline()) {
                    new APIService_Retrofit_JSON(WithdrawHistoryAct.this, this, j, false).execute(driverTripRequesting);
                } else {
                    alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

         //   Log.e("result " ,result);
            try {
                if (isSuccess) {
                    data = new ArrayList<HashMap<String, String>>();
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {

                        JSONArray jobject = object.getJSONArray("request_lists");
                        for (int i = 0; i < jobject.length(); i++) {
                            JSONObject jo = jobject.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<String, String>();

                            hm.put("wallet_request_id", jo.getString("wallet_request_id"));
                            hm.put("wallet_request_amount", jo.getString("wallet_request_amount"));
                            hm.put("status", jo.getString("status"));
                            hm.put("wallet_request_date", jo.getString("wallet_request_date"));

                            data.add(hm);

                        }

                        list.setAdapter(new Withdraw_referalhistory_adapter(WithdrawHistoryAct.this, data));
                      //  System.out.println("____________"+data.size());
                        if(data.size()<=0){
                            no_data_txt.setVisibility(View.VISIBLE);
                            lay_list.setVisibility(View.GONE);
                            //findViewById(R.id.no_data_txt).setVisibility(View.VISIBLE);
                            findViewById(R.id.progress_history).setVisibility(View.GONE);
                        }
                        else{
                            no_data_txt.setVisibility(View.GONE);
                            lay_list.setVisibility(View.VISIBLE);
                            findViewById(R.id.progress_history).setVisibility(View.GONE);
                          //  findViewById(R.id.no_data).setVisibility(View.GONE);
                        }
                    } else {
                        alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else
                {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            alert_view(WithdrawHistoryAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                        }
                    });
                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    }
}