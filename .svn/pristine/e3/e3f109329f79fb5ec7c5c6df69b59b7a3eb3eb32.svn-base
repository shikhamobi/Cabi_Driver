package com.cabi.driver.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.R;
import com.cabi.driver.adapter.PastBookingAdapter;
import com.cabi.driver.adapter.UpcomingAdapter;
import com.cabi.driver.data.apiData.ApiRequestData;
import com.cabi.driver.data.apiData.UpcomingResponse;
import com.cabi.driver.service.CoreClient;
import com.cabi.driver.service.ServiceGenerator;
import com.cabi.driver.utils.CL;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by developer on 1/11/16.
 */

/**
 * This class is used to show trip history,Here we can see both upcoming and past booking history
 */
public class TripHistory extends Fragment {

    private static boolean UP_COMING = true;
    TextView txt_up_coming, txt_past_booking;
    RecyclerView history_recyclerView;
    private TextView no_data;
    int start = 0;
    private int limit = 10;
    private int preLast = -9;
    private LinearLayoutManager mLayoutManager;
    private List<UpcomingResponse.PastBooking> pastData = new ArrayList<>();
    private int prevLimt;
    private PastBookingAdapter past_booking_adapter;
    private Dialog mDialog;

    private View upcoming_underline, past_underline;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trip_history_lay, container, false);
        Initialize(v);
        return v;
    }


    /**
     * Initializing UI Components
     */
    public void Initialize(View v) {
        txt_past_booking = (TextView) v.findViewById(R.id.txt_past_booking);
        txt_up_coming = (TextView) v.findViewById(R.id.txt_up_coming);
//        txt_up_coming_r = (LinearLayout) v.findViewById(R.id.txt_up_coming_r);
//        txt_past_booking_r = (LinearLayout) v.findViewById(R.id.txt_past_booking_r);
        history_recyclerView = (RecyclerView) v.findViewById(R.id.history_recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        history_recyclerView.setLayoutManager(mLayoutManager);
        no_data = (TextView) v.findViewById(R.id.nodataTxt);


        Colorchange.ChangeColor((ViewGroup) v, getActivity());

        upcoming_underline = (View) v.findViewById(R.id.upcoming_underline);
        past_underline = (View) v.findViewById(R.id.past_underline);

        //upcoming trip history onclick
        txt_up_coming.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //button color change
            /*    txt_up_coming.setBackgroundResource(R.drawable.book_select);
                txt_past_booking.setBackgroundResource(R.drawable.book_unselect);*/

                upcoming_underline.setBackgroundColor(CL.getResources().getColor(R.color.button_accept));
                past_underline.setBackgroundColor(CL.getResources().getColor(R.color.linebottom_light));

                txt_up_coming.setTextColor(CL.getResources().getColor(R.color.button_accept));
                txt_past_booking.setTextColor(CL.getResources().getColor(R.color.linebottom_light));

                history_recyclerView.setAdapter(null);
                UP_COMING = true;
                callUpComingData();
            }
        });

        //past booking trip history onclick
        txt_past_booking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               /* //button color change
                txt_up_coming.setBackgroundResource(R.drawable.book_unselect);
                txt_past_booking.setBackgroundResource(R.drawable.book_select);*/


                past_underline.setBackgroundColor(CL.getResources().getColor(R.color.button_accept));
                upcoming_underline.setBackgroundColor(CL.getResources().getColor(R.color.linebottom_light));

                txt_past_booking.setTextColor(CL.getResources().getColor(R.color.button_accept));
                txt_up_coming.setTextColor(CL.getResources().getColor(R.color.linebottom_light));

                pastData.clear();
                history_recyclerView.setAdapter(null);
                past_booking_adapter = null;
                start = 0;
                limit = 10;
                UP_COMING = false;
                callPastBookingData();
            }
        });

        // if (SessionSave.getSession("trip_id", getActivity()).equals(""))
        txt_past_booking.callOnClick();
//        else
//            callUpComingData();
        //By default upcoming history will be shown first
        // callUpComingData();

//        upcoming_underline.setBackgroundColor(CL.getResources().getColor(R.color.button_accept));
//        past_underline.setBackgroundColor(CL.getResources().getColor(R.color.linebottom_light));
//
//        txt_up_coming.setTextColor(CL.getResources().getColor(R.color.button_accept));
//        txt_past_booking.setTextColor(CL.getResources().getColor(R.color.linebottom_light));


//        history_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (!UP_COMING)
//                    if (dy > 0) //check for scroll down
//                    {
//                        int visibleItemCount = mLayoutManager.getChildCount();
//                        int totalItemCount = mLayoutManager.getItemCount();
//                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//                        Log.v("...", "Last Item Wow !" + visibleItemCount + "___" + pastVisiblesItems + "___" + totalItemCount);
//
//
////                    if (loading) {
//                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                            // loading = false;
//                            Log.v("...", "Last Item Wow !");
//                            //Do pagination.. i.e. fetch new data
//                            System.out.println("_*____" + limit + "***" + start + "***" + totalItemCount);
//                            if (totalItemCount >= 10 && limit >= prevLimt && totalItemCount == limit) {
//                                System.out.println("_*____*****_" + limit + "***" + start + "***" + totalItemCount);
//                                if (start == 0)
//                                    start = 11;
//                                else
//                                    start += 10;
//                                prevLimt = limit;
//                                limit += 10;
//                                callPastBookingData();
//                            }
//                        }
//                        // }
//                    }
//            }
//        });
    }

    @Override
    public void onStop() {
        super.onStop();
        closeDialog();
    }


    /**
     * Upcomingtrip API response parsing.
     */
    private void callUpComingData() {
        CoreClient client = new ServiceGenerator(getActivity()).createService(CoreClient.class);
        ApiRequestData.UpcomingRequest request = new ApiRequestData.UpcomingRequest();
        request.setId(SessionSave.getSession("Id", getActivity()));
        request.setDeviceType("2");
        request.setLimit("10");
        request.setStart("0");
        request.setRequestType("1");

        Call<UpcomingResponse> LoginResponse = client.callData(ServiceGenerator.COMPANY_KEY, request, SessionSave.getSession("Lang", getActivity()), ServiceGenerator.DYNAMIC_AUTH_KEY);

        showDialog();

        LoginResponse.enqueue(new Callback<UpcomingResponse>() {

            @Override
            public void onResponse(Call<UpcomingResponse> call, Response<UpcomingResponse> response) {

                if (getView() != null) {

                    closeDialog();
                    UpcomingResponse data = response.body();
                    if (data != null) {
                        if (data.status == 1) {

                            if (data.detail.pending_booking == null)
                                no_data.setVisibility(View.VISIBLE);
                            else {
                                if (data.detail.pending_booking.size() == 0)
                                    no_data.setVisibility(View.VISIBLE);
                                else {
                                    history_recyclerView.setAdapter(new UpcomingAdapter(getContext(), data.detail.pending_booking));

                                    no_data.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), NC.getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                    }
                    //((MainActivity) getActivity()).closeProgressDialog();
                }


            }

            @Override
            public void onFailure(Call<UpcomingResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), NC.getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();

                // (getActivity() != null)
                //((MainActivity) getActivity()).closeProgressDialog();
            }
        });
    }

    /**
     * Show alert dialog
     */

    public void showDialog() {
        try {
            if (NetworkStatus.isOnline(getActivity())) {
                View view = View.inflate(getActivity(), R.layout.progress_bar, null);
                mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
                mDialog.setContentView(view);
                mDialog.setCancelable(false);
                mDialog.show();

                ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(TripHistory.this)
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);

            } else {

            }
        } catch (Exception e) {

        }

    }

    /**
     * Close alert dialog
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
     * Pastbooking API response parsing.
     */
    private void callPastBookingData() {
        CoreClient client = new ServiceGenerator(getActivity()).createService(CoreClient.class);
//        ApiRequestData.PastBookingRequest request = new ApiRequestData.PastBookingRequest();
//        request.setPassenger_id(SessionSave.getSession("Id", getActivity()));
//        request.setDevice_type("2");
//        request.setLimit("10");
//        request.setStart(String.valueOf(start));
//        request.setMonth("12");
//        request.setYear("2016");


        ApiRequestData.UpcomingRequest request = new ApiRequestData.UpcomingRequest();
        request.setId(SessionSave.getSession("Id", getActivity()));
        request.setDeviceType("2");
        request.setLimit("10");
        request.setStart("0");
        request.setRequestType("2");
        // System.out.println("_*____****_"+limit+"***"+start);
        Call<UpcomingResponse> LoginResponse = client.callData_(ServiceGenerator.COMPANY_KEY, request, SessionSave.getSession("Lang", getActivity()), ServiceGenerator.DYNAMIC_AUTH_KEY);
        showDialog();
        LoginResponse.enqueue(new Callback<UpcomingResponse>() {
            @Override
            public void onResponse(Call<UpcomingResponse> call, Response<UpcomingResponse> response) {
                if (getView() != null) {
                    closeDialog();
                    UpcomingResponse data = response.body();

                    if (data != null) {
                        if (data.status == 1) {

                            if (data != null) {
                                if (data.status == 1) {
                                    System.out.println("_)_____" + data.detail.past_booking.size());
                                    if (data.detail.past_booking == null)
                                        no_data.setVisibility(View.VISIBLE);
                                    else {
                                        if (data.detail.past_booking.size() == 0)
                                            no_data.setVisibility(View.VISIBLE);
                                        else {
                                            history_recyclerView.setAdapter(new PastBookingAdapter(getContext(), data.detail.past_booking));

                                            no_data.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), NC.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), NC.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpcomingResponse> call, Throwable t) {
                t.printStackTrace();
                closeDialog();
                if(getActivity()!=null)
                Toast.makeText(getActivity(), NC.getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
//        ((MainHomeFragmentActivity) getActivity()).setTitle(getString(R.string.mybookings));
//        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
//        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
    }
}
