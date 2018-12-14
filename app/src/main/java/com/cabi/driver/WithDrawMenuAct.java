package com.cabi.driver;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class is used to  withdraw both referal and trip amount
 */

public class WithDrawMenuAct extends MainActivity {

    public static WithDrawMenuAct withdrawAct;

    private TextView withdraw_btn1, withdraw_btn2, txt_referalamt, txt_tripamount, txt_refpendingamt, txt_trippendingamt, btn_back;

    private ImageView btn_withdrawhistory;
    private int walletamountr = 0;

    @Override
    public int setLayout() {

        setLocale();
        return R.layout.withdraw_menu;
    }

    // Initialize the views on layout and variable declarations
    @Override
    public void Initialize() {

        try {
            Colorchange.ChangeColor((ViewGroup) (((ViewGroup) WithDrawMenuAct.this
                    .findViewById(android.R.id.content)).getChildAt(0)), WithDrawMenuAct.this);


            if (getIntent().getExtras().getInt("walletamountr") > 0)
                walletamountr = getIntent().getExtras().getInt("walletamountr");


            CommonData.mActivitylist.add(this);
            CommonData.sContext = this;
            CommonData.current_act = "WithDrawMenuAct";
            FontHelper.applyFont(this, findViewById(R.id.withdraw_menu));
            withdrawAct = this;

            //Intialize
            withdraw_btn1 = (TextView) findViewById(R.id.withdraw_btn1);
            withdraw_btn2 = (TextView) findViewById(R.id.withdraw_btn2);

            txt_referalamt = (TextView) findViewById(R.id.withdraw_BalanceAmttext1);
            txt_tripamount = (TextView) findViewById(R.id.withdraw_BalanceAmttext2);
            txt_refpendingamt = (TextView) findViewById(R.id.withdraw_pendingtext1);
            txt_trippendingamt = (TextView) findViewById(R.id.withdraw_pendingtext2);

            btn_back = (TextView) findViewById(R.id.slideImg);

            btn_withdrawhistory = (ImageView) findViewById(R.id.history);
            //setting ui values
            txt_referalamt.setText(SessionSave.getSession("site_currency", WithDrawMenuAct.this) + "" + SessionSave.getSession("driver_wallet_amount", WithDrawMenuAct.this));
            txt_refpendingamt.setText(NC.getResources().getString(R.string.payment_pending) + "-" + SessionSave.getSession("site_currency", WithDrawMenuAct.this) + " " + SessionSave.getSession("driver_wallet_pending_amount", WithDrawMenuAct.this));
            txt_tripamount.setText(SessionSave.getSession("site_currency", WithDrawMenuAct.this) + "" + SessionSave.getSession("total_amount", WithDrawMenuAct.this));
            txt_trippendingamt.setText(NC.getResources().getString(R.string.payment_pending) + "-" + SessionSave.getSession("site_currency", WithDrawMenuAct.this) + " " + SessionSave.getSession("trip_pending_amount", WithDrawMenuAct.this));


            withdraw_btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (walletamountr > 0) {
                        withDraw(1);
                    } else {
                        alert_view(WithDrawMenuAct.this, "" + getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.no_sufficient_amount), "" + NC.getResources().getString(R.string.ok), "");
                    }

                }
            });

            withdraw_btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    withDraw(2);

                }
            });


            btn_withdrawhistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(WithDrawMenuAct.this, WithdrawHistoryAct.class));
                }
            });

            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    // finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This class is used to  withdraw both referal and trip amount
     * Withdraw popup
     * type:1(referal)
     * type:2(trip)
     */
    public void withDraw(final int type) {
        try {

            final View view = View.inflate(WithDrawMenuAct.this, R.layout.withdrawrequestdialog, null);
            final Dialog mcancelDialog = new Dialog(WithDrawMenuAct.this, R.style.dialogwinddow);
            mcancelDialog.setContentView(view);
            mcancelDialog.setCancelable(true);
            mcancelDialog.show();


            FontHelper.applyFont(WithDrawMenuAct.this, mcancelDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) view, WithDrawMenuAct.this);
//          /*  SessionSave.saveSession("driver_wallet_amount", details.getString("driver_wallet_amount"), WithDrawMenuAct.this);
//            SessionSave.saveSession("driver_wallet_pending_amount", details.getString("driver_wallet_pending_amount"), WithDrawMenuAct.this);*/final

            final Button button_success = (Button) mcancelDialog.findViewById(R.id.okbtn);
            final Button button_failure = (Button) mcancelDialog.findViewById(R.id.cancelbtn);

            TextView txt_avaibal = (TextView) mcancelDialog.findViewById(R.id.paymentavailedTxt);
            TextView txt_pendingbal = (TextView) mcancelDialog.findViewById(R.id.paymentpendingTxt);
            TextView txt_reqqmt = (TextView) mcancelDialog.findViewById(R.id.withdrawamountTxt);


            if (type == 1) {
                txt_avaibal.setText(SessionSave.getSession("site_currency", this) + " " + SessionSave.getSession("driver_wallet_amount", WithDrawMenuAct.this));
                txt_pendingbal.setText(SessionSave.getSession("site_currency", this) + " " + SessionSave.getSession("driver_wallet_pending_amount", WithDrawMenuAct.this));
                txt_reqqmt.setText(SessionSave.getSession("site_currency", this) + " " + SessionSave.getSession("driver_wallet_amount", WithDrawMenuAct.this));
            } else if (type == 2) {
                txt_avaibal.setText(SessionSave.getSession("site_currency", this) + " " + SessionSave.getSession("trip_amount", WithDrawMenuAct.this));
                txt_pendingbal.setText(SessionSave.getSession("site_currency", this) + " " + SessionSave.getSession("trip_pending_amount", WithDrawMenuAct.this));
                txt_reqqmt.setText(SessionSave.getSession("site_currency", this) + " " + SessionSave.getSession("total_amount", WithDrawMenuAct.this));
            } else {
               /* txt_avaibal.setText(SessionSave.getSession("driver_wallet_amount",WithDrawMenuAct.this));
                txt_pendingbal.setText("");
                txt_reqqmt.setText("");*/
            }

            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    mcancelDialog.dismiss();

                    try {
                        if (type == 1) {
                            if (Double.parseDouble(SessionSave.getSession("driver_wallet_amount", WithDrawMenuAct.this).trim()) > 0) {
                                JSONObject j = new JSONObject();

                                try {
                                    j.put("driver_id", SessionSave.getSession("Id", WithDrawMenuAct.this));
                                    j.put("driver_wallet_amount", SessionSave.getSession("driver_wallet_amount", WithDrawMenuAct.this));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                final String withdrawUrl = "type=driver_wallet_request";
                                new WithDraw(withdrawUrl, j);
                            } else {
                                Toast.makeText(WithDrawMenuAct.this, NC.getString(R.string.insufficent_amout), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (Double.parseDouble(SessionSave.getSession("total_amount", WithDrawMenuAct.this).trim()) > 0) {
                                JSONObject j = new JSONObject();

                                j.put("driver_id", SessionSave.getSession("Id", WithDrawMenuAct.this));
                                j.put("available_amount", SessionSave.getSession("trip_amount", WithDrawMenuAct.this));
                                j.put("request_amount", SessionSave.getSession("total_amount", WithDrawMenuAct.this));

                                final String withdrawUrl = "type=driver_send_withdraw_request";
                                new WithDraw(withdrawUrl, j);
                            } else {
                                Toast.makeText(WithDrawMenuAct.this, NC.getString(R.string.insufficent_amout), Toast.LENGTH_SHORT).show();
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


            button_failure.setVisibility(View.VISIBLE);
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    mcancelDialog.dismiss();
                }
            });


//            final TextView title_text = (TextView) mcancelDialog.findViewById(R.id.title_text);
//            final TextView message_text = (TextView) mcancelDialog.findViewById(R.id.message_text);
//            final Button button_success = (Button) mcancelDialog.findViewById(R.id.button_success);
//            final Button button_failure = (Button) mcancelDialog.findViewById(R.id.button_failure);
//            button_failure.setVisibility(View.VISIBLE);
//            title_text.setText("" + NC.getResources().getString(R.string.message));
//            message_text.setText("" + NC.getResources().getString(R.string.with_draw) + SessionSave.getSession("site_currency", WithDrawMenuAct.this)+" " + String.valueOf(walletamountr));
//            button_success.setText("" + NC.getResources().getString(R.string.yes));
//            button_failure.setText("" + NC.getResources().getString(R.string.no));
//            button_success.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO
//                    // Auto-generated
//                    // method stub
//                    try {
//                        mcancelDialog.dismiss();
//                        // TODO Auto-generated method stub
//                        {
//                            //http://192.168.1.116:1027/mobileapi116/index/dGF4aV9hbGw=/?type=driver_wallet_request
//                            //{"driver_id":"1529","driver_wallet_amount":"150"}
//                            // JSONObject j = new JSONObject();
//                            //j.put("driver_id", SessionSave.getSession("Id", WithDrawMenuAct.this));
//                            //j.put("driver_wallet_amount", walletamountr);
//
//                            // final String withdrawUrl = "type=driver_wallet_request";
//                            // new WithDraw(withdrawUrl, j);
//
//                            Intent in = new Intent(WithDrawMenuAct.this, WithDrawMenuAct.class);
//                            startActivity(in);
//                        }
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                        e.printStackTrace();
//                    }
//                }
//            });
//            button_failure.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO
//                    // Auto-generated
//                    // method stub
//                    mcancelDialog.dismiss();
//                }
//            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    /**
     * Withdraw API response parsing.
     */
    private class WithDraw implements APIResult {

        public WithDraw(final String url, JSONObject data) {
           // Log.e("WithDrawurl", url);
            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(WithDrawMenuAct.this, this, data, false).execute(url);
                } else {
                    alert_view(WithDrawMenuAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        /**
         * Parse the response and update the UI.
         */
        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {

            //    Log.e("result", result);
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
               //     System.out.println("_______" + json.toString());
                    if (json.getString("status").trim().equals("1")) {
//                        txt_referalamt.setText(SessionSave.getSession("site_currency", WithDrawMenuAct.this)+" " + "0");
//                        txt_refpendingamt.setText(NC.getResources().getString(R.string.payment_pending) + " "
//                                + SessionSave.getSession("site_currency", WithDrawMenuAct.this)+" " + (Integer.parseInt(SessionSave.getSession("", WithDrawMenuAct.this))
//                                + Integer.parseInt(SessionSave.getSession("driver_wallet_pending_amount", WithDrawMenuAct.this))));

                        if (json.has("details")) {
                            JSONObject details = json.getJSONObject("details");
                          //  System.out.println("_______1" + json.toString());
                            if (details.has("trip_pending_amount")) {
                                SessionSave.saveSession("trip_pending_amount", details.getString("trip_pending_amount"), WithDrawMenuAct.this);
                                SessionSave.saveSession("trip_amount", details.getString("trip_amount"), WithDrawMenuAct.this);
                                SessionSave.saveSession("total_amount", details.getString("total_amount"), WithDrawMenuAct.this);
                                startActivity(new Intent(WithDrawMenuAct.this, WithDrawMenuAct.class));
                                // Toast.makeText(WithDrawMenuAct.this, "hiiiii", Toast.LENGTH_SHORT).show();
//                                finish();
                            }


                        }
                        if (json.has("driver_wallet_amount")) {
                            SessionSave.saveSession("driver_wallet_amount", json.getString("driver_wallet_amount"), WithDrawMenuAct.this);
                            SessionSave.saveSession("driver_wallet_pending_amount", json.getString("driver_wallet_pending_amount"), WithDrawMenuAct.this);
                            //  SessionSave.saveSession("total_amount", details.getString("total_amount"), WithDrawMenuAct.this);
                            startActivity(new Intent(WithDrawMenuAct.this, WithDrawMenuAct.class));
                            // Toast.makeText(WithDrawMenuAct.this, "hiiiii", Toast.LENGTH_SHORT).show();
//                                finish();
                        }
                        txt_referalamt.setText(SessionSave.getSession("site_currency", WithDrawMenuAct.this) + " " + SessionSave.getSession("driver_wallet_amount", WithDrawMenuAct.this));
                        txt_refpendingamt.setText(NC.getResources().getString(R.string.payment_pending) + "-" + SessionSave.getSession("site_currency", WithDrawMenuAct.this) + " " + SessionSave.getSession("driver_wallet_pending_amount", WithDrawMenuAct.this));

                        txt_tripamount.setText(SessionSave.getSession("site_currency", WithDrawMenuAct.this) + " " + SessionSave.getSession("total_amount", WithDrawMenuAct.this));
                        txt_trippendingamt.setText(NC.getResources().getString(R.string.payment_pending) + "-" + SessionSave.getSession("site_currency", WithDrawMenuAct.this) + " " + SessionSave.getSession("trip_pending_amount", WithDrawMenuAct.this));


                    }
                    alert_view(WithDrawMenuAct.this, "", json.getString("message"), NC.getResources().getString(R.string.ok), "");

                }

              //  alert_view(WithDrawMenuAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.server_error), "" + NC.getResources().getString(R.string.ok), "");
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent i = new Intent(this, MeAct.class);
        startActivity(i);
        finish();
    }
}
