package com.cabi.driver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.utils.SessionSave;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by developer on 30/11/16.
 */
public class AuthService extends Service {
  int getdetailtimer =  1620000;
//    int getdetailtimer =  60000;
    private final Timer mTimer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // http://192.168.1.56:1002/mobileapi117/index/dGF4aV9hbGw=/?type=get_authentication
        //{"mobilehost":"192.168.1.56:1002","encode":"MTkyLjE2OC4xLjU2OjEwMDItMTQ4MDUwMjUzMw=="}


        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String lastCalled = SessionSave.getSession("auth_last_call_type", this);
        if (lastCalled.equals("")) {
            mTimer.scheduleAtFixedRate(new GetAuthKey(), 0, getdetailtimer);
        } else {

            //3000(millliseconds in a second)*60(seconds in a minute)*5(number of minutes)=300000
            if (Math.abs(Long.parseLong(lastCalled) - System.currentTimeMillis()) > 300000) {
                //server timestamp is within 5 minutes of current system time

                if (mTimer != null)
                    mTimer.scheduleAtFixedRate(new GetAuthKey(), 0, getdetailtimer);
            } else {
                //server is not within 5 minutes of current system time
                if (mTimer != null)
                mTimer.scheduleAtFixedRate(new GetAuthKey(), 0, getdetailtimer);
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }
//void callRepeat(){
//    while (true){
//        System.out.println("naga______calling_sleep" );
//        getAuthKeyy();
//        //SystemClock.sleep(500000);
//
//        //callRepeat();
//    }
//}
//    private void getAuthKeyy() {
//        String url = "type=get_authentication";
//        JSONObject j = new JSONObject();
//        try {
//            j.put("mobilehost", ServiceGenerator.API_BASE_URL.replace("mobileapi117/index/", ""));
//            j.put("encode", ServiceGenerator.DYNAMIC_AUTH_KEY);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        System.out.println("naga______callinghiii" + url);
//
//    }

    //private void getAuthKey() {
    private class GetAuthKey extends TimerTask implements APIResult {



        @Override
        public void run() {
            String url = "type=get_authentication";
            JSONObject j = new JSONObject();
            try {
                ServiceGenerator.API_BASE_URL = SessionSave.getSession("base_url", AuthService.this);
//                ServiceGenerator.COMPANY_KEY = SessionSave.getSession("api_key", AuthService.this);
//                ServiceGenerator.DYNAMIC_AUTH_KEY = SessionSave.getSession("encode", AuthService.this);
                URL urls = null;
                String host="";
                try {
                    urls = new URL(SessionSave.getSession("base_url", AuthService.this));
                    host = urls.getHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                j.put("mobilehost", host);
                j.put("encode", ServiceGenerator.DYNAMIC_AUTH_KEY);
                j.put("user_id", SessionSave.getSession("Id",AuthService.this));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new APIService_Retrofit_JSON(AuthService.this, this, j, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();
//                        if(!json.getString("encode").trim().equals(""))
//                        SessionSave.saveSession("api_key",json.getString("encode"), AuthService.this);
                       // ServiceGenerator.DYNAMIC_AUTH_KEY=json.getString("encode");
                        SessionSave.saveSession("auth_last_call_type", String.valueOf(ts), AuthService.this);
                    }else
                    {
                        try {
                            Toast.makeText(AuthService.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // alert_view(this, "" + getResources().getString(R.string.message), "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
//                        if (json.getInt("status") == 2)
//                            alert_view(this, "" + getResources().getString(R.string.message), "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
                } else {
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                ShowToast(this, result);
//                            }
//                        });
                }
            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // }
    }
}
