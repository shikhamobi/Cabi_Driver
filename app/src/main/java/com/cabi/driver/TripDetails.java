package com.cabi.driver;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.ImageDownloader;
import com.cabi.driver.utils.SessionSave;

import java.util.Locale;


/**
 * This class is used to Show trip details for driver
 */
public class TripDetails extends MainActivity implements ImageDownloader {
    // Class members declarations
    private ImageView imgView;
    private TextView txtPassenger, txtTripTime, txtTripPick, txtTripAmount, txtTripPayment, txtTripId, back;
    private String passenger_name;
    private TextView passnameTxt;
    private TextView droptimeTxt;
    private TextView dropplaceTxt;
    private TextView distanceTxt;
    private TextView duartionTxt;
    private double mdistance;

    // Set the layout to activity.
    @Override
    public int setLayout() {

        setLocale();
        return R.layout.trip_detailslayout;
    }


    // Initialize the views on layout
    @Override
    public void Initialize() {

        CommonData.mActivitylist.add(this);
        CommonData.sContext = this;
        CommonData.current_act = "TripDetails";
        imgView = (ImageView) findViewById(R.id.driverImg);

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) TripDetails.this
                .findViewById(android.R.id.content)).getChildAt(0)), TripDetails.this);

        FontHelper.applyFont(this, findViewById(R.id.id_tripdetails));
        back = (TextView) findViewById(R.id.slideImg);
        txtPassenger = (TextView) findViewById(R.id.drivernameTxt);
        txtTripTime = (TextView) findViewById(R.id.time);
        txtTripPick = (TextView) findViewById(R.id.place);
        txtTripAmount = (TextView) findViewById(R.id.amount);
        txtTripPayment = (TextView) findViewById(R.id.payment);
        txtTripId = (TextView) findViewById(R.id.jobref);
        passnameTxt = (TextView) findViewById(R.id.passnameTxt);
        droptimeTxt = (TextView) findViewById(R.id.droptimeTxt);
        dropplaceTxt = (TextView) findViewById(R.id.droplocTxt);
        distanceTxt = (TextView) findViewById(R.id.distanceTxt);
        duartionTxt = (TextView) findViewById(R.id.duartionTxt);
        Bundle bundle = getIntent().getExtras();
        passenger_name = Character.toUpperCase(bundle.getString("passenger_name").charAt(0)) + bundle.getString("passenger_name").substring(1);
        txtPassenger.setText(passenger_name);
        passnameTxt.setText(passenger_name);
        //txtTripTime.setText(date_conversion(bundle.getString("pickup_time")));
        txtTripTime.setText((bundle.getString("pickup_time")));
        txtTripPick.setText("" + bundle.getString("pickup_location"));
        txtTripAmount.setText(SessionSave.getSession("site_currency", getApplicationContext()) + " " + bundle.getString("trip_amt"));
        txtTripPayment.setText("" + bundle.getString("paymenttype"));
        txtTripId.setText("" + bundle.getString("passengers_log_id"));
       // droptimeTxt.setText("" + date_conversion(bundle.getString("drop_time")));
        droptimeTxt.setText("" + (bundle.getString("drop_time")));
        dropplaceTxt.setText("" + bundle.getString("drop_location"));
        if (bundle.getString("distance").length() != 0)
            mdistance = Double.parseDouble(bundle.getString("distance"));
        distanceTxt.setText("" + String.format(Locale.UK,"%.2f", mdistance) + " " + SessionSave.getSession("Metric", TripDetails.this));
        duartionTxt.setText("" + bundle.getString("trip_duration"));
        String imagePath = "" + bundle.getString("pimage");
        // This for load the passenger image in details page image view this current activity.
        try {
            if (imagePath.length() != 0) {
                imageLoader.displayImage("" + imagePath, imgView, roundedImageoptions);
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        // This for close this current activity.
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

}
