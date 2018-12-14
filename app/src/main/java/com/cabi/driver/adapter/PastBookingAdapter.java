package com.cabi.driver.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cabi.driver.R;
import com.cabi.driver.data.apiData.UpcomingResponse;
import com.cabi.driver.fragments.TripDetailNewFrag;
import com.cabi.driver.utils.CL;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by developer on 1/11/16.
 */

/**
 * This adapter class is used to show driver past booking history
 */
public class PastBookingAdapter extends RecyclerView.Adapter<PastBookingAdapter.CustomViewHolder> {

    private final List<UpcomingResponse.PastBooking> data;
    private final Context mContext;


    public PastBookingAdapter(Context c, List<UpcomingResponse.PastBooking> data) {
        this.mContext = c;
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = null;
        view = inflater.inflate(R.layout.past_booking_item, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        if (!data.get(position).profile_image.trim().equals("")) {
            Glide.with(mContext).load(data.get(position).map_image).into(holder.map_image);
         //   System.out.println("imageLink_"+data.get(position).profile_image);
            Picasso.with(mContext).load(data.get(position).profile_image).into(holder.driver_image);
        }
        Colorchange.ChangeColor(holder.book_lay, mContext);
        holder.trip_time.setText(data.get(position).pickup_time);
        holder.trip_driver_name.setText(data.get(position).model_name);
        holder.trip_time.setText(data.get(position).pickup_time);
        if (data.get(position).payment_type.trim().equalsIgnoreCase(NC.getString(R.string.cash).trim())) {
            holder.trip_payment_type.setText(NC.getString(R.string.cash));
            holder.trip_payment_type.setTextColor(CL.getResources().getColor(R.color.pastbookingcashtext));

        } else if (data.get(position).payment_type.trim().equalsIgnoreCase(NC.getString(R.string.wallet).trim())) {
            holder.trip_payment_type.setText(NC.getString(R.string.wallet));
            holder.trip_payment_type.setTextColor(CL.getResources().getColor(R.color.pastbookingcashtext));
        } else {
            holder.trip_payment_type.setText(NC.getString(R.string.card));
            holder.trip_payment_type.setTextColor(CL.getResources().getColor(R.color.pastbookingcard));
        }
        holder.trip_payment_amount.setText(SessionSave.getSession("site_currency", mContext) + " " + data.get(position).amt);
        //holder.trip_status.setText(mContext.getString(R.string.completed));
//        holder.map_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Fragment ff = new TripDetailNewFrag();
//                Bundle b = new Bundle();
//                b.putString("trip_id", data.get(position).trip_id);
//                b.putString("title", data.get(position).pickup_time);
//                ff.setArguments(b);
//                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag, ff).commit();
//            }
//        });


        if (data.get(position).travel_status.trim().equals("1")) {
            holder.trip_status.setText(NC.getString(R.string.trip_completed));
            holder.map_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment ff = new TripDetailNewFrag();
                    Bundle b = new Bundle();
                    b.putString("trip_id", data.get(position).passengers_log_id);
                    b.putString("title", data.get(position).pickup_time);
                    ff.setArguments(b);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, ff).commit();
                }
            });

        } else {
            holder.trip_status.setText(NC.getString(R.string.cancelled));
            holder.map_image.setOnClickListener(null);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    /**
     * View holder class member this contains in every row in list.
     */
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView map_image, driver_image;
        TextView trip_time, trip_driver_name;
        TextView trip_payment_type, trip_payment_amount, trip_status;
        LinearLayout book_lay;

        public CustomViewHolder(View v) {
            super(v);
            map_image = (ImageView) v.findViewById(R.id.map_image);
            driver_image = (ImageView) v.findViewById(R.id.driver_image);
            trip_time = (TextView) v.findViewById(R.id.trip_time);
            trip_driver_name = (TextView) v.findViewById(R.id.trip_driver_name);
            book_lay = (LinearLayout) v.findViewById(R.id.book_lay);
            trip_payment_type = (TextView) v.findViewById(R.id.trip_payment_type);
            trip_payment_amount = (TextView) v.findViewById(R.id.trip_payment_amount);
            trip_status = (TextView) v.findViewById(R.id.trip_status);
            //super(view);
        }
    }
}
