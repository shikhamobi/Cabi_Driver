package com.cabi.driver.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.cabi.driver.MainActivity;
import com.cabi.driver.MyStatus;
import com.cabi.driver.R;
import com.cabi.driver.data.CommonData;
import com.cabi.driver.service.WaitingTimerRun;

import org.json.JSONObject;

/**
 * Created by developer on 19/6/18.
 */

public class PaymentCompleted extends Activity {

    public static boolean tripcompleted = false;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypopup_lay);

        Cleardata();

        Intent in = new Intent(PaymentCompleted.this, MyStatus.class);
        in.setAction(Intent.ACTION_MAIN);
        in.addCategory(Intent.CATEGORY_LAUNCHER);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ComponentName cn = new ComponentName(getApplicationContext(), MyStatus.class);
        in.setComponent(cn);
        startActivity(in);
//			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			startActivity(in);
        finish();
    }

    private void Cleardata() {
        SessionSave.saveSession("travel_status", "", PaymentCompleted.this);
        SessionSave.saveSession("trip_id", "", PaymentCompleted.this);
        SessionSave.saveSession("status", "F", PaymentCompleted.this);
        WaitingTimerRun.ClearSession(PaymentCompleted.this);
                       /* CommonData.mBlnLowBalanceShowStatus = false;
                        if (SessionSave.getSession("wallet_notification_status", PaymentCompleted.this).equals("1")) {
                            CommonData.mBlnLowBalance = true;
                        } else {
                            CommonData.mBlnLowBalance = false;
                        }*/

        tripcompleted = true;
        MainActivity.mMyStatus.setdistance("");
        // msg = json.getString("message");
        MainActivity.mMyStatus.setOnstatus("");
        MainActivity.mMyStatus.setStatus("F");
        SessionSave.saveSession("status", "F", PaymentCompleted.this);
        MainActivity.mMyStatus.setOnPassengerImage("");
        MainActivity.mMyStatus.setOnstatus("On");
        MainActivity.mMyStatus.setOnstatus("Complete");
        MainActivity.mMyStatus.setOnpassengerName("");
        MainActivity.mMyStatus.setOndropLocation("");
        MainActivity.mMyStatus.setOndropLocation("");
        MainActivity.mMyStatus.setOnpickupLatitude("");
        MainActivity.mMyStatus.setOnpickupLongitude("");
        MainActivity.mMyStatus.setOndropLatitude("");
        MainActivity.mMyStatus.setOndropLongitude("");
        // JSONObject jsonDriver = json.getJSONObject("driver_statistics");
        SessionSave.saveSession("driver_statistics", "" + "", PaymentCompleted.this);
        CommonData.hstravel_km = "";
        WaitingTimerRun.sTimer = "00:00:00";
        WaitingTimerRun.finalTime = 0L;
        WaitingTimerRun.timeInMillies = 0L;
        SessionSave.saveSession("waitingHr", "", PaymentCompleted.this);
        CommonData.travel_km = 0;
        SessionSave.setGoogleDistance(0f, PaymentCompleted.this);
        SessionSave.setDistance(0f, PaymentCompleted.this);
        SessionSave.saveGoogleWaypoints(null, null, "", 0.0, "", PaymentCompleted.this);
        SessionSave.saveWaypoints(null, null, "", 0.0, "", PaymentCompleted.this);
    }

}
