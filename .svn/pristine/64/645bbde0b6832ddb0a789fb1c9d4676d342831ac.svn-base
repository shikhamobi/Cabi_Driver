package com.cabi.driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class is used to view withdraw history list details
 */

public class WithdrawReqAct extends MainActivity{

    public static WithdrawReqAct withdrawAct;

    TextView reqId,brandType,companyName,withdrawAmount,waitTimeCost,
    reqDate,status,paymentmode,transactionID,comments,btn_back;

    ImageView img_attachment;

    String withdrawrequestId="";

    @Override
    public int setLayout() {

        setLocale();
        return R.layout.withdrawreq;
    }




    /**
     * Initialize the views on layout and variable declarations
     */
    @Override
    public void Initialize() {

        try
        {

            Colorchange.ChangeColor((ViewGroup) (((ViewGroup) WithdrawReqAct.this
                    .findViewById(android.R.id.content)).getChildAt(0)), WithdrawReqAct.this);

            CommonData.mActivitylist.add(this);
            CommonData.sContext = this;
            CommonData.current_act = "WithDrawMenuAct";
            FontHelper.applyFont(this, findViewById(R.id.withdraw_menu));
            withdrawAct = this;

            Bundle bundle = getIntent().getExtras();

            if(bundle != null)
            {
                withdrawrequestId = bundle.getString("wallet_request_id");
              //  Log.e("wallet_request_id",withdrawrequestId);
            }

            reqId = (TextView) findViewById(R.id.reqIdTxt);
            brandType = (TextView) findViewById(R.id.brandtypeTxt);
            companyName = (TextView) findViewById(R.id.companynameTxt);
            withdrawAmount = (TextView) findViewById(R.id.withdrawamtTxt);
            waitTimeCost = (TextView) findViewById(R.id.waitingtimecostTxt);
            reqDate = (TextView) findViewById(R.id.requesteddateTxt);
            status = (TextView) findViewById(R.id.statusTxt);
            paymentmode = (TextView) findViewById(R.id.pmodeTxt);
            transactionID = (TextView) findViewById(R.id.TIdTxt);
            comments = (TextView) findViewById(R.id.commentstxt);
            img_attachment = (ImageView) findViewById(R.id.imgattachment);
            btn_back=(TextView)findViewById(R.id.slideImg);

            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(WithdrawReqAct.this, WithdrawHistoryAct.class));
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        new requestingApi();
    }

    /**
     * Withdraw History View API response parsing.
     */
    public class requestingApi implements APIResult {
        public requestingApi() {

            try {
                JSONObject j = new JSONObject();

                j.put("withdraw_request_id",withdrawrequestId);
                j.put("driver_id",SessionSave.getSession("Id", WithdrawReqAct.this) );

           //     Log.e("Json ", j.toString());

                String driverTripRequesting = "type=driver_withdraw_list_detail";
                if (isOnline()) {
                    new APIService_Retrofit_JSON(WithdrawReqAct.this, this, j, false).execute(driverTripRequesting);
                } else {
                    alert_view(WithdrawReqAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            try {

             //   Log.e("isSuccess ",result);

                if (isSuccess) {

                    JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {
                        JSONArray jobject = object.getJSONArray("details");

                        JSONArray jobject2 = object.getJSONArray("activity_log");

                        reqId.setText(jobject.getJSONObject(0).getString("request_id"));

                        brandType.setText(jobject.getJSONObject(0).getString("brand_type"));
                        companyName.setText(jobject.getJSONObject(0).getString("company_name"));
                        withdrawAmount.setText(jobject.getJSONObject(0).getString("withdraw_amount"));
                        waitTimeCost.setText("");
                        reqDate.setText(jobject.getJSONObject(0).getString("request_date"));
                        status.setText(jobject.getJSONObject(0).getString("request_status"));
                        paymentmode.setText(jobject2.getJSONObject(0).getString("payment_mode_name"));
                        transactionID.setText(jobject2.getJSONObject(0).getString("transaction_id"));
                        comments.setText(jobject2.getJSONObject(0).getString("comments"));

                        String attachment = jobject2.getJSONObject(0).getString("attachment");


                        if(attachment.endsWith("png"))
                        {
                            img_attachment.setVisibility(View.VISIBLE);
                            Picasso.with(WithdrawReqAct.this).load(attachment).into(img_attachment);
                        }
                        else
                        {
                            img_attachment.setVisibility(View.INVISIBLE);
                        }

                    } else {
                        alert_view(WithdrawReqAct.this, "" + NC.getResources().getString(R.string.message), "" + object.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            alert_view(WithdrawReqAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
