package com.cabi.driver.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ForegroundEnablingService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (LocationUpdate.instance == null)
                throw new RuntimeException(LocationUpdate.class.getSimpleName() + " not running");

            //Set both services to foreground using the same notification id, resulting in just one notification
            startForeground(LocationUpdate.instance);
            startForeground(this);

            //Cancel this service's notification, resulting in zero notifications
            stopForeground(true);

            //Stop this service so we don't waste RAM.
            //Must only be called after doing the work or the notification won't be hidden.
            stopSelf();

            return START_NOT_STICKY;
        }


        private void startForeground(Service service) {
            Notification notification = new Notification.Builder(service).getNotification();
            service.startForeground(10, notification);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }


}
