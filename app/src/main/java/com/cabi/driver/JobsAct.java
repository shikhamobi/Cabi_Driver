//package com.Taximobility.driver;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cabi.driver.adapter.JobsListAdapter;
//import com.cabi.driver.adapter.UpcomingAdapter;
//import com.cabi.driver.data.CommonData;
//import com.cabi.driver.data.apiData.ApiRequestData;
//import com.cabi.driver.data.apiData.UpcomingResponse;
//import com.cabi.driver.interfaces.APIResult;
//import com.cabi.driver.service.APIService_Retrofit_JSON;
//import com.cabi.driver.service.CoreClient;
//import com.cabi.driver.service.NonActivity;
//import com.cabi.driver.service.ServiceGenerator;
//import com.cabi.driver.utils.FontHelper;
//import com.cabi.driver.utils.NetworkStatus;
//import com.cabi.driver.utils.SessionSave;
//import com.cabi.driver.data.apiData.UpcomingResponse;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class JobsAct extends MainActivity implements OnClickListener {
//    // Class members declarations.
//    private TextView btnPendingBooking;
//    private TextView btnPastBooking;
//    private TextView emptyView;
//    private ImageView btnRefresh;
//    private ListView list;
//    private int limit = 10;
//    private JSONArray pendingJsonArray;
//    private JSONArray pastJsonArray;
//    private TextView back;
//    private boolean isShowingPendingTrip = true;
//    NonActivity nonactiityobj = new NonActivity();
//    private Dialog mDialog;
//    RecyclerView history_recyclerView;
//
//    // Set the layout to activity.
//    @Override
//    public int setLayout() {
//
//        setLocale();
//        return R.layout.jobs_layout;
//    }
//
//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(getApplicationContext(), MyStatus.class));
//        finish();
////        super.onBackPressed();
//    }
//
//    // Initialize the views on layout
//    @Override
//    public void Initialize() {
//
//        try {
//            CommonData.mActivitylist.add(this);
//            CommonData.sContext = this;
//            CommonData.current_act = "JobsAct";
//            FontHelper.applyFont(this, findViewById(R.id.jos_layout));
//            btnPendingBooking = (TextView) findViewById(R.id.btnPendingBooking);
//            btnPastBooking = (TextView) findViewById(R.id.btnPastBooking);
//            btnRefresh = (ImageView) findViewById(R.id.refresh_btn);
//            emptyView = (TextView) findViewById(R.id.txtempty);
//            back = (TextView) findViewById(R.id.slideImg);
//            list = (ListView) findViewById(R.id.history_listView);
//            history_recyclerView = (RecyclerView) findViewById(R.id.history_recyclerView);
//            btnPendingBooking.setOnClickListener(this);
//            btnPastBooking.setOnClickListener(this);
//            btnRefresh.setOnClickListener(this);
//            back.setOnClickListener(this);
//            isShowingPendingTrip = true;
//            new requestingApi();
//            // This for move the control from list view to particular activity based on the selected item from list.
//            list.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//
//                    try {
//                        if (!isShowingPendingTrip) {
//                            showLoading(JobsAct.this);
//                            Intent intent = new Intent(JobsAct.this, TripDetails.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("passenger_name", "" + pastJsonArray.getJSONObject(position).getString("passenger_name"));
//                            bundle.putString("passengers_log_id", "" + pastJsonArray.getJSONObject(position).getString("passengers_log_id"));
//                            bundle.putString("pickup_location", "" + pastJsonArray.getJSONObject(position).getString("pickup_location"));
//                            bundle.putString("pickup_time", "" + pastJsonArray.getJSONObject(position).getString("pickup_time"));
//                            bundle.putString("drop_location", "" + pastJsonArray.getJSONObject(position).getString("drop_location"));
//                            bundle.putString("drop_time", "" + pastJsonArray.getJSONObject(position).getString("drop_time"));
//                            bundle.putString("distance", "" + pastJsonArray.getJSONObject(position).getString("distance"));
//                            bundle.putString("trip_duration", "" + pastJsonArray.getJSONObject(position).getString("trip_duration"));
//                            bundle.putString("travel_status", "" + pastJsonArray.getJSONObject(position).getString("travel_status"));
//                            bundle.putString("trip_amt", "" + pastJsonArray.getJSONObject(position).getString("amt"));
//                            bundle.putString("pimage", "" + pastJsonArray.getJSONObject(position).getString("profile_image"));
//                            bundle.putString("paymenttype", "" + pastJsonArray.getJSONObject(position).getString("payment_type"));
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        } else if (pendingJsonArray.getJSONObject(position).getString("travel_status").equals("5")) {
//                            SessionSave.saveSession("trip_id", "" + pendingJsonArray.getJSONObject(position).getString("passengers_log_id"), JobsAct.this);
//                            Intent i = new Intent(JobsAct.this, FarecalcAct.class);
//                            i.putExtra("from", "pending");
//                            i.putExtra("lat", pendingJsonArray.getJSONObject(position).getString("drop_latitude"));
//                            i.putExtra("lon", pendingJsonArray.getJSONObject(position).getString("drop_longitude"));
//                            i.putExtra("distance", pendingJsonArray.getJSONObject(position).getString("travel_status"));
//                            i.putExtra("waitingHr", pendingJsonArray.getJSONObject(position).getString("waiting_hour"));
//                            i.putExtra("drop_location", pendingJsonArray.getJSONObject(position).getString("drop_location"));
//                            startActivity(i);
//                        } else {
//                            SessionSave.saveSession("trip_id", "" + pendingJsonArray.getJSONObject(position).getString("passengers_log_id"), JobsAct.this);
//                            showLoading(JobsAct.this);
//                            Intent intent;
//                            intent = new Intent(JobsAct.this, OngoingAct.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            JobsAct.this.startActivity(intent);
//                        }
//                    } catch (Exception e2) {
//                        e2.printStackTrace();
//                    }
//                }
//            });
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            getWindow().setStatusBarColor(getResources().getColor(R.color.colorprimary));
////        }
//    }
//
//
//    // This onclick method is to update the UI based on selected view(Pending and past booking with local data that already stored in session)
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.btnPendingBooking:
//                try {
//
//                    callUpComingData("1");
//                   /* btnPastBooking.setBackgroundResource(R.drawable.draw_select_button);
//                    btnPastBooking.setTextColor(getResources().getColor(R.color.signuptitle));
//                    btnPendingBooking.setTextColor(getResources().getColor(R.color.white));
//                    btnPendingBooking.setBackgroundResource(R.drawable.back_black_to_white);
//                    nonactiityobj.stopServicefromNonActivity(JobsAct.this);
//                    if (!pendingJsonArray.equals("")) {
//                        pendingBooking(pendingJsonArray);
//                        isShowingPendingTrip = true;
//                    }*/
//                } catch (Exception e)
//                {
//                    // TODO: handle exception
//                }
//                break;
//            case R.id.btnPastBooking:
//                try {
//                    callUpComingData("2");
//                   /* btnPastBooking.setBackgroundResource(R.drawable.back_black_to_white);
//                    btnPendingBooking.setBackgroundResource(R.drawable.draw_select_button);
//                    btnPendingBooking.setTextColor(getResources().getColor(R.color.signuptitle));
//                    btnPastBooking.setTextColor(getResources().getColor(R.color.white));
//                    nonactiityobj.stopServicefromNonActivity(JobsAct.this);
//
//                    if (!pastJsonArray.equals("")) {
//                        pastBooking(pastJsonArray);
//                        isShowingPendingTrip = false;
//                    }
//                    new requestingApi();*/
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.refresh_btn:
//                nonactiityobj.stopServicefromNonActivity(JobsAct.this);
//                new requestingApi();
//                break;
//            case R.id.slideImg:
//                startActivity(new Intent(getApplicationContext(), MyStatus.class));
//                finish();
//                break;
//        }
//    }
//
//
//
//
//
//    public void showDialog() {
//        try {
//            if (NetworkStatus.isOnline(JobsAct.this)) {
//                View view = View.inflate(JobsAct.this, R.layout.progress_bar, null);
//                mDialog = new Dialog(JobsAct.this, R.style.dialogwinddow);
//                mDialog.setContentView(view);
//                mDialog.setCancelable(false);
//                mDialog.show();
//            } else {
//
//            }
//        } catch (Exception e) {
//
//        }
//
//    }
//
//    public void closeDialog() {
//        try {
//            if (mDialog != null)
//                if (mDialog.isShowing())
//                    mDialog.dismiss();
//        } catch (Exception e) {
//
//        }
//    }
//    /**
//     * Requesting Web service custom class and returning the response to getResult() Method
//     */
//    public class requestingApi implements APIResult {
//        public requestingApi() {
//
//            try {
//                JSONObject j = new JSONObject();
//                j.put("driver_id", SessionSave.getSession("Id", JobsAct.this));
//                j.put("start", "0");
//                j.put("limit", limit);
//                j.put("device_type", "1");
//                String type = "";
//                if (isShowingPendingTrip)
//                    type = "1";
//                else
//                    type = "2";
//                j.put("request_type", type);
//                String driverTripRequesting = "type=driver_booking_list";
//                if (isOnline()) {
//                    new APIService_Retrofit_JSON(JobsAct.this, this, j, false).execute(driverTripRequesting);
//                } else {
//                    alert_view(JobsAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void getResult(boolean isSuccess, String result) {
//            // TODO Auto-generated method stub
//            try {
//                if (isSuccess) {
//                    JSONObject object = new JSONObject(result);
//                    if (object.getInt("status") == 1) {
//                        object = object.getJSONObject("detail");
//                        pendingJsonArray = object.getJSONArray("pending_booking");
//                        pastJsonArray = object.getJSONArray("past_booking");
//                        if (isShowingPendingTrip)
//                            pendingBooking(pendingJsonArray);
//                        else
//                            pastBooking(pastJsonArray);
//                    } else {
//                        alert_view(JobsAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                    }
//                    nonactiityobj.startServicefromNonActivity(JobsAct.this);
//                } else {
//                    // check net connection
////                    alert_view(JobsAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
//
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            alert_view(JobsAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
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
//     * @param pendingArray Array List with hash map values to ListView
//     */
//    private void pendingBooking(JSONArray pendingArray) {
//
//        try {
//            int arrayLength = pendingArray.length();
//            nonactiityobj.startServicefromNonActivity(JobsAct.this);
//            if (arrayLength > 0) {
//                ArrayList<HashMap<String, String>> pendingTripList = new ArrayList<HashMap<String, String>>(arrayLength);
//                for (int iterator = 0; iterator < arrayLength; iterator++) {
//                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                    hashMap.put("passenger_name", "" + pendingArray.getJSONObject(iterator).getString("passenger_name"));
//                    hashMap.put("passengers_log_id", "" + pendingArray.getJSONObject(iterator).getString("passengers_log_id"));
//                    hashMap.put("pickup_location", "" + pendingArray.getJSONObject(iterator).getString("pickup_location"));
//                    hashMap.put("pickup_time", "" + pendingArray.getJSONObject(iterator).getString("pickup_time"));
//                    hashMap.put("drop_location", "" + pendingArray.getJSONObject(iterator).getString("drop_location"));
//                    hashMap.put("travel_status", "" + pendingArray.getJSONObject(iterator).getString("travel_status"));
//                    hashMap.put("waiting_hour", "" + pendingArray.getJSONObject(iterator).getString("waiting_hour"));
//                    hashMap.put("distance", "" + pendingArray.getJSONObject(iterator).getString("distance"));
//                    hashMap.put("drop_latitude", "" + pendingArray.getJSONObject(iterator).getString("drop_latitude"));
//                    hashMap.put("drop_longitude", "" + pendingArray.getJSONObject(iterator).getString("drop_longitude"));
//                    pendingTripList.add(hashMap);
//                }
//                list.setVisibility(View.VISIBLE);
//                emptyView.setVisibility(View.GONE);
//                JobsListAdapter adapter = new JobsListAdapter(JobsAct.this, pendingTripList);
//                list.setAdapter(adapter);
//            } else {
//                list.setVisibility(View.GONE);
//                emptyView.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            nonactiityobj.startServicefromNonActivity(JobsAct.this);
//        }
//    }
//
//    /**
//     * @param pastArray Array List with hash map values to ListView
//     */
//    private void pastBooking(JSONArray pastArray) {
//
//        try {
//            int arrayLength = pastArray.length();
//            nonactiityobj.startServicefromNonActivity(JobsAct.this);
//            if (arrayLength > 0) {
//                ArrayList<HashMap<String, String>> pastTripList = new ArrayList<HashMap<String, String>>(arrayLength);
//                for (int iterator = 0; iterator < arrayLength; iterator++) {
//                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                    hashMap.put("passenger_name", "" + pastArray.getJSONObject(iterator).getString("passenger_name"));
//                    hashMap.put("passengers_log_id", "" + pastArray.getJSONObject(iterator).getString("passengers_log_id"));
//                    hashMap.put("pickup_location", "" + pastArray.getJSONObject(iterator).getString("pickup_location"));
//                    hashMap.put("pickup_time", "" + pastArray.getJSONObject(iterator).getString("pickup_time"));
//                    hashMap.put("drop_location", "" + pastArray.getJSONObject(iterator).getString("drop_location"));
//                    hashMap.put("travel_status", "" + pastArray.getJSONObject(iterator).getString("travel_status"));
//                    hashMap.put("waiting_hour", "" + pastArray.getJSONObject(iterator).getString("waiting_hour"));
//                    hashMap.put("distance", "" + pastArray.getJSONObject(iterator).getString("distance"));
//                    hashMap.put("drop_latitude", "" + pastArray.getJSONObject(iterator).getString("drop_latitude"));
//                    hashMap.put("drop_longitude", "" + pastArray.getJSONObject(iterator).getString("drop_longitude"));
//                    pastTripList.add(hashMap);
//                }
//                list.setVisibility(View.VISIBLE);
//                emptyView.setVisibility(View.GONE);
//                JobsListAdapter adapter = new JobsListAdapter(JobsAct.this, pastTripList);
//                adapter.notifyDataSetChanged();
//                list.setAdapter(adapter);
//            } else {
//                // no data found
//                list.setVisibility(View.GONE);
//                emptyView.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            nonactiityobj.startServicefromNonActivity(JobsAct.this);
//        }
//    }
//
//    private void callUpComingData(String type) {
//        CoreClient client = new ServiceGenerator().createService(CoreClient.class);
//        ApiRequestData.UpcomingRequest request = new ApiRequestData.UpcomingRequest();
//        request.setId(SessionSave.getSession("Id", JobsAct.this));
//        request.setDeviceType("2");
//        request.setLimit("100");
//        request.setStart("0");
//        request.setDeviceType(type);
//
//        Call<UpcomingResponse> LoginResponse = client.callData(request,SessionSave.getSession("Lang",JobsAct.this));
//        showDialog();
//        LoginResponse.enqueue(new Callback<UpcomingResponse>() {
//            @Override
//            public void onResponse(Call<UpcomingResponse> call, Response<UpcomingResponse> response) {
//
//                closeDialog();
//                UpcomingResponse data = response.body();
//                if (data != null) {
//                    if (data.status == 1) {
//                        history_recyclerView.setAdapter(new UpcomingAdapter(JobsAct.this, data.detail.pending_booking));
//
//                           /* if (data.detail.pending_bookings.size() == 0)
//                                no_data.setVisibility(View.VISIBLE);
//                            else
//                               no_data.setVisibility(View.GONE);*/
//                    } else {
//                        Toast.makeText(JobsAct.this, data.message, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(JobsAct.this, getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
//                }
//                //((MainActivity) JobsAct.this).closeProgressDialog();
//            }
//
//
//        @Override
//        public void onFailure(Call<UpcomingResponse> call, Throwable t) {
//            t.printStackTrace();
//            Toast.makeText(JobsAct.this, getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
//
//            // (JobsAct.this != null)
//            //((MainActivity) JobsAct.this).closeProgressDialog();
//        }
//
//    });
//
//}
//}
