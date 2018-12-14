package com.cabi.driver.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.cabi.driver.MainActivity;
import com.cabi.driver.MyStatus;
import com.cabi.driver.NotificationAct;
import com.cabi.driver.R;
import com.cabi.driver.SplashAct;
import com.cabi.driver.UserLoginAct;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

@SuppressLint("InlinedApi")
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 123;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static MainActivity MAIN_ACT;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {// Handling gcm message from
        // pubnub
        final Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        System.out.println("GCMM_________" + messageType);
        System.out.println("GCMM_________" + extras.getString("message"));
        if (!extras.isEmpty()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (new JSONObject(extras.getString("message")).getString("status").equals("15"))
                            if (MAIN_ACT != null) {
                                Intent i = new Intent(MAIN_ACT, UserLoginAct.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(getApplicationContext(), UserLoginAct.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //				sendNotification("Deleted messages on server: "
                //						+ extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i("", "Received: " + extras.toString());
                //  Toast.makeText(GcmIntentService.this, extras.toString(), Toast.LENGTH_SHORT).show();
                generateNotification(this, extras.getString("message"), MyStatus.class);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MyStatus.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle(getString(R.string.app_name)).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(getBaseContext(), notification1);
            r.play();
        } catch (NullPointerException ex) {
        }
    }

    @SuppressWarnings("deprecation")
    public void generateNotification(Context context, String message, Class<?> class1) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Notification notification = new Notification(R.drawable.app_icon, message, System.currentTimeMillis());
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(this, SplashAct.class);
        notificationIntent.putExtra("GCMnotification", message);
        SessionSave.saveSession("GCMnotification", message, context);
        System.out.println("GGGGGGGGG" + message);
        int requestID = (int) System.currentTimeMillis();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // notification.setLatestEventInfo(context, title, message, pendingIntent);
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;
        String Message = "";
        try {
            JSONObject jo = new JSONObject(message);
            if (jo.getString("status").equals("25")) {
                System.out.println("_________sddsfdsfdsfsll" + jo);

                try {
                    SessionSave.saveSession("status", "", GcmIntentService.this);
                    SessionSave.saveSession("Id", "", GcmIntentService.this);
                    SessionSave.saveSession("Driver_locations", "", GcmIntentService.this);
                    SessionSave.saveSession("driver_id", "", GcmIntentService.this);
                    SessionSave.saveSession("Name", "", GcmIntentService.this);
                    SessionSave.saveSession("company_id", "", GcmIntentService.this);
                    SessionSave.saveSession("bookedby", "", GcmIntentService.this);
                    SessionSave.saveSession("p_image", "", GcmIntentService.this);
                    SessionSave.saveSession("Email", "", GcmIntentService.this);
                    SessionSave.saveSession("trip_id", "", GcmIntentService.this);
                    SessionSave.saveSession("phone_number", "", GcmIntentService.this);
                    SessionSave.saveSession("driver_password", "", GcmIntentService.this);

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                Message = jo.getString("message");
                SessionSave.saveSession("LogoutMessage", Message, GcmIntentService.this);
                Notification.Builder builder = new Notification.Builder(context);
                builder.setAutoCancel(false);
                builder.setTicker(NC.getString(R.string.app_name));
                builder.setContentTitle(title);
                builder.setContentText(Message);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(R.drawable.small_logo);
                    builder.setColor(ContextCompat.getColor(getBaseContext(),R.color.button_accept));
                } else {
                    builder.setSmallIcon(R.drawable.ic_launcher);
                }
                builder.setContentIntent(pendingIntent);
                builder.setOngoing(false);
                //builder.setSubText("This is subtext...");   //API level 16
//        builder.setNumber(100);
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
                Intent i = new Intent(GcmIntentService.this, UserLoginAct.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            } else {
                if (NotificationAct.notificationObject != null) {
                    System.out.println("_________sddsfdsfdsfs");
                    if (jo.getString("status").equals("7")) {
                        SessionSave.saveSession("trip_id", "", GcmIntentService.this);
                    }
                    NotificationAct.notificationObject.stopTimerAndNavigateToHome(jo.getString("message"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}