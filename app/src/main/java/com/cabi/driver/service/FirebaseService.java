package com.cabi.driver.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.cabi.driver.CanceltripAct;
import com.cabi.driver.MainActivity;
import com.cabi.driver.MyStatus;
import com.cabi.driver.NotificationAct;
import com.cabi.driver.OngoingAct;
import com.cabi.driver.R;
import com.cabi.driver.SplashAct;
import com.cabi.driver.UserLoginAct;
import com.cabi.driver.data.CommonData;
import com.cabi.driver.data.apiData.DetailInfo;
import com.cabi.driver.utils.DeviceUtils;
import com.cabi.driver.utils.DriverUtils;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by developer on 30/8/17.
 */

/**
 * Created by developer on 30/8/17.
 */

public class FirebaseService extends FirebaseMessagingService {
    public static final int NOTIFICATION_ID = 123;
    private NotificationManager mNotificationManager;
    Notification.Builder builder;
    public static MainActivity MAIN_ACT;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Log.d("MyFirebaseIIDService", "From: " + remoteMessage.getNotification().getBody());
        System.out.println("MyFirebaseIIDServices" + remoteMessage.getData());
        onHandleIntent(remoteMessage);
        // Check if message contains a data payload.

        //  Log.d("MyFirebaseIIDService", "Message data payload: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            Log.d("MyFirebaseIIDService", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("sssss", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


    protected void onHandleIntent(RemoteMessage remoteMessage) {// Handling gcm message from
        // pubnub
//        final Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = remoteMessage.getMessageType();
//        System.out.println("GCMM_________" + messageType);
        String message = "";
        String unique = "";
        try {
            message = remoteMessage.getData().get("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!message.isEmpty()) {
            try {
               final JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.has("unique")) {
                    unique = jsonObject.getString("unique");
                }
                if (jsonObject.getString("status").equals("15")) {

                    try {
                        SessionSave.saveSession("status", "", getApplicationContext());
                        SessionSave.saveSession("Id", "", getApplicationContext());
                        SessionSave.saveSession("Driver_locations", "", getApplicationContext());
                        SessionSave.saveSession("driver_id", "", getApplicationContext());
                        SessionSave.saveSession("Name", "", getApplicationContext());
                        SessionSave.saveSession("company_id", "", getApplicationContext());
                        SessionSave.saveSession("bookedby", "", getApplicationContext());
                        SessionSave.saveSession("p_image", "", getApplicationContext());
                        SessionSave.saveSession("Email", "", getApplicationContext());
                        SessionSave.saveSession("trip_id", "", getApplicationContext());
                        SessionSave.saveSession("phone_number", "", getApplicationContext());
                        SessionSave.saveSession("driver_password", "", getApplicationContext());
                        SessionSave.setWaitingTime(0L, getApplicationContext());

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    if (MAIN_ACT != null) {
                        Intent i = new Intent(MAIN_ACT, UserLoginAct.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(), UserLoginAct.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                    generateNotification(this, message, UserLoginAct.class);
                } else if (jsonObject.getInt("status") == 7 || jsonObject.getInt("status") == 10) {
//                    final JSONObject jsons = new JSONObject(message);
                    try {
                        String cancelmsg = "";
                        cancelmsg = jsonObject.getString("message");
                        MainActivity.mMyStatus.setStatus("F");
                        SessionSave.saveSession("status", "F", getApplicationContext());
                        MainActivity.mMyStatus.settripId("");
                        SessionSave.saveSession("trip_id", "", getApplicationContext());
                        SessionSave.setWaitingTime(0L, getApplicationContext());
                        MainActivity.mMyStatus.setOnstatus("");
                        MainActivity.mMyStatus.setOnPassengerImage("");
                        MainActivity.mMyStatus.setOnpassengerName("");
                        MainActivity.mMyStatus.setOndropLocation("");
                        MainActivity.mMyStatus.setOnpickupLatitude("");
                        MainActivity.mMyStatus.setOnpickupLongitude("");
                        MainActivity.mMyStatus.setOndropLatitude("");
                        MainActivity.mMyStatus.setOndropLongitude("");
                        MainActivity.mMyStatus.setOndriverLatitude("");
                        MainActivity.mMyStatus.setOndriverLongitude("");
                        WaitingTimerRun.ClearSessionwithTrip(getApplicationContext());
                        stopService(new Intent(FirebaseService.this, WaitingTimerRun.class));
                        Intent cancelIntent = new Intent();
                        Bundle bun = new Bundle();
                        bun.putString("message", cancelmsg);
                        cancelIntent.putExtras(bun);
                        cancelIntent.setAction(Intent.ACTION_MAIN);
                        cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ComponentName cn = new ComponentName(getApplicationContext(), CanceltripAct.class);
                        cancelIntent.setComponent(cn);
                        startActivity(cancelIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (jsonObject.getString("status").equals("99")) {
                    JSONObject json = new JSONObject(message);
                    sendNotification(json.getString("message"));
                    Intent ongoing = new Intent();
                    Bundle extras = new Bundle();
                    String lcabiutlmsg = "";
                    lcabiutlmsg = json.getString("message");
                    extras.putString("alert_message", lcabiutlmsg);
                    extras.putString("status", json.getString("status"));
                    ongoing.putExtras(extras);
                    ongoing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ComponentName cn = new ComponentName(getApplicationContext(), OngoingAct.class);
                    ongoing.setComponent(cn);
                    getApplication().startActivity(ongoing);
                } else if (jsonObject.getString("status").equals("41")) {

                    sendInfo(getApplicationContext(), unique);

                    generateNotification(this, message, SplashAct.class);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else if (jsonObject.getString("status").equals("42")) {
                    sendInfo(getApplicationContext(), unique);
                    final Intent i = new Intent(getApplicationContext(), SplashAct.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    SessionSave.saveSession("need_animation", true, getApplicationContext());
                    generateNotification(this, message, SplashAct.class);
                } else if (jsonObject.getString("status").equals("43")) {
                    sendInfo(getApplicationContext(), unique);

                } else if (jsonObject.getString("status").equals("44")) {
                    sendInfo(getApplicationContext(), unique);
                    generateNotification(this, message, SplashAct.class);
                    final Intent i = new Intent(getApplicationContext(), LocationUpdate.class);
                    stopService(i);
                    if (!SessionSave.getSession("driver_type", getApplicationContext()).equals("D")
                            && !SessionSave.getSession(CommonData.SHIFT_OUT, getApplicationContext(), false)
                            && !SessionSave.getSession(CommonData.LOGOUT, getApplicationContext(), false))
                        LocationUpdate.startLocationService(getApplicationContext());
                } else
                    generateNotification(this, message, SplashAct.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        //  GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {


        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//       WaitingTimerRun.ClearSessionwithTrip(FirebaseService.this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MyStatus.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.small_logo).setContentTitle(getString(R.string.app_name)).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(getBaseContext(), notification1);
            r.play();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void generateNotification(Context context, String message, Class<?> class1) {

        String Message = "";
        try {
            final JSONObject jo = new JSONObject(message);
            if (jo.getString("status").equals("25") || jo.getString("status").equals("15")) {
                System.out.println("_________sddsfdsfdsfsll" + jo);

                try {
                    SessionSave.saveSession("status", "", FirebaseService.this);
                    SessionSave.saveSession("Id", "", FirebaseService.this);
                    SessionSave.saveSession("Driver_locations", "", FirebaseService.this);
                    SessionSave.saveSession("driver_id", "", FirebaseService.this);
                    SessionSave.saveSession("Name", "", FirebaseService.this);
                    SessionSave.saveSession("company_id", "", FirebaseService.this);
                    SessionSave.saveSession("bookedby", "", FirebaseService.this);
                    SessionSave.saveSession("p_image", "", FirebaseService.this);
                    SessionSave.saveSession("Email", "", FirebaseService.this);
                    SessionSave.saveSession("trip_id", "", FirebaseService.this);
                    SessionSave.saveSession("phone_number", "", FirebaseService.this);
                    SessionSave.saveSession("driver_password", "", FirebaseService.this);
                    SessionSave.setWaitingTime(0L, FirebaseService.this);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                Message = jo.getString("message");
                showNotification(context, Message, message);
                if (MAIN_ACT != null) {
                    Handler h = new Handler();
                    if (h != null)
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                MAIN_ACT.checkGCM();
                            }
                        });

                }
                Intent i = new Intent(FirebaseService.this, UserLoginAct.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            } else if (jo.getString("status").equals("42") || jo.getString("status").equals("45")
                    || jo.getString("status").equals("41")
                    || jo.getString("status").equals("45")
                    || jo.getString("status").equals("44")) {
                Message = jo.getString("message");
                showNotification(context, Message, message);
            } else {
                if (NotificationAct.notificationObject != null) {
                    System.out.println("_________sddsfdsfdsfs");
                    try {
                        if (jo.getString("status").equals("7")) {
                            SessionSave.saveSession("trip_id", "", FirebaseService.this);
                        }
                        NotificationAct.notificationObject.stopTimerAndNavigateToHome(jo.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                if (jo.getString("status").equals("7")) {
//                                    SessionSave.saveSession("trip_id", "", FirebaseService.this);
//                                }
//                                NotificationAct.notificationObject.stopTimerAndNavigateToHome(jo.getString("message"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showNotification(Context context, String Message, String data) {

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(this, SplashAct.class);
        notificationIntent.putExtra("GCMnotification", data);
        SessionSave.saveSession("GCMnotification", data, context);
        System.out.println("GGGGGGGGG" + data);
        int requestID = (int) System.currentTimeMillis();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        SessionSave.saveSession("LogoutMessage", Message, FirebaseService.this);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            mNotificationManager.createNotificationChannel(notificationChannel);

            builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentText(Message)
                    .setContentTitle(title)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.app_icon)).getBitmap())
                    .setWhen(System.currentTimeMillis());
        } else {
            builder = new Notification.Builder(context);
            builder.setAutoCancel(false);
            builder.setTicker(NC.getString(R.string.app_name));
            builder.setContentTitle(title);
            builder.setContentText(Message);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.small_logo);
                builder.setColor(ContextCompat.getColor(getBaseContext(), R.color.button_accept));
            } else {
                builder.setSmallIcon(R.drawable.app_icon);
            }
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(false);
        }
        builder.build();
        Notification myNotication = builder.getNotification();
        myNotication.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, myNotication);
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification1);
            r.play();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    private void sendInfo(Context context, String unique) {

        String base_url = SessionSave.getSession("base_url", context);
        Uri uri = Uri.parse(base_url);
        String path = uri.getPath();
        CoreClient client = new ServiceGenerator(context,
                false, base_url.replaceAll(path, "")).createService(CoreClient.class);

        Call<ResponseBody> detail_infoCall = client.detail_infoCall(new DetailInfo(DeviceUtils.INSTANCE.getAllInfo(context), DriverUtils.INSTANCE.driverInfo(context), unique), SessionSave.getSession("Lang", context));

        detail_infoCall.enqueue(new Callback<ResponseBody>() {

            @Override

            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.v("FirebaseService", "" + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override

            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();


            }

        });

    }
}