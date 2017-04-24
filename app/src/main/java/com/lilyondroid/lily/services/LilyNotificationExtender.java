package com.lilyondroid.lily.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.math.BigInteger;

/**
 * Created by jason on 23/04/2017.
 */

public class LilyNotificationExtender extends NotificationExtenderService {

    String TAG = "ptasdevz";



    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {

        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {

            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                builder.setVisibility(Notification.VISIBILITY_SECRET);
                return builder.setColor(new BigInteger("FF00FF00", 16).intValue());

            }
        };


        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        NotificationManager notificationManager = (NotificationManager)getSystemService(
                getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(displayedResult.androidNotificationId);

        Log.d(TAG, "Notification displayed with id: " + displayedResult.androidNotificationId);

        return false;
    }
}
