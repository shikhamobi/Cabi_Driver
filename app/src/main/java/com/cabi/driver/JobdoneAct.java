package com.cabi.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.service.NonActivity;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * This class is used to show trip fare once the payment is completed
 */
public class JobdoneAct extends MainActivity {
    // Class members declarations
    private TextView fareTxt;
    private TextView referalTxt;
    private TextView HeadTitle;
    private Button back_main;
    private String j_fare;
    private String j_referal;
    private double m_fare = 0.0;
    private String message;
    private TextView back;
    NonActivity nonactiityobj = new NonActivity();



    /**
     * Set the layout to activity.
     */
    @Override
    public int setLayout() {

        // This is method for set the language configuration.
        setLocale();
        return R.layout.paypopup_lay;
    }


    /**
     *Initialize the views on layout
     */
    @Override
    public void Initialize() {

        CommonData.mActivitylist.add(this);
        Bundle bun = getIntent().getExtras();
        SessionSave.saveSession("status", "F", getApplicationContext());
        if (SessionSave.getSession("shift_status", JobdoneAct.this).equals("IN"))
        nonactiityobj.startServicefromNonActivity(JobdoneAct.this);
        FontHelper.applyFont(this, findViewById(R.id.inner_content));
        CommonData.current_act = "JobdoneAct";

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) JobdoneAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), JobdoneAct.this);

        Glide.with(JobdoneAct.this).load(SessionSave.getSession("image_path", JobdoneAct.this) + "eReceiptCash.png").into((ImageView) findViewById(R.id.currency_symbol));
        if (bun != null) {
            message = bun.getString("message");
            fareTxt = (TextView) findViewById(R.id.fareTxt);
            referalTxt = (TextView) findViewById(R.id.jobreferralTxt);
            HeadTitle = (TextView) findViewById(R.id.headerTxt);
            back_main = (Button) findViewById(R.id.back_main);
            back = (TextView) findViewById(R.id.slideImg);
            back.setVisibility(View.VISIBLE);
            HeadTitle.setText("" + NC.getResources().getString(R.string.trip_completed));
            CommonData.km_calc = 0;
            SessionSave.saveSession("speedwaiting", "", JobdoneAct.this);
            CommonData.currentspeed = "";
            SessionSave.saveSession("drop_location", "", JobdoneAct.this);
            SessionSave.saveSession("waitingHr", "", JobdoneAct.this);

            SessionSave.saveSession("trip_id", "", JobdoneAct.this);

            // Show the job completion notification with fare,id and payment type.
            try {
                JSONObject json = new JSONObject(message);
                JSONObject detail = json.getJSONObject("detail");
                if (json.getInt("status") == 1) {
                    j_fare = detail.getString("fare");
                    j_referal = detail.getString("trip_id");
                    if (j_fare.length() > 0) {
                        m_fare = Double.parseDouble(j_fare);
                    }
                    fareTxt.setText(SessionSave.getSession("site_currency", getApplicationContext()) + " " + String.format(Locale.UK, "%.2f", m_fare));
                    referalTxt.setText("" + NC.getResources().getString(R.string.trip_id) + " : " + j_referal);
                }
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // This for close this activity and move to dashboard activity.
        back_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showLoading(JobdoneAct.this);
                startActivity(new Intent(getApplicationContext(), MyStatus.class));
                finish();
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoading(JobdoneAct.this);
                startActivity(new Intent(getApplicationContext(), MyStatus.class));
                finish();
            }
        });
    }
}
