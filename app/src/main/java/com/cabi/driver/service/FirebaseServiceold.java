/*
package com.cabi.driver.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
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
import com.cabi.driver.OngoingAct;
import com.cabi.driver.R;
import com.cabi.driver.SplashAct;
import com.cabi.driver.UserLoginAct;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

*/
/**
 * Created by developer on 30/8/17.
 *//*


public class FirebaseService extends FirebaseMessagingService {
    public static final int NOTIFICATION_ID = 123;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
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
//        System.out.println("GCMM_________" + extras.getString("message"));
        String message = "";
        try {
            message = remoteMessage.getData().get("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!message.isEmpty()) {
            try {
                if (new JSONObject(message).getString("status").equals("15")) {

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
                } else if (new JSONObject(message).getString("status").equals("99")) {
//                    Intent i = new Intent(getApplicationContext(), OngoingAct.class);
//
//                    Bundle bun = new Bundle();
//                    bun.putString("alert_message", new JSONObject(message).getString("message"));
//                    bun.putString("status", "11");
//                    i.putExtras(bun);
//                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    i.setAction(Intent.ACTION_MAIN);
//                    i.addCategory(Intent.CATEGORY_LAUNCHER);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(i);
                    JSONObject json = new JSONObject(message);
                    sendNotification(json.getString("message"));
                    Intent ongoing = new Intent();
                    Bundle extras = new Bundle();
                    String lTaximobilityutlmsg = "";
                    lTaximobilityutlmsg = json.getString("message");
                    extras.putString("alert_message", lTaximobilityutlmsg);
                    extras.putString("status", json.getString("status"));
                    ongoing.putExtras(extras);
                    ongoing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ComponentName cn = new ComponentName(getApplicationContext(), OngoingAct.class);
                    ongoing.setComponent(cn);
                    getApplication().startActivity(ongoing);

                } else
                    generateNotification(this, message, SplashAct.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //				sendNotification("Deleted messages on server: "
                //						+ extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                //     Log.i("", "Received: " + extras.toString());
                //  Toast.makeText(FirebaseService.this, extras.toString(), Toast.LENGTH_SHORT).show();
                //generateNotification(this, extras.getString("message"), MyStatus.class);
            }

        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        //  GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MyStatus.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.small_logo).setContentTitle(getString(R.string.app_name)).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);
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
            final JSONObject jo = new JSONObject(message);
            System.out.println("nnn-----status"+jo.getString("status"));
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

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                Message = jo.getString("message");
                SessionSave.saveSession("LogoutMessage", Message, FirebaseService.this);
                Notification.Builder builder = new Notification.Builder(context);
                builder.setAutoCancel(false);
                builder.setTicker(NC.getString(R.string.app_name));
                builder.setContentTitle(title);
                builder.setContentText(Message);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(R.drawable.small_logo);
                    builder.setColor(ContextCompat.getColor(getBaseContext(), R.color.button_accept));
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
                Intent i = new Intent(FirebaseService.this, UserLoginAct.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            } else {
                System.out.println("nnn-----notificationObject");
                if (NotificationAct.notificationObject != null) {
                    System.out.println("nnn-----notificationObject--NOT null");
                    try {
                        if (jo.getString("status").equals("7")) {
                            System.out.println("nnn-----statu 7");
                            SessionSave.saveSession("trip_id", "", FirebaseService.this);
                        }
                        NotificationAct.notificationObject.stopTimerAndNavigateToHome(jo.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (jo.getString("status").equals("7")) {
                                    SessionSave.saveSession("trip_id", "", FirebaseService.this);
                                }
                                System.out.println("chry_chkng_1");
                                NotificationAct.notificationObject.stopTimerAndNavigateToHome(jo.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
*/
