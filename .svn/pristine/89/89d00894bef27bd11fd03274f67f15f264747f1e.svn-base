package com.cabi.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.R;
import com.cabi.driver.data.apiData.ApiRequestData;
import com.cabi.driver.data.apiData.TripDetailResponse;
import com.cabi.driver.service.CoreClient;
import com.cabi.driver.service.ServiceGenerator;
import com.cabi.driver.utils.CL;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.RoundedImageView;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by developer on 2/11/16.
 */


/**
 * This class is used to show driver trip details
 */
public class TripDetailNewFrag extends Fragment {
    private TextView details_trip_id, distance, dfare,
            vdfare, waiting, wcost, vWait, sub, tax, promo, total,
            wallet, cash, t_pickup_value, t_dropoff_text_value, min_fare, min_fare_per, min_total_fare, pay_type;
    private String trip_id = " ";
    private ImageView trip_map_view;
    private RoundedImageView driverImg;
    private TextView fares;
    private ImageView driverRat;
    private LinearLayout tripdetails, help_lay;
    private BottomSheetBehavior<View> mBottomSheetBehavior;

    DecimalFormat df = new DecimalFormat("####0.00");

    private View eve_night_sep;
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
    private String title = "";
    private LinearLayout loading;
    private TextView user;
    private TextView night_fare;
    private TextView evefare;
    private LinearLayout distance_lay;
    private LinearLayout miniutes_lay;
    private LinearLayout night_lay, evening_lay, promo_lay;
    private TextView tax_label;
    private TextView distance_fare_txt;
    private TextView payment_type_c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.include_details, container, false);
        loading = (LinearLayout) v.findViewById(R.id.loading);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());

        ImageView iv = (ImageView) v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        distance_lay = (LinearLayout) v.findViewById(R.id.distance_lay);
        miniutes_lay = (LinearLayout) v.findViewById(R.id.miniutes_lay);
        t_pickup_value = (TextView) v.findViewById(R.id.t_pickup_value);
        distance_fare_txt = (TextView) v.findViewById(R.id.distance_fare_txt);
        driverImg = (RoundedImageView) v.findViewById(R.id.driverImg);
        trip_map_view = (ImageView) v.findViewById(R.id.trip_map_view);
        night_lay = (LinearLayout) v.findViewById(R.id.night_lay);
        evening_lay = (LinearLayout) v.findViewById(R.id.evening_lay);
        promo_lay = (LinearLayout) v.findViewById(R.id.promo_lay);
        driverRat = (ImageView) v.findViewById(R.id.rating);
        tripdetails = (LinearLayout) v.findViewById(R.id.tripdetails);
        help_lay = (LinearLayout) v.findViewById(R.id.help_lay);
        user = (TextView) v.findViewById(R.id.user);
        t_dropoff_text_value = (TextView) v.findViewById(R.id.t_dropoff_text_value);
        details_trip_id = (TextView) v.findViewById(R.id.details_trip_id);
        distance = (TextView) v.findViewById(R.id.dist);
        dfare = (TextView) v.findViewById(R.id.dfare);
        vdfare = (TextView) v.findViewById(R.id.vehicle_detail_fare);
        waiting = (TextView) v.findViewById(R.id.wait);
        wcost = (TextView) v.findViewById(R.id.wcost);
        vWait = (TextView) v.findViewById(R.id.vwcost);
        sub = (TextView) v.findViewById(R.id.sTotal);
        tax = (TextView) v.findViewById(R.id.tax);
        tax_label = (TextView) v.findViewById(R.id.tax_label);
        promo = (TextView) v.findViewById(R.id.promo);
        total = (TextView) v.findViewById(R.id.total);
        wallet = (TextView) v.findViewById(R.id.wallet);
        cash = (TextView) v.findViewById(R.id.cash);
        eve_night_sep =  v.findViewById(R.id.eve_night_sep);
        fares = (TextView) v.findViewById(R.id.fares);
        min_fare = (TextView) v.findViewById(R.id.min_fare);
        min_fare_per = (TextView) v.findViewById(R.id.min_fare_per);
        min_total_fare = (TextView) v.findViewById(R.id.min_total_fare);
        pay_type = (TextView) v.findViewById(R.id.pay_type);
        trip_id = getArguments().getString("trip_id");

        View bottomSheet = v.findViewById(R.id.tripdetails_scroll);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        payment_type_c = (TextView) v.findViewById(R.id.payment_type_c);

//help_lay.setVisibility(View.INVISIBLE);
//        help_lay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Fragment ff = new HelpFrag();
//                Bundle bb = new Bundle();
//                bb.putString("trip_id", trip_id);
//                ff.setArguments(bb);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, ff).addToBackStack(null).commit();
//            }
//        });
        callDetail();
        dropVisible(v);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = getArguments().getString("title");
        // ((MainHomeFragmentActivity) getActivity()).setTitle(title);
    }


    /**
     * Loading UI Component Resources
     */
    public void dropVisible(View v) {
        if (!SessionSave.getSession("Lang", getActivity()).trim().equals("ar")||!SessionSave.getSession("Lang", getActivity()).trim().equals("fa")) {
            pickup_pinlay = (FrameLayout
                    ) v.findViewById(R.id.pickup_pinlay);
            drop_pin = (ImageView
                    ) v.findViewById(R.id.drop_pin);
            pickupp = (LinearLayout
                    ) v.findViewById(R.id.pickupp);
            pickup_pin = (ImageView
                    ) v.findViewById(R.id.pickup_pin);
            currentlocTxt = (TextView
                    ) v.findViewById(R.id.currentlocTxt);
            lay_pick_fav = (LinearLayout
                    ) v.findViewById(R.id.lay_pick_fav);
            pick_fav = (ImageView
                    ) v.findViewById(R.id.pick_fav);
            dropppp = (LinearLayout
                    ) v.findViewById(R.id.dropppp);
            droplocEdt = (TextView
                    ) v.findViewById(R.id.t_dropoff_text_value);
            drop_fav = (LinearLayout
                    ) v.findViewById(R.id.drop_fav);
            drop_close = (ImageView
                    ) v.findViewById(R.id.drop_close);
            evefare = (TextView) v.findViewById(R.id.evefare);
            night_fare = (TextView) v.findViewById(R.id.night_fare);
            dropppp.setVisibility(View.VISIBLE);
//        pick_fav.setImageResource(R.drawable.star);
            pickup_pinlay.setVisibility(View.VISIBLE);
            pickup_pin.setVisibility(View.GONE);
//        if (SessionSave.getSession("Lang", getActivity()).equals("ar"))
//            pickupp.setBackgroundResource(R.drawable.search_pickup_ar);
//        else
            pickupp.setBackgroundResource(R.drawable.search_pickup);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
//
//        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
//        ((MainHomeFragmentActivity) getActivity()).small_title(true);

        try {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //   ((MainHomeFragmentActivity) getActivity()).small_title(false);
    }

    public static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return String.valueOf((double) tmp / factor);
    }


    /**
     * TripDetail method API call and response parsing.
     */
    private void callDetail() {
        CoreClient client = new ServiceGenerator(getActivity()).createService(CoreClient.class);
        ApiRequestData.getTripDetailRequest request = new ApiRequestData.getTripDetailRequest();
        request.setTrip_id(trip_id);

        Call<TripDetailResponse> LoginResponse = client.callData(ServiceGenerator.COMPANY_KEY, request, SessionSave.getSession("Lang", getActivity()), ServiceGenerator.DYNAMIC_AUTH_KEY);
        LoginResponse.enqueue(new Callback<TripDetailResponse>() {
            @Override
            public void onResponse(Call<TripDetailResponse> call, Response<TripDetailResponse> response) {
                if (getView() != null) {
                    TripDetailResponse data = response.body();
                    if (data != null) {
                        if (data.status == 1) {
                            loading.setVisibility(View.GONE);
                            tripdetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            details_trip_id.setText(NC.getString(R.string.trip_id) + ": " + data.detail.trip_id);
                            user.setText(data.detail.passenger_name);
                            fares.setText(SessionSave.getSession("site_currency", getActivity()) + " " + data.detail.amt);
                            Picasso.with(getActivity()).load(data.detail.passenger_image).into(driverImg);
                            Glide.with(getActivity()).load(data.detail.map_image).into(trip_map_view);
                            t_dropoff_text_value.setText(data.detail.drop_location);
                            t_pickup_value.setText(data.detail.current_location);
                            distance.setText(data.detail.distance + " " + data.detail.metric);
                            dfare.setText(SessionSave.getSession("site_currency", getActivity()) + " " + data.detail.distance_fare_metric);
                            vdfare.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.distance_fare));
                            min_fare.setText(data.detail.trip_minutes + " " + NC.getString(R.string.mins));
                            min_fare_per.setText(SessionSave.getSession("site_currency", getActivity()) + " " + data.detail.fare_per_minute);
                            min_total_fare.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.minutes_fare));
                            waiting.setText(String.valueOf(data.detail.waiting_time));
                            payment_type_c.setText(data.detail.payment_type_label);
                            if (data.detail.payment_type_label.trim().equalsIgnoreCase(NC.getString(R.string.cash).trim())) {
                                // payment_type_c.setText(NC.getString(R.string.cash));
                                payment_type_c.setTextColor(CL.getResources().getColor(R.color.pastbookingcashtext));

                            } else if (data.detail.payment_type_label.trim().equalsIgnoreCase(NC.getString(R.string.wallet).trim())) {
                                //  payment_type_c.setText(NC.getString(R.string.wallet));
                                payment_type_c.setTextColor(CL.getResources().getColor(R.color.pastbookingcashtext));
                            } else {
                                // payment_type_c.setText(NC.getString(R.string.card));
                                payment_type_c.setTextColor(CL.getResources().getColor(R.color.pastbookingcard));
                            }
                            wcost.setText(SessionSave.getSession("site_currency", getActivity()) + " " + data.detail.waiting_fare_hour + "/" + NC.getString(R.string.hour));
                            vWait.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.waiting_fare));
                            sub.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.subtotal));
                            tax.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.tax_fare));
                            tax_label.setText(NC.getString(R.string.Tax) + "( " + data.detail.tax_percentage + "%)");
                            promo.setText(data.detail.promocode_fare);
                            total.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.amt));
                            wallet.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.used_wallet_amount));
                            cash.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.actual_paid_amount));
                            pay_type.setText(data.detail.payment_type_label);
                            distance_fare_txt.setText(NC.getString(R.string.dist_fare) + "(1/" + data.detail.metric + ")");
                            //Changes
                            if (night_fare != null) {
                                night_fare.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.nightfare));
                                evefare.setText(SessionSave.getSession("site_currency", getActivity()) + " " + (data.detail.eveningfare));
                            }
                            if (data.detail.fare_calculation_type.trim().equals("1"))
                                miniutes_lay.setVisibility(View.GONE);
                            else if (data.detail.fare_calculation_type.trim().equals("2"))
                                distance_lay.setVisibility(View.GONE);

                            if (Double.parseDouble(data.detail.nightfare) <= 0.0) {
                                night_lay.setVisibility(View.GONE);
                            }

                            if (Double.parseDouble(data.detail.eveningfare) <= 0.0) {
                                evening_lay.setVisibility(View.GONE);
                            }
                            if (Double.parseDouble(data.detail.nightfare) <= 0.0 && Double.parseDouble(data.detail.eveningfare) <= 0.0) {
                                eve_night_sep.setVisibility(View.GONE);
                            }
                            if (data.detail.promocode_fare.equals("0")) {
                                promo_lay.setVisibility(View.GONE);
                            }


                            if (data.detail.payment_type.equals("1"))
                                pay_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cash, 0, 0, 0);
                            else if (data.detail.payment_type.equals("5")) {
                                pay_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cash, 0, 0, 0);
                                pay_type.setVisibility(View.GONE);
                                cash.setVisibility(View.GONE);
                            } else
                                pay_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, 0, 0);
                            int driver_rating = Integer.parseInt(data.detail.rating);


                            if (driver_rating == 0)
                                driverRat.setImageResource(R.drawable.star6);
                            else if (driver_rating == 1)
                                driverRat.setImageResource(R.drawable.star1);
                            else if (driver_rating == 2)
                                driverRat.setImageResource(R.drawable.star2);
                            else if (driver_rating == 3)
                                driverRat.setImageResource(R.drawable.star3);
                            else if (driver_rating == 4)
                                driverRat.setImageResource(R.drawable.star4);
                            else if (driver_rating == 5)
                                driverRat.setImageResource(R.drawable.star5);


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
            public void onFailure(Call<TripDetailResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();

                // (getActivity() != null)
                //((MainActivity) getActivity()).closeProgressDialog();
            }
        });
    }

}