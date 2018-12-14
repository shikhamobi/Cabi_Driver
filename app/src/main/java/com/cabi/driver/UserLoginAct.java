package com.cabi.driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.LocationUpdate;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.DeviceUtils;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is the main Login page
 */
public class UserLoginAct extends MainActivity {
    private static boolean FORCE_LOGIN = false;
    // Class members declarations.
    private EditText PhoneEdt;
    private EditText PasswordEdt;
    private TextView ForgotTxt;
    private TextView CancelBtn;
    private TextView DoneBtn, hidePwd;
    private TextView HeadTitle;
    private String phone;
    private String password;
    private SplashAct mSplash;
    private String alert_msg;
    private Bundle alert_bundle = new Bundle();
    private Dialog mDialogs;
    private JSONObject jsonDriver;
    private boolean isReferalSuccess;

    // Set the layout to activity.
    @Override
    public int setLayout() {
        setLocale();

        return R.layout.signin;
    }


    /**
     * Show logout alert message
     */
    public void logout_p(String msg) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserLoginAct.this);
        alertDialog.setTitle("" + NC.getResources().getString(R.string.message));
        alertDialog.setCancelable(false);
        alertDialog.setMessage("" + msg);
        SessionSave.saveSession("LogoutMessage", "", UserLoginAct.this);
        alertDialog.setPositiveButton("" + NC.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
//                final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);

            }
        });
//        alertDialog.setNegativeButton("" + NC.getResources().getString(R.string.cancell), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialog, final int which) {
//                dialog.cancel();
//                finish();
//            }
//        });
        alertDialog.show();
    }


    /**
     * Initialize the views on layout
     */
    @Override
    public void Initialize() {
        mSplash = new SplashAct();
        alert_bundle = getIntent().getExtras();

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) UserLoginAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), UserLoginAct.this);

        if (alert_bundle != null) {
            alert_msg = alert_bundle.getString("alert_message");
         //   System.err.println("oncreate called....." + alert_msg);
        }
        if (alert_msg != null && alert_msg.length() != 0)
            alert_view(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
        FontHelper.applyFont(this, findViewById(R.id.signinlayout));
        CommonData.current_act = "SplashAct";
//		CancelBtn = (TextView) findViewById(R.id.cancelBtn);

//		CancelBtn.setVisibility(View.GONE);
        DoneBtn = (TextView) findViewById(R.id.doneBtn1);
        HeadTitle = (TextView) findViewById(R.id.headerTxt);
//

        TextView HeadTitle = (TextView) findViewById(R.id.header_titleTxt);
//        HeadTitle.setText(NC.getResources().getString(R.string.invite_friend));

        LinearLayout leftIcontxt = (LinearLayout) findViewById(R.id.leftIconTxt);

        TextView leftIcon = (TextView) findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.VISIBLE);
        leftIcon.setBackgroundResource(R.drawable.back);
        //DoneBtn.setText("" + NC.getResources().getString(R.string.done));
        hidePwd = (TextView) findViewById(R.id.hidePwd);
        HeadTitle.setText("" + NC.getResources().getString(R.string.signin));
        PhoneEdt = (EditText) findViewById(R.id.phoneEdt);
        PhoneEdt.setText("");
    //    System.out.println("imagepathhhhhhh" + SessionSave.getSession("image_path", this) + "signInLogo.png");
       // Glide.with(this).load(SessionSave.getSession("image_path", this) + "signInLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.imageview));
        PasswordEdt = (EditText) findViewById(R.id.passwordEdt);
        PasswordEdt.setText("");
//        if (!SessionSave.getSession("phone_number", UserLoginAct.this).equals("") && !SessionSave.getSession("driver_password", UserLoginAct.this).equals("")) {
//            PhoneEdt.setText("" + SessionSave.getSession("phone_number", UserLoginAct.this));
//            PasswordEdt.setText("" + SessionSave.getSession("driver_password", UserLoginAct.this));
//        }

        PasswordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    phone = PhoneEdt.getText().toString().trim();
                    if (validations(ValidateAction.isValueNULL, UserLoginAct.this, phone))
                        if (validations(ValidateAction.isValidPassword, UserLoginAct.this, PasswordEdt.getText().toString().trim())) {
                            SessionSave.saveSession("phone_number", phone, UserLoginAct.this);
                            SessionSave.saveSession("driver_password", PasswordEdt.getText().toString().trim(), UserLoginAct.this);
                            password = convertPassMd5(PasswordEdt.getText().toString().trim());
                            final String url = "type=driver_login";
                            new SignIn(url, false);
                        }
                }
                return false;
            }
        });
        ForgotTxt = (TextView) findViewById(R.id.forgotpswdTxt);
        CommonData.mDevice_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
         /* newly add for password hide and show*/

        hidePwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    FontHelper.applyFont(getApplicationContext(), PasswordEdt);
                    hidePwd.setText("" + NC.getResources().getString(R.string.hide));

                } else {
                    PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(getApplicationContext(), PasswordEdt);
                    hidePwd.setText("" + NC.getResources().getString(R.string.show));

                }
            }
        });
        PasswordEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    hidePwd.setVisibility(View.VISIBLE);


                } else {
                    hidePwd.setVisibility(View.GONE);
                    PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(getApplicationContext(), PasswordEdt);
                    if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                        // PasswordEdt.setText("");
                    }
                }
            }
        });

        /**
         * Forget password action on click

         *
         */
        ForgotTxt.setOnClickListener(new OnClickListener() {
            private Dialog mDialog;

            @Override
            public void onClick(final View v) {
                final View view = View.inflate(UserLoginAct.this, R.layout.forgot_popup, null);
                mDialog = new Dialog(UserLoginAct.this, R.style.NewDialog);
                mDialog.setContentView(view);
                Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.inner_content), UserLoginAct.this);
                FontHelper.applyFont(UserLoginAct.this, mDialog.findViewById(R.id.inner_content));
                mDialog.setCancelable(true);
                mDialog.show();
                final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
                final Button OK = (Button) mDialog.findViewById(R.id.okbtn);
                final Button Cancel = (Button) mDialog.findViewById(R.id.cancelbtn);
                OK.setOnClickListener(new OnClickListener() {
                    private String Email;

                    @Override
                    public void onClick(final View v) {
                        try {
                            Email = mail.getText().toString();
                            if (validations(ValidateAction.isValueNULL, UserLoginAct.this, Email)) {
                                JSONObject j = new JSONObject();
                                j.put("phone_no", Email);
                                j.put("user_type", "D");
                                final String url = "type=forgot_password";
                                new ForgotPassword(url, j);
                                mail.setText("");
                                mDialog.dismiss();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                });
                Cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mDialog.dismiss();
                    }
                });
            }
        });
        /**
         * Action performed,done button onclick
         *
         */
        DoneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                phone = PhoneEdt.getText().toString().trim();
                if (validations(ValidateAction.isValueNULL, UserLoginAct.this, phone))
                    if (validations(ValidateAction.isValidPassword, UserLoginAct.this, PasswordEdt.getText().toString().trim())) {
                        SessionSave.saveSession("phone_number", phone, UserLoginAct.this);
                        SessionSave.saveSession("driver_password", PasswordEdt.getText().toString().trim(), UserLoginAct.this);
                        password = convertPassMd5(PasswordEdt.getText().toString().trim());
                        final String url = "type=driver_login";
                        new SignIn(url, FORCE_LOGIN);
                    }
            }
        });

        leftIcon.setVisibility(View.INVISIBLE);
        leftIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        leftIcontxt.setVisibility(View.INVISIBLE);
        leftIcontxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SessionSave.getSession("LogoutMessage", UserLoginAct.this).trim().equals("")) {
            //  Toast.makeText(SplashAct.this, , Toast.LENGTH_SHORT).show();
            logout_p(SessionSave.getSession("LogoutMessage", UserLoginAct.this));
        }
    }

    @Override
    protected void onStop() {
        CommonData.closeDialog(mDialog);
        CommonData.closeDialog(mDialogs);
        CommonData.closeDialog(alertDialog);
        super.onStop();
    }

    /**
     * ForgotPassword API response parsing.
     */
    private class ForgotPassword implements APIResult {
        public ForgotPassword(String url, JSONObject data) {
            if (isOnline()) {
                new APIService_Retrofit_JSON(UserLoginAct.this, this, data, false).execute(url);
            } else {
                alert_view(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
            }
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1)
                        alert_view(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    else
                        alert_view(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            } else {
//				alert_view(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(UserLoginAct.this, getString(R.string.server_error));
                    }
                });
            }
        }
    }

    /**
     * Signin post method API call and response parsing.
     */
    private class SignIn implements APIResult {
        public SignIn(final String url, boolean FORCE_LOGIN) {
            try {
                JSONObject j = new JSONObject();
                j.put("phone", phone);
                j.put("password", password);
                j.put("device_id", CommonData.mDevice_id);
                String token = FirebaseInstanceId.getInstance().getToken();
                if(token!=null && !token.toString().trim().equals(""))
                    SessionSave.saveSession("device_token", token,UserLoginAct.this);
                j.put("device_token", SessionSave.getSession("device_token", UserLoginAct.this));
                j.put("device_type", "1");
                j.put("force_login", FORCE_LOGIN);
                j.put("device_info", new JSONObject(new Gson().toJson(DeviceUtils.INSTANCE.getAllInfo(UserLoginAct.this))));
               // Log.e("_signin_", j.toString());
                UserLoginAct.this.FORCE_LOGIN = false;
                new APIService_Retrofit_JSON(UserLoginAct.this, this, j, false).execute(url);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        final JSONObject obj = json.getJSONObject("detail");
                        final JSONArray ary = obj.getJSONArray("driver_details");
                        final JSONObject detail = ary.getJSONObject(0);
                        SessionSave.saveSession("Email", detail.getString("email"), UserLoginAct.this);
                        SessionSave.saveSession("Id", detail.getString("userid"), UserLoginAct.this);
                        SessionSave.saveSession("Lastname", detail.getString("lastname"), UserLoginAct.this);
                        SessionSave.saveSession("Name", detail.getString("name"), UserLoginAct.this);
                        SessionSave.saveSession("Bankname", detail.getString("bankname"), UserLoginAct.this);
                        SessionSave.saveSession("Bankaccount_No", detail.getString("bankaccount_no"), UserLoginAct.this);
                        SessionSave.saveSession("Salutation", detail.getString("salutation"), UserLoginAct.this);
                        SessionSave.saveSession("taxi_id", detail.getString("taxi_id"), UserLoginAct.this);
                        SessionSave.saveSession("company_id", detail.getString("company_id"), UserLoginAct.this);
                        SessionSave.saveSession("status", detail.getString("driver_status"), UserLoginAct.this);
                        SessionSave.saveSession("Shiftupdate_Id", detail.getString("shiftupdate_id"), UserLoginAct.this);
                        if (!detail.getString("shiftupdate_id").equals(""))
                            SessionSave.saveSession("driver_shift", "IN", UserLoginAct.this);
                        SessionSave.saveSession("Picture", detail.getString("profile_picture"), UserLoginAct.this);
                        SessionSave.saveSession("Register", "", UserLoginAct.this);
                        if (!detail.getString("trip_id").equals("")) {
                            SessionSave.saveSession("trip_id", detail.getString("trip_id"), UserLoginAct.this);
                            MainActivity.mMyStatus.settripId(detail.getString("trip_id"));
                            SessionSave.saveSession("status", detail.getString("driver_status"), UserLoginAct.this);
                            SessionSave.saveSession("travel_status", detail.getString("travel_status"), UserLoginAct.this);
                        }

                      //  SessionSave.saveSession("taxi_model_id","3",UserLoginAct.this);
                        jsonDriver = detail.getJSONObject("driver_statistics");
                        SessionSave.saveSession("driver_statistics", "" + jsonDriver, UserLoginAct.this);
                        SessionSave.saveSession("Version_Update", "0", UserLoginAct.this);
                        SessionSave.saveSession("shift_status", detail.getJSONObject("driver_statistics").getString("shift_status"), UserLoginAct.this);

                    //    Log.e("driverstatus", detail.getJSONObject("driver_statistics").getString("shift_status"));

                        String isFirst = detail.getString("driver_first_login");
                        if (isFirst.equals("1")) {
                            if ((Integer.parseInt(SessionSave.getSession("referal", UserLoginAct.this))) == 1)
                                referalPopup();
                            else
                                pop_up(jsonDriver);
                        } else
                            pop_up(jsonDriver);

                    } else if (json.getInt("status") == -5)
                        ShowToast(UserLoginAct.this, "" + json.getString("message"));
                    else if (json.getInt("status") == 0)
                        loggedInOtherDevice(json.getString("message"));
                    else
                        ShowToast(UserLoginAct.this, json.getString("message"));
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(UserLoginAct.this, getString(R.string.server_error));
                        }
                    });
                }
//					alert_view(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Referal post method API call and response parsing.
     */
    private void referalPopup() {

        try {
            final View view = View.inflate(UserLoginAct.this, R.layout.referal_popup, null);
            mDialogs = new Dialog(UserLoginAct.this, R.style.dialogwinddow);
            mDialogs.setContentView(view);
            mDialogs.setCancelable(true);
            if (!mDialogs.isShowing())
                mDialogs.show();
            FontHelper.applyFont(UserLoginAct.this, mDialogs.findViewById(R.id.inner_content));
            mDialogs.findViewById(R.id.f_textview);
            final EditText mail = (EditText) mDialogs.findViewById(R.id.forgotmail);

            final Button OK = (Button) mDialogs.findViewById(R.id.okbtn);
            final Button Cancel = (Button) mDialogs.findViewById(R.id.cancelbtn);
            OK.setOnClickListener(new OnClickListener() {
                private String mobilenumber;

                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    try {
                        mobilenumber = mail.getText().toString();
                        if (!mobilenumber.trim().equals("")) {
                            JSONObject j = new JSONObject();
                            j.put("driver_id", SessionSave.getSession("Id", UserLoginAct.this));
                            j.put("referral_code", mobilenumber);
                            final String url = "type=check_driver_referral_code";
                            new ReferalCode(url, j);
                            mail.setText("");
                            mDialogs.dismiss();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            });
            Cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mDialogs.dismiss();
                    pop_up(jsonDriver);
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * Referal  method API call and response parsing.
     */
    private class ReferalCode implements APIResult {
        public ReferalCode(String url, JSONObject data) {
            if (isOnline()) {
                new APIService_Retrofit_JSON(UserLoginAct.this, this, data, false).execute(url);
            } else {
                alert_view(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
            }
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);

                    if (json.getInt("status") == 1) {
                        isReferalSuccess = true;
                        alert_views(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    } else {
                        isReferalSuccess = false;
                        alert_views(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            } else {
//				alert_view(UserLoginAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(UserLoginAct.this, getString(R.string.server_error));
                    }
                });
            }
        }
    }


    /**
     * Alert view for referal code
     */
    public void alert_views(Context m, String title, String message, String success_txt, String failure_txt) {

        final View view = View.inflate(UserLoginAct.this, R.layout.alert_view, null);
        alertDialog = new Dialog(UserLoginAct.this, R.style.NewDialog);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);

        Colorchange.ChangeColor((ViewGroup) alertDialog.findViewById(R.id.alert_id), UserLoginAct.this);
        FontHelper.applyFont(UserLoginAct.this, alertDialog.findViewById(R.id.alert_id));

        alertDialog.show();
        final TextView title_text = (TextView) alertDialog.findViewById(R.id.title_text);
        final TextView message_text = (TextView) alertDialog.findViewById(R.id.message_text);
        final Button button_success = (Button) alertDialog.findViewById(R.id.button_success);
        title_text.setText(title);
        message_text.setText(message);
        button_success.setText(success_txt);
        button_success.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                if (isReferalSuccess)
                    pop_up(jsonDriver);
                else referalPopup();
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    Dialog mDialog;

    /**
     * Function: To show the success popup dialog and move to dashboard
     */

    /**
     * This function will redirect to other activities
     */
    public void pop_up(final JSONObject jsonDriverObject) {
//        final View view = View.inflate(UserLoginAct.this, R.layout.forgot_popup, null);
//        mDialog = new Dialog(UserLoginAct.this, R.style.NewDialog);
//        mDialog.setContentView(view);
//        FontHelper.applyFont(UserLoginAct.this, mDialog.findViewById(R.id.inner_content));
//        mDialog.setCancelable(false);
//        mDialog.show();
//        final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
//
//        mail.setVisibility(View.GONE);
//        final Button OK = (Button) mDialog.findViewById(R.id.okbtn);
//        final Button Cancel = (Button) mDialog.findViewById(R.id.cancelbtn);
//        Cancel.setText("" + NC.getResources().getString(R.string.t_ok));
//
//        OK.setVisibility(View.GONE);
//        final TextView heading = (TextView) mDialog.findViewById(R.id.message);
//        heading.setText("" + NC.getResources().getString(R.string.t_success));
//        final TextView message = (TextView) mDialog.findViewById(R.id.timetoleave);
//        message.setVisibility(View.VISIBLE);
//        mDialog.findViewById(R.id.forsep).setVisibility(View.GONE);
//        message.setText("" + NC.getResources().getString(R.string.login_success));
//        Cancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View v) {
        if (!SessionSave.getSession("trip_id", UserLoginAct.this).equals("")) {
            if (SessionSave.getSession("travel_status", UserLoginAct.this).equals("5")) {
                SessionSave.saveSession("status", "A", UserLoginAct.this);
                Intent in = new Intent(UserLoginAct.this, OngoingAct.class);
                startActivity(in);
               if (SessionSave.getSession("shift_status", UserLoginAct.this).equals("IN"))
                startService(new Intent(UserLoginAct.this, LocationUpdate.class));
                finish();
                if (mDialog != null)
                    mDialog.dismiss();
            } else if (SessionSave.getSession("travel_status", UserLoginAct.this).equals("2")) {
                SessionSave.saveSession("status", "A", UserLoginAct.this);
                Intent in = new Intent(UserLoginAct.this, OngoingAct.class);
                startActivity(in);
                if(SessionSave.getSession("shift_status", UserLoginAct.this).equals("IN"))
                startService(new Intent(UserLoginAct.this, LocationUpdate.class));
                finish();
                if(mDialog!=null && UserLoginAct.this!=null)
                mDialog.dismiss();
            } else {
                final Intent i = new Intent(UserLoginAct.this, MyStatus.class);
                if(SessionSave.getSession("shift_status", UserLoginAct.this).equals("IN"))
                startService(new Intent(UserLoginAct.this, LocationUpdate.class));
                startActivity(i);
                finish();
                if (mDialog != null)
                    mDialog.dismiss();
            }
        } else {
            try {
                final Intent i = new Intent(UserLoginAct.this, MyStatus.class);

                Intent service = new Intent(UserLoginAct.this, LocationUpdate.class);
                UserLoginAct.this.startService(service);
                if(SessionSave.getSession("shift_status", UserLoginAct.this).equals("IN"))
                startService(new Intent(UserLoginAct.this, LocationUpdate.class));
                startActivity(i);
                finish();
                if (mDialog != null)
                    mDialog.dismiss();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    // });
    // }


    /**
     * Already logged in dialog
     */

    public void loggedInOtherDevice(String msg) {
        try {

            if (sDialog != null)
                sDialog.dismiss();
            final View view = View.inflate(UserLoginAct.this, R.layout.netcon_lay, null);
            sDialog = new Dialog(UserLoginAct.this, R.style.dialogwinddow);

            try {
                Colorchange.ChangeColor((ViewGroup) view, UserLoginAct.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sDialog.setContentView(view);
            sDialog.setCancelable(true);
            FontHelper.applyFont(UserLoginAct.this, sDialog.findViewById(R.id.alert_id));
            if (!((Activity) UserLoginAct.this).isFinishing()) {
                //show dialog
                sDialog.show();
            }


            final TextView title_text = (TextView) sDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) sDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) sDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) sDialog.findViewById(R.id.button_failure);
            title_text.setText("" + NC.getResources().getString(R.string.message));
            message_text.setText("" + msg);
            button_success.setText("" + NC.getResources().getString(R.string.ok));
            button_failure.setText("" + NC.getResources().getString(R.string.cancell));
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    if (NetworkStatus.isOnline(UserLoginAct.this)) {
                        sDialog.dismiss();
                        FORCE_LOGIN = true;
                        UserLoginAct.this.DoneBtn.performClick();
                    } else {
                        Toast.makeText(UserLoginAct.this, NC.getResources().getString(R.string.check_net_connection), Toast.LENGTH_LONG).show();
                    }


                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    sDialog.dismiss();
//                    Activity activity = (Activity) UserLoginAct.this;
//                    activity.finish();
//                    final Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_HOME);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    activity.startActivity(intent);
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}